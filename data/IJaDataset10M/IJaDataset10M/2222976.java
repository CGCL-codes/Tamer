package org.openorb.orb.rmi;

/**
 * This class wraps a CORBA SystemException in a Java IOException.
 *
 * @author Chris Wood
 */
public class CORBAIOException extends java.io.IOException {

    private org.omg.CORBA.SystemException m_exception;

    /**
     * Constructor.
     *
     * @param ex The CORBA SystemException.
     */
    public CORBAIOException(org.omg.CORBA.SystemException ex) {
        m_exception = ex;
    }

    /**
     * Return the CORBA SystemException wrapped by this IOException.
     *
     * @return The target CORBA SystemException.
     */
    public org.omg.CORBA.SystemException getTargetException() {
        return m_exception;
    }
}
