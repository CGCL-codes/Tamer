package com.duroty.controller.requestprocessor;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ModuleConfig;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DOCUMENT ME!
 *
 * @author DUROT
 * @version 1.0
 */
public class DRequestProcessor extends RequestProcessor {

    /**
     * DOCUMENT ME!
     *
     * @param servlet DOCUMENT ME!
     * @param moduleConfig DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     */
    public void init(ActionServlet servlet, ModuleConfig moduleConfig) throws ServletException {
        super.init(servlet, moduleConfig);
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     * @param action DOCUMENT ME!
     * @param form DOCUMENT ME!
     * @param mapping DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     */
    protected ActionForward processActionPerform(HttpServletRequest request, HttpServletResponse response, Action action, ActionForm form, ActionMapping mapping) throws IOException, ServletException {
        return super.processActionPerform(request, response, action, form, mapping);
    }
}
