package org.amlfilter.engine.filter;

import java.util.List;
import java.util.Map;
import org.amlfilter.model.Result;

/**
 * This interface declares methods for name search operations
 * @author Harish Seshadri
 * @version $Id: NameSearchFilter.java,v 1.1 2007/01/28 07:13:47 hseshadr Exp $
 */
public interface NameSearchFilter {

    /**
	 * Filter the search results
	 * @param pSearchResults The search results
	 * @param pParametersMap The parameters map
	 * @throws Exception 
	 */
    public void filterSearchResults(List<Result> pSearchResults, Map pParametersMap) throws Exception;
}
