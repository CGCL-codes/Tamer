package org.amlfilter.service;

import org.amlfilter.model.SearchExplain;

/**
 * The search explain service interface
 * @author Harish Seshadri
 * @version $Id$
 */
public interface SearchExplainServiceInterface {

    public static final String SIMPLE_LEVEL = "SIMPLE_LEVEL";

    public static final String DETAILED_LEVEL = "DETAILED_LEVEL";

    /**
	 * Create a search explain object (with the default level)
	 * @return A search explain object
	 */
    public SearchExplain createSearchExplain();

    /**
	 * Create a search explain object given the level
	 * @return A search explain object for the given level
	 */
    public SearchExplain createSearchExplain(String pExplainLevel);

    /**
	 * Convert the search explain object to XML
	 * @param pSearchExplain The search explain object
	 * @return The search explain object as XML
	 */
    public String explainToXML(SearchExplain pSearchExplain);

    /**
	 * Convert the XML to search explain object
	 * @param pSearchExplainStr The search explain string
	 * @return The search explain object
	 */
    public SearchExplain XMLToExplain(String pSearchExplainStr);
}
