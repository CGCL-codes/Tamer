package com.xy.sframe.component.xml;

import com.xy.sframe.component.exception.NestedRuntimeException;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XmlParserException extends NestedRuntimeException {

    /**
	 * @param msg
	 */
    public XmlParserException(String msg) {
        super(msg);
    }

    /**
	 * @param msg
	 * @param ex
	 */
    public XmlParserException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
