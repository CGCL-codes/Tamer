package com.centraview.contacts.entity;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.settings.Settings;

public final class ViewMOCHandler extends Action {

    private static Logger logger = Logger.getLogger(ViewMOCHandler.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        String returnStatus = ".view.error";
        try {
            MethodOfContactVO mcontactVO = new MethodOfContactVO();
            String rowId[] = null;
            if (request.getParameterValues("rowId") != null) {
                rowId = request.getParameterValues("rowId");
            } else {
                rowId[0] = new String(request.getParameter(Constants.PARAMID));
            }
            ContactFacadeHome aa = (ContactFacadeHome) CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
            ContactFacade remote = aa.create();
            remote.setDataSource(dataSource);
            mcontactVO = remote.getMOC(Integer.parseInt(rowId[0]));
            DynaActionForm dynaForm = (DynaActionForm) form;
            dynaForm.set("select", new Integer(mcontactVO.getMocType()).toString());
            dynaForm.set("text3", mcontactVO.getContent());
            dynaForm.set("mocid", new Integer(mcontactVO.getMocID()).toString());
            dynaForm.set("text4", mcontactVO.getNote());
            dynaForm.set("syncas", mcontactVO.getSyncAs());
            returnStatus = ".view.contacts.new_contact_method";
        } catch (Exception e) {
            logger.error("[execute]: Exception", e);
        }
        return mapping.findForward(returnStatus);
    }
}
