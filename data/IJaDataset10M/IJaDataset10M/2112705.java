package ispyb.client.admin;

import java.util.ArrayList;
import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.data.interfaces.ProposalLightValue;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Category;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @struts.action name="tunningForm"
 *                path="/user/tunningAction"
 *                type="ispyb.client.admin.TunningAction"
 *                input="user.admin.tunning.page"
 *                validate="false" parameter="reqCode" scope="request"
 *                
 * @struts.action-forward name="tunningPage"
 *                        path="user.admin.tunning.page"
 *                        
 * @struts.action-forward name="error" path="site.default.error.page"
 */
public class TunningAction extends org.apache.struts.actions.DispatchAction {

    static Category Log = Category.getInstance(TunningAction.class.getName());

    /**
	 * display
	 * 
	 * @param mapping
	 * @param actForm
	 * @param request
	 * @param in_reponse
	 * @return
	 */
    public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
        if (!request.getUserPrincipal().toString().equalsIgnoreCase("ehtpx1")) return null;
        return mapping.findForward("tunningPage");
    }

    public ActionForward changeCurrentProposal(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
        try {
            TunningForm form = (TunningForm) actForm;
            String newProposalCode = form.getProposalCode();
            String newProposalNumber = form.getProposalNumber();
            request.getSession().setAttribute(Constants.PROPOSAL_CODE, newProposalCode);
            request.getSession().setAttribute(Constants.PROPOSAL_NUMBER, newProposalNumber);
            ArrayList authenticationInfo = StringUtils.GetProposalNumberAndCode(newProposalCode + newProposalNumber, null, null, null);
            String auth_proposalCode = (String) authenticationInfo.get(0);
            String auth_prefix = (String) authenticationInfo.get(1);
            String auth_proposalNumber = (String) authenticationInfo.get(2);
            ProposalLightValue proposal = ispyb.client.util.DBTools.getProposal(auth_proposalCode, new Integer(auth_proposalNumber));
            Integer proposalId = proposal.getProposalId();
            request.getSession().setAttribute(Constants.PROPOSAL_ID, proposalId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward("tunningPage");
    }
}
