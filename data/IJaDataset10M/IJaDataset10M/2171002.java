package org.formaria.editor.project;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.formaria.xml.jaxp.JaxpXmlParser;
import org.formaria.aria.Project;

/**
 * An error handler for the JAXP parser
 * <p> Copyright (c) Formaria Ltd., 2002-2003</p>
 * <p> $Revision: 1.1 $</p>
 * <p> License: see License.txt</p>
 */
public class EditorXmlErrorHandler implements ErrorHandler {

    public EditorXmlErrorHandler(Project project) {
        JaxpXmlParser parser = JaxpXmlParser.getInstance(project);
        parser.setErrorHandler(this);
    }

    /**
   * Receive notification of a warning.
   *
   * <p>SAX parsers will use this method to report conditions that
   * are not errors or fatal errors as defined by the XML 1.0
   * recommendation.  The default behaviour is to take no action.</p>
   *
   * <p>The SAX parser must continue to provide normal parsing events
   * after invoking this method: it should still be possible for the
   * application to process the document through to the end.</p>
   *
   * <p>Filters may use this method to report other, non-XML warnings
   * as well.</p>
   *
   * @param exception The warning information encapsulated in a
   *                  SAX parse exception.
   * @exception org.xml.sax.SAException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.SAParseException
   */
    public void warning(SAXParseException exception) throws SAXException {
        System.err.println("Warning (" + exception.getLineNumber() + "," + exception.getColumnNumber() + "): " + exception.getMessage());
    }

    /**
   * Receive notification of a recoverable error.
   *
   * <p>This corresponds to the definition of "error" in section 1.2
   * of the W3C XML 1.0 Recommendation.  For example, a validating
   * parser would use this callback to report the violation of a
   * validity constraint.  The default behaviour is to take no
   * action.</p>
   *
   * <p>The SAX parser must continue to provide normal parsing events
   * after invoking this method: it should still be possible for the
   * application to process the document through to the end.  If the
   * application cannot do so, then the parser should report a fatal
   * error even if the XML 1.0 recommendation does not require it to
   * do so.</p>
   *
   * <p>Filters may use this method to report other, non-XML errors
   * as well.</p>
   *
   * @param exception The error information encapsulated in a
   *                  SAX parse exception.
   * @exception org.xml.sax.SAException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.SAParseException
   */
    public void error(SAXParseException exception) throws SAXException {
        System.err.println("Error (" + exception.getLineNumber() + "," + exception.getColumnNumber() + "): " + exception.getMessage());
    }

    /**
   * Receive notification of a non-recoverable error.
   *
   * <p>This corresponds to the definition of "fatal error" in
   * section 1.2 of the W3C XML 1.0 Recommendation.  For example, a
   * parser would use this callback to report the violation of a
   * well-formedness constraint.</p>
   *
   * <p>The application must assume that the document is unusable
   * after the parser has invoked this method, and should continue
   * (if at all) only for the sake of collecting addition error
   * messages: in fact, SAX parsers are free to stop reporting any
   * other events once this method has been invoked.</p>
   *
   * @param exception The error information encapsulated in a
   *                  SAX parse exception.
   * @exception org.xml.sax.SAException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.SAParseException
   */
    public void fatalError(SAXParseException exception) throws SAXException {
        System.err.println("Fatal Error (" + exception.getLineNumber() + "," + exception.getColumnNumber() + "): " + exception.getMessage());
    }
}
