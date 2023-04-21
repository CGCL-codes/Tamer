package sketch.util;

import java.util.*;
import java.io.*;

/**
 * Recursive file listing under a specified directory.
 * 
 * @author javapractices.com
 * @author Alex Wong
 * @author anonymous user
 */
public final class FileListing {

    /**
	 * Demonstrate use.
	 * 
	 * @param aArgs
	 *            - <tt>aArgs[0]</tt> is the full name of an existing directory
	 *            that can be read.
	 */
    public static void main(String... aArgs) throws FileNotFoundException {
        String fPath = "./examples";
        File startingDirectory = new File(fPath);
        List<File> files = FileListing.getFileListing(startingDirectory);
        for (File file : files) {
            System.out.println(file);
        }
    }

    public static List<File> getFiles(File f) {
        try {
            return getFileListing(f);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Recursively walk a directory tree and return a List of all Files found;
	 * the List is sorted using File.compareTo().
	 * 
	 * @param aStartingDir
	 *            is a valid directory, which can be read.
	 */
    private static List<File> getFileListing(File aStartingDir) throws FileNotFoundException {
        validateDirectory(aStartingDir);
        List<File> result = getFileListingNoSort(aStartingDir);
        Collections.sort(result);
        return result;
    }

    private static List<File> getFileListingNoSort(File aStartingDir) throws FileNotFoundException {
        List<File> result = new ArrayList<File>();
        File[] filesAndDirs = aStartingDir.listFiles();
        List<File> filesDirs = Arrays.asList(filesAndDirs);
        for (File file : filesDirs) {
            result.add(file);
            if (!file.isFile()) {
                List<File> deeperList = getFileListingNoSort(file);
                result.addAll(deeperList);
            }
        }
        return result;
    }

    /**
	 * Directory is valid if it exists, does not represent a file, and can be
	 * read.
	 */
    private static void validateDirectory(File aDirectory) throws FileNotFoundException {
        if (aDirectory == null) {
            throw new IllegalArgumentException("Directory should not be null.");
        }
        if (!aDirectory.exists()) {
            throw new FileNotFoundException("Directory does not exist: " + aDirectory);
        }
        if (!aDirectory.isDirectory()) {
            throw new IllegalArgumentException("Is not a directory: " + aDirectory);
        }
        if (!aDirectory.canRead()) {
            throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
        }
    }
}
