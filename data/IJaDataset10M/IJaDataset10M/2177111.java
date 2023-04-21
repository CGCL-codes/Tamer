package org.openuss.chat;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * The default exception thrown for unexpected errors occurring within
 * {@link org.openuss.chat.ChatService}.
 * 
 * @author Ingo Dueppe
 */
public class ChatServiceException extends RuntimeException {

    private static final long serialVersionUID = -7441101761011987171L;

    /**
	 * The default constructor for <code>ChatServiceException</code>.
	 */
    public ChatServiceException() {
    }

    /**
	 * Constructs a new instance of <code>ChatServiceException</code>.
	 * 
	 * @param throwable
	 *            the parent Throwable
	 */
    public ChatServiceException(Throwable throwable) {
        super(findRootCause(throwable));
    }

    /**
	 * Constructs a new instance of <code>ChatServiceException</code>.
	 * 
	 * @param message
	 *            the throwable message.
	 */
    public ChatServiceException(String message) {
        super(message);
    }

    /**
	 * Constructs a new instance of <code>ChatServiceException</code>.
	 * 
	 * @param message
	 *            the throwable message.
	 * @param throwable
	 *            the parent of this Throwable.
	 */
    public ChatServiceException(String message, Throwable throwable) {
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
