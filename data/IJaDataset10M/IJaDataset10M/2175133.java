package com.stericson.RootTools;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

class InternalVariables {

    protected static String TAG = "RootTools v1.7";

    protected static boolean accessGiven = false;

    protected static boolean nativeToolsReady = false;

    protected static String[] space;

    protected static String getSpaceFor;

    protected static String busyboxVersion;

    protected static Set<String> path;

    protected static ArrayList<Mount> mounts;

    protected static ArrayList<Symlink> symlinks;

    protected static int timeout = 10000;

    protected static final String PS_REGEX = "^\\S+\\s+([0-9]+).*$";

    protected static Pattern psPattern;

    static {
        psPattern = Pattern.compile(PS_REGEX);
    }
}
