package com.peterhi.launcher;

import java.net.URL;
import java.net.MalformedURLException;

public class DownloadUtil {

    public static URL[] convert(String baseDir, String[] array) throws MalformedURLException {
        String fileSeparator = System.getProperty("file.separator");
        URL[] ret = new URL[array.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new URL(baseDir + fileSeparator + array[i]);
        }
        return ret;
    }
}
