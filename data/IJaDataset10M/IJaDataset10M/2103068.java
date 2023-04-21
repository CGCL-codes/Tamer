package jaxlib.xml.sax;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * String constants specifying the standard SAX properties.
 *
 * @see org.xml.sax.XMLReader#setProperty(String,Object)
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: SAXParserProperties.java 1215 2004-08-02 20:10:09Z joerg_wassmer $
 */
public class SAXParserProperties {

    protected SAXParserProperties() throws InstantiationException {
        throw new InstantiationException();
    }

    private static final String create(String id) {
        return (PREFIX + id).intern();
    }

    public static final String PREFIX = "http://xml.org/sax/properties/";

    /**
   * Used to see most DTD declarations except those treated as lexical ("document element name is ...") or 
   * which are mandatory for all SAX parsers (DTDHandler). The object must implement 
   * {@link org.xml.sax.ext.DeclHandler}. 
   *
   * @since JaXLib 1.0
   */
    public static final String DECL_HANDLER = create("declaration-handler");

    /**
   * May be examined only during a parse, after the startDocument() callback has been completed; read-only. 
   * This property is a literal string describing the actual XML version of the document, such as "1.0" 
   * or "1.1".
   *
   * @since JaXLib 1.0
   */
    public static final String DOCUMENT_XML_VERSION = create("document-xml-version");

    /**
   * For "DOM Walker" style parsers, which ignore their parser.parse() parameters, this is used to specify 
   * the DOM (sub)tree being walked by the parser. The object must implement the {@link org.w3c.dom.Node} 
   * interface.   
   *
   * @since JaXLib 1.0
   */
    public static final String DOM_NODE = create("dom-node");

    /**
   * Used to see some syntax events that are essential in some applications: comments, CDATA delimiters, 
   * selected general entity inclusions, and the start and end of the DTD (and declaration of document 
   * element name). 
   * The object must implement {@link org.xml.sax.ext.LexicalHandler}.    
   *
   * @since JaXLib 1.0
   */
    public static final String LEXICAL_HANDLER = create("lexical-handler");

    /**
   * Readable only during a parser callback, this exposes a TBS chunk of characters responsible for the 
   * current event.    
   *
   * @since JaXLib 1.0
   */
    public static final String XML_STRING = create("xml-string");
}
