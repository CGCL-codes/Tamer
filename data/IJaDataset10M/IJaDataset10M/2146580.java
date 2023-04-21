package org.didicero.base.entity;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * <p>
 * The base class of all human and product resources.
 * </p>
 */
public class ResourceDaoException extends java.lang.Exception {

    /** 
     * The serial version UID of this class. Needed for serialization. 
     */
    private static final long serialVersionUID = -5895545210483181048L;

    /**
     * The default constructor.
     */
    public ResourceDaoException() {
    }

    /**
     * Constructs a new instance of ResourceDaoException
     *
     * @param throwable the parent Throwable
     */
    public ResourceDaoException(Throwable throwable) {
        super(findRootCause(throwable));
    }

    /**
     * Constructs a new instance of ResourceDaoException
     *
     * @param message the throwable message.
     */
    public ResourceDaoException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of ResourceDaoException
     *
     * @param message the throwable message.
     * @param throwable the parent of this Throwable.
     */
    public ResourceDaoException(String message, Throwable throwable) {
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
