package sample.action.templates;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import sample.dao.Address;
import strutter.config.ActionConfig;
import strutter.optional.FormlessDispatchAction;

public class BlankFormlessAction extends FormlessDispatchAction {

    private static Log log = LogFactory.getLog(BlankFormlessAction.class);

    public static ActionConfig struts = new ActionConfig();

    static {
        struts.addForward("/formless_view.jsp");
    }

    String[] anreden = new String[] { "Herr", "Frau", "Dr." };

    Address customer = new Address();

    String memo;

    public ActionForward view(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("view");
        return mapping.findForward("view");
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Address getCustomer() {
        return customer;
    }

    public void setCustomer(Address customer) {
        this.customer = customer;
    }

    public String[] getAnreden() {
        return anreden;
    }

    public void setAnreden(String[] anreden) {
        this.anreden = anreden;
    }
}
