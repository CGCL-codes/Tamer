package com.i3sp.sso;

import com.mortbay.Util.Code;

/** 
 * Exception that can be thrown to indicate that a provided string parameter
 * does not meet whatever the requirements for the string may be.
 * @see
 * @version $Revision: 624 $ $Date: 2001-03-13 16:51:38 -0500 (Tue, 13 Mar 2001) $
 * @author Aoife Kavanagh (aoife)
 */
public class InvalidStringException extends Exception {

    public InvalidStringException() {
        super();
    }

    public InvalidStringException(String s) {
        super(s);
    }
}
