package de.nava.informa.core;

import org.jdom.Element;

/**
 * Interface for a parser which reads in document instances according 
 * to some specific channel format specification and generates a news 
 * channel object.
 * 
 * @author Italo Borssatto
 */
public interface ChannelParserIF {

    /**
   * Method that implements the parser.
   */
    ChannelIF parse(ChannelBuilderIF cBuilder, Element channel) throws ParseException;
}
