package openbiomind.gui.util;

import java.io.Closeable;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import openbiomind.gui.common.Constants;
import openbiomind.gui.console.Console;

/**
 * The class Utility.
 * 
 * @author bsanghvi
 * @since Jun 14, 2008
 * @version Aug 18, 2008
 */
public final class Utility implements Constants {

    /**
    * Checks if is the string empty.
    * 
    * @param string the string
    * 
    * @return true, if is empty
    */
    public static boolean isEmpty(final String string) {
        return (string == null || string.trim().isEmpty());
    }

    /**
    * Checks if is empty or existing file.
    * 
    * @param filePath the file path
    * 
    * @return true, if is empty or existing file
    */
    public static boolean isEmptyOrExistingFile(final String filePath) {
        return (isEmpty(filePath) || fileExists(filePath));
    }

    /**
    * Specifies if a file or directory specified by pathname exists or not?.
    * 
    * @param pathName the path name
    * 
    * @return true, if successful
    */
    public static boolean exists(final String pathName) {
        return (!isEmpty(pathName) && new File(pathName).exists());
    }

    /**
    * File exists.
    * 
    * @param pathname the pathname
    * 
    * @return true, if successful
    */
    public static boolean fileExists(final String pathname) {
        boolean valid = false;
        if (!isEmpty(pathname)) {
            final File file = new File(pathname);
            valid = file.exists() && file.isFile();
        }
        return valid;
    }

    /**
    * Directory exists.
    * 
    * @param pathname the pathname
    * 
    * @return true, if successful
    */
    public static boolean directoryExists(final String pathname) {
        boolean valid = false;
        if (!isEmpty(pathname)) {
            final File file = new File(pathname);
            valid = directoryExists(file);
        }
        return valid;
    }

    /**
    * Directory exists.
    * 
    * @param directory the directory
    * 
    * @return true, if successful
    */
    public static boolean directoryExists(final File directory) {
        return directory.exists() && directory.isDirectory();
    }

    /**
    * Checks if pathname can be a file or not?
    * 
    * @param pathname the pathname
    * 
    * @return true, if pathname can be a file
    */
    public static boolean isPossibleFile(final String pathname) {
        return (!isEmpty(pathname) && isPossibleFile(new File(pathname)));
    }

    /**
    * Checks if is possible file.
    * 
    * @param file the file
    * 
    * @return true, if is possible file
    */
    public static boolean isPossibleFile(final File file) {
        return !file.isDirectory();
    }

    /**
    * Checks if pathname can be a directory or not?
    * 
    * @param pathname the pathname
    * 
    * @return true, if pathname can be a directory
    */
    public static boolean isPossibleDirectory(final String pathname) {
        return (!isEmpty(pathname) && isPossibleDirectory(new File(pathname)));
    }

    /**
    * Checks if is possible directory.
    * 
    * @param file the file
    * 
    * @return true, if is possible directory
    */
    public static boolean isPossibleDirectory(final File file) {
        return !file.isFile();
    }

    /**
    * Extract directory. If the given <code>pathName</code> is a possible directory then it is returned. If it is a
    * file, then its parent is returned.
    * 
    * @param pathName the path name
    * 
    * @return the string
    */
    public static String extractDirectory(final String pathName) {
        if (isEmpty(pathName)) {
            return EMPTY;
        }
        if (!isPossibleDirectory(pathName)) {
            return new File(pathName).getParent();
        } else {
            return pathName;
        }
    }

    /**
    * Extract name of the file or the directory from the path. The file name includes extension.
    * 
    * @param pathName the path name
    * 
    * @return the string
    */
    public static String extractFullName(final String pathName) {
        if (isEmpty(pathName)) {
            return EMPTY;
        }
        return new File(pathName).getName();
    }

    /**
    * Extract name of the file without the extension.
    * 
    * @param filePath the file path
    * 
    * @return the string
    */
    public static String extractFileName(final String filePath) {
        if (isEmpty(filePath)) {
            return EMPTY;
        }
        final String name = extractFullName(filePath);
        final int lastIndexOfDot = name.lastIndexOf(DOT);
        return (lastIndexOfDot > -1 ? name.substring(0, lastIndexOfDot) : name);
    }

    /**
    * List the names of file in the given <code>directory</code> such that the names of the files begin with
    * <code>startsWith</code> and ends with <code>endsWith</code>.
    * 
    * @param directory the directory
    * @param startsWith the starts with
    * @param endsWith the ends with
    * 
    * @return the string[]
    */
    public static String[] listFileNames(final File directory, final String startsWith, final String endsWith) {
        return directory.list(new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                return (name.startsWith(startsWith) && name.endsWith(endsWith));
            }
        });
    }

    /**
    * Close the closeable resource.
    * 
    * @param closeable the resource to be closed
    * 
    * @return true, if no exception occurred while closing
    */
    public static boolean close(final Closeable closeable) {
        boolean success = true;
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final IOException e) {
                success = false;
                Console.debug(e);
            }
        }
        return success;
    }
}
