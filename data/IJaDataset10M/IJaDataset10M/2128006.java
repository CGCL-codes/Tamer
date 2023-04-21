package com.gorillalogic.gosh.commands;

import com.gorillalogic.gosh.*;
import com.gorillalogic.dal.*;
import com.gorillalogic.dal.parser.SourceLine;
import com.gorillalogic.help.Help;
import java.io.PrintWriter;

public class StatCommand extends BaseCommand {

    private Command command = null;

    public void summary(Help.Out out) {
        out.print("Report system statistics");
    }

    public void detail(Help.Out out) {
        final String cmd = out.var("GSL_COMMAND");
        out.print("stat " + cmd);
        out.brk();
        out.print("Reports system statistics, as follows:");
        out.brk();
        out.println(1, "System time since last call to stat");
        out.println(1, "Free JVM memory");
        out.println(1, "Total JVM memory");
        out.println(1, "Max JVM memory");
        out.brk();
        out.print("Changes in the memory values are noted where they occur.");
        out.brk();
        out.print("If " + cmd + " is supplied, that command is executed and the");
        out.print("reported delta values are made for that command only,");
        out.print("specifically for the execution of that command (excluding");
        out.print("pre-processing and parsing of it).");
    }

    public void preProcess(Goshell gosh, PrintWriter out) throws AccessException {
        if (src().more()) {
            SourceLine commandSrc = new SourceLine(src().toString());
            command = gosh.mapCommand(commandSrc, gosh);
            command.preProcess(gosh, out);
        }
        super.preProcess(gosh, out);
    }

    static long lastTime;

    static long lastFreeMemory;

    static long lastTotalMemory;

    static long lastMaxMemory;

    static {
        update();
    }

    static void update() {
        lastTime = System.currentTimeMillis();
        Runtime rt = Runtime.getRuntime();
        lastFreeMemory = rt.freeMemory();
        lastTotalMemory = rt.totalMemory();
        lastMaxMemory = rt.maxMemory();
    }

    private String report(long pre, long post) {
        String delta = "";
        if (pre < post) {
            delta = " (increased by " + (post - pre) + ')';
        } else if (pre > post) {
            delta = " (decreased by " + ((pre - post) / 1000) + ')';
        }
        return String.valueOf(post / 1000) + delta;
    }

    public void exec(Goshell gosh, PrintWriter out) throws AccessException {
        String msg = "  Time since last stat: ";
        if (command != null) {
            msg = "        Execution time: ";
            update();
            command.exec(gosh, out);
        }
        long thisTime = System.currentTimeMillis();
        float diff = thisTime - lastTime;
        lastTime = thisTime;
        float secs = diff / 1000;
        Runtime rt = Runtime.getRuntime();
        out.println("");
        out.println(msg + secs + " seconds");
        out.println("       Free JVM memory: " + report(lastFreeMemory, rt.freeMemory()) + " Kbytes");
        out.println("      Total JVM memory: " + report(lastTotalMemory, rt.totalMemory()) + " Kbytes");
        out.println("    Maximum JVM memory: " + report(lastMaxMemory, rt.maxMemory()) + " Kbytes");
        out.println("");
    }
}
