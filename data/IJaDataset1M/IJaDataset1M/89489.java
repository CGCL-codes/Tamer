package jcompiler;

import java.io.*;
import java.util.Vector;
import org.gjt.sp.jedit.EditBus;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.View;
import org.gjt.sp.util.Log;
import errorlist.DefaultErrorSource;
import console.Console;
import console.ConsolePane;
import console.Output;
import console.Shell;

/**
 * a JCompiler shell for the Console plugin.
 */
public class JCompilerShell extends Shell {

    public JCompilerShell() {
        super("JCompiler");
    }

    /**
	 * print an information message.
	 *
	 * @param  output  where to put the information
	 */
    public void printInfoMessage(Output output) {
        output.print(null, jEdit.getProperty("jcompiler.msg.info"));
    }

    /**
	 * prints a prompt to the specified console.
	 *
	 * @param console  the console.
	 * @param output  the output.
	 */
    public void printPrompt(Console console, Output output) {
        output.writeAttrs(ConsolePane.colorAttributes(console.getInfoColor()), jEdit.getProperty("jcompiler.msg.prompt"));
        output.writeAttrs(null, " ");
    }

    /**
	 * execute a command.
	 *
	 * @param  console  the Console where the command was entered.
	 * @param  output  where the output should go.
	 * @param  command  the entered command.
	 */
    public void execute(Console console, String input, Output output, Output error, String command) {
        stop(console);
        String cmd = command.trim();
        DefaultErrorSource errorSource = console.getErrorSource();
        if ("compile".equals(cmd)) {
            errorSource.clear();
            compileTask = new JCompilerTask(false, false, console, output, errorSource);
        } else if ("compilepkg".equals(cmd)) {
            errorSource.clear();
            compileTask = new JCompilerTask(true, false, console, output, errorSource);
        } else if ("rebuildpkg".equals(cmd)) {
            errorSource.clear();
            compileTask = new JCompilerTask(true, true, console, output, errorSource);
        } else if ("javac".equals(cmd)) {
            compileTask = new JCompilerTask(new String[] {}, console, output, errorSource);
        } else if (cmd.startsWith("javac ")) {
            String[] args;
            try {
                args = parseCmdLineArguments(cmd.substring(6), console.getView().getBuffer().getPath());
            } catch (IOException ex) {
                output.print(console.getErrorColor(), jEdit.getProperty("jcompiler.msg.errorCommandLine", new Object[] { ex }));
                return;
            }
            errorSource.clear();
            compileTask = new JCompilerTask(args, console, output, errorSource);
        } else if ("help".equals(cmd)) {
            printInfoMessage(output);
            output.commandDone();
        } else {
            output.print(console.getInfoColor(), jEdit.getProperty("jcompiler.msg.errorUnknownCommand", new Object[] { cmd }));
            output.commandDone();
        }
    }

    public void stop(Console console) {
        if (compileTask != null) {
            if (compileTask.isAlive()) {
                Output output = console.getOutput();
                output.print(console.getErrorColor(), jEdit.getProperty("jcompiler.msg.stopping"));
                compileTask.stop();
                output.commandDone();
            }
            compileTask = null;
        }
    }

    public boolean waitFor(Console console) {
        if (compileTask != null) {
            try {
                synchronized (compileTask) {
                    compileTask.wait();
                }
            } catch (InterruptedException ie) {
                return false;
            }
            compileTask = null;
        }
        return true;
    }

    private String[] parseCmdLineArguments(String cmd, String bufferFileName) throws IOException {
        cmd = JCompiler.expandVariables(cmd, bufferFileName);
        cmd = cmd.replace('\\', NON_PRINTABLE);
        StreamTokenizer st = new StreamTokenizer(new StringReader(cmd));
        st.resetSyntax();
        st.wordChars('!', 255);
        st.whitespaceChars(0, ' ');
        st.quoteChar('"');
        st.quoteChar('\'');
        Vector args = new Vector();
        loop: for (; ; ) {
            switch(st.nextToken()) {
                case StreamTokenizer.TT_EOF:
                    break loop;
                case StreamTokenizer.TT_WORD:
                case '"':
                case '\'':
                    args.addElement(st.sval.replace(NON_PRINTABLE, '\\'));
                    break;
            }
        }
        Log.log(Log.DEBUG, this, "arguments=" + args);
        String[] array = new String[args.size()];
        args.copyInto(array);
        return array;
    }

    private JCompilerTask compileTask;

    private static final char NON_PRINTABLE = 127;
}
