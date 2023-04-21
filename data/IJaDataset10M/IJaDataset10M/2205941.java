package base.MyExampleXXX;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Action extends com.kn.struts.action.KNAction {

    public ActionForward init(ActionMapping param0, ActionForm param1, HttpServletRequest param2, HttpServletResponse param3) {
        return param0.findForward("success");
    }

    public ActionForward abc(ActionMapping param0, ActionForm param1, HttpServletRequest param2, HttpServletResponse param3) {
        return param0.findForward("success");
    }
}
