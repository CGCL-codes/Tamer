package com.fujitsu.arcon.njs.admin;

import java.util.*;
import java.io.*;

/**
 * Abort AbstractActions on NJS
 *
 * @author Sven van den Berghe, fujitsu
 *
 * @version $Revision: 1.2 $ $Date: 2004/06/30 13:45:27 $
 *
 **/
public class Abort extends AdminCommand {

    private com.fujitsu.arcon.njs.KnownActionDB kadb;

    public Abort(com.fujitsu.arcon.njs.KnownActionDB kadb) {
        super("abort");
        short_help = "abort [help] selection";
        long_help = short_help + "\n" + "  Abort actions known to the NJS (results kept by NJS).\n" + "  selection :\n" + "    ajos = abort all root ajos (use CAREFULLY)\n" + "     all = abort all actions (use CAREFULLY)\n" + "      id = abort the action\n" + "  selection expressions are allowed (see manual)";
        this.kadb = kadb;
    }

    public void process(String cmd_line, BufferedReader in, PrintWriter out) {
        StringTokenizer st = new StringTokenizer(cmd_line);
        st.nextToken();
        String expr = "ajos";
        if (st.hasMoreTokens()) {
            String t = st.nextToken();
            if (t.equalsIgnoreCase("help")) {
                out.println("OK\n" + long_help);
                return;
            }
            expr = t;
            while (st.hasMoreTokens()) {
                expr += " " + st.nextToken();
            }
        } else {
            out.println("ERROR\n No default for Abort! Please supply an AbstractAction selection.");
            return;
        }
        try {
            ApplyControl.doAbort(Select.select(expr, kadb), kadb);
            out.println("OK\nActions aborted");
        } catch (Exception ex) {
            out.println("ERROR\nErrors aborting actions: " + ex);
        }
        return;
    }
}
