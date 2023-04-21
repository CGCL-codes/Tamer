package com.alexmcchesney.delicious;

/**
 * Exception thrown when there is a problem parsing the result.
 * @author AMCCHESNEY
 *
 */
public class XmlParserException extends DeliciousException {

    /**
	 * Constructor taking a source exception
	 * @param source
	 */
    public XmlParserException(Throwable source) {
        super(m_resources.getString("XML_PARSER_ERROR"), source);
    }
}
