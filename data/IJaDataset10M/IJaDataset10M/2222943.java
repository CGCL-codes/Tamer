package org.choncms.console.utils;

public class PathResolver {

    public static String getAbsPath(String rel, String current) {
        if (rel == null || rel.equals(".") || rel.equals("./")) {
            return current;
        }
        if (rel.startsWith("/")) {
            return rel;
        }
        return (current.equals("/") ? "" : current) + "/" + rel;
    }
}
