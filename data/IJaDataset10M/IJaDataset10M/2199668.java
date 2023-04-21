package com.vanga.parsing;

/**
 * Utility class to help in the parsing of the command line.
 *
 */
public class CommandLineParsing {

    /**
	 * Retrieve the value corresponding to the particular command line key.
	 * @param args the command line arguments.
	 * @param key the required command line switch.
	 * @param deflt the default value which should be returned should the switch not be found.
	 * @return the value corresponding to the specified key.
	 */
    public static String getValue(String[] args, String key, String deflt) {
        boolean bReturn = false;
        for (String k : args) {
            if (bReturn) return k;
            if (k.equalsIgnoreCase(key) || key.equalsIgnoreCase("-" + key)) bReturn = true;
        }
        return deflt;
    }
}
