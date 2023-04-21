package com.anasoft.os.sample.gwt.client;

/**
 * 
 * @gwt.properties.filename book.properties
 */
public interface BookPropertiesReader extends PropertiesReader {

    String year();

    /**
	 * @gwt.key name
	 */
    String getBookName();

    String publisher();

    /**
	 * @gwt.key author
	 */
    String getAuthorName();
}
