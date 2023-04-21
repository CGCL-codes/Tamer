package de.juwimm.cms.util;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class should only receive error messages from the underlying SaxHandler for returning content.
 * @author <a href="sascha.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version $Id: ContentErrorListener.java 8 2009-02-15 08:54:54Z skulawik $
 */
public class ContentErrorListener implements ErrorHandler, ErrorListener {

    private static Logger log = Logger.getLogger(ContentErrorListener.class);

    private Exception exceptionGot = null;

    public void error(TransformerException exception) throws TransformerException {
        if (log.isDebugEnabled()) log.debug("error parsing content", exception);
        exceptionGot = exception;
    }

    public void fatalError(TransformerException exception) throws TransformerException {
        log.error("fatal error parsing content", exception);
        exceptionGot = exception;
    }

    public void warning(TransformerException exception) throws TransformerException {
        if (log.isDebugEnabled()) log.debug("", exception);
    }

    public void error(SAXParseException exception) throws SAXException {
        if (log.isDebugEnabled()) log.debug("error parsing content", exception);
        exceptionGot = exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        log.error("fatal error parsing content", exception);
        exceptionGot = exception;
    }

    public void warning(SAXParseException exception) throws SAXException {
        if (log.isDebugEnabled()) log.debug("", exception);
    }

    /**
	 * @return Returns the exceptionGot.
	 */
    public Exception getExceptionGot() {
        return this.exceptionGot;
    }
}
