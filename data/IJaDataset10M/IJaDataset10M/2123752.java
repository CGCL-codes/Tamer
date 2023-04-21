package ghm.follow.io;

import java.io.PrintStream;

/**
 * Implementation of {@link OutputDestination} which prints Strings to a
 * {@link PrintStream}.
 * 
 * @see OutputDestination
 * @see PrintStream
 * @author <a href="mailto:greghmerrill@yahoo.com">Greg Merrill</a>
 */
public class PrintStreamDestination implements OutputDestination {

    protected PrintStream printStream;

    public PrintStreamDestination(PrintStream printStream) {
        this.printStream = printStream;
    }

    public PrintStream getPrintStream() {
        return printStream;
    }

    public void setPrintStream(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void print(String s) {
        printStream.print(s);
    }

    public void clear() {
    }
}
