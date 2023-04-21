package page.user;

import javabean.UserInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import table.DepartmentTable;
import table.UserInfoTable;
import util.Constant;
import page.inc.HtmlPage;

public class UserInfoPage extends HtmlPage {

    public String print(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserInfo user = getSessionUser(req);
        if (user == null) {
            resp.sendRedirect(Constant.REDIRECT_LOGIN_PAGE);
            return null;
        }
        String returnStr = "/jsp/user/create_user.jsp";
        int userID = this.getIntParameter("user_id", 0, req);
        if (userID == 0) {
        } else {
            req.setAttribute("user", UserInfoTable.loadUserInfoByID(userID));
            returnStr = "/jsp/user/edit_user.jsp";
        }
        req.setAttribute("departmentList", DepartmentTable.loadDepartment());
        return returnStr;
    }
}
