package myconDB;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class ShowUserAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String no = request.getParameter("no");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        DataManager dm = new DataManager();
        String type = request.getParameter("type");
        if (dm.postUserData(no, userName, password, type)) {
            request.setAttribute("al", dm.showAllUser("mytable"));
            return mapping.findForward("ShowUser");
        } else {
            return mapping.findForward("error");
        }
    }
}
