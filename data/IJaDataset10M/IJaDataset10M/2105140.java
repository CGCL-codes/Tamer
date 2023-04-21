package com.gorillalogic.gosh.commands;

import com.gorillalogic.dal.Log;
import com.gorillalogic.help.*;

class GoshCategory extends BaseCategory {

    static GoshCategory the = new GoshCategory();

    private GoshCategory() {
        super(Help.factory.gsl());
        addHelpItem(new CurrentContextItem());
        addHelpItem(new TraceItem());
        addHelpItem(new PromptItem());
        addHelpItem(new RedirectOverwriteItem());
        addHelpItem(new RedirectAppendItem());
    }

    public String getHelpId() {
        return "Gosh";
    }

    public void summary(Out out) {
        final String gosh = out.ref("Gosh", "gosh");
        out.print("Gorilla Shell (" + gosh + ")");
    }

    public void detail(Out out) {
        final String var = out.ref("$$");
        final String option = var + out.var("option");
        final String set = out.ref("set");
        final String trigger = out.topic("trigger");
        final String constraint = out.topic("constraint");
        final String run = out.ref("run");
        final String call = out.ref("call");
        final String brk = out.ref("break");
        final String prompt = out.ref("prompt");
        final String trace = out.ref("trace");
        out.print("The Gorilla Shell can be used for exploration, definition,");
        out.print("and management of any GXE data store. It can be run interactively");
        out.print("or through scripts. Gosh executes " + out.ref("GSL"));
        out.brk();
        out.print("Key features include:");
        out.brk();
        out.bullet("Incremental evaluation against a " + out.ref("current context"));
        out.bullet("Single stepping and " + trace + "ing");
        out.bullet("Nested break points with " + out.ref("break"));
        out.bullet("Command " + out.ref("history"));
        out.bullet("Output redirection (see " + out.ref(">>") + ")");
        out.brk();
        out.print("Gosh execution is controllable through a number of user-settable options");
        out.print("that may accessed through " + option + " that can be changed with the ");
        out.print(set + " command. An outline of the execution process, along with");
        out.print("options that control that process, follows.");
        out.brk();
        out.print("A top-level gosh is always in existence, whether the system is run");
        out.print("standalone or through a web server. Access the shell through a web");
        out.print("browser under the System>Shell menu. Gosh instances are also created");
        out.print("during normal execution by the " + run + ", " + call + ", or " + brk + " commands,");
        out.print("or during " + trigger + " or " + constraint + " evaluation.");
        out.brk();
        out.print("Gosh is line-oriented, and always operates based on the first word");
        out.print("of each line. An error is generated if an unknown command appears as");
        out.print("the first word. Set " + option + ".trace to echo command execution to");
        out.print("the console prior to each command being executed.");
        out.brk();
        out.print("When Gosh encounters an error(s), the message is displayed to the console");
        out.print("and the script sequence is exited. However, if " + option + ".break is set,");
        out.print("an interactive nested shell is started at the point, which preserves the");
        out.print("execution state for user analysis. Alternatively, " + option + ".softErrors");
        out.print("forces gosh to try to continue execution after errors are encountered, which");
        out.print("is sometimes useful for more quickly catching larger numbers of errors in scripts");
        out.print("although any subsequent error might of course be occuring simply as a by-product of");
        out.print("an earlier error.");
        out.brk();
        out.print("The execution state is encoded in the gosh " + prompt);
    }

    private class CurrentContextItem extends BaseHelpItem {

        public String getHelpId() {
            return "Current context";
        }

        public void summary(Help.Out out) {
            out.print("Gosh execution state");
        }

        public void detail(Help.Out out) {
            final String gosh = out.ref("Gosh");
            final String gsl = out.ref("GSL");
            out.print(gosh + " maintains execution state against which many " + gsl + "commands");
            out.print("operate by default, when no other expression is explicitly supplied.");
            out.print("The primary component of this execution state is a table whose path");
            out.print("is reflected in the gosh prompt string.");
            out.brk();
            out.print("This \"current context\" is changed by several commands, and the previous");
            out.print("context is maintained on a stack that can be restored with " + out.ref("back"));
            out.print(". The commands that change the current context are as follows:");
            out.brk();
            out.tab("cc");
            out.tab("cd");
            out.tab("add");
            out.tab("call");
            out.tab("return");
            out.tab("run");
            out.tab("shave");
            out.tab("invert");
            out.tab("meta");
            out.tab("data");
            out.tab("optimize");
            out.tab("sortBy");
            out.tab("back");
        }
    }

    private class TraceItem extends BaseHelpItem {

        public String getHelpId() {
            return "trace";
        }

        public void summary(Help.Out out) {
            out.print("Gosh Stepping and Tracing");
        }

        public void detail(Help.Out out) {
            final String gosh = out.ref("Gosh");
            final String gsl = out.ref("GSL");
            final String var = out.ref("$$");
            final String tv = var + "shell.trace";
            final String test = out.ref("test");
            final String step = out.ref("step");
            final String stop = out.ref("stop");
            final String cont = out.ref("cont");
            final String brk = out.ref("break");
            final String ls = out.ref("ls");
            final String msg = out.ref("msg");
            out.print("The boolean variable " + tv + " is set to turn " + gosh + " tracing");
            out.print("on and off. When in effect, most " + gsl + " command lines are displayed");
            out.print("before they are executed. A few commands, such as " + msg + " are not");
            out.print("displayed in this manner.");
            out.brk();
            out.print("For " + gsl + " that is read from a script,");
            out.print("the script name and line number is included as a prefix. Where");
            out.print("commands are pre-processed before execution, the prefix includes");
            out.print("the term 'collect'.");
            out.brk();
            out.print("Method, trigger and constraint execution are also traced. For triggers, the");
            out.print("command portion is traced. For constraints, the evaluation appears in");
            out.print("the form of " + test + " commands.");
            out.brk();
            out.print("Single stepping mode is entered by invoking the " + step + " command.");
            out.print("In this mode, the next pending command (e.g. from an underlying script or");
            out.print("method) is displayed prior to its execution and a recursive shell is then");
            out.print("entered allowing the user control over the actual execution of that command.");
            out.print("Invoking " + step + " while already in step mode executes the next pending");
            out.print("command. Invoking " + cont + " exits step mode and continues execution.");
            out.print("Invoking " + stop + " exits step mode but leaves the current recursive shell");
            out.print("in place.");
            out.brk();
            out.print("The recursive shell in step mode is like the recursive shell that can");
            out.print("be entered with the " + brk + " command. Any operation that can be performed");
            out.print("in any shell is permissable while in step mode. Typically, " + ls + " used");
            out.print("against various expressions to examine program state.");
        }
    }

    private class PromptItem extends BaseHelpItem {

        public String getHelpId() {
            return "prompt";
        }

        public void summary(Help.Out out) {
            out.print("Gosh prompt");
        }

        public void detail(Help.Out out) {
            final String gosh = out.ref("Gosh");
            final String gsl = out.ref("GSL");
            final String run = out.ref("run");
            final String begin = out.ref("begin");
            final String method = out.topic("method");
            out.print("The Gosh prompt line consists of a position indicator");
            out.print("followed by the path of the current context. The position");
            out.print("indicator is an ordered sequnce of characters each with");
            out.print("the following meaning:");
            out.brk();
            out.tab(">", "A recursive Gosh shell invoked by a " + run + ", breakpoint, or " + method + " invocation");
            out.tab("!", "An exception occured");
            out.tab("^", "A transaction was entered thru " + begin + " at top level or \"begin -n\" at any level");
            out.tab("+", "A 'soft' transaction was invoked (\"begin\" without -n), which did not result in a new transaction being created since a transaction already was in place");
            out.brk();
            out.print("Note that the position indicator at Gosh startup is empty.");
        }
    }

    private class RedirectOverwriteItem extends BaseHelpItem {

        public String getHelpId() {
            return ">>";
        }

        public void summary(Help.Out out) {
            out.print("Redirect/overwrite output");
        }

        public void detail(Help.Out out) {
            final String gosh = out.ref("Gosh");
            final String gsl = out.ref("GSL");
            final String cmd = out.var("CMD");
            final String file = out.var("FILE");
            out.print(cmd + " >> " + file);
            out.brk();
            out.print("Execute the GSL " + cmd + " but redirect its output to the specified " + file + ".");
            out.print("If it already exists, " + file + " is overwritten and its previous contents lost.");
            out.brk();
            out.seeAlso(">>>");
        }
    }

    private class RedirectAppendItem extends BaseHelpItem {

        public String getHelpId() {
            return ">>>";
        }

        public void summary(Help.Out out) {
            out.print("Redirect/append output");
        }

        public void detail(Help.Out out) {
            final String gosh = out.ref("Gosh");
            final String gsl = out.ref("GSL");
            final String cmd = out.var("CMD");
            final String file = out.var("FILE");
            out.print(cmd + " >> " + file);
            out.brk();
            out.print("Execute the GSL " + cmd + " but redirect its output to the specified " + file + ".");
            out.print("If it already exists, " + file + " is append to rather than overwritten.");
            out.brk();
            out.seeAlso(">>");
        }
    }
}
