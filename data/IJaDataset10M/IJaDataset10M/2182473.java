package org.didicero.base.entity;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 */
public class MainGroupDaoException extends java.lang.Exception {

    /** 
     * The serial version UID of this class. Needed for serialization. 
     */
    private static final long serialVersionUID = 6064117864310938382L;

    /**
     * The default constructor.
     */
    public MainGroupDaoException() {
    }

    /**
     * Constructs a new instance of MainGroupDaoException
     *
     * @param throwable the parent Throwable
     */
    public MainGroupDaoException(Throwable throwable) {
        super(findRootCause(throwable));
    }

    /**
     * Constructs a new instance of MainGroupDaoException
     *
     * @param message the throwable message.
     */
    public MainGroupDaoException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of MainGroupDaoException
     *
     * @param message the throwable message.
     * @param throwable the parent of this Throwable.
     */
    public MainGroupDaoException(String message, Throwable throwable) {
        super(message, findRootCause(throwable));
    }

    /**
     * Finds the root cause of the parent exception
     * by traveling up the exception tree
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
