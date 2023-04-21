package com.goodcodeisbeautiful.opensearch.osd11;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.goodcodeisbeautiful.opensearch.OpenSearchException;

public class OpenSearchDescription11SaxHandler extends DefaultHandler {

    /** log */
    private static final Log log = LogFactory.getLog(OpenSearchDescription11SaxHandler.class);

    /** namespace */
    private static final String ATTR_XMLNS = "xmlns";

    /** template is an attribute name for URL. */
    private static final String ATTR_URL_TEMPLATE = "template";

    /** type is an attribute name for URL */
    private static final String ATTR_URL_TYPE = "type";

    /** method is an attribute name for URL */
    private static final String ATTR_URL_METHOD = "method";

    /** a name attribute for Param */
    private static final String ATTR_PARAM_NAME = "name";

    /** a value attribute for Param */
    private static final String ATTR_PARAM_VALUE = "value";

    /** width attribute for Image */
    private static final String ATTR_IMAGE_WIDTH = "width";

    /** height attribute for Image */
    private static final String ATTR_IMAGE_HEIGHT = "height";

    /** type attribute for Image */
    private static final String ATTR_IMAGE_TYPE = "type";

    /** role attribute for Query */
    private static final String ATTR_QUERY_ROLE = "role";

    /** searchTerms attribute for Query */
    private static final String ATTR_QUERY_SEARCH_TERMS = "searchTerms";

    /** an unknown state */
    private static final int STATE_UNKNOWN = -1;

    /** a state flag while parsing OpenSearchDescription */
    private static final int STATE_OPEN_SEARCH_DESCRIPTION = 1;

    /** a state flag while parsing ShortName */
    private static final int STATE_SHORT_NAME = 2;

    /** a state flag while parsing Description */
    private static final int STATE_DESCRIPTION = 3;

    /** a state flag while parsing Tags */
    private static final int STATE_TAGS = 4;

    /** a state flag while parsing Contact */
    private static final int STATE_CONTACT = 5;

    /** a state flag while parsing Url */
    private static final int STATE_URL = 6;

    /** a state flag while parsing Param */
    private static final int STATE_PARAM = 7;

    /** a state flag while parsing LongName */
    private static final int STATE_LONG_NAME = 8;

    /** a state flag while parsing Image */
    private static final int STATE_IMAGE = 9;

    /** a state flag while parsing Developer */
    private static final int STATE_DEVELOPER = 10;

    /** a state flag while parsing Attribution */
    private static final int STATE_ATTRIBUTION = 11;

    /** a state flag while parsing Language */
    private static final int STATE_LANGUAGE = 12;

    /** a state flag while parsing OutputEncoding */
    private static final int STATE_OUTPUT_ENCODING = 13;

    /** a state flag while parsing InputEncoding */
    private static final int STATE_INPUT_ENCODING = 14;

    /** a state flag while parsing Query */
    private static final int STATE_QUERY = 15;

    /** a state flag while parsing SyndicationRight */
    private static final int STATE_SYNDICATION_RIGHT = 16;

    /** a state flag while parsing AdultContent */
    private static final int STATE_ADULT_CONTENT = 17;

    /** a state for parsing */
    private int m_state;

    /** an instance to store some information for OpenSearchDescription11 */
    private DefaultOpenSearchDescription11 m_osd11;

    /** an instance to store url information */
    private DefaultOpenSearchUrl m_osUrl;

    /** an instance to store image information */
    private DefaultOpenSearchImage m_osImage;

    /**
	 * Default constructor.
	 */
    public OpenSearchDescription11SaxHandler() {
        m_osd11 = null;
    }

    /**
	 * Constructor.
	 * @param osd11 is an instance to be initialized by this handler.
	 */
    public OpenSearchDescription11SaxHandler(DefaultOpenSearchDescription11 osd11) {
        m_osd11 = osd11;
    }

    /**
	 * Get a created instance of OpenSearchDescription11.
	 * @return a created instance or null.
	 */
    public OpenSearchDescription11 getOpenSearchDescription11() {
        return m_osd11;
    }

    /**
     * Initialize members.
     * @exception SAXException is thrown if some error occured.
     * @see org.xml.sax.helpers.DefaultHandler#startDocument()
     */
    public void startDocument() throws SAXException {
    }

    /**
     * do nothing in this current implementation.
     * @see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    public void endDocument() throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if ("OpenSearchDescription".equals(qName)) {
                if (!OpenSearchDescription11.XMLNS_OSD11.equals(attributes.getValue(ATTR_XMLNS))) {
                    throw new SAXException("Doesn't support " + attributes.getValue(ATTR_XMLNS));
                } else {
                    if (m_osd11 == null) m_osd11 = new DefaultOpenSearchDescription11();
                    m_state = STATE_OPEN_SEARCH_DESCRIPTION;
                }
            } else if ("ShortName".equals(qName)) {
                m_state = STATE_SHORT_NAME;
            } else if ("Description".equals(qName)) {
                m_state = STATE_DESCRIPTION;
            } else if ("Tags".equals(qName)) {
                m_state = STATE_TAGS;
            } else if ("Contact".equals(qName)) {
                m_state = STATE_CONTACT;
            } else if ("Url".equals(qName)) {
                m_osUrl = new DefaultOpenSearchUrl(attributes.getValue(ATTR_URL_TEMPLATE), attributes.getValue(ATTR_URL_TYPE), attributes.getValue(ATTR_URL_METHOD));
                m_state = STATE_URL;
            } else if ("Param".equals(qName)) {
                m_osUrl.addParam(new DefaultOpenSearchParam(attributes.getValue(ATTR_PARAM_NAME), attributes.getValue(ATTR_PARAM_VALUE)));
                m_state = STATE_PARAM;
            } else if ("LongName".equals(qName)) {
                m_state = STATE_LONG_NAME;
            } else if ("Image".equals(qName)) {
                m_osImage = new DefaultOpenSearchImage(attributes.getValue(ATTR_IMAGE_WIDTH), attributes.getValue(ATTR_IMAGE_HEIGHT), attributes.getValue(ATTR_IMAGE_TYPE));
                m_state = STATE_IMAGE;
            } else if ("Query".equals(qName)) {
                m_state = STATE_QUERY;
                m_osd11.addOpenSearchQuery(new DefaultOpenSearchQuery(attributes.getValue(ATTR_QUERY_ROLE), attributes.getValue(ATTR_QUERY_SEARCH_TERMS)));
            } else if ("Developer".equals(qName)) {
                m_state = STATE_DEVELOPER;
            } else if ("Attribution".equals(qName)) {
                m_state = STATE_ATTRIBUTION;
            } else if ("SyndicationRight".equals(qName)) {
                m_state = STATE_SYNDICATION_RIGHT;
            } else if ("AdultContent".equals(qName)) {
                m_state = STATE_ADULT_CONTENT;
            } else if ("Language".equals(qName)) {
                m_state = STATE_LANGUAGE;
            } else if ("OutputEncoding".equals(qName)) {
                m_state = STATE_OUTPUT_ENCODING;
            } else if ("InputEncoding".equals(qName)) {
                m_state = STATE_INPUT_ENCODING;
            } else {
                log.debug("Unknown tag found. tag = " + qName);
            }
        } catch (OpenSearchException e) {
            log.error("Error happened.", e);
            throw new SAXException(e);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            switch(m_state) {
                case STATE_URL:
                    m_osd11.addOpenSearchUrl(m_osUrl);
                    m_osUrl = null;
                case STATE_SHORT_NAME:
                case STATE_DESCRIPTION:
                case STATE_TAGS:
                case STATE_CONTACT:
                case STATE_LONG_NAME:
                case STATE_DEVELOPER:
                case STATE_ATTRIBUTION:
                case STATE_LANGUAGE:
                case STATE_OUTPUT_ENCODING:
                case STATE_INPUT_ENCODING:
                case STATE_QUERY:
                case STATE_SYNDICATION_RIGHT:
                case STATE_ADULT_CONTENT:
                    m_state = STATE_OPEN_SEARCH_DESCRIPTION;
                    break;
                case STATE_PARAM:
                    m_state = STATE_URL;
                    break;
                case STATE_IMAGE:
                    m_osd11.addOpenSearchImage(m_osImage);
                    m_osImage = null;
                    m_state = STATE_OPEN_SEARCH_DESCRIPTION;
                    break;
                default:
                    m_state = STATE_UNKNOWN;
                    break;
            }
        } catch (Exception e) {
            log.error("Exception occured.", e);
            throw new SAXException(e);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            switch(m_state) {
                case STATE_OPEN_SEARCH_DESCRIPTION:
                    break;
                case STATE_SHORT_NAME:
                    m_osd11.setShortName(new String(ch, start, length));
                    break;
                case STATE_DESCRIPTION:
                    m_osd11.setDescription(new String(ch, start, length));
                    break;
                case STATE_TAGS:
                    m_osd11.setTags(new String(ch, start, length));
                    break;
                case STATE_CONTACT:
                    m_osd11.setContact(new String(ch, start, length));
                    break;
                case STATE_LONG_NAME:
                    m_osd11.setLongName(new String(ch, start, length));
                    break;
                case STATE_IMAGE:
                    m_osImage.setUrl(new String(ch, start, length));
                    break;
                case STATE_DEVELOPER:
                    m_osd11.setDeveloper(new String(ch, start, length));
                    break;
                case STATE_ATTRIBUTION:
                    String s = m_osd11.getAttribution();
                    m_osd11.setAttribution((s != null ? s : "") + new String(ch, start, length));
                    break;
                case STATE_LANGUAGE:
                    m_osd11.setLanguage(new String(ch, start, length));
                    break;
                case STATE_OUTPUT_ENCODING:
                    m_osd11.setOutputEncoding(new String(ch, start, length));
                    break;
                case STATE_INPUT_ENCODING:
                    m_osd11.setInputEncoding(new String(ch, start, length));
                    break;
                case STATE_SYNDICATION_RIGHT:
                    m_osd11.setSyndicationRight(new String(ch, start, length));
                    break;
                case STATE_ADULT_CONTENT:
                    m_osd11.setAdultContent(new String(ch, start, length));
                    break;
                case STATE_QUERY:
                case STATE_URL:
                    break;
                default:
                    log.debug("Unknown state found. state is " + m_state);
                    break;
            }
        } catch (OpenSearchException e) {
            log.error("Error happened while parsing .", e);
            throw new SAXException(e);
        }
    }
}
