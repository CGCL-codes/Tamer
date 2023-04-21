package jaxlib.xml.sax;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * String constants specifying the standard SAX feature flags.
 *
 * @see org.xml.sax.XMLReader#setFeature(String,boolean)
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: SAXParserFeatures.java 1215 2004-08-02 20:10:09Z joerg_wassmer $
 */
public class SAXParserFeatures {

    protected SAXParserFeatures() throws InstantiationException {
        throw new InstantiationException();
    }

    private static final String create(String id) {
        return (PREFIX + id).intern();
    }

    public static final String PREFIX = "http://xml.org/sax/features/";

    /**
   * Reports whether the parser processes external general entities; always true if validating. 
   * <p>
   * Access: read-write</br>
   * Default: unspecified
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String EXTERNAL_GENERAL_ENTITIES = create("external-general-entities");

    /**
   * Reports whether the parser processes external parameter entities; always true if validating.    
   * <p>
   * Access: read-write</br>
   * Default: unspecified
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String EXTERNAL_PARAMETER_ENTITIES = create("external-parameter-entities");

    /**
   * May be examined only during a parse, after the <tt>startDocument()</tt> callback has been completed; 
   * read-only. 
   * The value is true if the document specified standalone="yes" in its XML declaration, and otherwise is 
   * false. 
   * <p>
   * Access: (parsing) read-only; (not parsing) none</br>
   * Default: not applicable
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String IS_STANDALONE = create("is-standalone");

    /**
   * A value of "true" indicates namespace URIs and unprefixed local names for element and attribute names 
   * will be available. 
   * <p>
   * Access: read-write</br>
   * Default: true
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String NAMESPACES = create("namespaces");

    /**
   * A value of "true" indicates that XML qualified names (with prefixes) and attributes (including xmlns* 
   * attributes) will be available. 
   * <p>
   * Access: read-write</br>
   * Default: false
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String NAMESPACE_PREFIXES = create("namespace-prefixes");

    /**
   * A value of "true" indicates that system IDs in declarations will be absolutized 
   * (relative to their base URIs) before reporting. (That is the default behavior for all SAX2 XML parsers.)
   * A value of "false" indicates those IDs will not be absolutized; parsers will provide the base URI from
   * <em>Locator.getSystemId()</em>.
   * This applies to system IDs passed in 
   * <ul>
   *   <li><em>DTDHandler.notationDecl()</em>,</li>
   *   <li><em>DTDHandler.unparsedEntityDecl()</em>, and</li>
   *   <li><em>DeclHandler.externalEntityDecl()</em>.</li>
   * </ul>
   * It does not apply to <em>EntityResolver.resolveEntity()</em>, which is not used to report declarations, 
   * or to <em>LexicalHandler.startDTD()</em>, which already provides  the non-absolutized URI.
   * <p>
   * Access: read-write</br>
   * Default: true
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String RESOLVE_DTD_URIS = create("resolve-dtd-uris");

    /**
   * Has a value of "true" if all XML names (for elements, prefixes, attributes, entities, notations, and 
   * local names), as well as namespace URIs, will have been interned using 
   * {@link java.lang.String#intern()}. 
   * This supports fast testing of equality/inequality against string constants, rather than forcing slower 
   * calls to {@link String#equals(Object)}. 
   * <p>
   * Access: read-write</br>
   * Default: unspecified
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String STRING_INTERNING = create("string-interning");

    /**
   * Controls whether the parser reports Unicode normalization errors as described in section 2.13 and 
   * Appendix B of the XML 1.1 Recommendation. If true, Unicode normalization errors are reported using the 
   * ErrorHandler.error() callback. Such errors are not fatal in themselves (though, obviously, other 
   * Unicode-related encoding errors may be). 
   * <p>
   * Access: read-write</br>
   * Default: false
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String UNICODE_NORMALIZATION_CHECKING = create("unicode-normalization-checking");

    /**
   * Returns "true" if the <em>Attributes</em> objects passed by the parser in 
   * <em>ContentHandler.startElement()</em> implement the {@link org.xml.sax.ext.Attributes2} interface.
   * That interface exposes additional DTD-related information, such as whether the attribute was specified 
   * in the source text rather than defaulted.   
   * <p>
   * Access: read-write</br>
   * Default: not applicable
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String USE_ATTRIBUTES2 = create("use-attributes2");

    /**
   * Returns "true" if, when <em>setEntityResolver</em> is given an object implementing the 
   * {@link org.xml.sax.ext.EntityResolver2} interface, those new methods will be used. Returns "false" to 
   * indicate that those methods will not be used. 
   * <p>
   * Access: read-write</br>
   * Default: true
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String USE_ENTITY_RESOLVER2 = create("use-entity-resolver2");

    /**
   * Returns "true" if the Locator objects passed by this parser in 
   * <em>ContentHandler.setDocumentLocator()</em> implement the {@link org.xml.sax.ext.Locator2} interface. 
   * That interface exposes additional entity information, such as the character encoding and XML version 
   * used. 
   * <p>
   * Access: read-write</br>
   * Default: not applicable
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String USE_LOCATOR2 = create("use-locator2");

    /**
   * Controls whether the parser is reporting all validity errors; if true, all external entities will be 
   * read. 
   * <p>
   * Access: read-write</br>
   * Default: unspecified
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String VALIDATION = create("validation");

    /**
   * Controls whether, when the namespace-prefixes feature is set, the parser treats namespace declaration 
   * attributes as being in the <a href="http://www.w3.org/2000/xmlns/">http://www.w3.org/2000/xmlns/</a> 
   * namespace. By default, SAX2 conforms to the original "Namespaces in XML" recommendation, which 
   * explicitly states that such attributes are not in any namespace. Setting this optional flag to "true" 
   * makes the SAX2 events conform to a later backwards-incompatible revision of that recommendation, placing
   * those attribute in a namespace. 
   * <p>
   * Access: read-write</br>
   * Default: false
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String XMLNS_URIS = create("xmlns-uris");

    /**
   * Returns "true" if the parser supports both XML 1.1 and XML 1.0. Returns "false" if the parser supports 
   * only XML 1.0. 
   * <p>
   * Access: read-write</br>
   * Default: not applicable
   * </p>
   *
   * @since JaXLib 1.0
   */
    public static final String XML_1_1 = create("xml-1.1");
}
