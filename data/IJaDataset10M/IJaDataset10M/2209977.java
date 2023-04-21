package com.sun.iis.tools.cmd;

import java.io.*;

public class jcat {

    static jcat main = new jcat();

    public static void main(String args[]) {
        if (!parse_args(args)) System.exit(usage(1));
        log(debug, "main():  main.p is " + main.p + " p is " + p);
        if (SHOWHELP) System.exit(usage(0));
        if (USE_STDIN) {
            log(info, "using stdin");
            catfile(System.in);
        } else {
            for (int ii = 0; ii < FILES.length; ii++) {
                catfile(FILES[ii]);
            }
        }
        System.exit(0);
    }

    public static void catfile(InputStream fh) {
        log(info, "InputStream is '" + fh + "'");
        int bb;
        long cnt = 0;
        try {
            while ((bb = fh.read()) != -1) {
                ++cnt;
                System.out.write(bb);
            }
        } catch (Exception e) {
            log(error, p + "[catfile(InputStream)]: " + e.getMessage());
            return;
        }
        log(info, "read " + cnt + " bytes");
    }

    public static void catfile(String fn) {
        FileInputStream myfile = null;
        log(info, "input file name is '" + fn + "'");
        try {
            myfile = new FileInputStream(fn);
        } catch (Exception e) {
            log(error, p + "[catfile(String)]: " + e.getMessage());
            return;
        }
        catfile(myfile);
        try {
            if (myfile != null) myfile.close();
        } catch (IOException e) {
            ;
        }
    }

    public static int usage(int status) {
        String usage[] = { "Usage: " + p + " [-help] [-verbose] [-debug] [file...]", "", "Concatenate files to stdout.  If no <file> arguments, read from stdin.", "", "Options:", " -help     display this help message", " -verbose  display information messages", " -debug    display debug messages", "", "Example:", p + " -verbose foo.txt" };
        for (int ii = 0; ii < usage.length; ii++) {
            log(usage[ii]);
        }
        return (status);
    }

    public static boolean parse_args(String args[]) {
        String arg;
        int ii = 0;
        while (ii < args.length) {
            arg = args[ii].toLowerCase();
            if (arg.startsWith("-h")) {
                SHOWHELP = true;
            } else if (arg.startsWith("-v")) {
                VERBOSE = true;
            } else if (arg.startsWith("-d")) {
                DEBUG = true;
            } else if (arg.startsWith("-")) {
                log(error, p + ":  unrecognized arg, '" + arg + "'.");
                return false;
            } else {
                break;
            }
            ++ii;
        }
        while (ii < args.length && args[ii].equals("")) ++ii;
        if (ii >= args.length) {
            USE_STDIN = true;
            return true;
        }
        int nempty = 0;
        for (int jj = ii; jj < args.length; jj++) {
            if (args[jj].equals("")) ++nempty;
        }
        FILES = new String[args.length - (ii + nempty)];
        int jj = 0;
        while (ii < args.length) {
            if (!args[ii].equals("")) FILES[jj++] = args[ii];
            ++ii;
        }
        return true;
    }

    private static void log(String msg) {
        System.err.println(msg);
    }

    private static void log(int level, String msg) {
        boolean showit = (DEBUG & (level == info | level == debug)) | (VERBOSE & (level == info)) | (level == error);
        if (showit) System.err.println(levelmsg[level] + ": " + msg);
    }

    private static String p = "jcat";

    private static boolean SHOWHELP = false, VERBOSE = false, DEBUG = false, USE_STDIN = false;

    private static String[] FILES;

    private static int xx = 0;

    private static final int debug = xx++, info = xx++, error = xx++;

    private static String[] levelmsg = { "DEBUG", " INFO", "ERROR" };

    jcat() {
    }
}
