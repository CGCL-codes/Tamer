package com.ivis.xprocess.web.utils;

public class FileUtil {

    private FileUtil() {
    }

    public static String makeFilenameCrossPlatform(String filename) {
        String butchered = filename.replace(' ', '_');
        return butchered;
    }
}
