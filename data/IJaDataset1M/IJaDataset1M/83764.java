package sample.action.advanced;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import strutter.action.FormlessDispatchAction;
import strutter.config.ActionConfig;
import strutter.config.tags.ConfigInterface;
import strutter.helper.ActionHelper;

public class ProcessAction extends FormlessDispatchAction implements ConfigInterface {

    private static Log log = LogFactory.getLog(ProcessAction.class);

    public void config(ActionConfig struts) {
        struts.addForward("process_view.jsp");
    }

    private String lastname;

    private String firstname;

    public ActionForward view() throws Exception {
        log.debug("view");
        return ActionHelper.findForward("view");
    }

    public ActionForward runner() throws Exception {
        log.debug("runner");
        Object obj = new Object();
        synchronized (obj) {
            log.debug("start");
            obj.wait(3000);
            log.debug("ende");
        }
        return ActionHelper.findForward("view");
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
