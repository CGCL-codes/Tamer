package ces.research.oa.document.duty.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.arch.action.SaveAction;
import ces.arch.form.BaseForm;
import ces.platform.system.dbaccess.User;
import ces.research.oa.document.duty.form.DutyForm;
import ces.research.oa.document.util.OAConstants;
import ces.research.oa.entity.DutyPlanPojo;

public class DutyAction extends SaveAction {

    protected ActionForward process(ActionMapping mapping, BaseForm form, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) throws Exception {
        User user = (User) request.getSession().getAttribute(OAConstants.ATTRIBUTE_USER);
        if (user == null) {
            return mapping.findForward(OAConstants.FORWARD_LOGIN);
        }
        DutyForm dutyForm = (DutyForm) form;
        String forward = "success";
        String doAction = dutyForm.getDoAction();
        request.setCharacterEncoding("UTF-8");
        if ("save".equals(doAction) || "update".equals(doAction)) {
            getBo().insertOrUpdate(((DutyPlanPojo) dutyForm.getBean()));
        } else if ("find".equals(doAction)) {
            dutyForm.setBean(getBo().get(DutyPlanPojo.class, dutyForm.getId()));
        } else if ("delete".equals(doAction)) {
            getBo().delete(DutyPlanPojo.class, dutyForm.getId());
        } else {
        }
        request.setAttribute("DutyForm", form);
        return mapping.findForward(forward);
    }
}
