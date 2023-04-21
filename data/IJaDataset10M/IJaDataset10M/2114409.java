package consciouscode.util;

import org.apache.commons.logging.Log;

/**
   Basic logging interface.

   @deprecated We are switching to Jakarta Commons Logging framework.
   Use of Logger should be replaced by {@link Log}.
*/
public interface Logger extends Log {

    /**
       Record a debugging message.

       @deprecated Use {@link Log#debug(Object)}.
    */
    public void logDebug(String message);

    /**
        Record an error message.

       @deprecated Use {@link Log#error(Object)}.
    */
    public void logError(String message);

    /**
        Record an error message, along with the stack trace of the exception.

       @deprecated Use {@link Log#error(Object,Throwable)}.
    */
    public void logError(String message, Throwable exception);

    /**
        Record an error message, along with the stack trace of the exception.

       @deprecated Use {@link Log#error(Object,Throwable)}.
    */
    public void logError(Throwable exception);
}
