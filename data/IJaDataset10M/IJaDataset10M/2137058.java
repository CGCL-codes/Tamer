package net.sf.practicalxml.util;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import net.sf.practicalxml.XmlException;

/**
 *  An error handler that throws <code>XmlException</code> on any error,
 *  and maintains a list of warnings. This is used by <code>ParseUtil</code>,
 *  to avoid the logging of the default error handler.
 */
public class ExceptionErrorHandler implements ErrorHandler {

    private List<SAXParseException> _warnings = new ArrayList<SAXParseException>();

    public void error(SAXParseException e) throws SAXException {
        throw new XmlException("unable to parse", e);
    }

    public void fatalError(SAXParseException e) throws SAXException {
        throw new XmlException("unable to parse", e);
    }

    public void warning(SAXParseException e) throws SAXException {
        _warnings.add(e);
    }

    /**
     *  Returns the list of warnings generated during parsing. May be empty,
     *  will not be <code>null</code>.
     */
    public List<SAXParseException> getWarnings() {
        return _warnings;
    }
}
