package edu.neu.ccs.demeterf.batch;

import edu.neu.ccs.demeterf.lib.List;
import edu.neu.ccs.demeterf.dispatch.Type;
import edu.neu.ccs.demeterf.demfgen.*;
import edu.neu.ccs.demeterf.demfgen.classes.*;
import edu.neu.ccs.demeterf.util.CLI;
import edu.neu.ccs.demeterf.batch.classes.*;
import edu.neu.ccs.demeterf.inline.Inline;
import java.io.FileInputStream;

public class Batch {

    static boolean FOR_ALL = false;

    static boolean RESIDUE = true;

    static void p(String s) {
        System.err.print(s);
    }

    public static void main(String args[]) {
        List<String>[] all = CLI.splitArgs(args);
        List<String> opts = all[CLI.OPTS];
        Diff.storeOptions(opts);
        String nonOpt[] = all[CLI.ARGS].toArray(new String[all[CLI.ARGS].length()]);
        List<String> unknown = CLI.invalidOptions(opts, Inline.allOpts);
        if (!unknown.isEmpty()) usage("Unknown Option(s): " + unknown.toString(", ", ""));
        if (nonOpt.length != 1) usage("Not Enough Aguments");
        String trvFile = nonOpt[0];
        for (String p : CLI.separateOption(Inline.PATH, opts)) Type.addPath(p);
        try {
            TRVFile file = TRVFile.parse(new FileInputStream(trvFile));
            GenBatch.createTraversals(file, opts);
            return;
        } catch (edu.neu.ccs.demeterf.batch.classes.ParseException pe) {
            error(pe, "Parse");
        } catch (java.io.FileNotFoundException fe) {
            error(fe, "File");
        } catch (RTFileNotFound fe) {
            error(fe, "File");
        } catch (edu.neu.ccs.demeterf.inline.classes.TypeError te) {
            error(te, "Type");
        } catch (RuntimeException e) {
            error(e, "", true);
        }
        System.exit(1);
    }

    static void error(Throwable t, String type) {
        error(t, type, false);
    }

    static void error(Throwable t, String type, boolean stack) {
        p("\n !! " + type + " Error:\n" + t.getMessage() + "\n\n");
        if (stack) t.printStackTrace();
    }

    static void usage(String msg) {
        p((msg.length() > 0 ? " !! " + msg + "\n" : "") + " !! Usage: Batch [Options] <TRV-File>\n\n" + "    The order/placement of options doesn\'t matter, but the relative\n" + "       order of the manditory ones must be as shown.\n\n" + "\n");
        System.exit(1);
    }
}

class Help {

    static boolean verbose = false;

    static void print(String s) {
        if (verbose) System.err.print(s);
    }

    static void tell(String s) {
        System.err.print(s);
    }
}
