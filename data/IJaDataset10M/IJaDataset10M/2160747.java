package org.stanwood.media.xml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.stanwood.media.logging.LoggerOutputStream;
import org.stanwood.media.util.FileHelper;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class is used to handle parsing errors that occur when validating a xml file.
 * It will usally display 5 lines either side of the error and give a messages
 * with info about the problem that was found.
 */
public class SimpleErrorHandler extends XMLErrorHandler {

    private static final Log log = LogFactory.getLog(SimpleErrorHandler.class);

    private File xmlFile;

    /**
	 * Used to construct the error handler
	 * @param xmlFile The file that is been parsed
	 */
    public SimpleErrorHandler(File xmlFile) {
        super();
        this.xmlFile = xmlFile;
    }

    /**
	 * Used to print warnings when they occur while validating the XML file
	 * @param e The exception that is been processed
	 */
    @Override
    public void warning(SAXParseException e) throws SAXException {
        super.warning(e);
        if (log.isDebugEnabled()) {
            try {
                FileHelper.displayFile(xmlFile, e.getLineNumber() - 5, e.getLineNumber() + 5, System.out);
            } catch (IOException e1) {
                log.error(e1.getMessage(), e1);
            }
        }
    }

    /**
	 * Used to print errors when they occur while validating the XML file
	 * @param e The exception that is been processed
	 */
    @Override
    public void error(SAXParseException e) throws SAXException {
        super.error(e);
        if (log.isDebugEnabled()) {
            displayFile(e);
        }
    }

    /**
	 * Used to print fatal errors when they occur while validating the XML file
	 * @param e The exception that is been processed
	 */
    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        super.fatalError(e);
        if (log.isDebugEnabled()) {
            displayFile(e);
        }
    }

    private void displayFile(SAXParseException e) {
        OutputStream os = null;
        try {
            os = new LoggerOutputStream(Level.INFO);
            FileHelper.displayFile(xmlFile, e.getLineNumber() - 5, e.getLineNumber() + 5, os);
        } catch (IOException e1) {
            log.error(e1.getMessage(), e1);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    log.error(Messages.getString("SimpleErrorHandler.UNABLE_CLOSE_STREAM"));
                }
            }
        }
    }
}
