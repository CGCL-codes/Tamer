package com.kenai.jbosh;

/**
 * Interface for parser implementations to implement in order to abstract the
 * business of XML parsing out of the Body class.  This allows us to leverage
 * a variety of parser implementations to gain performance advantages.
 */
interface BodyParser {

    /**
     * Parses the XML message, extracting the useful data from the initial
     * body element and returning it in a results object.
     *
     * @param xml XML to parse
     * @return useful data parsed out of the XML
     * @throws BOSHException on parse error
     */
    BodyParserResults parse(String xml) throws BOSHException;
}
