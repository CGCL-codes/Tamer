package com.duroty.lucene.parser;

import com.duroty.lucene.parser.exception.ParserException;
import java.io.File;
import java.io.InputStream;

/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public interface Parser {

    /**
     * DOCUMENT ME!
     *
     * @param charset DOCUMENT ME!
     */
    public void setCharset(String charset);

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParserException DOCUMENT ME!
     */
    public String parse(File file) throws ParserException;

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ParserException DOCUMENT ME!
     */
    public String parse(InputStream in) throws ParserException;

    /**
     * DOCUMENT ME!
     *
     * @param sleep DOCUMENT ME!
     */
    public void setSleep(long sleep);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle();
}
