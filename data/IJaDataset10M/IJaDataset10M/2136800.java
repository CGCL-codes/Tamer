package edu.stanford.ejalbert.launching.utils;

/**
 *
 * @author not attributable
 * @version 1.0
 */
public class LaunchingUtils {

    private static final String REPLACE_BROWSER = "<browser>";

    private static final String REPLACE_URL = "<url>";

    public static String replaceArgs(String commands, String browserArg, String urlArg) {
        if (browserArg != null) {
            commands = commands.replaceAll(REPLACE_BROWSER, browserArg);
        }
        if (urlArg != null) {
            int urlPos = commands.indexOf(REPLACE_URL);
            StringBuffer buf = new StringBuffer();
            while (urlPos > 0) {
                buf.append(commands.substring(0, urlPos));
                buf.append(urlArg);
                buf.append(commands.substring(urlPos + REPLACE_URL.length()));
                commands = buf.toString();
                urlPos = commands.indexOf(REPLACE_URL);
                buf.setLength(0);
            }
        }
        return commands;
    }
}
