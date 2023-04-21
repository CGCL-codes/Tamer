package com.centraview.email;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.centraview.common.CVUtility;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.settings.Settings;

public class RuleListHandler extends org.apache.struts.action.Action {

    /** method overridden from Action class
*/
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        System.out.println("In  RuleListHandler ");
        try {
            HttpSession session = request.getSession(true);
            com.centraview.common.UserObject userobjectd = (com.centraview.common.UserObject) session.getAttribute("userobject");
            ListPreference listpreference = userobjectd.getListPreference("Rule");
            RuleList displaylistSession = null;
            RuleList displaylist = null;
            try {
                displaylistSession = (RuleList) session.getAttribute("displaylist");
            } catch (ClassCastException e) {
                displaylistSession = null;
            }
            try {
                displaylist = (RuleList) request.getAttribute("displaylist");
            } catch (ClassCastException e) {
                displaylist = null;
            }
            RuleList DL = null;
            if (displaylist == null) {
                com.centraview.common.ListGenerator lg = com.centraview.common.ListGenerator.getListGenerator(dataSource);
                int records = listpreference.getRecordsPerPage();
                String sortelement = listpreference.getSortElement();
                DL = setLinksfunction(DL);
                session.setAttribute("displaylist", DL);
                request.setAttribute("displaylist", DL);
            } else if (displaylistSession != null) {
                String searchSession = displaylistSession.getSearchString();
                String searchrequest = displaylist.getSearchString();
                System.out.println("searchSession" + searchSession);
                System.out.println("searchrequest" + searchrequest);
                if (searchSession == null) searchSession = "";
                if (searchrequest == null) searchrequest = "";
                if ((displaylistSession.getListID() == displaylist.getListID()) && (displaylist.getDirtyFlag() == false) && (displaylist.getStartAT() >= displaylistSession.getBeginIndex()) && (displaylist.getEndAT() <= displaylistSession.getEndIndex()) && (displaylist.getSortMember().equals(displaylistSession.getSortMember())) && (displaylist.getSortType() == (displaylistSession.getSortType())) && (searchSession.equals(searchrequest))) {
                    request.setAttribute("displaylist", displaylistSession);
                } else {
                    ListGenerator lg = ListGenerator.getListGenerator(dataSource);
                    System.out.println(displaylist);
                    DL = setLinksfunction(DL);
                    session.setAttribute("displaylist", DL);
                    request.setAttribute("displaylist", DL);
                }
            }
            request.setAttribute("showAdvancedSearch", new Boolean(false));
            System.out.println("out from  RuleListHandler ");
        } catch (Exception e) {
            e.printStackTrace();
            return (mapping.findForward("failure"));
        }
        return (mapping.findForward("showerulelist"));
    }

    /**
  @   uses
    This function sets links on members
  */
    public RuleList setLinksfunction(RuleList DL) {
        Set listkey = DL.keySet();
        Iterator it = listkey.iterator();
        while (it.hasNext()) {
            try {
                String str = (String) it.next();
                StringMember sm = null;
                ListElement ele = (ListElement) DL.get(str);
                sm = (StringMember) ele.get("RuleName");
                sm.setRequestURL("goTo('GetRuleDetailsHandler.do?rowId=" + ele.getElementID() + "&listId=" + DL.getListID() + "')");
            } catch (Exception e) {
                System.out.println("RuleList:setLinksfunction" + e);
            }
        }
        return DL;
    }
}
