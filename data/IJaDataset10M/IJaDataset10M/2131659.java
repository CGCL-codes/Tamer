package org.gvsig.gpe.xml.stream;

import java.io.IOException;

/**
 * Signals either a parsing or io error ocurred while scanning or writing an xml formatted document.
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id: XmlStreamException.java 19593 2008-03-12 17:23:30Z groldan $
 */
public class XmlStreamException extends IOException {

    public XmlStreamException(String message) {
        this(message, null);
    }

    public XmlStreamException(String message, Throwable cause) {
        super(message);
        super.initCause(cause);
    }

    public XmlStreamException(Throwable cause) {
        this(null, cause);
    }
}
