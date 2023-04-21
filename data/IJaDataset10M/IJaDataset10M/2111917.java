package mzmatch.ipeak;

import java.io.*;
import cmdline.*;
import peakml.*;
import peakml.io.*;
import peakml.io.peakml.*;
import mzmatch.util.*;

public class Example {

    static final String version = "1.0.0";

    static final String application = "Example";

    @OptionsClass(name = application, version = version, author = "", description = "General description of the function of the tool.")
    public static class Options {

        @Option(name = "i", param = "filename", type = Option.Type.REQUIRED_ARGUMENT, usage = "Descriptive text for the option.")
        public String input = null;

        @Option(name = "o", param = "filename", type = Option.Type.REQUIRED_ARGUMENT, usage = "Descriptive text for the option.")
        public String output = null;

        @Option(name = "h", param = "", type = Option.Type.NO_ARGUMENT, usage = "When this is set, the help is shown.")
        public boolean help = false;

        @Option(name = "v", param = "", type = Option.Type.NO_ARGUMENT, usage = "When this is set, the progress is shown on the standard output.")
        public boolean verbose = false;
    }

    @SuppressWarnings("unchecked")
    public static void main(String args[]) {
        try {
            Tool.init();
            Options options = new Options();
            CmdLineParser cmdline = new CmdLineParser(options);
            cmdline.parse(args);
            if (options.help) {
                Tool.printHeader(System.out, application, version);
                cmdline.printUsage(System.out, "");
                return;
            }
            if (options.verbose) {
                Tool.printHeader(System.out, application, version);
                cmdline.printOptions();
            }
            {
            }
            InputStream input = System.in;
            if (options.input != null) input = new FileInputStream(options.input);
            OutputStream output = System.out;
            if (options.output != null) output = new FileOutputStream(options.output);
            ParseResult result = PeakMLParser.parse(input, true);
            IPeakSet<IPeak> peaks = (IPeakSet<IPeak>) result.measurement;
            PeakMLWriter.write(result.header, peaks.getPeaks(), null, output, null);
        } catch (Exception e) {
            Tool.unexpectedError(e, application);
        }
    }
}
