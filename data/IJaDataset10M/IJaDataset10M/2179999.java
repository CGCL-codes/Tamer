package net.sf.jvifm.util;

import java.io.*;

public class HomeLocator {

    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    public static String getConfigHome() {
        return getUserHome() + File.separator + ".jvifm";
    }

    public static String getTempDir() {
        String tempdir = System.getProperty("java.io.tmpdir");
        if (!(tempdir.endsWith("/") || tempdir.endsWith("\\"))) tempdir = tempdir + System.getProperty("file.separator");
        return tempdir;
    }
}
