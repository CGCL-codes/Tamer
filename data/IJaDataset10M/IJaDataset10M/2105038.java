package org.gdi3d.vrmlloader.vrml;

/**  Description of the Class */
public class InvalidFieldException extends IllegalArgumentException {

    /**Constructor for the InvalidFieldException object */
    public InvalidFieldException() {
        super();
    }

    /**
     *Constructor for the InvalidFieldException object
     *
     *@param  s Description of the Parameter
     */
    public InvalidFieldException(String s) {
        super(s);
    }
}
