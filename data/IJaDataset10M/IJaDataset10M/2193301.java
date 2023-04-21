package com.maiereni.web.filter;

import java.util.Iterator;
import java.util.List;

/**
 * Defines methods of a class that is used by the Ajax***Filter to store or load matching
 * criteria for request urls
 * 
 * @author Petre Maierean
 *
 */
public interface ConfigMap {

    /**
	 * Store the content of the object
	 * @param destination the destination to store to. This string must be a file path
	 * @throws Exception
	 */
    void store(String destination) throws Exception;

    /**
	 * Load matching criteria from the specified source
	 * @param source the source to load from (this can be an absolute file path or a resource in the classpath)
	 * @return
	 * @throws Exception
	 */
    String load(String source) throws Exception;

    /**
	 * Add a new matching criteria 
	 * @param url a string representation of a request including any parameters
	 * @param elementId the destination element id that receives an AJAX update for the given URL
	 */
    void addToMap(String url, String elementId);

    /**
	 * Lists all the element ids that receive AJAX updates for the given URL
	 * @param url a string representation of a request including any parameters
	 * @return a list of ids
	 */
    List<String> getElementIds(String url);

    /**
	 * Check if the specified url does match any criteria in the map
	 * @param url a string representation of a request including any parameters
	 * @return true or false
	 */
    boolean matchURL(String url);

    /**
	 * Lists all the urls contained in the map
	 * @return a list of matching urls
	 */
    Iterator<Object> getURLs();

    /**
	 * Returns a script representation of the matching criteria
	 * @return a script representation of the criteria
	 */
    String scriptify();
}
