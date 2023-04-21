package ws.prova.ide.eclipse;

import java.io.OutputStream;
import java.io.PrintStream;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * @author seb
 * A class to create a console output monitor that will echo stdout and stderr to
 * the eclipse console.
 */
public final class ProvaConsole {

    private static ProvaConsole instance;

    private MessageConsoleStream stdout;

    private MessageConsoleStream stderr;

    /**
   * Get the ProvaConsole
   * @return console
   */
    public static ProvaConsole getInstance() {
        if (!(instance instanceof ProvaConsole)) instance = new ProvaConsole();
        return instance;
    }

    private ProvaConsole() {
        MessageConsole console = new MessageConsole("Prova Console", null);
        ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
        ConsolePlugin.getDefault().getConsoleManager().showConsoleView(console);
        stdout = console.newMessageStream();
        stdout.setColor(new Color(null, 0, 0, 255));
        System.setOut(new PrintStreamAdapter(stdout));
        stderr = console.newMessageStream();
        stderr.setColor(new Color(null, 255, 0, 0));
        System.setErr(new PrintStreamAdapter(stderr));
    }

    private class PrintStreamAdapter extends PrintStream {

        private MessageConsoleStream stream;

        private PrintStreamAdapter(MessageConsoleStream s) {
            super(System.out);
            stream = s;
        }

        String logMessage() {
            return "";
        }

        public PrintStreamAdapter(OutputStream arg0) {
            super(arg0);
        }

        public void print(boolean b) {
            stream.print(logMessage() + b);
        }

        public void print(char c) {
            stream.print(logMessage() + c);
        }

        public void print(char[] c) {
            stream.print(logMessage() + c.toString());
        }

        public void print(double d) {
            stream.print(logMessage() + d);
        }

        public void print(float f) {
            stream.print(logMessage() + f);
        }

        public void print(int i) {
            stream.print(logMessage() + i);
        }

        public void print(Object o) {
            stream.print(logMessage() + o);
        }

        public void print(String s) {
            stream.print(logMessage() + s);
        }

        public void println(boolean b) {
            stream.println(logMessage() + b);
        }

        public void println(char c) {
            stream.println(logMessage() + c);
        }

        public void println(char[] c) {
            stream.println(logMessage() + c.toString());
        }

        public void println(double d) {
            stream.println(logMessage() + d);
        }

        public void println(float f) {
            stream.println(logMessage() + f);
        }

        public void println(int i) {
            stream.println(logMessage() + i);
        }

        public void println(Object o) {
            stream.println(logMessage() + o);
        }

        public void println(String s) {
            stream.println(logMessage() + s);
        }

        public void println() {
            stream.println(logMessage());
        }

        public boolean checkError() {
            return false;
        }

        public void close() {
        }

        public void flush() {
        }

        protected void setError() {
        }

        public void write() {
        }

        public void write(byte b) {
        }
    }
}
