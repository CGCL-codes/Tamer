package ispyb.client.admin;

import ispyb.client.util.ClientLogger;
import ispyb.server.data.interfaces.ProteinFacadeLocal;
import ispyb.server.data.interfaces.ProteinFacadeUtil;
import ispyb.server.data.interfaces.SessionFacadeLocal;
import ispyb.server.data.interfaces.SessionFacadeUtil;
import ispyb.server.data.interfaces.ShippingFacadeLocal;
import ispyb.server.data.interfaces.ShippingFacadeUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

/**
 * 
 * 
 * @struts.action name="updateProposalIdForm" 
 * 					path="/manager/updateProposalId"
 *                  input="manager.welcome.page"
 * 					validate="false"
 * 					parameter="reqCode"
 * 					scope="request"
 * @struts.action-forward name="error" 
 * 					path="site.default.error.page"
 * @struts.action-forward name="success" 
 * 					path="manager.admin.updateProposalId.page"
 *    
 * @web.ejb-local-ref name="ejb/SessionFacade" type="Stateless"
 *                    home="ispyb.server.data.interfaces.SessionFacadeLocalHome"
 *                    local="ispyb.server.interfaces.SessionFacadeLocal"
 *                    link="SessionFacade"
 * 
 * 
 * @jboss.ejb-local-ref jndi-name="ispyb/SessionFacadeLocalHome"
 *                      ref-name="SessionFacade"
 * 
 */
public class UpdateProposalIdAction extends DispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
        return mapping.findForward("success");
    }

    public ActionForward update(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_reponse) {
        ActionMessages errors = new ActionMessages();
        ActionMessages messages = new ActionMessages();
        UpdateProposalIdForm form = (UpdateProposalIdForm) actForm;
        Integer newPropId = form.getNewPropId();
        Integer oldPropId = form.getOldPropId();
        int nbUpdated = 0;
        try {
            SessionFacadeLocal session = SessionFacadeUtil.getLocalHome().create();
            nbUpdated = session.updateProposalId(newPropId, oldPropId);
            ShippingFacadeLocal ship = ShippingFacadeUtil.getLocalHome().create();
            nbUpdated = ship.updateProposalId(newPropId, oldPropId);
            ProteinFacadeLocal prot = ProteinFacadeUtil.getLocalHome().create();
            nbUpdated = prot.updateProposalId(newPropId, oldPropId);
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.updated", "proposalId"));
        } catch (Exception e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.user.collection.viewDataCollection"));
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
            e.printStackTrace();
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }
            if (!messages.isEmpty()) saveMessages(request, messages);
            ClientLogger.getInstance().debug("Update of proposalId is finished");
        }
        return mapping.findForward("success");
    }
}
