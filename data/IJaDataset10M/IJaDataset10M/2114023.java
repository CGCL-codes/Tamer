package salto.test.fwk.mvc.ajax.refresh;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import salto.fwk.mvc.action.GenericAction;

public class RefreshAction extends GenericAction {

    protected ActionForward process(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getRefreshForward();
    }
}
