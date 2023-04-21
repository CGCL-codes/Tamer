package com.centraview.customer.email;

import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public class EmailListHandler extends Action {

    private static Logger logger = Logger.getLogger(EmailListHandler.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        String forward = ".view.customer.email_list";
        HttpSession session = request.getSession();
        UserObject userObject = (UserObject) session.getAttribute("userobject");
        int individualID = userObject.getIndividualID();
        int entityID = userObject.getEntityId();
        ActionErrors allErrors = new ActionErrors();
        try {
            ListPreference listPreference = userObject.getListPreference("Email");
            ValueListParameters listParameters = ActionUtil.valueListParametersSetUp(listPreference, request, ValueListConstants.CUSTOMER_EMAIL_LIST_TYPE, ValueListConstants.customerEmailViewMap, false);
            String filter = "SELECT em.MessageID " + "FROM emailmessage em LEFT JOIN emailrecipient er ON (em.MessageID=er.MessageID) " + "LEFT JOIN emailmessagefolder es ON (em.MessageID = es.MessageID) " + "LEFT JOIN methodofcontact moc ON (er.Address=moc.Content) " + "LEFT JOIN mocrelate mr ON (moc.MOCID=mr.MOCID) " + "LEFT JOIN individual i ON (mr.ContactID=i.IndividualID) " + "WHERE mr.ContactType=2 AND moc.MOCType=1 AND es.FolderID IS NOT NULL AND i.Entity = " + entityID + " UNION SELECT em.MessageID " + "FROM emailmessage em LEFT JOIN emailrecipient er ON (em.MessageID=er.MessageID) " + "LEFT JOIN emailmessagefolder es ON (em.MessageID = es.MessageID) " + "LEFT JOIN methodofcontact moc ON (er.Address=moc.Content) " + "LEFT JOIN mocrelate mr ON (moc.MOCID=mr.MOCID) " + "WHERE mr.ContactType=1 AND moc.MOCType=1 AND es.FolderID IS NOT NULL AND mr.ContactID = " + entityID;
            listParameters.setFilter(filter);
            ValueList valueList = (ValueList) CVUtility.setupEJB("ValueList", "com.centraview.valuelist.ValueListHome", dataSource);
            ValueListVO listObject = valueList.getValueList(individualID, listParameters);
            ValueListDisplay displayParameters = new ValueListDisplay(new ArrayList(), false, false, true, true, true, true);
            listObject.setDisplay(displayParameters);
            request.setAttribute("valueList", listObject);
        } catch (Exception e) {
            logger.error("[execute] Exception thrown.", e);
            allErrors.add("error.unknownError", new ActionMessage("error.unknownError"));
        }
        if (!allErrors.isEmpty()) {
            saveErrors(request, allErrors);
        }
        return (mapping.findForward(forward));
    }
}
