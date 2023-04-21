package page.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import page.inc.HtmlPage;
import javabean.UserInfo;
import table.UserInfoTable;
import util.Constant;
import util.PubFun;

public class DeleteUserInfoPage extends HtmlPage {

    public String print(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        UserInfo user = getSessionUser(req);
        if (user == null) {
            resp.sendRedirect(Constant.REDIRECT_LOGIN_PAGE);
            return null;
        }
        int userId = getIntParameter("user_id", 0, req);
        if (userId != 0 && UserInfoTable.isExistsUserById(userId)) {
            UserInfoTable.delUserInfo(userId, getBooleanParameter("is_del", false, req));
            return "/MainCtrl?page=UserManagePage";
        } else {
            PubFun.ajaxPrintStr("要操作的用户不存在。", resp);
            return null;
        }
    }
}
