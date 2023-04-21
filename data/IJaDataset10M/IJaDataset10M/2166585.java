package org.bdgp.util;

import java.io.*;
import java.util.*;

/**
 * A general purpose Error that can wrap another Throwable object.
 * <P>
 * NestedError is an Error that should be thrown whenever some exceptional and
 * unforseable event takes place. For example, sometimes exceptions can be
 * thrown by a given method, but not when the calling method is a member of
 * the same class. In this case, the try-catch block would collect the
 * 'impossible' exception and throw a NestedError that wraps it.
 *
 * @author Matthew Pocock
 */
public class NestedError extends Error {

    /**
   * The wrapped Throwable object
   */
    private Throwable subException;

    public NestedError(String message) {
        this(null, message);
    }

    public NestedError(Throwable ex) {
        this(ex, null);
    }

    public NestedError(Throwable ex, String message) {
        super(message);
        this.subException = ex;
    }

    public NestedError() {
        this(null, null);
    }

    public Throwable getWrappedException() {
        return subException;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream ps) {
        printStackTrace(new PrintWriter(ps));
    }

    public void printStackTrace(PrintWriter pw) {
        if (subException != null) {
            StringWriter sw1 = new StringWriter();
            subException.printStackTrace(new PrintWriter(sw1));
            String mes1 = sw1.toString();
            StringWriter sw2 = new StringWriter();
            super.printStackTrace(new PrintWriter(sw2));
            String mes2 = sw2.toString();
            try {
                Vector lines1 = lineSplit(new BufferedReader(new StringReader(mes1)));
                Vector lines2 = lineSplit(new BufferedReader(new StringReader(mes2)));
                Vector removeThese = new Vector();
                for (int i = 0; i < lines1.size() && i < lines2.size(); i++) {
                    Object s1 = lines1.elementAt(lines1.size() - i - 1);
                    Object s2 = lines2.elementAt(lines2.size() - i - 1);
                    if (s1.equals(s2)) removeThese.addElement(s1); else break;
                }
                for (int i = 0; i < removeThese.size(); i++) {
                    lines1.removeElement(removeThese.elementAt(i));
                }
                for (int i = 0; i < lines1.size(); i++) {
                    pw.println(lines1.elementAt(i));
                }
                pw.print("rethrown as ");
                pw.print(mes2);
            } catch (IOException ioe) {
                throw new Error("Coudn't merge stack-traces");
            }
        } else {
            super.printStackTrace(pw);
        }
        pw.flush();
    }

    private Vector lineSplit(BufferedReader in) throws IOException {
        Vector lines = new Vector();
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            lines.addElement(line);
        }
        return lines;
    }
}
