package org.omegat.filters.xml;

/**
 * An abstract filter of XML streams.
 *
 * @author Keith Godfrey
 */
public abstract class XMLStreamFilter {

    public abstract String convertToEscape(char c);

    public abstract char convertToChar(String escapeSequence);
}
