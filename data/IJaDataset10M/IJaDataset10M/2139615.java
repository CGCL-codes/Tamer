package com.centraview.sale;

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
import org.apache.struts.action.ActionMessages;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;

/**
 * DeleteOpportunityHandler
 *  Used to delete opportunity information.
 *
 * @author  Sunita
 */
public class DeleteOpportunityHandler extends Action {

    /** Global Forwards for exception handling.   */
    public static final String GLOBAL_FORWARD_failure = "failure";

    /** Local Forwards for redirecting to jsp. */
    private static final String FORWARD_Opportunity = "displayEditOpportunity";

    /** Local Forwards for redirecting to jsp. */
    private static final String FORWARD_ViewOpportunity = "viewOpportunity";

    /** Redirect constant.   */
    private static String FORWARD_final = GLOBAL_FORWARD_failure;

    private static Logger logger = Logger.getLogger(DeleteOpportunityHandler.class);

    /**
   *  Executes initialization of required parameters and open window for entry of note
   *  returns ActionForward
   */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        FORWARD_final = FORWARD_Opportunity;
        StringBuffer returnPath = new StringBuffer();
        try {
            HttpSession session = request.getSession();
            UserObject userObject = (UserObject) session.getAttribute("userobject");
            int individualID = userObject.getIndividualID();
            int opportunityid = Integer.parseInt(request.getParameter("opportunityid"));
            SaleFacadeHome aa = (SaleFacadeHome) CVUtility.getHomeObject("com.centraview.sale.salefacade.SaleFacadeHome", "SaleFacade");
            SaleFacade remote = (SaleFacade) aa.create();
            remote.setDataSource(dataSource);
            String closeornew = request.getParameter("closeornew");
            String pathParameter = null;
            try {
                int result = remote.deleteOpportunity(individualID, opportunityid);
                if (result == -2) {
                    ActionErrors allErrors = new ActionErrors();
                    allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "You have to delete first one or more associated proposal."));
                    session.setAttribute("listErrorMessage", allErrors);
                    FORWARD_final = FORWARD_ViewOpportunity;
                    pathParameter = "?rowId=" + opportunityid;
                }
            } catch (AuthorizationFailedException ae) {
                String errorMessage = ae.getExceptionDescription();
                ActionErrors allErrors = new ActionErrors();
                allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "You do not have permission to delete one or more of the records you selected."));
                session.setAttribute("listErrorMessage", allErrors);
                FORWARD_final = FORWARD_ViewOpportunity;
                pathParameter = "?rowId=" + opportunityid;
            }
            returnPath.append(mapping.findForward(FORWARD_final).getPath());
            if (pathParameter != null) {
                returnPath.append(pathParameter);
            }
        } catch (Exception e) {
            logger.error("[Exception] DeleteOpportunityHandler.execute: ", e);
        }
        return (new ActionForward(returnPath.toString(), true));
    }
}
