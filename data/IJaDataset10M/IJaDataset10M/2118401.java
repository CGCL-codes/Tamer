package org.zkforge.apache.commons.el;

import java.io.PrintStream;
import java.text.MessageFormat;
import org.zkoss.xel.XelException;

/**
 *
 * <p>The evaluator may pass an instance of this class to operators
 * and expressions during evaluation.  They should use this to log any
 * warning or error messages that might come up.  This allows all of
 * our logging policies to be concentrated in one class.
 *
 * <p>Errors are conditions that are severe enough to abort operation.
 * Warnings are conditions through which the operation may continue,
 * but which should be reported to the developer.
 * 
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: luehe $
 **/
public class Logger {

    PrintStream mOut;

    /**
   *
   * Constructor
   *
   * @param pOut the PrintStream to which warnings should be printed
   **/
    public Logger(PrintStream pOut) {
        mOut = pOut;
    }

    /**
   *
   * Returns true if the application should even bother to try logging
   * a warning.
   **/
    public boolean isLoggingWarning() {
        return false;
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pMessage, Throwable pRootCause) throws XelException {
        if (isLoggingWarning()) {
            if (pMessage == null) {
                System.out.println(pRootCause);
            } else if (pRootCause == null) {
                System.out.println(pMessage);
            } else {
                System.out.println(pMessage + ": " + pRootCause);
            }
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate) throws XelException {
        if (isLoggingWarning()) {
            logWarning(pTemplate, null);
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(Throwable pRootCause) throws XelException {
        if (isLoggingWarning()) {
            logWarning(null, pRootCause);
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate, Object pArg0) throws XelException {
        if (isLoggingWarning()) {
            logWarning(MessageFormat.format(pTemplate, new Object[] { "" + pArg0 }));
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate, Throwable pRootCause, Object pArg0) throws XelException {
        if (isLoggingWarning()) {
            logWarning(MessageFormat.format(pTemplate, new Object[] { "" + pArg0 }), pRootCause);
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate, Object pArg0, Object pArg1) throws XelException {
        if (isLoggingWarning()) {
            logWarning(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1 }));
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate, Throwable pRootCause, Object pArg0, Object pArg1) throws XelException {
        if (isLoggingWarning()) {
            logWarning(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1 }), pRootCause);
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate, Object pArg0, Object pArg1, Object pArg2) throws XelException {
        if (isLoggingWarning()) {
            logWarning(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2 }));
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate, Throwable pRootCause, Object pArg0, Object pArg1, Object pArg2) throws XelException {
        if (isLoggingWarning()) {
            logWarning(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2 }), pRootCause);
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate, Object pArg0, Object pArg1, Object pArg2, Object pArg3) throws XelException {
        if (isLoggingWarning()) {
            logWarning(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2, "" + pArg3 }));
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate, Throwable pRootCause, Object pArg0, Object pArg1, Object pArg2, Object pArg3) throws XelException {
        if (isLoggingWarning()) {
            logWarning(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2, "" + pArg3 }), pRootCause);
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate, Object pArg0, Object pArg1, Object pArg2, Object pArg3, Object pArg4) throws XelException {
        if (isLoggingWarning()) {
            logWarning(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2, "" + pArg3, "" + pArg4 }));
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate, Throwable pRootCause, Object pArg0, Object pArg1, Object pArg2, Object pArg3, Object pArg4) throws XelException {
        if (isLoggingWarning()) {
            logWarning(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2, "" + pArg3, "" + pArg4 }), pRootCause);
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate, Object pArg0, Object pArg1, Object pArg2, Object pArg3, Object pArg4, Object pArg5) throws XelException {
        if (isLoggingWarning()) {
            logWarning(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2, "" + pArg3, "" + pArg4, "" + pArg5 }));
        }
    }

    /**
   *
   * Logs a warning
   **/
    public void logWarning(String pTemplate, Throwable pRootCause, Object pArg0, Object pArg1, Object pArg2, Object pArg3, Object pArg4, Object pArg5) throws XelException {
        if (isLoggingWarning()) {
            logWarning(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2, "" + pArg3, "" + pArg4, "" + pArg5 }), pRootCause);
        }
    }

    /**
   *
   * Returns true if the application should even bother to try logging
   * an error.
   **/
    public boolean isLoggingError() {
        return true;
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pMessage, Throwable pRootCause) throws XelException {
        if (isLoggingError()) {
            if (pMessage == null) {
                throw new XelException(pRootCause);
            } else if (pRootCause == null) {
                throw new XelException(pMessage);
            } else {
                throw new XelException(pMessage, pRootCause);
            }
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate) throws XelException {
        if (isLoggingError()) {
            logError(pTemplate, null);
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(Throwable pRootCause) throws XelException {
        if (isLoggingError()) {
            logError(null, pRootCause);
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate, Object pArg0) throws XelException {
        if (isLoggingError()) {
            logError(MessageFormat.format(pTemplate, new Object[] { "" + pArg0 }));
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate, Throwable pRootCause, Object pArg0) throws XelException {
        if (isLoggingError()) {
            logError(MessageFormat.format(pTemplate, new Object[] { "" + pArg0 }), pRootCause);
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate, Object pArg0, Object pArg1) throws XelException {
        if (isLoggingError()) {
            logError(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1 }));
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate, Throwable pRootCause, Object pArg0, Object pArg1) throws XelException {
        if (isLoggingError()) {
            logError(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1 }), pRootCause);
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate, Object pArg0, Object pArg1, Object pArg2) throws XelException {
        if (isLoggingError()) {
            logError(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2 }));
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate, Throwable pRootCause, Object pArg0, Object pArg1, Object pArg2) throws XelException {
        if (isLoggingError()) {
            logError(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2 }), pRootCause);
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate, Object pArg0, Object pArg1, Object pArg2, Object pArg3) throws XelException {
        if (isLoggingError()) {
            logError(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2, "" + pArg3 }));
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate, Throwable pRootCause, Object pArg0, Object pArg1, Object pArg2, Object pArg3) throws XelException {
        if (isLoggingError()) {
            logError(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2, "" + pArg3 }), pRootCause);
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate, Object pArg0, Object pArg1, Object pArg2, Object pArg3, Object pArg4) throws XelException {
        if (isLoggingError()) {
            logError(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2, "" + pArg3, "" + pArg4 }));
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate, Throwable pRootCause, Object pArg0, Object pArg1, Object pArg2, Object pArg3, Object pArg4) throws XelException {
        if (isLoggingError()) {
            logError(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2, "" + pArg3, "" + pArg4 }), pRootCause);
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate, Object pArg0, Object pArg1, Object pArg2, Object pArg3, Object pArg4, Object pArg5) throws XelException {
        if (isLoggingError()) {
            logError(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2, "" + pArg3, "" + pArg4, "" + pArg5 }));
        }
    }

    /**
   *
   * Logs an error
   **/
    public void logError(String pTemplate, Throwable pRootCause, Object pArg0, Object pArg1, Object pArg2, Object pArg3, Object pArg4, Object pArg5) throws XelException {
        if (isLoggingError()) {
            logError(MessageFormat.format(pTemplate, new Object[] { "" + pArg0, "" + pArg1, "" + pArg2, "" + pArg3, "" + pArg4, "" + pArg5 }), pRootCause);
        }
    }
}
