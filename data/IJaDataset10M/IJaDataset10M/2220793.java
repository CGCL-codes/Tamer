package com.ub.jcrypto.utils;

/** 
 * @author  ubatra 
 * @version A utility class to provide some basic operations 
 *          that can be applied on file resource path names 
 */
public class Filename {

    /** 
	 * final constant to represent file extension that 
	 * would be used for converted files and directories 
	 **/
    public static final String EXTENSION = ".encrypted";

    /** 
	 * remove redundant "\" from the path 
	 * @param path 
	 * @return String 
	 */
    public static String removeSlash(String sourcePath) {
        String lastCharacterOfFilename = sourcePath.substring(sourcePath.length() - 1);
        if (lastCharacterOfFilename.equals("\\") || lastCharacterOfFilename.equals("/")) {
            sourcePath = sourcePath.substring(0, sourcePath.length() - 1);
        }
        return sourcePath;
    }

    /** 
	 * remove extension from the path 
	 * @param filename 
	 * @return String 
	 */
    public static String removeExtension(String filename) {
        if (filename.lastIndexOf(Filename.EXTENSION) != -1) filename = filename.substring(0, filename.lastIndexOf(Filename.EXTENSION));
        return filename;
    }
}
