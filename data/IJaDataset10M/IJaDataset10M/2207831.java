package org.openremote.irbuilder.utils;

/**
 * @author <a href="mailto:allen.wei@finalist.cn">allen.wei</a>
 */
public class StringUtils {

    public static String fileNameRemoveUUID(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("_"));
    }

    public static String getFileExt(String fileName) {
        String[] parts = fileName.split("\\.");
        if (parts.length == 0) {
            return "";
        }
        return parts[parts.length - 1];
    }
}
