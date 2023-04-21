package uk.org.ogsadai.exception;

/**
 * Exception thrown when unable to create an instance of a named
 * class using reflection.  
 * <p>
 * This may be because the given class 
 * name is abstract or an interface.  Another possible cause is
 * that a nullary constant is not available.
 * <p>
 * Associated with error code: uk.org.ogsadai.CLASS_CREATE_ERROR.
 * 
 * @author The OGSA-DAI Project Team
 */
public class DAIClassCreateException extends DAIException {

    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2002 - 2005";

    /**
     * Constructor.
     * 
     * @param className Name of class for which an instance cannot be created.
     */
    public DAIClassCreateException(String className) {
        super(ErrorID.CLASS_CREATE_ERROR, new Object[] { className });
    }

    /**
     * Constructor.
     * 
     * @param className Name of class for which an instance cannot be created.
     * @param e Cause of the problem.
     */
    public DAIClassCreateException(String className, Throwable e) {
        super(ErrorID.CLASS_CREATE_ERROR, new Object[] { className });
        super.initCause(e);
    }
}
