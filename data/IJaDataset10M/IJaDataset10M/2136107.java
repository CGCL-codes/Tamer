package net.sourceforge.myvd.protocol.ldap.mina.ldap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.io.PrintWriter;
import java.io.PrintStream;

/**
 * This exception is thrown when Base class for nested exceptions.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Revision: 479160 $
 */
public class MultiException extends Exception {

    static final long serialVersionUID = 2889747406899775761L;

    /** Collection of nested exceptions. */
    private Collection<Throwable> nestedExceptions = new ArrayList<Throwable>();

    /**
     * Constructs an Exception without a message.
     */
    public MultiException() {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * 
     * @param message
     *            The message associated with the exception.
     */
    public MultiException(String message) {
        super(message);
    }

    /**
     * Lists the nested exceptions that this Exception encapsulates.
     * 
     * @return an Iterator over the nested exceptions.
     */
    public Iterator<Throwable> listNestedExceptions() {
        return nestedExceptions.iterator();
    }

    /**
     * Gets the size of this nested exception which equals the number of
     * exception nested within.
     * 
     * @return the size of this nested exception.
     */
    public int size() {
        return nestedExceptions.size();
    }

    /**
     * Tests to see if there are any nested exceptions within this
     * MultiException.
     * 
     * @return true if no exceptions are nested, false otherwise.
     */
    public boolean isEmpty() {
        return nestedExceptions.isEmpty();
    }

    /**
     * Add an exeception to this multiexception.
     * 
     * @param nested
     *            exception to add to this MultiException.
     */
    public void addThrowable(Throwable nested) {
        nestedExceptions.add(nested);
    }

    /**
     * Beside printing out the standard stack trace this method prints out the
     * stack traces of all the nested exceptions.
     * 
     * @param out
     *            PrintWriter to write the nested stack trace to.
     */
    public void printStackTrace(PrintWriter out) {
        super.printStackTrace(out);
        out.println("Nested exceptions to follow:\n");
        boolean isFirst = true;
        for (Throwable throwable : nestedExceptions) {
            if (isFirst) {
                isFirst = false;
            } else {
                out.println("\n\t<<========= Next Nested Exception" + " ========>>\n");
            }
            throwable.printStackTrace();
        }
        out.println("\n\t<<========= Last Nested Exception" + " ========>>\n");
    }

    /**
     * Beside printing out the standard stack trace this method prints out the
     * stack traces of all the nested exceptions.
     * 
     * @param out
     *            PrintStream to write the nested stack trace to.
     */
    public void printStackTrace(PrintStream out) {
        super.printStackTrace(out);
        out.println("Nested exceptions to follow:\n");
        boolean isFirst = true;
        for (Throwable throwable : nestedExceptions) {
            if (isFirst) {
                isFirst = false;
            } else {
                out.println("\n\t<<========= Next Nested Exception" + " ========>>\n");
            }
            throwable.printStackTrace();
        }
        out.println("\n\t<<========= Last Nested Exception" + " ========>>\n");
    }

    /**
     * Beside printing out the standard stack trace this method prints out the
     * stack traces of all the nested exceptions using standard error.
     */
    public void printStackTrace() {
        this.printStackTrace(System.err);
    }
}
