package org.systemsbiology.apps.gui.client.util;

/**
 * @author Mark Christiansen
 *
 */
public class StringUtils {

    private StringUtils() {
    }

    /**
	 * @param str string to remove space
	 * @return string without spaces
	 */
    public static String removeSpace(String str) {
        return str.replaceAll("\\s", "");
    }

    /**
	 * @param toReverse string to reverse
	 * @return reversed reversed string
	 */
    public static StringBuffer reverseStringBuffer(StringBuffer toReverse) {
        StringBuffer reversed = new StringBuffer();
        for (int i = toReverse.length() - 1; i <= 0; i--) {
            reversed.append(toReverse.charAt(i));
        }
        return reversed;
    }

    /**
	 * @param validateMe path to validate
	 * @return boolean
	 */
    public static boolean isValidFilePath(String validateMe) {
        if (validateMe.length() < 1) return false;
        if (validateMe.matches("[a-zA-Z0-9\\.\\-_\\/]+")) {
            return true;
        }
        return false;
    }

    /**
	 * @param validateMe string to verify as a valid project name
	 * @return boolean
	 */
    public static boolean isValidProjectName(String validateMe) {
        if (validateMe.length() < 1) return false;
        if (validateMe.matches("[a-zA-Z0-9\\.\\-_]+")) {
            return true;
        }
        return false;
    }

    /**
	 * Get the file name without the path
	 * @param filePath path of file
	 * @return file name
	 */
    public static String getJustFileName(String filePath) {
        int i = filePath.lastIndexOf("/");
        return filePath.substring(i + 1);
    }

    /**
	 * Get the file extension
	 * @param filePath path of file
	 * @return file name
	 */
    public static String getFileNameExt(String filePath) {
        String fileName = getJustFileName(filePath);
        int i = fileName.lastIndexOf(".");
        return fileName.substring(i + 1);
    }
}
