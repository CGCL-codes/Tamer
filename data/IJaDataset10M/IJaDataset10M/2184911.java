package com.lts.util.zip;

import java.io.File;
import java.util.Comparator;

public class SimpleZipEntry {

    public String myDirectoryName;

    public String myFileName;

    public static Comparator getComparator() {
        return new Comparator() {

            public int compare(Object o1, Object o2) {
                SimpleZipEntry e1 = (SimpleZipEntry) o1;
                SimpleZipEntry e2 = (SimpleZipEntry) o2;
                return e1.myFileName.compareTo(e2.myFileName);
            }
        };
    }

    public SimpleZipEntry(String dirname, String fname) {
        myDirectoryName = dirname;
        myFileName = fname;
    }

    public File getCompletePath() {
        File f;
        if (null == myDirectoryName) f = new File(myFileName); else f = new File(myDirectoryName, myFileName);
        return f;
    }
}
