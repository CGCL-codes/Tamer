package jp.co.msr.osaka.umgr.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.msr.osaka.umgr.actionform.RoleBatchEditSubmitForm;
import jp.co.msr.osaka.umgr.actionform.RoleBatchEditSubmitSubForm;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class RoleBatchEditAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RoleBatchEditSubmitForm nextForm = new RoleBatchEditSubmitForm();
        List<RoleBatchEditSubmitSubForm> subList = new ArrayList<RoleBatchEditSubmitSubForm>();
        for (int i = 0; i < 5; i++) {
            subList.add(new RoleBatchEditSubmitSubForm());
        }
        nextForm.setSubList(subList);
        request.setAttribute("roleBatchEditSubmitForm", nextForm);
        return mapping.findForward("success");
    }
}
