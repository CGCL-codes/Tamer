package ch.iserver.ace.net.protocol;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * ResponseParserHandler is used to parse responses received from a 
 * Channel communication.
 */
public class ResponseParserHandler extends ParserHandler {

    private static Logger LOG = Logger.getLogger(ResponseParserHandler.class);

    private Request response;

    private int responseType;

    private QueryInfo info;

    private String userId;

    public ResponseParserHandler() {
    }

    public void startDocument() throws SAXException {
        response = null;
        responseType = -1;
        info = (info == null) ? new QueryInfo("", -1) : info;
    }

    public void endDocument() throws SAXException {
        if (responseType == USER_DISCOVERY) {
            response = new RequestImpl(USER_DISCOVERY, userId, info.getPayload());
            info = null;
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals(USER)) {
            responseType = USER_DISCOVERY;
            userId = attributes.getValue(ID);
            String name = attributes.getValue(NAME);
            info.setPayload(name);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    /**
	 * {@inheritDoc}
	 */
    public Request getResult() {
        LOG.debug("getResult(" + response + ")");
        return response;
    }

    public int getType() {
        return responseType;
    }

    /**
	 * {@inheritDoc}
	 */
    public void setMetaData(Object metadata) {
        if (metadata instanceof QueryInfo) {
            info = (QueryInfo) metadata;
        } else {
            LOG.warn("unknown metadata type [" + metadata + "]");
        }
    }
}
