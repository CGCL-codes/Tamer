package ces.sf.oa.onwatch.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.arch.action.SaveAction;
import ces.arch.form.BaseForm;
import ces.oa.util.BusinessUtil;
import ces.sf.oa.onwatch.form.OnWatchEditForm;
import ces.sf.oa.onwatch.pojo.OnWatch;
import ces.sf.oa.onwatch.util.OnWatchUtil;

public class OnWatchChangeSaveAction extends SaveAction {

    protected ActionForward process(ActionMapping mapping, BaseForm form, HttpServletRequest request, HttpServletResponse response, ActionErrors errors) throws Exception {
        String doAction = request.getParameter("doAction") == null ? "" : request.getParameter("doAction");
        String userid = String.valueOf(BusinessUtil.getCurrentUserId(request));
        String username = String.valueOf(BusinessUtil.getCurrentUserName(request));
        OnWatchEditForm editForm = (OnWatchEditForm) form;
        OnWatch watch = (OnWatch) editForm.getBean();
        if (watch.getZbType().equals("�հ�")) {
            watch.setZbType("0");
        } else {
            watch.setZbType("1");
        }
        if ("add".equals(doAction)) {
            watch.setCol1("1");
            watch.setCreateId(Long.parseLong(userid));
            watch.setCreateName(username);
            watch.setCreateDate(new java.sql.Date(new java.util.Date().getTime()));
        }
        editForm.setBean(watch);
        doSave(mapping, editForm, request, response, errors);
        request.setAttribute("message", "����ɹ���");
        return mapping.findForward(FORWARD_SUCCESS);
    }
}
