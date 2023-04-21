package cck.parser;

import cck.text.Terminal;

/**
 * The <code>SourceError</code> class represents an error that occurs in a source file,
 * such as a verification error (in the case of jintgen), a compilation error (in the
 * case of a compiler), or an assembler error. A <code>SourceError</code> occurs
 * in a named source file at a particular location (line number and column number).
 *
 * @author Ben L. Titzer
 */
public class SourceException extends SourceError {

    /**
     * The <code>trace</code> field stores a reference to the stack trace corresponding
     * to where the source exception ocurred.
     */
    public final StackTrace trace;

    /**
     * The default constructor for a source error accepts an error type, a program
     * point which indicates the location in a file where the error occured, a message,
     * and a list of parameters to the error (such as the name of a class or method
     * where the error occurred).
     *
     * @param type a string that indicates the type of error that occured such as
     *             "Undefined Variable"
     * @param p    the point in the file where the error occurred
     * @param msg  a short message reported to the user that explains the error
     * @param ps   a list of parameters to the error such as the name of the variable
     *             that is undeclared, etc.
     */
    public SourceException(String type, StackTrace p, String msg, String[] ps) {
        super(type, p == null ? null : p.getSourcePoint(), msg, null);
        trace = p;
    }

    /**
     * The <code>report()</code> method generates a textual report of this error
     * for the user. For source errors, this method will report the file, line number,
     * and column number where this error occurred.
     */
    public void report() {
        Terminal.print("");
        Terminal.printRed(errorType);
        Terminal.println(": " + message + ' ');
        for (StackTrace tr = trace; tr != null; tr = tr.prev) {
            Terminal.print("\t");
            Terminal.print("in ");
            Terminal.printGreen(tr.getMethod() + ' ');
            SourcePoint p = tr.getSourcePoint();
            if (p != null) p.report();
            Terminal.nextln();
        }
    }
}
