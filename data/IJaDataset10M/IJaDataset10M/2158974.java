package com.jaspersoft.jasperserver.api.metadata.view.service;

import java.util.List;
import com.jaspersoft.jasperserver.api.common.domain.ExecutionContext;
import com.jaspersoft.jasperserver.api.common.domain.Id;
import com.jaspersoft.jasperserver.api.metadata.common.domain.RenderableView;
import com.jaspersoft.jasperserver.api.metadata.view.domain.FilterCriteria;
import com.jaspersoft.jasperserver.api.metadata.view.domain.OutputFormat;

/**
 * @author swood
 * @version $Id: ViewService.java 3 2006-04-30 18:09:42Z sgwood $
 */
public interface ViewService {

    /**
	 * Given the execution context and the filtering criteria, find the list
	 * of relevant views and return them in the desired format. In addition to 
	 * requested filters, security filters will restrict the returned views to
	 * only those the "user" in the execution context is allowed to access.
	 * 
	 * @param context
	 * @param criteria
	 * @param format
	 * @return List of ViewNode
	 */
    public List getViews(ExecutionContext context, FilterCriteria criteria, OutputFormat format);

    /**
	 * Given a context and a view id, return the metadata defining the view.
	 * If the "user" does not have access to the view, return null(?)
	 * 
	 * @param context
	 * @param viewId
	 * @return RenderableView or null
	 */
    public RenderableView getView(ExecutionContext context, Id viewId);
}
