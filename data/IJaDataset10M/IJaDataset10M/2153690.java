package com.nogoodatcoding.folder2feed;

import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author no.good.at.coding
 */
public class Utils {

    public static final short INDEX_NOQUIT = 0;

    public static final short INDEX_TTI = 1;

    public static final short INDEX_FEED = 2;

    public static final short INDEX_IGNORE = 3;

    public static final short INDEX_HELP = 4;

    public static final short INDEX_CLI = 5;

    public static final short FILES_ONLY = 0;

    public static final short FOLDERS_ONLY = 1;

    public static final short FILES_AND_FOLDERS = 2;

    public static final String FEED_PROPERTIES_FOLDER = "feeds";

    public static final String FEED_PROPERTIES_FILE_EXTENSION = ".ffc";

    private static Logger log_ = Logger.getLogger(Utils.class);

    private static ResourceBundle messages_ = ResourceBundle.getBundle("com.nogoodatcoding.folder2feed.messages.Messages_Utils");

    private static final String USAGE = ("\n" + Utils.messages_.getString("utils.usage") + "\n\n" + Utils.messages_.getString("utils.options") + "\n" + Utils.messages_.getString("utils.options.noquit") + "\n\n" + Utils.messages_.getString("utils.options.tti") + "\n\n" + Utils.messages_.getString("utils.options.feed") + "\n\n" + Utils.messages_.getString("utils.options.ignore") + "\n\n" + Utils.messages_.getString("utils.options.help"));

    private static final String[] ERROR_TITLES = { "Don't Panic", "Frak!", "Reavers!", "Danger, Will Robinson!", "Shields Up, Commander Worf!", "Kree!" };

    /**
     *
     * Iterates over the command line arguments and checks for the switches
     * or arguments. If any are found, the appropriate index is set in the
     * {@code Object []} that is returned
     *
     * @param args The command line arguments
     *
     * @return The Object[] containing either {@code null} (if the argument for
     *         that index was not found) or an appropriate object
     *
     */
    public static Object[] parseArgs(String[] args) {
        Object[] arguments = new Object[6];
        short matchCount = 0;
        boolean foundMatchInThisIteration = false;
        String arrayItemLowerCase = null;
        arguments[Utils.INDEX_CLI] = false;
        for (String arrayItem : args) {
            arrayItemLowerCase = arrayItem.toLowerCase();
            if (arguments[Utils.INDEX_NOQUIT] == null && "-noquit".equals(arrayItemLowerCase)) {
                arguments[Utils.INDEX_NOQUIT] = true;
                matchCount++;
                foundMatchInThisIteration = true;
            } else if (arguments[Utils.INDEX_TTI] == null && arrayItemLowerCase.startsWith("-tti=")) {
                arguments[Utils.INDEX_TTI] = (short) Integer.parseInt(arrayItem.substring(arrayItemLowerCase.indexOf("-tti=") + 5).trim());
                matchCount++;
                foundMatchInThisIteration = true;
            } else if (arguments[Utils.INDEX_FEED] == null && arrayItemLowerCase.startsWith("-feed=")) {
                arguments[Utils.INDEX_FEED] = arrayItem.substring(arrayItemLowerCase.indexOf("-feed=") + 6).split(",");
                matchCount++;
                foundMatchInThisIteration = true;
            } else if (arguments[Utils.INDEX_IGNORE] == null && arrayItemLowerCase.startsWith("-ignore=")) {
                HashMap map = new HashMap();
                String[] feedsToIgnore = arrayItem.substring(arrayItemLowerCase.indexOf("-ignore=") + 8).split(",");
                for (String feed : feedsToIgnore) {
                    map.put(feed + Utils.FEED_PROPERTIES_FILE_EXTENSION, feed + Utils.FEED_PROPERTIES_FILE_EXTENSION);
                }
                arguments[Utils.INDEX_IGNORE] = map;
                matchCount++;
                foundMatchInThisIteration = true;
                feedsToIgnore = null;
            } else if ("-help".equals(arrayItemLowerCase) || "--help".equals(arrayItemLowerCase) || "/?".equals(arrayItemLowerCase) || "/help".equals(arrayItemLowerCase) || "-usage".equals(arrayItemLowerCase) || "--usage".equals(arrayItemLowerCase)) {
                arguments[Utils.INDEX_HELP] = true;
                matchCount++;
                foundMatchInThisIteration = true;
            } else if ("-cli".equals(arrayItemLowerCase)) {
                arguments[Utils.INDEX_CLI] = true;
                matchCount++;
                foundMatchInThisIteration = true;
            }
            if (!foundMatchInThisIteration) throw new IllegalArgumentException(Utils.messages_.getString("utils.exception.illegalArgumentException.invalidUsage"));
        }
        if (matchCount != args.length) throw new IllegalArgumentException(Utils.messages_.getString("utils.exception.illegalArgumentException.invalidUsage"));
        return arguments;
    }

    /**
     *
     * Performs the appropriate work for arguments -noquit, -help/-usage, -tti=
     *
     * @param arguments The {@code Object[]} from
     *                  {@link #parseArgs(java.lang.String[])} with appropriate
     *                  values set at the correct indices
     */
    public static void setSettings(Object[] arguments) {
        if (arguments[Utils.INDEX_HELP] != null) {
            printUsage(Level.INFO);
            System.exit(0);
        }
        if (arguments[Utils.INDEX_NOQUIT] != null) {
            Settings.setIsSingleRun(false);
        }
        if (arguments[Utils.INDEX_TTI] != null) {
            Settings.setTreeTraversalInterval((Short) arguments[Utils.INDEX_TTI]);
        }
    }

    /**
     *
     * Prints the usage string to the console and log
     *
     */
    public static void printUsage(Level LogLevel) {
        Utils.log_.log(LogLevel, USAGE);
        System.out.println(USAGE);
    }

    /**
     *
     * Parses the value set in the properties file for this feed and returns
     * the programmatically usable numeric value
     *
     * @param propertyString The value from the property file
     *
     * @return A {@code short} indicating files or folders or both
     */
    public static short whatToInclude(String propertyString) {
        short toInclude = 0;
        propertyString = propertyString.toUpperCase();
        if (propertyString.indexOf("FOLDER") != -1) {
            toInclude++;
            if (propertyString.indexOf("FILE") != -1) {
                toInclude++;
            }
        }
        return toInclude;
    }

    /**
     *
     * Returns a title for error dialogs
     *
     * @return A title for error dialogs
     *
     */
    public static String getErrorDialogTitle() {
        Random randomGenerator = new Random();
        return ERROR_TITLES[randomGenerator.nextInt(ERROR_TITLES.length)];
    }
}
