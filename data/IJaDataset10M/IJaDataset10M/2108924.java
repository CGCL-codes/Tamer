package com.citizenhawk.antmakerunscript.util;

import java.io.File;

/**
 * <u><b><font color="red">FOR INTERNAL USE ONLY.</font></b></u>
 * <p/>
 * Copyright (c)2007, Daniel Kaplan
 *
 * @author Daniel Kaplan
 * @since 7.10.5
 */
public class RelativePathUtils {

    private RelativePathUtils() {
    }

    public static String relativePathIfUnderCWD(String path, File basedir) {
        String cwd = basedir.getAbsolutePath() + System.getProperty("file.separator");
        if (!path.startsWith(cwd)) {
            return path;
        }
        return path.substring(cwd.length());
    }
}
