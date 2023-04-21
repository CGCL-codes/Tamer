package page.user;

import javabean.UserInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import table.DepartmentTable;
import page.inc.HtmlPage;
import util.Constant;

public class EditDepartmentPage extends HtmlPage {

    public String print(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserInfo user = getSessionUser(req);
        if (user == null) {
            resp.sendRedirect(Constant.REDIRECT_LOGIN_PAGE);
            return null;
        }
        String value = getStringParameter("edit_value", "", req);
        int id = getIntParameter("edit_id", 0, req);
        if (id == 0) {
            DepartmentTable.insertDepartment(value.toLowerCase());
        } else {
            DepartmentTable.updateDepartment(id, value.toLowerCase());
        }
        return "/MainCtrl?page=DepManagePage";
    }
}
