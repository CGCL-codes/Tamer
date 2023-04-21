package org.hip.kernel.exc;

/**
 * 	Concrete default implementation for ErrorHandler. The actual
 *  implementation simply prints out the error-text to standard error.
 *
 * 	@author	Benno Luthiger
 */
public class DefaultErrorHandler extends AbstractExceptionHandler {

    private static ExceptionHandler cInstance = null;

    /**
 *
 */
    public DefaultErrorHandler() {
        super();
    }

    /**
 * @return java.lang.Throwable
 * @param inThrowable 	java.lang.Throwable
 * @param inId 			java.lang.String
 * @param inToFatal 	boolean
 */
    public Throwable convert(Throwable inThrowableToBeConverted, String inId) {
        VThrowable outException = new VError(inId, null);
        outException.setRootCause(inThrowableToBeConverted);
        ((Throwable) outException).fillInStackTrace();
        return (Throwable) outException;
    }

    /**
 * 	Returns the single instance of this handler class.
 * 
 * 	@return org.hip.kernel.exc.ExceptionHandler
 */
    public static ExceptionHandler instance() {
        if (cInstance == null) {
            cInstance = new DefaultErrorHandler();
        }
        return cInstance;
    }

    /**
 * 	A Default implementation to handle the exception. Simply
 *  prints the exception to the standard error stream.
 *
 * 	@param inCatchingObject java.lang.Object
 * 	@param inThrowable java.lang.Throwable
 * 	@param inPrintStackTrace boolean
 */
    protected void protectedHandle(Object inCatchingObject, Throwable inThrowable, boolean inPrintStackTrace) {
        DefaultExceptionWriter.printOut(inCatchingObject, inThrowable, inPrintStackTrace);
    }
}
