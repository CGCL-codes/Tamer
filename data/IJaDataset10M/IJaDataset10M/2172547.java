package com.centraview.administration.user;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This class sets up the necessary information for the
 * List View. 
 * 
 * @author CentraView, LLC.
 */
public class UserListHandler extends Action {

    private static Logger logger = Logger.getLogger(UserListHandler.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        HttpSession session = request.getSession(true);
        UserObject user = (UserObject) session.getAttribute("userobject");
        int individualId = user.getIndividualID();
        ListPreference listPreference = user.getListPreference("USER");
        ValueListParameters listParameters = ActionUtil.valueListParametersSetUp(listPreference, request, ValueListConstants.USER_LIST_TYPE, ValueListConstants.userViewMap, true);
        ValueList valueList = null;
        try {
            valueList = (ValueList) CVUtility.setupEJB("ValueList", "com.centraview.valuelist.ValueListHome", dataSource);
        } catch (Exception e) {
            logger.error("[execute] Exception thrown.", e);
            throw new ServletException(e);
        }
        ValueListVO listObject = valueList.getValueList(individualId, listParameters);
        ArrayList buttonList = new ArrayList();
        buttonList.add(new Button("New", "newUser", "c_goTo('/administration/new_user.do');", false));
        buttonList.add(new Button("Delete", "delete", "vl_deleteList();", false));
        ValueListDisplay displayParameters = new ValueListDisplay(buttonList, true, false, true, true, true, true);
        listObject.setDisplay(displayParameters);
        request.setAttribute("valueList", listObject);
        return mapping.findForward(".view.administration.user_list");
    }
}
