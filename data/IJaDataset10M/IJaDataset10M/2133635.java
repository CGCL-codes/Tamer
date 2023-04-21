package com.centraview.marketing;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.ListGenerator;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

public final class DeleteEventAttendeesHandler extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        try {
            String rowId[] = null;
            String listType = null;
            HttpSession session = request.getSession(true);
            rowId = request.getParameterValues("rowId");
            listType = request.getParameter("listType");
            String listId = request.getParameter("listId");
            long idd = 0;
            if (listId != null) idd = Long.parseLong(listId);
            ListGenerator lg = ListGenerator.getListGenerator(dataSource);
            DisplayList DL = lg.getDisplayList(idd);
            DisplayList displaylistSession = (DisplayList) session.getAttribute("displaylist");
            if ((displaylistSession != null) && (displaylistSession.getListID() == idd)) displaylistSession.setDirtyFlag(true);
            String eventID = request.getParameter("eventid").toString();
            int iCount;
            HashMap hm = new HashMap();
            hm.put("eventid", eventID);
            hm.put("individualid", rowId);
            MarketingFacadeHome aa = (MarketingFacadeHome) CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
            MarketingFacade remote = (MarketingFacade) aa.create();
            remote.setDataSource(dataSource);
            iCount = remote.deleteEventRegister(hm);
            request.setAttribute("TYPEOFOPERATION", "EDIT");
            request.setAttribute("FromAttendee", "NO");
            request.setAttribute("eventid", eventID);
        } catch (Exception e) {
            System.out.println("[Exception][DeleteEventAttendeesHandler.execute] Exception Thrown: " + e);
            e.printStackTrace();
        }
        return new ActionForward(request.getParameter("currentPage"), true);
    }
}
