package glaceo.utils.xml;

import glaceo.error.GConfigurationException;
import java.util.Locale;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 * Must be implemented by classes offering service methods that help dealing with XML
 * data.
 * 
 * @version $Id: IGXmlService.java 39 2006-10-07 16:29:39Z janjanke $
 * @author jjanke
 */
public interface IGXmlService {

    /** Constant that specifies the attribute that contains the language for a given element. */
    public static final String XML_ATTR_LANG = "lang";

    /**
   * Parses the XML document located at the specified URL and validates it by using the
   * schema whose location has been given as parameter.
   * 
   * @param strXmlPath the path of the XML file to be parsed
   * @param strSchemaPath the path of the XML schema which should be used for validation
   * @return the in-memory XML representation as Dom4j Document
   * @throws GConfigurationException if the parser could not be configured correctly or
   *         if the parsing itsself failed or if errors were reported during the
   *         validation of the document against the specified schema
   */
    public Document getDocument(String strXmlPath, String strSchemaPath) throws GConfigurationException;

    /**
   * Retrieves the child node of the parent Element that matches the specified
   * Locale.
   * 
   * @param parent the parent element
   * @param strChildTagName the name of the child element tag
   * @param locale the Locale the retrieved child shall conform to
   * @return The element that matches the language specified by the passed Locale. If no
   *         child element that matches the given Locale was found, the first encountered
   *         child is returned. If no child with the specified name is found
   *         <code>null</code> is returned.
   */
    public Element getLocaleSpecificChild(Element parent, String strChildTagName, Locale locale);
}
