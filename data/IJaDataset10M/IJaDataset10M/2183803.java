package com.sun.iis.tools.cmd;

import java.lang.Runtime;
import java.io.*;

public class jexec {

    static jexec main = new jexec();

    public static void main(String args[]) {
        if (!parse_args(args)) System.exit(usage(1));
        log(debug, "main():  main.p is " + main.p + " p is " + p);
        if (SHOWHELP) System.exit(usage(0));
        for (int ii = 0; ii < COMMANDS.length; ii++) {
            exec_command(COMMANDS[ii]);
        }
        System.exit(0);
    }

    public static void exec_command(String cmd) {
        log(info, "executing command '" + cmd + "'");
        try {
            Process childpid = runtime.exec(cmd);
            InputStream child_stdout = childpid.getInputStream();
            InputStream child_stderr = childpid.getErrorStream();
            OutputStream child_stdin = childpid.getOutputStream();
            child_stdin.close();
            int bb;
            while ((bb = child_stdout.read()) != -1) {
                System.out.write(bb);
            }
            while ((bb = child_stderr.read()) != -1) {
                System.err.write(bb);
            }
            childpid.waitFor();
            log(debug, "child exited with status " + childpid.exitValue());
            log(debug, "number of bytes available on child stdout is " + child_stdout.available());
            log(debug, "number of bytes available on child stderr is " + child_stderr.available());
        } catch (Exception e) {
            log(error, p + "[exec_command]: " + e.getMessage());
            return;
        }
    }

    public static int usage(int status) {
        String usage[] = { "Usage: " + p + " [-help] [-verbose] [-debug] command ...", "", "Execute commands and return status", "", "Options:", " -help     display this help message", " -verbose  display information messages", " -debug    display debug messages", "", "Example:", p + " -verbose 'sh -c \"ls /tmp\"'" };
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
            log(error, p + ":  missing commands.");
            return false;
        }
        int nempty = 0;
        for (int jj = ii; jj < args.length; jj++) {
            if (args[jj].equals("")) ++nempty;
        }
        COMMANDS = new String[args.length - (ii + nempty)];
        int jj = 0;
        while (ii < args.length) {
            if (!args[ii].equals("")) COMMANDS[jj++] = args[ii];
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

    private static String p = "jexec";

    private static boolean SHOWHELP = false, VERBOSE = false, DEBUG = false, USE_STDIN = false;

    private static String[] COMMANDS;

    private static int xx = 0;

    private static final int debug = xx++, info = xx++, error = xx++;

    private static String[] levelmsg = { "DEBUG", " INFO", "ERROR" };

    public static Runtime runtime;

    jexec() {
        p = this.getClass().getName();
        runtime = java.lang.Runtime.getRuntime();
    }
}
