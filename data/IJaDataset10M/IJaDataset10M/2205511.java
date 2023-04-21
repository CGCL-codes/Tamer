package org.slizardo.madcommander.util.io;

import java.io.File;

/**
 * Utilities for work with files.
 */
public class FileUtil {

    public static final String LINE_SEP = System.getProperty("line.separator");

    public static String extractDirPart(File file) {
        if (file == null) return "null";
        String fileName = file.getAbsolutePath();
        if (file.isDirectory()) return fileName;
        int last = fileName.lastIndexOf(File.separator);
        String dirPart = fileName.substring(0, last);
        return dirPart;
    }

    public static String extractFilePart(File file) {
        if (file == null) return "null";
        String fileName = file.getAbsolutePath();
        if (file.isDirectory()) return "";
        int last = fileName.lastIndexOf(File.separator);
        String filePart = fileName.substring(last + 1);
        return filePart;
    }

    /**
	 * Returns the attributes for the file passed as parameter, as a string.
	 * 
	 * @param file
	 * @return
	 */
    public static String getAttributes(File file) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(file.canRead() && file.canWrite() == false ? "r" : "-");
        buffer.append("a");
        buffer.append(file.isHidden() ? "h" : "-");
        buffer.append("-");
        return buffer.toString();
    }

    /**
	 * Returns the humanized attributes for the file passed as parameter, as a string.
	 * 
	 * @param file
	 * @return
	 */
    public static String getHumanizedAttributes(File file) {
        String att = "";
        if (file.canRead()) att += "r ";
        if (file.canWrite()) att += "w ";
        if (file.canExecute()) att += "e ";
        return att;
    }
}
