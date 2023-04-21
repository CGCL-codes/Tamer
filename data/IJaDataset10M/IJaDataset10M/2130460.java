package com.sts.webmeet.server.util;

import java.io.File;
import java.io.FilenameFilter;

public class ZipFilenameFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(FileUtil.ZIP_SUFFIX);
    }
}
