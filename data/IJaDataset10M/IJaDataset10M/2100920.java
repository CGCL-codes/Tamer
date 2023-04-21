package com.centraview.hr.timesheet;

import java.io.IOException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.centraview.common.CVUtility;
import com.centraview.hr.hrfacade.HrFacade;
import com.centraview.hr.hrfacade.HrFacadeHome;
import com.centraview.settings.Settings;

public class DeleteTimeSheetHandler extends org.apache.struts.action.Action {

    private com.centraview.common.UserObject userobjectd;

    private org.apache.struts.validator.DynaValidatorForm dynaValidatorForm;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        String returnStatus = "failure";
        HttpSession session = request.getSession(true);
        if (session.getAttribute("highlightmodule") != null) session.setAttribute("highlightmodule", "hr");
        userobjectd = (com.centraview.common.UserObject) session.getAttribute("userobject");
        HrFacadeHome aa = (HrFacadeHome) CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
        try {
            HrFacade remote = (HrFacade) aa.create();
            remote.setDataSource(dataSource);
            int indvID = userobjectd.getIndividualID();
            String sTimeSheetId = (String) request.getParameter("timesheetId");
            Integer iTimeSheetId = new Integer(sTimeSheetId);
            int timeSheetID = 0;
            if (iTimeSheetId != null) timeSheetID = iTimeSheetId.intValue();
            remote.deleteTimeSheet(indvID, timeSheetID);
            returnStatus = "success";
        } catch (Exception e) {
            returnStatus = "failure";
            System.out.println("[Exception][DeleteTimeSheetHandler.execute] Exception Thrown: " + e);
            e.printStackTrace();
        }
        return mapping.findForward(returnStatus);
    }
}
