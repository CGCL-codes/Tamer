package com.myapp.util.duplicatefinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Cruncher {

    public static final String CMD_CREATE_HASHFILE = "hash";

    public static final String CMD_GENERATE_SCRIPT = "generate";

    public static final String PARAM_PREFIX_ROOT_DIR = "--rootDir=";

    public static final String PARAM_PREFIX_HASH_FILE = "--hashFile=";

    public static final String PARAM_PREFIX_CRUNCH_SCRIPT = "--scriptFile=";

    public static final String PARAM_PREFIX_LOG_FILE = "--logFile=";

    public static final String CMD_HELP = "--help";

    public static final String FLAG_VERBOSE = "--verbose";

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            p("call cruncher with " + CMD_HELP + " to show the usage.");
            System.exit(5);
        }
        if (args.length == 1 && args[0].equals(CMD_HELP)) {
            printUsage();
            System.exit(0);
        }
        Cruncher cruncher = new Cruncher(args);
        try {
            File logFile = parseFile(PARAM_PREFIX_LOG_FILE, args);
            PrintStream logOut = (logFile == null) ? System.out : new PrintStream(new FileOutputStream(logFile));
            cruncher.setLogOut(logOut);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Set<String> commands = parseCommands(args[0]);
        if (commands.contains(CMD_CREATE_HASHFILE)) {
            cruncher.hash();
        }
        if (commands.contains(CMD_GENERATE_SCRIPT)) {
            cruncher.generate();
        }
    }

    @SuppressWarnings("unused")
    private static String[] testArgs() {
        try {
            Runtime.getRuntime().exec(new String[] { "bash", "-c", "rm -r /tmp/nasen;        " + "mkdir /tmp/nasen;        " + "echo a > /tmp/nasen/a;   " + "echo b > /tmp/nasen/b;   " + "echo c > /tmp/nasen/c;   " + "mkdir /tmp/nasen/foo;    " + "echo a > /tmp/nasen/foo/d" }).waitFor();
        } catch (Exception e) {
            throw new RuntimeException();
        }
        String[] testArgs = { CMD_CREATE_HASHFILE + "+" + CMD_GENERATE_SCRIPT, PARAM_PREFIX_ROOT_DIR + "/tmp/nasen", PARAM_PREFIX_CRUNCH_SCRIPT + "/home/andre/Desktop/crunch.sh", "--verbose" };
        return testArgs;
    }

    private boolean verbose = false;

    private File rootDir;

    private File hashFile;

    private File crunchScript;

    private PrintStream logOut;

    private Set<String> commands;

    private Cruncher(String[] args) {
        commands = parseCommands(args[0]);
        Collection<String> flags = parseFlags(args);
        if (flags.contains(FLAG_VERBOSE)) {
            System.out.println("+ Verbose mode is on.");
            verbose = true;
        }
        this.rootDir = parseFile(PARAM_PREFIX_ROOT_DIR, args);
        this.hashFile = parseFile(PARAM_PREFIX_HASH_FILE, args);
        this.crunchScript = parseFile(PARAM_PREFIX_CRUNCH_SCRIPT, args);
    }

    public void setLogOut(PrintStream logOut) {
        this.logOut = logOut;
    }

    private void hash() {
        if (rootDir == null || !rootDir.isDirectory() || !rootDir.canRead()) throw new RuntimeException("not a readable directory: " + rootDir);
        if (hashFile == null && commands.contains(CMD_GENERATE_SCRIPT)) {
            try {
                hashFile = File.createTempFile("hashFile", ".sha1");
            } catch (IOException e) {
                throw new RuntimeException("error while creating temporary hash file!", e);
            }
        }
        try {
            if (hashFile == null) throw new NullPointerException("hashFile not specified!");
        } catch (Exception e1) {
            throw new RuntimeException("error while creating file: " + hashFile, e1);
        }
        try {
            Hasher hasher = new Hasher(rootDir);
            hasher.setVerbose(verbose);
            hasher.createHashFile(hashFile, logOut);
        } catch (Exception e) {
            e.printStackTrace(logOut);
            throw new RuntimeException("error during creating hashfile!", e);
        }
    }

    private void generate() {
        if (!hashFile.isFile() || !hashFile.canRead()) throw new RuntimeException("not a readable file: " + hashFile);
        if (crunchScript == null) throw new RuntimeException("not a writeable file: " + crunchScript);
        try {
            ScriptGenerator generator = new ScriptGenerator(hashFile);
            generator.setVerbose(verbose);
            generator.generateCrunchScript(crunchScript, logOut);
        } catch (Exception e) {
            e.printStackTrace(logOut);
            throw new RuntimeException("error during generating script!", e);
        }
    }

    private static Set<String> parseCommands(String command) {
        Set<String> commands;
        if (command.contains("+")) {
            Set<String> tmp = new HashSet<String>();
            for (String cmd : command.split("[+]")) {
                tmp.add(cmd);
            }
            commands = Collections.unmodifiableSet(tmp);
        } else {
            commands = Collections.singleton(command);
        }
        return commands;
    }

    private static File parseFile(String prefix, String[] args) {
        String path = null;
        for (int i = 1; i < args.length; i++) {
            String param = args[i];
            if (param.startsWith(prefix)) {
                if (path != null) throw new RuntimeException("parameter " + prefix + "specified more than once!");
                path = param.substring(prefix.length());
            }
        }
        File f = null;
        if (path != null) {
            f = new File(path);
        }
        return f;
    }

    private static Collection<String> parseFlags(String[] args) {
        Collection<String> flags = new HashSet<String>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals(FLAG_VERBOSE)) {
                flags.add(arg);
            }
        }
        return flags;
    }

    private static void printUsage() {
        p("cruncher");
        p("  A small util that helps to find file duplicates. It can also");
        p("  \"crunch\" a file collection by replacing files by hardlinks.");
        p("  (This is only useful when the contents of the replaced files");
        p("  will not be changed, e.g. song-, photo- or videocollections!)");
        p("");
        p("USAGE:");
        p("  Enter one or more of the available commands.");
        p("  If you wish to use more than 1 command, separate them by '+' chars.");
        p("  The given commands are always executed in the order specified below:");
        p("");
        p("COMMANDS:");
        p(" 1.) " + CMD_CREATE_HASHFILE);
        p("   Creates a MAPPING FILE from hashcode to filepath for files");
        p("   in a given directory. Each line contains the sha1sum");
        p("   followed by the absolute path of the file. The lines are");
        p("   sorted by hascodes, so matching files can be found easily.");
        p("");
        p(" 2.) " + CMD_GENERATE_SCRIPT);
        p("   GENERATES a bash SHELL SCRIPT upon a mapping file (as created");
        p("   with" + CMD_CREATE_HASHFILE + ") that will check each file group");
        p("   sharing the same hash for bitwise equality. if they are equal,");
        p("   they will be replaced by hardlinks to the first file.");
        p("");
        p("PARAMETERS:  (in any order after the command string)");
        p("");
        p(" " + PARAM_PREFIX_ROOT_DIR + "<directory>");
        p("   Specifies the root directory that will be searched for files.");
        p("");
        p(" " + PARAM_PREFIX_HASH_FILE + "<targetfile>");
        p("   Specifies the hashfile to use.");
        p("");
        p(" " + PARAM_PREFIX_CRUNCH_SCRIPT + "<targetfile>");
        p("   Specifies the crunch script file to use.");
        p("");
        p(" " + PARAM_PREFIX_LOG_FILE + "<targetfile>");
        p("   Specifies where the log output will be written to. If omitted, ");
        p("   all log output will be written to standard output.");
        p("");
        p("EXAMPLES:");
        p(" " + CMD_CREATE_HASHFILE + " " + PARAM_PREFIX_ROOT_DIR + "/media/sound " + PARAM_PREFIX_HASH_FILE + "/tmp/hashfile.txt");
        p("      creates a hashfile for the soundcollection.");
        p("");
        p(" " + CMD_GENERATE_SCRIPT + " " + PARAM_PREFIX_HASH_FILE + "/tmp/hashfile.txt " + PARAM_PREFIX_CRUNCH_SCRIPT + "/tmp/crunch.sh");
        p("      generates a crunch-script based on the given hashfile.");
        p("");
        p(" " + CMD_CREATE_HASHFILE + "+" + CMD_GENERATE_SCRIPT + " " + PARAM_PREFIX_ROOT_DIR + "/media/sound " + PARAM_PREFIX_CRUNCH_SCRIPT + "/tmp/crunch.sh");
        p("      generates a crunch-script for the soundcollection.");
        p("      if the hashfile is not given, then a temp file will be used.");
        p(" ");
    }

    private static void p(String msg) {
        System.out.println(msg);
    }

    private static Format format = new SimpleDateFormat("HH:mm:ss");

    static String now() {
        return format.format(new Date());
    }
}
