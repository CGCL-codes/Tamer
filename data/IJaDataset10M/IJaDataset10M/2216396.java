package com.student.dao;

public class Util {

    public static final String DOC_DIRNAME = "doc";

    public static final int KB = 1024;

    public static final int MB = 1024 * KB;

    public static final String FILE_SPEAR = "/";

    public static String getFileExt(String s) {
        int i = s.lastIndexOf("."), leg = s.length();
        return (i > 0 ? (i + 1) == leg ? " " : s.substring(i, s.length()) : " ");
    }

    public static void fileRm() {
    }
}
