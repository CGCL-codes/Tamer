package org.xvr.xvrengine.parser;

/**
 * @author Raffaello
 *
 */
public class XVRParserException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public XVRParserException() {
    }

    /**
	 * @param arg0
	 */
    public XVRParserException(String arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 */
    public XVRParserException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 * @param arg1
	 */
    public XVRParserException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
