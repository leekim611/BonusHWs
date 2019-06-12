package implementing.ls;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import implementing.ls.exception.IncorrectPathException;

public class ImplementingLsOption {

	private String setPath;	// p, path
	private boolean help;	// h, help
	private boolean printPerLine; // l, line
	private boolean reverse;	// r, reverse
	private boolean printAbsolutePath;	// a, absolute
	
	
	public void run(String[] args) {
		Options options = createOptions();
		
		if (parseOptions(options, args)) {
			
			// option 'h'
			if (help) {
				printHelp(options);
				return;
			}
			
			// option 'p'
			if (setPath == null) {
				System.out.println("You don't provide path as the value of the option p");
				System.out.println("Run in project directory");
				setPath = System.getProperty("user.dir");
			}
			else {
				try {
					System.out.println("You provided \"" + setPath + "\" as the value of the option p");
					if (!new File(setPath).isDirectory()) {
						throw new IncorrectPathException("\nThis is not directory or wrong path. Enter 'p' option again.");
					}
				} catch (IncorrectPathException e) {
					System.out.println(e.getMessage());
					return;
				}
			}
			File file = new File(setPath);
			String[] files = file.list();
			
			// option 'r'
			if (reverse) {
				List<String> list = Arrays.asList(files);
				Collections.reverse(list);
				files = list.toArray(new String[list.size()]);
			}
			
			// option 'a'
			if (printAbsolutePath) {
				System.out.println("\nAbsolute path is");
				System.out.println(file.getAbsolutePath() + "\n");
			}
			
			// option 'l'
			if (printPerLine) {
				System.out.println("File list:");
				for (String fileName : files) {
					System.out.println("\t" + fileName);
				}
				System.out.println("[You enter '-l'. So, Print out per line]");
				if (reverse) {
					System.out.println("[You enter '-r'. So, Print out reverse order]");
				}
			}
			else {
				int count = 0;
				boolean defaultPrint = false;
				System.out.println("File list:");
				for (String fileName : files) {
					System.out.print("\t" + fileName);
					count++;
					if (count == 5) {
						System.out.println();
						count = 0;
						defaultPrint = true;
					}
				}
				if (defaultPrint) {
					System.out.print("\n[By default, five outputs per line.]");
				}
				if (reverse) {
					System.out.println("\n[You enter '-r'. So, Print out reverse order]");
				}
			}
			
			
		}
	}
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options,  args);
			
			setPath = cmd.getOptionValue("p");
			printPerLine = cmd.hasOption("l");
			help = cmd.hasOption("h");
			reverse = cmd.hasOption("r");
			printAbsolutePath = cmd.hasOption("a");
		} catch (Exception e) {
			printHelp(options);
			return false;
		}
		
		return true;
	}
	private Options createOptions() {
		Options options = new Options();
		
		options.addOption(Option.builder("p").longOpt("path")
				.desc("Set a path of a directory to display [Only directory]")
				.hasArg()
				.argName("Path name to display")
				.build());
		options.addOption(Option.builder("l").longOpt("line")
				.desc("Print out list per line")
				.build());
		options.addOption(Option.builder("h").longOpt("help")
				.desc("Help")
				.build());
		options.addOption(Option.builder("r").longOpt("reverse")
				.desc("Display list reverse order")
				.build());
		options.addOption(Option.builder("a").longOpt("absolute")
				.desc("Print out absolute path of present directory")
				.build());
		
		return options;
	}
	
	
	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String header = "Command 'ls' implementing program";
		String footer = "";
		formatter.printHelp("BonusHWs", header, options, footer, true);
	}
}
