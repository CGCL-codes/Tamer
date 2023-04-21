package pt.igeo.snig.mig.editor.validation;

import java.net.*;
import java.util.LinkedList;
import java.io.*;
import oracle.xml.parser.schema.XMLSchema;
import oracle.xml.parser.schema.XSDBuilder;
import oracle.xml.parser.schema.XSDException;
import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLParseException;
import oracle.xml.parser.v2.XMLParser;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import pt.igeo.snig.mig.editor.i18n.StringsManager;
import pt.igeo.snig.mig.editor.record.exception.XMLValidationException;

/**
 * Validate The XML according a XSD file    
 * 
 * @author David Duque
 * @since 3.0
 */
public class XSDValidator {

    private static LinkedList<String> errorMessages = new LinkedList<String>();

    /**
	 * 
	 * @param xsdURI XSD URI
	 * @param sbis XML String Stream 
	 * @throws XMLValidationException XML Validation Errors
	 * @throws IOException 
	 * @throws XSDException 
	 * @throws SAXException 
	 * @throws XMLParseException 
	 * @throws Exception 
	 */
    public static void process(String xsdURI, StringReader sbis) throws XMLValidationException, IOException, XSDException, XMLParseException, SAXException {
        DOMParser dp = new DOMParser();
        URL url = createURL(xsdURI);
        dp.setValidationMode(XMLParser.SCHEMA_VALIDATION);
        dp.setPreserveWhitespace(true);
        dp.setErrorStream(System.out);
        XSDBuilder builder = new XSDBuilder();
        url = new URL(xsdURI);
        XMLSchema schemadoc = (XMLSchema) builder.build(url);
        dp.setXMLSchema(schemadoc);
        dp.setErrorHandler(new ErrorHandler() {

            @Override
            public void error(SAXParseException arg0) throws SAXException {
                errorMessages.add("XML error: " + StringsManager.getInstance().getString("line") + ":" + arg0.getLineNumber() + " " + StringsManager.getInstance().getString("column") + ":" + arg0.getColumnNumber() + " " + StringsManager.getInstance().getString("message") + ":" + arg0.getMessage());
            }

            @Override
            public void fatalError(SAXParseException arg0) throws SAXException {
                errorMessages.add("XML fatalError: " + StringsManager.getInstance().getString("line") + ":" + arg0.getLineNumber() + " " + StringsManager.getInstance().getString("column") + ":" + arg0.getColumnNumber() + " " + StringsManager.getInstance().getString("message") + ":" + arg0.getMessage());
            }

            @Override
            public void warning(SAXParseException arg0) throws SAXException {
                errorMessages.add("XML warning: " + StringsManager.getInstance().getString("line") + ":" + arg0.getLineNumber() + " " + StringsManager.getInstance().getString("column") + ":" + arg0.getColumnNumber() + " " + StringsManager.getInstance().getString("message") + ":" + arg0.getMessage());
            }
        });
        errorMessages.clear();
        dp.parse(sbis);
        if (!errorMessages.isEmpty()) {
            throw new XMLValidationException(errorMessages);
        }
    }

    /**
	 * Helper method to create a URL from a file name
	 */
    static URL createURL(String fileName) {
        URL url = null;
        try {
            url = new URL(fileName);
        } catch (MalformedURLException ex) {
            File f = new File(fileName);
            try {
                String path = f.getAbsolutePath();
                String fs = System.getProperty("file.separator");
                if (fs.length() == 1) {
                    char sep = fs.charAt(0);
                    if (sep != '/') path = path.replace(sep, '/');
                    if (path.charAt(0) != '/') path = '/' + path;
                }
                path = "file://" + path;
                url = new URL(path);
            } catch (MalformedURLException e) {
                System.out.println("Cannot create url for: " + fileName);
                System.exit(0);
            }
        }
        return url;
    }
}
