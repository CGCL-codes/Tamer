package net.sf.layoutParser.util.xml.validation;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.sf.layoutParser.build.BuilderException;
import net.sf.layoutParser.exception.ExceptionKey;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * TODO: Comentar esta classe
 * 
 * @author M�rio Valentim Junior
 * @since 1.1
 * @date 11/06/2008
 */
public abstract class XsdChecker {

    /**
	 * "http://www.w3.org/2001/XMLSchema"
	 */
    public static final String XMLSCHEMA = "http://www.w3.org/2001/XMLSchema";

    /**
	 * "http://java.sun.com/xml/jaxp/properties/schemaLanguage"
	 */
    public static final String SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /**
	 * @param arquivo
	 * @throws BuilderException
	 */
    public static void checkLayout(String fileName, String xsdFileName) throws BuilderException {
        SAXParser saxParser = null;
        try {
            SAXParserFactory f = SAXParserFactory.newInstance();
            f.setNamespaceAware(true);
            f.setValidating(true);
            saxParser = f.newSAXParser();
            saxParser.setProperty(SCHEMA_LANGUAGE, XMLSCHEMA);
            saxParser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", " http://layoutparser.sourceforge.net/ " + Thread.currentThread().getContextClassLoader().getResource(xsdFileName).toExternalForm());
            saxParser.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName), new LayoutErrorHandler());
        } catch (SAXException e) {
            throw new BuilderException(ExceptionKey.SCHEMA_CHECK, new Object[] { fileName }, e);
        } catch (IOException e) {
            throw new BuilderException(ExceptionKey.FILE_NOT_FOUND, new Object[] { fileName }, e);
        } catch (ParserConfigurationException e) {
            throw new BuilderException(e);
        }
    }

    /**
	 * TODO: Comentar esta classe
	 *
	 * @author M�rio Valentim Junior
	 * @since 1.1
	 * @date 11/06/2008
	 */
    private static class LayoutErrorHandler extends DefaultHandler {

        public void warning(SAXParseException e) throws SAXException {
            System.out.println("Warning: ");
            printInfo(e);
        }

        public void error(SAXParseException e) throws SAXException {
            throw e;
        }

        public void fatalError(SAXParseException e) throws SAXException {
            throw e;
        }

        private void printInfo(SAXParseException e) {
            System.out.println("   Public ID: " + e.getPublicId());
            System.out.println("   System ID: " + e.getSystemId());
            System.out.println("   Line number: " + e.getLineNumber());
            System.out.println("   Column number: " + e.getColumnNumber());
            System.out.println("   Message: " + e.getMessage());
        }
    }
}
