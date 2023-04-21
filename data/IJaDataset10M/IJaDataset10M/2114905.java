package jist.minisim;

import jargs.gnu.*;

/** 
 * Measures cost of a function call.
 *
 * @author Rimon Barr &lt;barr+jist@cs.cornell.edu&gt;
 * @version $Id: loop.java,v 1.1 2006/10/21 00:03:46 lmottola Exp $
 * @since JIST1.0
 */
public final class loop {

    /** benchmark version. */
    public static final String VERSION = "0.1";

    /**
   * Print benchmark version information.
   */
    private static void showVersion() {
        System.out.println("Java loop micro-benchmark v" + VERSION + ", Java in Simulation Time Project.");
        System.out.println("Rimon Barr <barr+jist@cs.cornell.edu>, Cornell University.");
        System.out.println();
    }

    /**
   * Print benchmark command-line syntax.
   */
    private static void showUsage() {
        System.out.println("Usage: loop -n <num> [-w <warm>]");
        System.out.println("       loop -v | -h");
        System.out.println();
        System.out.println("  -h, --help         display this help information");
        System.out.println("  -v, --version      display version information");
        System.out.println("  -n, --num          number of calls");
        System.out.println("  -w, --warm         number of warm-up calls");
        System.out.println();
    }

    /** Parsed command-line options. */
    private static class cmdlineOpts {

        /** print help. */
        public boolean help = false;

        /** print version. */
        public boolean version = false;

        /** number of events to time. */
        public long num = 0;

        /** number of warmup events. */
        public long warm = 0;
    }

    /**
   * Parse command-line options.
   *
   * @param args command-line parameters
   * @return parsed command-line options
   * @throws CmdLineParser.OptionException invalid option encountered
   */
    private static cmdlineOpts parseCommandLineOptions(String[] args) throws CmdLineParser.OptionException {
        cmdlineOpts options = new cmdlineOpts();
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option opt_help = parser.addBooleanOption('h', "help");
        CmdLineParser.Option opt_version = parser.addBooleanOption('v', "version");
        CmdLineParser.Option opt_num = parser.addStringOption('n', "num");
        CmdLineParser.Option opt_warm = parser.addStringOption('w', "warm");
        parser.parse(args);
        if (parser.getOptionValue(opt_help) != null) {
            options.help = true;
        }
        if (parser.getOptionValue(opt_version) != null) {
            options.version = true;
        }
        if (parser.getOptionValue(opt_num) != null) {
            options.num = Long.parseLong((String) parser.getOptionValue(opt_num));
        }
        if (parser.getOptionValue(opt_warm) != null) {
            options.warm = Long.parseLong((String) parser.getOptionValue(opt_warm));
        }
        String[] rest = parser.getRemainingArgs();
        return options;
    }

    /**
   * Benchmark entry point: measure proxy event performance.
   *
   * @param args command-line parameters
   */
    public static void main(String[] args) {
        try {
            final cmdlineOpts options = parseCommandLineOptions(args);
            showVersion();
            if (options.help) {
                showUsage();
                return;
            }
            if (options.version) {
                return;
            }
            if (options.num == 0) {
                showUsage();
                return;
            }
            for (int i = 0; i < options.warm; i++) {
                foo();
            }
            System.out.println("benchmark BEGIN");
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < options.num; i++) {
                foo();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("benchmark END");
            System.out.println("seconds: " + ((endTime - startTime) / 1000.0));
        } catch (CmdLineParser.OptionException e) {
            System.out.println("Error parsing command line: " + e.getMessage());
        }
    }

    /**
   * Dummy method to test loop performance.
   */
    private static void foo() {
    }
}
