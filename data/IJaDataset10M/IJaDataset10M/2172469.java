package com.missingmatter.ivr.reporting;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Missing Matter Productions Pty Ltd</p>
 * @version 1.0
 * @author alan whiteside
 *
 */
public interface IDataCollector {

    public void doDataCollection(ServletRequest p_Request, ServletResponse p_Response);

    /**
	 * initialises the Report with the given set of Params, Conditions (a set of IDataCollectorCondition's) and SessionVar id's .
	 * 
	 * @param p_Params
	 * @param p_Conditions
	 * @param p_SessionVars
	 */
    public void init(String p_Category, String p_SubCategory, Map p_Params, Set p_Conditions, Set p_SessionVars);

    /**
	 * Called at the end of the session to remove any db connections etc.
	 *
	 */
    public void destroy();
}
