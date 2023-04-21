package com.jdevflow.sunny.request.processors;

import com.jdevflow.sunny.application.IApplication;
import com.jdevflow.sunny.components.IComponent;
import com.jdevflow.sunny.components.page.IPage;
import com.jdevflow.sunny.html.tree.Node;
import com.jdevflow.sunny.request.ISessionState;
import com.jdevflow.sunny.request.parameters.RequestParameters;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Page request processor.
 * 
 * @author  SIY
 * @version 1.0
 */
public class PageRequestProcessor extends AbstractRequestProcessor {

    private static final long serialVersionUID = 1L;

    private final IPage page;

    /**
     * Constructor.
     * 
     * @param request
     * @param response
     * @param parameters
     * @param state
     * @param application
     * @param page 
     */
    public PageRequestProcessor(HttpServletRequest request, HttpServletResponse response, RequestParameters parameters, ISessionState state, IApplication application, IPage page) {
        super(request, response, parameters, state, application);
        this.page = page;
        page.init(application);
    }

    /**
     * Render response.
     * 
     * @throws IOException 
     * @throws ServletException 
     */
    public void renderResponse() throws IOException, ServletException {
        getResponse().setContentType("text/html");
        page.beforeRender(this);
        page.render(this);
        page.afterRender(this);
    }

    /**
     * Process events.
     * 
     * @return <code>true</code> if further request processing should be performed.
     * 
     * @throws IOException
     * @throws ServletException
     */
    public boolean processEvents() throws IOException, ServletException {
        return page.processEvents(this);
    }

    /**
     * @see com.jdevflow.sunny.request.processors.AbstractRequestProcessor#process()
     */
    @Override
    public void process() throws IOException, ServletException {
        page.loadModel();
        page.initDocument(this);
        if (getParameters().isResource()) {
            page.processResource(this);
            return;
        }
        boolean doFullRender = true;
        if (getParameters().hasTarget() && getParameters().hasEvent()) {
            doFullRender = false;
        }
        if (getRequest().getMethod().equals("POST")) {
        }
        if (getParameters().hasTarget()) processEvents();
        if (doFullRender) renderResponse(); else renderAjaxResponse();
        if (page.isStateful()) getState().addStatefulPage(getParameters().getBaseUrl(), page); else {
            getState().removeStatefulTarget(getParameters().getBaseUrl());
            page.cleanup();
        }
        page.detachModel();
    }

    /**
     * Render partial page update.
     */
    protected void renderAjaxResponse() {
        Node response = new Node("response");
        for (IComponent target : getTargets()) response.add(target.body());
    }
}
