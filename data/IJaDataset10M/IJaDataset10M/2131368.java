package ces.sf.oa.leave.action;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.arch.action.LoadAction;
import ces.arch.form.BaseForm;
import ces.arch.form.EditForm;
import ces.platform.system.dbaccess.Organize;
import ces.platform.system.dbaccess.User;
import ces.sf.oa.leave.pojo.LeavePojo;
import ces.sf.oa.leave.form.LeaveForm;

public class NewDocLoadAction extends LoadAction {

    protected ActionForward process(ActionMapping mapping, BaseForm form, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) throws Exception {
        String tran = request.getParameter("trans") == null ? "" : request.getParameter("trans");
        try {
            LeaveForm editForm = (LeaveForm) form;
            doLoad(mapping, editForm, request, response, errors);
            String activityId = "REGISTER";
            request.setAttribute("tran", tran);
            return mapping.findForward(activityId);
        } catch (Exception ex) {
            request.setAttribute("Exception", ex);
            return null;
        }
    }
}
