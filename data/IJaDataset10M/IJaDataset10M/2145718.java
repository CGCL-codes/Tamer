package org.didicero.base.entity;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * <p>
 * <html>
 * </p>
 * <p>
 * <head>
 * </p>
 * <p>
 * </head>
 * </p>
 * <p>
 * <body>
 * </p>
 * <p>
 * <p>
 * </p>
 * <p>
 * Content is a non empty set of <a
 * href="mdel://_9_5_1_25e60543_1263562167062_475851_51">paragraphs</a>
 * </p>
 * <p>
 * or <a
 * href="mdel://_9_5_1_25e60543_1263220623000_32190_10560">phrases</a>
 * </p>
 * <p>
 * </p>
 * </p>
 * <p>
 * </body>
 * </p>
 * <p>
 * </html>
 * </p>
 */
public class ContentDaoException extends java.lang.Exception {

    /** 
     * The serial version UID of this class. Needed for serialization. 
     */
    private static final long serialVersionUID = 3512278465291654817L;

    /**
     * The default constructor.
     */
    public ContentDaoException() {
    }

    /**
     * Constructs a new instance of ContentDaoException
     *
     * @param throwable the parent Throwable
     */
    public ContentDaoException(Throwable throwable) {
        super(findRootCause(throwable));
    }

    /**
     * Constructs a new instance of ContentDaoException
     *
     * @param message the throwable message.
     */
    public ContentDaoException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of ContentDaoException
     *
     * @param message the throwable message.
     * @param throwable the parent of this Throwable.
     */
    public ContentDaoException(String message, Throwable throwable) {
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
