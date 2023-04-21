package com.gorillalogic.gosh.commands;

import com.gorillalogic.gosh.*;
import com.gorillalogic.dal.*;
import com.gorillalogic.help.Help;
import java.io.PrintWriter;

public class OptimizeCommand extends ExprCommand {

    private boolean verbose = false;

    private int level = 0;

    boolean requiresArgument() {
        return false;
    }

    public void summary(Help.Out out) {
        out.print("Optimize context");
    }

    public void detail(Help.Out out) {
        final String options = out.var("options");
        final String expr = out.var("EXPR");
        final String cx = out.ref("current context");
        final String unlink = out.ref("unlink");
        out.print("optimize " + options + " " + expr);
        out.println(1, "where " + options + " include:");
        out.brk();
        out.println(2, "-ln level n");
        out.println(2, "-v verbose");
        out.brk();
        out.print("Change the current context to an optimized variant of " + expr);
        out.print("(or the " + cx + " if no " + expr + " specified) computing the same result.");
        out.brk();
        out.print("The default optimization level is 0. Possible values are:");
        out.tab("0", "Remove invalid path elements");
        out.tab("1", "Remove redundant path elements.");
        out.tab("2", "More extensive redundancy removal, where the result may not return the same value if eval()'d.");
        out.brk();
        out.print("Invalid path elements arise when update operations such as " + unlink + " invalidate");
        out.print("paths which are currently in use. Optimize always fixes these situations by 'breaking");
        out.print("off' the invalid component and returning a table the yields the same result but with");
        out.print("a valid path.");
    }

    public void preProcess(Goshell gosh, PrintWriter out) throws AccessException {
        ArgAgent agent = prepareArgs();
        ArgAgent.IntArg levelArg = agent.intArg("l", 0);
        level = levelArg.intValue();
        ArgAgent.BooleanArg verboseArg = agent.booleanArg("v", false);
        verbose = verboseArg.booleanValue();
        super.preProcess(gosh, out);
    }

    public void exec(Goshell gosh, PrintWriter out) throws AccessException {
        boolean success = false;
        Table local = applyExprToScope(gosh, out);
        if (local != null) {
            Table rez = local.optimize(level);
            if (rez != local) {
                gosh.push(rez, true);
                success = true;
            }
        }
        if (verbose) {
            summarizeResults(out, success);
        }
    }

    private void summarizeResults(PrintWriter out, boolean success) {
        out.println("");
        out.println("   ****************************************************");
        out.println("   ***           Results of optimization:           ***");
        out.println("   ***==============================================***");
        if (success) {
            out.println("   ***              Optimizations made              ***");
        } else {
            out.println("   ***  No optimization made, no change to context  ***");
        }
        out.println("   ****************************************************");
        out.println("");
    }
}
