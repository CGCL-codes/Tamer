package org.apache.harmony.luni.util;

import java.io.PrintStream;

/**
 * This exception is thrown by methods that are not currently implemented, so
 * that programs that call the stubs fail early and predictably.
 * 
 */
public class NotImplementedException extends RuntimeException {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor.
	 */
    public NotImplementedException() {
        this(System.err);
    }

    @SuppressWarnings("nls")
    public NotImplementedException(PrintStream stream) {
        super();
        stream.println("*** NOT IMPLEMENTED EXCEPTION ***");
        StackTraceElement thrower = getStackTrace()[0];
        stream.println("*** thrown from class  -> " + thrower.getClassName());
        stream.println("***             method -> " + thrower.getMethodName());
        stream.print("*** defined in         -> ");
        if (thrower.isNativeMethod()) {
            stream.println("a native method");
        } else {
            String fileName = thrower.getFileName();
            if (fileName == null) {
                stream.println("an unknown source");
            } else {
                int lineNumber = thrower.getLineNumber();
                stream.print("the file \"" + fileName + "\"");
                if (lineNumber >= 0) {
                    stream.print(" on line #" + lineNumber);
                }
                stream.println();
            }
        }
    }

    /**
	 * Constructor that takes a reason message.
	 * 
	 * @param detailMessage
	 */
    public NotImplementedException(String detailMessage) {
        super(detailMessage);
    }

    /**
	 * Constructor that takes a reason and a wrapped exception.
	 * 
	 * @param detailMessage
	 * @param throwable
	 */
    public NotImplementedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
	 * Constructor that takes a wrapped exception.
	 * 
	 * @param throwable
	 */
    public NotImplementedException(Throwable throwable) {
        super(throwable);
    }
}
