package salto.test.fwk.mvc.ajax.table;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import salto.fwk.mvc.ajax.action.AbstractTableAction;
import salto.fwk.mvc.ajax.util.AjaxUtil;

public class Example4Action extends AbstractTableAction {

    protected boolean processDeleteAction(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws Exception {
        return false;
    }

    protected void processSelectAction(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws Exception {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String givenName = request.getParameter("givenName");
        AjaxUtil.addInnerHtml(request, "idFieldId", id);
        AjaxUtil.addInnerHtml(request, "nameFieldId", name);
        AjaxUtil.addInnerHtml(request, "givenNameFieldId", givenName);
    }
}
