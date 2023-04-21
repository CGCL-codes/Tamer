package sample.action.advanced;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import strutter.action.FormlessDispatchAction;
import strutter.config.ActionConfig;
import strutter.config.tags.ConfigInterface;
import strutter.helper.ActionHelper;

public class SecuredAction extends FormlessDispatchAction implements ConfigInterface {

    private static Log log = LogFactory.getLog(SecuredAction.class);

    public void config(ActionConfig struts) {
        struts.addForward("/secured_view.jsp");
        struts.setRoles("PING,PONG,ADMIN");
    }

    String memo;

    public ActionForward view() throws Exception {
        log.debug("view");
        return ActionHelper.findForward("view");
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
