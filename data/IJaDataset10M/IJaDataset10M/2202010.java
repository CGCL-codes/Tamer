package com.db4o.internal.odbgen.fileutils;

import com.db4o.odbgen.OdbgenException;

public class FileUtils {

    /**
	 * Returns the path of the directory of the specified file. 
	 * @param filePath The file to thet the directory path for.
	 * @throws OdbgenException
	 */
    public static String getDirectoryPathFromFile(String filePath) throws OdbgenException {
        char[] chars = filePath.toCharArray();
        int k = -1;
        for (int i = filePath.length() - 1; i >= 0; i--) {
            if (chars[i] == '/' || chars[i] == '\\') {
                k = i;
                break;
            }
        }
        if (k == -1) {
            throw new OdbgenException("Could not get the directory path for file '%s'.", filePath);
        }
        return filePath.substring(0, k);
    }
}
