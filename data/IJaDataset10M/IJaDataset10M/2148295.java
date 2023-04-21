package com.apelon.apps.dts.treebrowser.classify.actions;

import java.io.*;
import java.net.URLDecoder;
import java.util.Vector;
import java.util.Enumeration;
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
import com.apelon.apps.dts.treebrowser.reporting.beans.ReportingBean;
import com.apelon.apps.dts.treebrowser.resources.BrowserUtilities;

public final class NewConceptAttsAction extends Action {

    /**
   * Gets search parameters from request, coordinates data retrieval and sets display
   * bean with stored HTML in the session.
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
        NewConceptBean newConceptEntity = new NewConceptBean();
        ReportingBean reportingBean = null;
        String type = null;
        String conceptName = null;
        String conceptNamespace = null;
        String roleType = null;
        MessageResources errorMessages = MessageResources.getMessageResources("com.apelon.apps.dts.treebrowser.resources.error_messages");
        LogonUtilities logonUtilities = new LogonUtilities();
        if (logonUtilities.checkSession(session, status, reportingBean, errorMessages)) {
            reportingBean = (ReportingBean) session.getAttribute("reportingBean");
            if ((reportingBean.getMessageHtml().indexOf("Classify")) == -1) {
                reportingBean.setReportingHtml("");
            }
            if (!((NewConceptBean) session.getAttribute("newConceptEntity") == null)) {
                newConceptEntity = (NewConceptBean) session.getAttribute("newConceptEntity");
            }
            type = request.getParameter("type");
            conceptName = request.getParameter("concept_name");
            conceptNamespace = request.getParameter("concept_namespace");
            conceptName = BrowserUtilities.decodeUrl(conceptName);
            conceptNamespace = BrowserUtilities.decodeUrl(conceptNamespace);
            if (newConceptEntity.getConceptNamespace() == null) {
                newConceptEntity.setConceptNamespace(conceptNamespace);
            } else if (!newConceptEntity.getConceptNamespace().equals(conceptNamespace)) {
                newConceptEntity = new NewConceptBean();
                newConceptEntity.setConceptNamespace(conceptNamespace);
            }
            if (type.equals("super")) {
                Vector superConcepts = newConceptEntity.getSuperConcepts();
                SuperBean superBean = new SuperBean();
                superBean.setName(conceptName);
                superBean.setNamespace(conceptNamespace);
                if (!isContains(superConcepts, superBean)) {
                    superConcepts.add(superBean);
                }
                newConceptEntity.setSuperConcepts(superConcepts);
            }
            if (type.equals("role")) {
                roleType = request.getParameter("role_type");
                roleType = BrowserUtilities.decodeUrl(roleType);
                Vector roles = newConceptEntity.getRoles();
                RoleBean roleBean = new RoleBean();
                roleBean.setRoleType(roleType);
                roleBean.setRoleValue(conceptName);
                roleBean.setNamespace(conceptNamespace);
                if (!isContains(roles, roleBean)) {
                    roles.add(roleBean);
                }
                newConceptEntity.setRoles(roles);
            }
            NewConceptDisplayBean newConceptDisplayBean = new NewConceptDisplayBean();
            newConceptDisplayBean.setNewConceptHtml(newConceptEntity);
            status = "success";
            session.setAttribute("newConceptDisplayBean", newConceptDisplayBean);
            session.setAttribute("newConceptEntity", newConceptEntity);
        }
        return (mapping.findForward(status));
    }

    private boolean isContains(Vector vec, Object obj) {
        boolean result = vec.contains(obj);
        if (obj instanceof SuperBean) {
            SuperBean superBean = (SuperBean) obj;
            Enumeration enumElement = vec.elements();
            while (enumElement.hasMoreElements()) {
                SuperBean elem = (SuperBean) enumElement.nextElement();
                if (elem.getName().equals(superBean.getName()) && elem.getNamespace().equals(superBean.getNamespace())) {
                    result = true;
                    break;
                }
            }
        }
        if (obj instanceof RoleBean) {
            RoleBean roleBean = (RoleBean) obj;
            Enumeration enumElement = vec.elements();
            while (enumElement.hasMoreElements()) {
                RoleBean elem = (RoleBean) enumElement.nextElement();
                if (elem.getRoleType().equals(roleBean.getRoleType()) && elem.getRoleValue().equals(roleBean.getRoleValue())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
