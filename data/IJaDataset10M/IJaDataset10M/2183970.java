package com.organic.maynard.util.crawler;

import java.io.*;
import java.util.*;
import com.organic.maynard.io.*;
import com.organic.maynard.util.*;
import com.organic.maynard.util.string.StringTools;

public class SimpleMultiReplace {

    public static final String COMMAND_PARSER_SEPARATOR = "\t";

    public static final String COMMAND_SET = "set";

    public static final String COMMAND_START_PATH = "start_path";

    public static final String COMMAND_MATCH = "match";

    public static final String COMMAND_FILE_EXTENSION = "file_ext";

    public static final String COMMAND_LINE_ENDING = "line_ending";

    public static final String PLATFORM_MAC = "mac";

    public static final String PLATFORM_WIN = "win";

    public static final String PLATFORM_UNIX = "unix";

    public Vector matches = new Vector();

    public Vector replacements = new Vector();

    public String[] fileExtensions;

    public String startingPath = null;

    public String lineEnding = FileTools.LINE_ENDING_WIN;

    public boolean blockSetStartingPath = false;

    public SimpleMultiReplace(String args[]) {
        String configPath = null;
        try {
            configPath = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        if (configPath != null) {
            String startPathFromArgs = null;
            try {
                startPathFromArgs = args[1];
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            if (startPathFromArgs != null) {
                this.startingPath = startPathFromArgs;
                blockSetStartingPath = true;
            }
            CommandParser parser = new CommandParser(COMMAND_PARSER_SEPARATOR);
            parser.addCommand(new SimpleMultiReplaceConfigCommand(COMMAND_SET, this));
            CommandQueue commandQueue = new CommandQueue(30);
            commandQueue.loadFromFile(configPath);
            while (commandQueue.getSize() > 0) {
                try {
                    parser.parse((String) commandQueue.getNext());
                } catch (UnknownCommandException uce) {
                    System.out.println("Unknown Command");
                }
            }
        } else {
            startingPath = ConsoleTools.getNonEmptyInput("Enter starting path: ");
            System.out.println("Enter multiple matches/replacements below. When done, enter an empty response to proceed.");
            boolean done = false;
            while (!done) {
                String match = ConsoleTools.getNonNullInput("  Match: ");
                if (match.equals("")) {
                    if (matches.size() > 0) {
                        done = true;
                    }
                    continue;
                }
                String replacement = ConsoleTools.getNonNullInput("  Replace: ");
                matches.add(match);
                replacements.add(replacement);
            }
            fileExtensions = ConsoleTools.getSeriesOfInputs("Enter file extension to match: ");
            while (fileExtensions.length <= 0) {
                fileExtensions = ConsoleTools.getSeriesOfInputs("Enter file extension to match: ");
            }
            System.out.println("");
        }
        DirectoryCrawler crawler = new DirectoryCrawler();
        crawler.setFileHandler(new SimpleMultiReplacementFileContentsHandler(matches, replacements, lineEnding));
        crawler.setFileFilter(new FileExtensionFilter(fileExtensions));
        System.out.println("STARTING...");
        crawler.crawl(startingPath);
        System.out.println("DONE");
    }

    public static void main(String args[]) {
        SimpleMultiReplace sr = new SimpleMultiReplace(args);
    }
}
