package com.knwebapp.struts.googlemap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.knbase.struts.action.BaseAction;
import com.knwebapp.gwt.googlemap.client.KNConstant;

public class Action extends BaseAction {

    public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("LatLngX", KNConstant.SAIGON_X_S);
        request.setAttribute("LatLngY", KNConstant.SAIGON_Y_S);
        return mapping.findForward("success");
    }
}
