package jp.ne.nifty.iga.midori.shell.cmd.fileAccess;

import jp.ne.nifty.iga.midori.shell.eng.*;
import jp.ne.nifty.iga.midori.shell.dir.*;
import gnu.getopt.*;
import gnu.regexp.*;
import java.io.*;
import java.util.*;

/**
 * Grep command.
 * Grep is a pure-Java clone of the GNU grep utility.  As such, it is much
 * slower and not as full-featured, but it has the advantage of being
 * available on any system with a Java virtual machine.
 *
 * $Revision: 1.1 $
 */
public class grep implements MdShellCommand {

    private static final int BYTE_OFFSET = 0;

    private static final int COUNT = 1;

    private static final int LINE_NUMBER = 2;

    private static final int QUIET = 3;

    private static final int SILENT = 4;

    private static final int NO_FILENAME = 5;

    private static final int REVERT_MATCH = 6;

    private static final int FILES_WITH_MATCHES = 7;

    private static final int LINE_REGEXP = 8;

    private static final int FILES_WITHOUT_MATCH = 9;

    private static final String PROGNAME = "gnu.regexp.util.Grep";

    private static final String PROGVERSION = "1.01";

    String name;

    /** Constructor */
    public grep() {
        name = "grep";
    }

    /**
	 * Setting command name
	 *
	 * @param name command name
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Get command name
	 *
	 * @return command name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Get command description
	 *
	 * @return command description
	 */
    public String getDescription() {
        String description = "";
        BufferedReader br = new BufferedReader(new InputStreamReader((grep.class).getResourceAsStream("GrepUsage.txt")));
        String line;
        try {
            while ((line = br.readLine()) != null) description += line + "\n";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return description;
    }

    /**
	 * Command execution engine
	 *
	 * @param args command arguments
	 * @param commanThread command thread object
	 * @return result code
	 */
    public int execute(String args[], MdShellCommandThread commandThread) {
        MdShellEnv env = commandThread.getEnv();
        PrintWriter out = new PrintWriter(commandThread.getOut());
        grep(args, RESyntax.RE_SYNTAX_GREP, commandThread.getIn(), new PrintStream(commandThread.getOut()), env.getErr(), env);
        out.flush();
        return 0;
    }

    /**
	 * Runs Grep with the specified arguments.  For a list of 
	 * supported options, specify "--help".
	 *
	 * This is the meat of the grep routine, but unlike main(), you can
	 * specify your own syntax and PrintStream to use for output.
	 */
    public static int grep(String[] argv, RESyntax syntax, InputStream in, PrintStream out, PrintStream err, MdShellEnv env) {
        int cflags = 0;
        boolean[] options = new boolean[10];
        LongOpt[] longOptions = { new LongOpt("byte-offset", LongOpt.NO_ARGUMENT, null, 'b'), new LongOpt("count", LongOpt.NO_ARGUMENT, null, 'c'), new LongOpt("no-filename", LongOpt.NO_ARGUMENT, null, 'h'), new LongOpt("ignore-case", LongOpt.NO_ARGUMENT, null, 'i'), new LongOpt("files-with-matches", LongOpt.NO_ARGUMENT, null, 'l'), new LongOpt("help", LongOpt.NO_ARGUMENT, null, '!'), new LongOpt("line-number", LongOpt.NO_ARGUMENT, null, 'n'), new LongOpt("quiet", LongOpt.NO_ARGUMENT, null, 'q'), new LongOpt("silent", LongOpt.NO_ARGUMENT, null, 'q'), new LongOpt("no-messages", LongOpt.NO_ARGUMENT, null, 's'), new LongOpt("revert-match", LongOpt.NO_ARGUMENT, null, 'v'), new LongOpt("line-regexp", LongOpt.NO_ARGUMENT, null, 'x'), new LongOpt("extended-regexp", LongOpt.NO_ARGUMENT, null, 'E'), new LongOpt("fixed-strings", LongOpt.NO_ARGUMENT, null, 'F'), new LongOpt("basic-regexp", LongOpt.NO_ARGUMENT, null, 'G'), new LongOpt("files-without-match", LongOpt.NO_ARGUMENT, null, 'L'), new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'V') };
        Getopt g = new Getopt(PROGNAME, argv, "bchilnqsvxyEFGLV", longOptions);
        int c;
        String arg;
        while ((c = g.getopt()) != -1) {
            switch(c) {
                case 'b':
                    options[BYTE_OFFSET] = true;
                    break;
                case 'c':
                    options[COUNT] = true;
                    break;
                case 'h':
                    options[NO_FILENAME] = true;
                    break;
                case 'i':
                case 'y':
                    cflags |= RE.REG_ICASE;
                    break;
                case 'l':
                    options[FILES_WITH_MATCHES] = true;
                    break;
                case 'n':
                    options[LINE_NUMBER] = true;
                    break;
                case 'q':
                    options[QUIET] = true;
                    break;
                case 's':
                    options[SILENT] = true;
                    break;
                case 'v':
                    options[REVERT_MATCH] = true;
                    break;
                case 'x':
                    options[LINE_REGEXP] = true;
                    break;
                case 'E':
                    syntax = RESyntax.RE_SYNTAX_EGREP;
                    break;
                case 'F':
                    break;
                case 'G':
                    syntax = RESyntax.RE_SYNTAX_GREP;
                    break;
                case 'L':
                    options[FILES_WITHOUT_MATCH] = true;
                    break;
                case 'V':
                    System.err.println(PROGNAME + ' ' + PROGVERSION);
                    return 0;
                case '!':
                    BufferedReader br = new BufferedReader(new InputStreamReader((grep.class).getResourceAsStream("GrepUsage.txt")));
                    String line;
                    try {
                        while ((line = br.readLine()) != null) out.println(line);
                    } catch (IOException ie) {
                    }
                    return 0;
            }
        }
        InputStream is = null;
        RE pattern = null;
        int optind = g.getOptind();
        if (optind >= argv.length) {
            err.println("Usage: " + PROGNAME + " [OPTION]... PATTERN [FILE]...");
            err.println("Try `" + PROGNAME + " --help' for more information.");
            return 2;
        }
        try {
            pattern = new RE(argv[g.getOptind()], cflags, syntax);
        } catch (REException e) {
            err.println("Error in expression: " + e);
            return 2;
        }
        int retval = 1;
        if (argv.length >= g.getOptind() + 2) {
            for (int i = g.getOptind() + 1; i < argv.length; i++) {
                if (argv[i].equals("-")) {
                    if (processStream(pattern, in, options, (argv.length == g.getOptind() + 2) || options[NO_FILENAME] ? null : "(standard input)", out, err)) {
                        retval = 0;
                    }
                } else {
                    MdShellDirFileAbstract file = null;
                    try {
                        file = MdShellDirFileFactory.get(argv[i], "r", env);
                    } catch (MdShellException e) {
                        e.printStackTrace();
                        return -3;
                    }
                    is = file.getInputStream();
                    if (processStream(pattern, is, options, (argv.length == g.getOptind() + 2) || options[NO_FILENAME] ? null : argv[i], out, err)) retval = 0;
                }
            }
        } else {
            if (processStream(pattern, in, options, null, out, err)) retval = 1;
        }
        return retval;
    }

    private static boolean processStream(RE pattern, InputStream is, boolean[] options, String filename, PrintStream out, PrintStream err) {
        int newlineLen = System.getProperty("line.separator").length();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        int count = 0;
        long atByte = 0;
        int atLine = 1;
        String line;
        REMatch match;
        try {
            while ((line = br.readLine()) != null) {
                match = pattern.getMatch(line);
                if (((options[LINE_REGEXP] && pattern.isMatch(line)) || (!options[LINE_REGEXP] && (match != null))) ^ options[REVERT_MATCH]) {
                    count++;
                    if (!options[COUNT]) {
                        if (options[QUIET]) {
                            return true;
                        }
                        if (options[FILES_WITH_MATCHES]) {
                            if (filename != null) out.println(filename);
                            return true;
                        }
                        if (options[FILES_WITHOUT_MATCH]) {
                            return false;
                        }
                        if (filename != null) {
                            out.print(filename);
                            out.print(':');
                        }
                        if (options[LINE_NUMBER]) {
                            out.print(atLine);
                            out.print(':');
                        }
                        if (options[BYTE_OFFSET]) {
                            out.print(atByte + match.getStartIndex());
                            out.print(':');
                        }
                        out.println(line);
                    }
                }
                atByte += line.length() + newlineLen;
                atLine++;
            }
            br.close();
            if (options[COUNT]) {
                if (filename != null) out.println(filename + ':');
                out.println(count);
            }
            if (options[FILES_WITHOUT_MATCH] && count == 0) {
                if (filename != null) out.println(filename);
            }
        } catch (IOException e) {
            err.println(PROGNAME + ": " + e);
        }
        return ((count > 0) ^ options[REVERT_MATCH]);
    }
}
