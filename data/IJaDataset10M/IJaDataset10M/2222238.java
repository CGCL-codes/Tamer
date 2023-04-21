package net.sf.pojomaker.utils;

import java.io.File;

/**
 * Utility class.
 * 
 * @author Jean-Philippe Gravel, eng.
 */
public final class Util {

    /**
	 * This utility class cannot be instanciated.
	 */
    private Util() {
    }

    /**
	 * Throws NullPointerException if "s" is null or IllegalArgumentException if
	 * "s" is an empty string.
	 * 
	 * @param s
	 *            The string to check.
	 * @param argName
	 *            The name of the checked argument.
	 */
    public static void throwIfNullOrEmpty(final String s, final String argName) {
        throwIfNull(s, argName);
        String msg = "The parameter \"@ARG_NAME\" can't be empty.";
        if (s == null) {
            throw new IllegalArgumentException(msg.replace("@ARG_NAME", argName));
        }
    }

    /**
	 * Throws NullPointerException if "o" is null.
	 * 
	 * @param o
	 *            The object to check.
	 * @param argName
	 *            The name of the checked argument.
	 */
    public static void throwIfNull(final Object o, final String argName) {
        String msg = "The parameter \"@ARG_NAME\" can't be null.";
        if (o == null) {
            throw new NullPointerException(msg.replace("@ARG_NAME", argName));
        }
    }

    /**
	 * Delete the specified file or directory. If file is a directory, deletes
	 * it's content recursively before deleting the directory.
	 * 
	 * @param file
	 *            The file or directory to delete.
	 */
    public static void delete(final File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        File[] subFiles = file.listFiles();
        if ((subFiles != null) && (subFiles.length > 0)) {
            for (File subFile : subFiles) {
                delete(subFile);
            }
        }
        file.delete();
    }
}
