package net.sf.RecordEditor.examples.utils;

import java.util.HashMap;

/**
 * parses program aguments into a hashmap
 *
 * @author Bruce Martin
 *
 */
public class ParseArguments {

    private HashMap<String, String> argsMap = new HashMap<String, String>();

    /**
     * Parse program arguments
     *
     * @param validArgs list of all valid arguments
     *
     * @param args arguments supplied to the program
     */
    public ParseArguments(final String[] validArgs, final String[] args) {
        int i;
        HashMap<String, Integer> valid = new HashMap<String, Integer>();
        String currArg = null;
        String currValue = null;
        String sep = "";
        for (i = 0; i < validArgs.length; i++) {
            valid.put(validArgs[i].toUpperCase(), new Integer(i));
        }
        for (i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if (currArg != null) {
                    argsMap.put(currArg, currValue);
                }
                currValue = "";
                sep = "";
                currArg = args[i].toUpperCase();
                if (!valid.containsKey(currArg)) {
                    currArg = null;
                    System.out.println(" ** Invalid Argument " + args[i]);
                }
            } else {
                currValue += sep + args[i];
                sep = " ";
            }
        }
        if (currArg != null) {
            argsMap.put(currArg, currValue);
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Get a requested argument
     *
     * @param arg argument being requested
     *
     * @return Argument value
     */
    public String getArg(String arg) {
        return (String) argsMap.get(arg.toUpperCase());
    }

    /**
     * Get a requested argument
     *
     * @param arg argument being requested
     * @param defaultValue default argument value
     *
     * @return requested argument value
     */
    public String getArg(String arg, String defaultValue) {
        String ret = defaultValue;
        String key = arg.toUpperCase();
        if (argsMap.containsKey(key)) {
            ret = (String) argsMap.get(key);
        }
        return ret;
    }

    /**
     * Get a requested integer argument
     *
     * @param arg argument being requested
     *
     * @return Argment value
     * @throws Exception any error that occurs
     */
    public int getIntArg(String arg) throws Exception {
        try {
            return Integer.parseInt(getArg(arg));
        } catch (Exception e) {
            System.out.println();
            System.out.println("Error processing integer argument " + arg + "  - " + e.getMessage());
            System.out.println();
            throw e;
        }
    }

    /**
     * Get a requested integer argument
     *
     * @param arg argument being requested
     * @param defaultVal default value for the parameter
     * @return Argment value
      */
    public int getIntArg(String arg, int defaultVal) {
        try {
            return Integer.parseInt(getArg(arg));
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
