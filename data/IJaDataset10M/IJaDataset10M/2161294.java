package org.apache.roller.presentation.website.actions;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.RollerException;
import org.apache.roller.model.PingTargetManager;
import org.apache.roller.model.RollerFactory;
import org.apache.roller.pojos.PingTargetData;
import org.apache.roller.pojos.WebsiteData;
import org.apache.roller.presentation.RollerRequest;
import org.apache.roller.presentation.RollerSession;
import org.apache.roller.presentation.forms.PingTargetForm;
import org.apache.roller.presentation.weblog.actions.BasePingTargetsAction;

/**
 * Administer common ping targets.
 *
 * @author <a href="mailto:anil@busybuddha.org">Anil Gangolli</a>
 * @struts.action name="pingTargetForm" path="/admin/commonPingTargets" scope="request" parameter="method"
 * @struts.action-forward name="pingTargets.page" path=".CommonPingTargets"
 * @struts.action-forward name="pingTargetEdit.page" path=".CommonPingTargetEdit"
 * @struts.action-forward name="pingTargetDeleteOK.page" path=".CommonPingTargetDeleteOK"
 */
public class CommonPingTargetsAction extends BasePingTargetsAction {

    private static Log mLogger = LogFactory.getFactory().getInstance(CommonPingTargetsAction.class);

    protected Log getLogger() {
        return mLogger;
    }

    public String getPingTargetsTitle() {
        return "commonPingTargets.commonPingTargets";
    }

    public String getPingTargetEditTitle() {
        return "pingTarget.pingTarget";
    }

    public String getPingTargetDeleteOKTitle() {
        return "pingTarget.confirmRemoveTitle";
    }

    protected List getPingTargets(RollerRequest rreq) throws RollerException {
        PingTargetManager pingTargetMgr = RollerFactory.getRoller().getPingTargetManager();
        return pingTargetMgr.getCommonPingTargets();
    }

    protected PingTargetData createPingTarget(RollerRequest rreq, PingTargetForm pingTargetForm) throws RollerException {
        return new PingTargetData(null, pingTargetForm.getName(), pingTargetForm.getPingUrl(), null);
    }

    protected boolean hasRequiredRights(RollerRequest rreq, WebsiteData website) throws RollerException {
        RollerSession rollerSession = RollerSession.getRollerSession(rreq.getRequest());
        return rollerSession.isGlobalAdminUser();
    }
}
