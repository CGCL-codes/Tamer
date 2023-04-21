package org.apache.jdo.impl.enhancer;

import java.io.PrintWriter;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Set of options used by the JDO enhancer and its test programs.
 *
 * @author Martin Zaun
 */
public class GenericOptions extends OptionSet {

    /**
     * The help option.
     */
    public final HelpOption help = createHelpOption("help", "h", "              : print usage message and exit");

    /**
     * The verbose option.
     */
    public final FlagOption verbose = createFlagOption("verbose", "v", "           : print verbose messages");

    /**
     * The timing option.
     */
    public final FlagOption doTiming = createFlagOption("timing", "t", "            : do timing messures");

    /**
     * Creates an instance.
     */
    public GenericOptions(PrintWriter out, PrintWriter err) {
        super(out, err);
    }

    /**
     * Tests the class.
     */
    public static void main(String[] args) {
        final PrintWriter out = new PrintWriter(System.out, true);
        out.println("--> GenericOptions.main()");
        final GenericOptions options = new GenericOptions(out, out);
        out.println("    options.process() ...");
        int res = options.process(args);
        out.println("    return value: " + res);
        out.println("<-- GenericOptions.main()");
    }
}
