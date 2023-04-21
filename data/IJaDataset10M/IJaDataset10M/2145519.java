package com.livinglogic.struts.workflow;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.tiles.TilesRequestProcessor;

/**
 * The workflow request processor that must be used, if the
 * workflow extension is to be used together with the tiles plugin. 
 * The logic is not implemented here, but in WorkflowRequestProcessorLogic.
 * 
 * @author M. Bauer
 * @author $Author: jason_pratt $
 * @version $Revision: 1.3 $, $Date: 2004/04/08 14:58:23 $
 * @since $Name:  $
 */
public class TilesWorkflowRequestProcessor extends TilesRequestProcessor implements WorkflowRequestProcessorLogicAdapter {

    /**
	 * The WorkflowRequestProcessingLogic instance we are using 
	 */
    WorkflowRequestProcessorLogic logic;

    public void processForwardConfig(HttpServletRequest request, HttpServletResponse response, ForwardConfig forward) throws IOException, ServletException {
        super.processForwardConfig(request, response, forward);
    }

    public HttpServletRequest processMultipart(HttpServletRequest request) {
        return super.processMultipart(request);
    }

    public String processPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return super.processPath(request, response);
    }

    public void processLocale(HttpServletRequest request, HttpServletResponse response) {
        super.processLocale(request, response);
    }

    public void processContent(HttpServletRequest request, HttpServletResponse response) {
        super.processContent(request, response);
    }

    public void processNoCache(HttpServletRequest request, HttpServletResponse response) {
        super.processNoCache(request, response);
    }

    public boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
        return super.processPreprocess(request, response);
    }

    public ActionMapping processMapping(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        return super.processMapping(request, response, path);
    }

    public boolean processRoles(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException {
        return super.processRoles(request, response, mapping);
    }

    public ActionForm processActionForm(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) {
        return super.processActionForm(request, response, mapping);
    }

    public void processPopulate(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws ServletException {
        super.processPopulate(request, response, form, mapping);
    }

    public boolean processValidate(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws IOException, ServletException {
        return super.processValidate(request, response, form, mapping);
    }

    public boolean processForward(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException {
        return super.processForward(request, response, mapping);
    }

    public boolean processInclude(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException {
        return super.processInclude(request, response, mapping);
    }

    public Action processActionCreate(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException {
        return super.processActionCreate(request, response, mapping);
    }

    public ActionForward processActionPerform(HttpServletRequest request, HttpServletResponse response, Action action, ActionForm form, ActionMapping mapping) throws IOException, ServletException {
        return super.processActionPerform(request, response, action, form, mapping);
    }

    /**
	 * <p>Process an <code>HttpServletRequest</code> and create the
	 * corresponding <code>HttpServletResponse</code>.</p>
	 *
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 *
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a processing exception occurs
	 */
    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (null == logic) {
            logic = new WorkflowRequestProcessorLogic(this);
        }
        logic.process(request, response);
    }
}
