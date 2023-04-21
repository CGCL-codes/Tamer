package actions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.account.AccountDetails;
import logic.incomingMailFilter.IncomingMailFilterSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Handles moving filter rule one place up.
 * @author marcin
 */
public class MoveRuleUp extends CommonConfigRules {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            String nrStr = (String) request.getParameter("ruleMoveUpNr");
            int i = Integer.parseInt(nrStr);
            AccountDetails details = (AccountDetails) request.getSession().getAttribute("accountDetails");
            IncomingMailFilterSession aSession = homeFilter.create();
            aSession.swapTwoFilterRules(details.id, i, i - 1);
            return renewRulesConfig(request, mapping, aSession);
        } catch (Exception e) {
            e.printStackTrace();
            return mapping.findForward("notConnected");
        }
    }
}
