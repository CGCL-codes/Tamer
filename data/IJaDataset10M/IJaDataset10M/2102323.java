package com.ideo.sweetdevria.util;

/**
 * Utility class to parse and manipulate files
 * 
 * @author Julien Maupoux
 *
 */
public class FileUtils {

    /**
	 * Extract the file name from the full path of a file
	 * This file might be a non existing one, this is the reason why we cannot use the common APIs
	 * @param filename the full file name
	 * @return the file name extracted
	 */
    public static String extractFileName(String filename) {
        int lastSlash = filename.lastIndexOf("\\");
        if (lastSlash == -1) {
            lastSlash = filename.lastIndexOf("/");
        }
        if (lastSlash == -1) return filename;
        return filename.substring(lastSlash + 1, filename.length());
    }
}
