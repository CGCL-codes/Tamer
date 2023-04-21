package org.openuss.statistics;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * The default exception thrown for unexpected errors occurring within
 * {@link org.openuss.statistics.OnlineStatisticService}.
 */
public class OnlineStatisticServiceException extends RuntimeException {

    /**
	 * The serial version UID of this class. Needed for serialization.
	 */
    private static final long serialVersionUID = 7453431834375981929L;

    /**
	 * The default constructor for <code>OnlineStatisticServiceException</code>.
	 */
    public OnlineStatisticServiceException() {
    }

    /**
	 * Constructs a new instance of <code>OnlineStatisticServiceException</code>
	 * .
	 * 
	 * @param throwable
	 *            the parent Throwable
	 */
    public OnlineStatisticServiceException(Throwable throwable) {
        super(findRootCause(throwable));
    }

    /**
	 * Constructs a new instance of <code>OnlineStatisticServiceException</code>
	 * .
	 * 
	 * @param message
	 *            the throwable message.
	 */
    public OnlineStatisticServiceException(String message) {
        super(message);
    }

    /**
	 * Constructs a new instance of <code>OnlineStatisticServiceException</code>
	 * .
	 * 
	 * @param message
	 *            the throwable message.
	 * @param throwable
	 *            the parent of this Throwable.
	 */
    public OnlineStatisticServiceException(String message, Throwable throwable) {
        super(message, findRootCause(throwable));
    }

    /**
	 * Finds the root cause of the parent exception by traveling up the
	 * exception tree
	 */
    private static Throwable findRootCause(Throwable th) {
        if (th != null) {
            try {
                Throwable targetException = null;
                String exceptionProperty = "targetException";
                if (PropertyUtils.isReadable(th, exceptionProperty)) {
                    targetException = (Throwable) PropertyUtils.getProperty(th, exceptionProperty);
                } else {
                    exceptionProperty = "causedByException";
                    if (PropertyUtils.isReadable(th, exceptionProperty)) {
                        targetException = (Throwable) PropertyUtils.getProperty(th, exceptionProperty);
                    }
                }
                if (targetException != null) {
                    th = targetException;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (th.getCause() != null) {
                th = th.getCause();
                th = findRootCause(th);
            }
        }
        return th;
    }
}
