package org.openi.feeds;

import java.io.Reader;

/**
 * 
 * @author Dipendra Pokhrel
 *
 *
 */
public interface Parser {

    /**
	 * reads each line  from the reader, parses the fields and calls resultHandler's processFields()
	 * @param reader 
	 *  input reader	
	 * @param sampleSize 
	 * 	character delimeter
	 * @param sampleSize 
	 *  max lines to be processed. -1 to process all
	 * @param resultHandler 
	 *  {@link ParserResultHandler}
	 * @throws {@link ParseException}
	 */
    public void parse(Reader reader, char delimeter, long sampleSize, ParserResultHandler resultHandler) throws ParseException;
}
