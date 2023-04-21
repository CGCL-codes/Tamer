package com.release.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpSession;

public class AddReleaseDetail extends Action {

    public ActionForward execute(ActionMapping a_CAcmMapping, ActionForm a_CAcfForm, HttpServletRequest a_CSrvRequest, HttpServletResponse a_CSrvResponse) {
        String lCStrForwardTo = null;
        try {
            System.out.println("Login Action Called");
            lCStrForwardTo = "success";
        } catch (Exception e) {
            lCStrForwardTo = "genericErrorPage";
            a_CSrvRequest.setAttribute("EXCEPTIONTYPE", "PMI_EXCEPTION");
        }
        return (a_CAcmMapping.findForward(lCStrForwardTo));
    }
}
