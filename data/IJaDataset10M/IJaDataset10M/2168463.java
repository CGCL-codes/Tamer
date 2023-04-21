package org.merlotxml.util.xml;

import org.merlotxml.util.WrapperException;

/**
 * DOM Liaison Implementation Exception
 * 
 * Used to wrap exceptions thrown by implementors of DOMLiaison
 * 
 * @author Tim McCune
 */
public class DOMLiaisonImplException extends WrapperException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public DOMLiaisonImplException(Exception realException) {
        super(realException);
    }

    public DOMLiaisonImplException(Exception realException, String appendMsg) {
        super(realException, appendMsg);
    }
}
