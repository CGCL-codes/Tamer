package com.jdevflow.sunny.request;

import com.jdevflow.sunny.application.IApplication;
import com.jdevflow.sunny.components.page.IPage;
import com.jdevflow.sunny.request.parameters.RequestParameters;
import com.jdevflow.sunny.request.processors.PageRequestProcessor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple {@link IRequestProcessor} factory for regular pages. 
 * 
 * @author  SIY
 * @version 1.0
 */
public class DefaultPageProcessorFactory implements IProcessorFactory {

    private static final long serialVersionUID = 1L;

    private final IApplication app;

    /**
     * Constructor.
     * 
     * @param app
     */
    public DefaultPageProcessorFactory(IApplication app) {
        this.app = app;
    }

    /**
     * @see com.jdevflow.sunny.request.IProcessorFactory#cleanup()
     */
    public void cleanup() {
    }

    /**
     * @see IProcessorFactory#newInstance(HttpServletRequest, HttpServletResponse, RequestParameters, ISessionState, IApplication)
     */
    @SuppressWarnings("unchecked")
    public IRequestProcessor newInstance(HttpServletRequest request, HttpServletResponse response, RequestParameters parameters, ISessionState state, IApplication application) {
        try {
            IPage page = state.getStateful(parameters.getBaseUrl());
            String name = parameters.getPageClassName();
            Class<? extends IPage> cls = (Class<? extends IPage>) Class.forName(name);
            if (page != null && page.getClass() != cls) {
                state.removeStatefulTarget(parameters.getBaseUrl());
                page.cleanup();
            }
            page = cls.newInstance();
            return new PageRequestProcessor(request, response, parameters, state, application, page);
        } catch (Exception e) {
            app.log("Unable to create page", e);
            throw new RedirectException(app.getErrorPageURL());
        }
    }
}
