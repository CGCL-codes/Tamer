package com.apelon.apps.dts.treebrowser.classify.actions;

import java.io.*;
import java.util.Vector;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import com.apelon.apps.dts.treebrowser.logon.actions.LogonUtilities;
import com.apelon.apps.dts.treebrowser.classify.beans.*;
import com.apelon.apps.dts.treebrowser.classify.application.fetchers.*;
import com.apelon.dts.client.concept.SearchQuery;
import com.apelon.dts.client.concept.NavQuery;
import com.apelon.dts.client.concept.OntylogConceptQuery;
import com.apelon.dts.client.association.AssociationQuery;
import com.apelon.dts.client.namespace.NamespaceQuery;
import com.apelon.dts.client.match.MatchQuery;
import com.apelon.apps.dts.treebrowser.reporting.beans.ReportingBean;
import com.apelon.common.log4j.Categories;
import com.apelon.common.log4j.StackTracePrinter;

public final class RemoveAdderAction extends Action {

    /**
   * Gets concept display bean parameters from request, generates a formatted XML string.
   * <p>
   *
   * @param mapping       the ActionMapping used to select this instance
   * @param form          the optional ActionForm bean for this request (if any)
   * @param request       the HTTP request we are processing
   * @param response      the HTTP response we are creating
   *
   * @exception           IOException if an input/output error occurs
   * @exception           ServletException if a servlet exception occurs
   *
   * @return mapping.findForward(status)  the search outcome such as "success" or "fail"
   */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String status = null;
        HttpSession session = request.getSession();
        ReportingBean reportingBean = null;
        NewConceptBean conceptBean = null;
        String conceptName = null;
        String primitive = null;
        MessageResources errorMessages = MessageResources.getMessageResources("com.apelon.apps.dts.treebrowser.resources.error_messages");
        LogonUtilities logonUtilities = new LogonUtilities();
        if (logonUtilities.checkSession(session, status, reportingBean, errorMessages)) {
            reportingBean = (ReportingBean) session.getAttribute("reportingBean");
            if ((reportingBean.getMessageHtml().indexOf("Classify")) == -1) {
                reportingBean.setReportingHtml("");
            }
            conceptBean = (NewConceptBean) session.getAttribute("newConceptEntity");
            conceptName = request.getParameter("conceptName");
            primitive = request.getParameter("primitive");
            conceptBean.setConceptName(conceptName);
            conceptBean.setPrimitive(primitive);
            String[] roleMods = request.getParameterValues("some_or_all");
            String[] roleGrps = request.getParameterValues("role_group");
            Vector roles = conceptBean.getRoles();
            RoleBean[] roleCons = new RoleBean[roles.size()];
            roles.copyInto(roleCons);
            if (roleMods != null && roleGrps != null && roleCons != null) {
                if (roleCons.length == roleMods.length && roleCons.length == roleGrps.length) {
                    for (int i = 0; i < roleCons.length; i++) {
                        roleCons[i].setSomeOrAll(roleMods[i]);
                        roleCons[i].setRoleGroup(roleGrps[i]);
                    }
                }
            }
            String[] deleteSup = request.getParameterValues("deleteSup");
            String[] deleteRole = request.getParameterValues("deleteRole");
            Vector supConcepts = conceptBean.getSuperConcepts();
            SuperBean[] supCons = new SuperBean[supConcepts.size()];
            supConcepts.copyInto(supCons);
            if (supCons != null && deleteSup != null) {
                for (int i = 0; i < supCons.length; i++) {
                    for (int j = 0; j < deleteSup.length; j++) {
                        if (supCons[i].getName().equals(deleteSup[j])) {
                            supConcepts.remove(supCons[i]);
                        }
                    }
                }
            }
            if (roleCons != null && deleteRole != null) {
                for (int i = 0; i < roleCons.length; i++) {
                    for (int j = 0; j < deleteRole.length; j++) {
                        if (roleCons[i].getRoleValue().equals(deleteRole[j])) {
                            roles.remove(roleCons[i]);
                        }
                    }
                }
            }
            conceptBean.setSuperConcepts(supConcepts);
            conceptBean.setRoles(roles);
            if (supConcepts.isEmpty() && roles.isEmpty()) {
                conceptBean.setConceptNamespace(null);
            }
            NewConceptDisplayBean conceptDisplayBean = new NewConceptDisplayBean();
            conceptDisplayBean.setNewConceptHtml(conceptBean);
            session.setAttribute("newConceptEntity", conceptBean);
            session.setAttribute("newConceptDisplayBean", conceptDisplayBean);
            status = "success";
        }
        return (mapping.findForward(status));
    }
}
