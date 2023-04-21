package common.persistence.io.xmlio;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import java.io.IOException;

public class XercesParser implements IParserConstants {

    private String szXMLFile;

    private int iParserOptions;

    public XercesParser(String _XMLFile, final int _parserOptions) {
        this.iParserOptions = _parserOptions;
        this.szXMLFile = _XMLFile;
    }

    public Document parseXML() throws SAXException, IOException {
        Document objDoc = null;
        ParseAdapter objParsAdp = new ParseAdapter();
        try {
            objParsAdp.setDOMFeatures();
            objDoc = objParsAdp.parseXMLFile();
            if (objDoc == null) System.out.println("This line should never be reached at all." + System.err.toString());
        } catch (IOException e) {
            throw e;
        } catch (SAXException e) {
            throw e;
        }
        return objDoc;
    }

    private class ParseAdapter {

        private DOMParser objParser;

        private ValidationErrorTracker objErr;

        public ParseAdapter() {
            objParser = new DOMParser();
        }

        public void setDOMFeatures() throws SAXException {
            try {
                switch(iParserOptions) {
                    case SCHEMA_VALIDATION:
                        {
                            objParser.setFeature(FEATURE_DEFERRED_DOM, true);
                            objParser.setFeature(FEATURE_VALIDATION, true);
                            objParser.setFeature(FEATURE_SCHEMA, true);
                            objParser.setFeature(FEATURE_NAMESPACES, true);
                            objParser.setFeature(FEATURE_SCHEMA_FULL_SUPPORT, true);
                            objParser.setFeature(FEATURE_WHITESPACES, false);
                            break;
                        }
                    case DTD_VALIDATION:
                        {
                            objParser.setFeature(FEATURE_VALIDATION, true);
                            break;
                        }
                    case NO_VALIDATION:
                        break;
                    default:
                        throw (new SAXNotSupportedException("OPTION NOT SUPPORTED"));
                }
            } catch (SAXNotRecognizedException e) {
                System.err.println(e);
                throw e;
            } catch (SAXNotSupportedException e) {
                System.err.println(e);
                throw e;
            }
        }

        public Document parseXMLFile() throws SAXException, IOException {
            objErr = new ValidationErrorTracker();
            Document objDoc = null;
            try {
                objParser.setErrorHandler(objErr);
                System.out.println(LOG_START);
                objParser.parse(szXMLFile);
                System.out.println(LOG_END);
                objDoc = objParser.getDocument();
            } catch (SAXException e) {
                System.err.println(e);
                throw e;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw e;
            }
            return objDoc;
        }

        private class ValidationErrorTracker implements ErrorHandler {

            public void warning(SAXParseException e) throws SAXException {
                System.out.println("Warning: " + e.getMessage());
                System.out.println(" at line " + e.getLineNumber() + ", column " + e.getColumnNumber());
                System.out.println(" in entity " + e.getSystemId());
                throw e;
            }

            public void error(SAXParseException e) throws SAXException {
                System.out.println("Error: " + e.getMessage());
                System.out.println(" at line " + e.getLineNumber() + ", column " + e.getColumnNumber());
                System.out.println(" in entity " + e.getSystemId());
                throw e;
            }

            public void fatalError(SAXParseException e) throws SAXException {
                System.out.println("Fatal Error: " + e.getMessage());
                System.out.println(" at line " + e.getLineNumber() + ", column " + e.getColumnNumber());
                System.out.println(" in entity " + e.getSystemId());
                throw e;
            }
        }
    }
}
