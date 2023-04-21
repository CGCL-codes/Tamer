package joeq.ClassLib.sun14_linux.java.lang;

/**
 * StackTraceElement
 *
 * @author  John Whaley <jwhaley@alum.mit.edu>
 * @version $Id: StackTraceElement.java 1451 2004-03-09 06:27:08Z jwhaley $
 */
public final class StackTraceElement {

    private java.lang.String declaringClass;

    private java.lang.String methodName;

    private java.lang.String fileName;

    private int lineNumber;

    StackTraceElement(java.lang.String declaringClass, java.lang.String methodName, java.lang.String fileName, int lineNumber) {
        this.declaringClass = declaringClass;
        this.methodName = methodName;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
    }
}
