package schematool.core;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Helper class for validating NCNames.
 * 
 * @author Neal Arthorne
 * @version 1.0
 */
public class NCName {

    private static Document doc;

    private static Element tempElement;

    static {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Validates an NCName as defined by XML Namespaces and XML 1.0.
	 * 
	 * @param ncName name to validate
	 * @return true if valid, false otherwise
	 */
    public static boolean isValidNCName(String ncName) {
        if (ncName == null || ncName.length() == 0 || ncName.indexOf(":") > -1) return false;
        try {
            tempElement = doc.createElement(ncName);
            return true;
        } catch (DOMException e) {
            if (e.code != DOMException.INVALID_CHARACTER_ERR) e.printStackTrace();
        }
        return false;
    }
}
