package org.apache.roller.presentation.weblog.actions;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.roller.RollerException;
import org.apache.roller.config.PingConfig;
import org.apache.roller.model.PingTargetManager;
import org.apache.roller.model.RollerFactory;
import org.apache.roller.pojos.PingTargetData;
import org.apache.roller.pojos.WebsiteData;
import org.apache.roller.presentation.forms.PingTargetForm;
import org.apache.roller.presentation.RollerRequest;
import org.apache.roller.presentation.RollerSession;

/**
 * Administer custom ping targets.
 *
 * @author <a href="mailto:anil@busybuddha.org">Anil Gangolli</a>
 * @struts.action name="pingTargetForm" path="/editor/customPingTargets" scope="request" parameter="method"
 * @struts.action-forward name="pingTargets.page" path=".CustomPingTargets"
 * @struts.action-forward name="pingTargetEdit.page" path=".CustomPingTargetEdit"
 * @struts.action-forward name="pingTargetDeleteOK.page" path=".CustomPingTargetDeleteOK"
 */
public class CustomPingTargetsAction extends BasePingTargetsAction {

    private static Log mLogger = LogFactory.getFactory().getInstance(CustomPingTargetsAction.class);

    public String getPingTargetsTitle() {
        return "customPingTargets.customPingTargets";
    }

    public String getPingTargetEditTitle() {
        return "pingTarget.pingTarget";
    }

    public String getPingTargetDeleteOKTitle() {
        return "pingTarget.confirmRemoveTitle";
    }

    public CustomPingTargetsAction() {
        super();
    }

    protected Log getLogger() {
        return mLogger;
    }

    protected List getPingTargets(RollerRequest rreq) throws RollerException {
        HttpServletRequest req = rreq.getRequest();
        PingTargetManager pingTargetMgr = RollerFactory.getRoller().getPingTargetManager();
        Boolean allowCustomTargets = new Boolean(!PingConfig.getDisallowCustomTargets());
        req.setAttribute("allowCustomTargets", allowCustomTargets);
        List customPingTargets = allowCustomTargets.booleanValue() ? pingTargetMgr.getCustomPingTargets(rreq.getWebsite()) : Collections.EMPTY_LIST;
        return customPingTargets;
    }

    protected PingTargetData createPingTarget(RollerRequest rreq, PingTargetForm pingTargetForm) throws RollerException {
        return new PingTargetData(null, pingTargetForm.getName(), pingTargetForm.getPingUrl(), rreq.getWebsite());
    }

    protected boolean hasRequiredRights(RollerRequest rreq, WebsiteData website) throws RollerException {
        RollerSession rses = RollerSession.getRollerSession(rreq.getRequest());
        return (rses.isUserAuthorizedToAdmin(website) && !PingConfig.getDisallowCustomTargets());
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return view(mapping, actionForm, request, response);
    }
}
