package com.centraview.administration.history;

import java.io.IOException;
import java.util.ArrayList;
import javax.ejb.CreateException;
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
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListHome;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public class HistoryListHandler extends org.apache.struts.action.Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, NamingException, CommunicationException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        HttpSession session = request.getSession(true);
        UserObject user = (UserObject) session.getAttribute("userobject");
        int individualId = user.getIndividualID();
        ListPreference listPreference = user.getListPreference("History");
        ValueListParameters listParameters = ActionUtil.valueListParametersSetUp(listPreference, request, ValueListConstants.DATA_HISTORY_LIST_TYPE, ValueListConstants.historyListViewMap, true);
        listParameters.setFilter("");
        ValueListHome valueListHome = (ValueListHome) CVUtility.getHomeObject("com.centraview.valuelist.ValueListHome", "ValueList");
        ValueList valueList = null;
        try {
            valueList = valueListHome.create();
        } catch (CreateException e) {
            System.out.println("[execute] Exception thrown." + e);
            throw new ServletException(e);
        }
        valueList.setDataSource(dataSource);
        ValueListVO listObject = valueList.getValueList(individualId, listParameters);
        ArrayList buttonList = new ArrayList();
        ValueListDisplay displayParameters = new ValueListDisplay(buttonList, true, false, true, true, true, true);
        listObject.setDisplay(displayParameters);
        request.setAttribute("valueList", listObject);
        return mapping.findForward(".view.administration.history_list");
    }
}
