package org.yaorma.web.servletController;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.yaorma.web.action.ActionInterface;
import org.yaorma.web.multipartForm.MultiPartFormRequest;

/**
 * 
 * @author greshje
 * 
 *         The heart of the Controller part of the MVC implementation of the
 *         Yaorma framework. This is the only servlet that should be implemented
 *         in a Yaorma project.
 * 
 *         This class should be extended by a Servlet class of the project using
 *         the Yaorma framework. That class should implement the getActionName
 *         and getActionPrefix methods.
 * 
 *         The getActionName indicates what the parameter passed in from the web
 *         page should be named (e.g. actionName).
 * 
 *         The getActionPrefix should indicate the prefix that should be used
 *         for the class names of the Action classes implemented in the project.
 *         For example, if a project implements all of its action classes in a
 *         package called com.myCompany.myProject.action then
 *         com.myCompany.myProject.action. should be returned by
 *         getActionPrefix.
 * 
 *         Note on thread safety: no member variables in the servlet. All
 *         variables are method variables and therefore should get there own
 *         stack memory allocations. Hence, no thread safe worries.
 * 
 */
public abstract class ControllerServlet extends HttpServlet {

    public ControllerServlet() {
    }

    public abstract String getActionName();

    public abstract String getActionPrefix();

    /**
	 * 
	 * executeBeforeAction method is called before execute method of the action
	 * is called.
	 * 
	 */
    public void executeBeforeAction(HttpServletRequest req, HttpServletResponse res, ActionInterface action) throws Exception {
    }

    /**
	 * 
	 * executeAfterAction method is called before execute method of the action
	 * is called.
	 * 
	 */
    public void executeAfterAction(HttpServletRequest req, HttpServletResponse res, ActionInterface action) throws Exception {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        String actionName = null;
        ActionInterface action = null;
        try {
            if (ServletFileUpload.isMultipartContent(req)) {
                req = new MultiPartFormRequest(req);
            }
            actionName = getActionPrefix() + req.getParameter(getActionName());
            actionName = actionName.replace("/", ".");
            action = (ActionInterface) Class.forName(actionName).newInstance();
            executeBeforeAction(req, res, action);
            action.execute(req, res);
            executeAfterAction(req, res, action);
            String nextUrl = action.getNextUrl();
            if (nextUrl != null) {
                RequestDispatcher disp = req.getRequestDispatcher(nextUrl);
                disp.forward(req, res);
            }
        } catch (Throwable thr) {
            throw new ServletException(thr);
        }
    }
}
