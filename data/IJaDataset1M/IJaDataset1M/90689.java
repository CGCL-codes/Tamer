package com.centraview.support.faq;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.ListElement;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.common.SupportConstantKeys;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

/**
 * ViewTicketHandler.java
 *
 * @author CentraView, LLC.
 */
public class ViewFaqHandler extends Action {

    public static final String GLOBAL_FORWARD_failure = "failure";

    private static final String FORWARD_editfaq = ".view.support.faq.edit";

    private static final String FORWARD_viewfaq = ".view.support.faq.view";

    private static final String FORWARD_addfaq = ".view.support.faq.new";

    private static String FORWARD_final = GLOBAL_FORWARD_failure;

    private static Logger logger = Logger.getLogger(ViewFaqHandler.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        HttpSession session = request.getSession(true);
        UserObject userObject = (UserObject) session.getAttribute("userobject");
        DynaActionForm dynaForm = (DynaActionForm) form;
        int individualID = userObject.getIndividualID();
        int faqId = 0;
        Vector questionColumns = new Vector();
        questionColumns.addElement("Question");
        questionColumns.addElement("Answer");
        request.setAttribute(SupportConstantKeys.TYPEOFSUBMODULE, "FAQ");
        request.setAttribute("questionColumns", questionColumns);
        String typeOfOperation = request.getParameter(Constants.TYPEOFOPERATION);
        if (typeOfOperation.equals(SupportConstantKeys.EDIT) || typeOfOperation.equals(SupportConstantKeys.DUPLICATE) || typeOfOperation.equals(SupportConstantKeys.VIEW)) {
            if (request.getAttribute("faqId") != null) {
                faqId = ((Integer) (request.getAttribute("faqId"))).intValue();
            } else {
                faqId = Integer.parseInt(request.getParameter("rowId"));
            }
            int ownerID = 0;
            FaqVO faqVO = new FaqVO();
            SupportFacadeHome supFacade = (SupportFacadeHome) CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
            try {
                SupportFacade remote = supFacade.create();
                remote.setDataSource(dataSource);
                FaqVO fVO = remote.getFaq(individualID, faqId);
                dynaForm.set("faqid", new Integer(fVO.getFaqId()).toString());
                dynaForm.set("title", fVO.getTitle());
                dynaForm.set("status", fVO.getStatus());
                dynaForm.set("publishToCustomerView", fVO.getPublishToCustomerView());
                ownerID = fVO.getOwner();
                QuestionList questionList = remote.getQuestionList(individualID, fVO.getFaqId());
                questionList = setLinksfunction(questionList);
                request.setAttribute("questionlist", questionList);
                boolean editFlag = false;
                if (fVO.getStatus() != null && fVO.getStatus().equals("PUBLISH")) {
                    AuthorizationHome homeAuthorization = (AuthorizationHome) CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome", "Authorization");
                    try {
                        Authorization remoteAuthorization = homeAuthorization.create();
                        remoteAuthorization.setDataSource(dataSource);
                        editFlag = remoteAuthorization.canPerformRecordOperation(individualID, "FAQ", faqId, ModuleFieldRightMatrix.UPDATE_RIGHT);
                    } catch (Exception e) {
                        logger.error("[Exception] [ViewFaqHandler.execute Calling Authorization]  ", e);
                        FORWARD_final = GLOBAL_FORWARD_failure;
                    }
                } else if (individualID == ownerID) {
                    editFlag = true;
                }
                request.setAttribute("showEditRecordButton", new Boolean(editFlag));
            } catch (Exception e) {
                logger.error("[Exception] [ViewFaqHandler.execute Calling SupportFacade]  ", e);
                FORWARD_final = GLOBAL_FORWARD_failure;
            }
        }
        if (typeOfOperation.equals(SupportConstantKeys.VIEW)) {
            FORWARD_final = FORWARD_viewfaq;
        } else if (typeOfOperation.equals(SupportConstantKeys.ADD)) {
            FORWARD_final = FORWARD_addfaq;
        } else {
            FORWARD_final = FORWARD_editfaq;
        }
        String closeornew = (String) request.getParameter("closeornew");
        String saveandclose = null;
        String saveandnew = null;
        if (closeornew != null) {
            if (closeornew.equals("close")) {
                saveandclose = "saveandclose";
            } else if (closeornew.equals("new")) {
                dynaForm.set("title", "");
                dynaForm.set("status", "DRAFT");
                saveandnew = "saveandnew";
            }
            if (saveandclose != null) {
                request.setAttribute("closeWindow", "true");
            }
            request.setAttribute("refreshWindow", "true");
        }
        return mapping.findForward(FORWARD_final);
    }

    public QuestionList setLinksfunction(QuestionList DL) {
        Set listkey = DL.keySet();
        Iterator it = listkey.iterator();
        while (it.hasNext()) {
            try {
                String str = (String) it.next();
                StringMember sm = null;
                ListElement ele = (ListElement) DL.get(str);
                sm = (StringMember) ele.get("Question");
                sm.setRequestURL("c_openWindow('/support/display_question.do?typeofoperation=EDIT&rowId=" + ele.getElementID() + "', 'displayQuestion', 380, 270, '')");
            } catch (Exception e) {
                System.out.println("[Exception] ViewFaqHandler.setLinksfunction: " + e.toString());
                e.printStackTrace();
            }
        }
        return DL;
    }
}
