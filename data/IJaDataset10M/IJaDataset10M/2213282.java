package mortiforo.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapClient;
import mortiforo.beans.ForumUser;
import mortiforo.beans.UserPermissions;

/**
 * @author espectro
 *
 * Changes other user's settings! 
 * Also known as the BOFH mode. Yes, mortiforo is BOFH-friendly.
 * So if you are a user, never mess up with the BOFH.
 */
public class ModifyUser extends HttpServlet {

    private int items_per_page;

    public void init(ServletConfig arg0) throws ServletException {
        Properties prop = (Properties) arg0.getServletContext().getAttribute("config");
        items_per_page = Integer.parseInt(prop.getProperty("items_per_page"));
        super.init(arg0);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        String logged_user = null;
        boolean[] permissions;
        permissions = new boolean[UserPermissions.permission_count];
        if (session != null) {
            logged_user = (String) session.getAttribute("logged_user_name");
            permissions = (boolean[]) session.getAttribute("permission");
        }
        Properties p = (Properties) getServletContext().getAttribute("config");
        String languages[] = p.getProperty("installed_languages").split(",");
        String language_names[] = p.getProperty("language_names").split(",");
        Map language_list = new HashMap();
        for (int i = 0; i < languages.length; i++) {
            language_list.put(languages[i].trim(), language_names[i].trim());
        }
        request.setAttribute("languages", language_list);
        String user_id = request.getParameter("id");
        String page = request.getParameter("page");
        if (logged_user == null || logged_user.equals("") || permissions[UserPermissions.perm_administration] == false || permissions[UserPermissions.perm_edit_user] == false) {
            request.setAttribute("not_admin", "");
            RequestDispatcher rd = request.getRequestDispatcher("admin_modifyuser.ftl");
            rd.forward(request, response);
            return;
        }
        request.setAttribute("navigation_administration", "");
        request.setAttribute("navigation_administration_modifyuser", "");
        SqlMapClient sqlMap = (SqlMapClient) getServletContext().getAttribute("sqlmap");
        if (user_id != null && !user_id.equals("")) {
            ForumUser user = null;
            try {
                user = (ForumUser) sqlMap.queryForObject("getUserInfobyId", new Integer(user_id));
                if (user != null) {
                    request.setAttribute("UserSettings", user);
                } else {
                    request.setAttribute("invalid_user", "");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException nfe) {
                request.setAttribute("invalid_user", "");
            }
        } else {
            try {
                PaginatedList users = sqlMap.queryForPaginatedList("getUserList", null, items_per_page);
                if (page == null) {
                    users.gotoPage(0);
                    request.setAttribute("currentpage", new Integer(1));
                } else {
                    try {
                        users.gotoPage(new Integer(page).intValue());
                    } catch (NumberFormatException e) {
                        page = "0";
                    }
                    int pageint = new Integer(page).intValue();
                    request.setAttribute("currentpage", new Integer(pageint + 1));
                    if (users.size() == 0) {
                        users.gotoPage(0);
                        request.setAttribute("currentpage", new Integer(1));
                    }
                }
                if (users.isNextPageAvailable()) {
                    request.setAttribute("nextpage", new Integer(users.getPageIndex() + 1));
                }
                if (users.isPreviousPageAvailable()) {
                    request.setAttribute("previouspage", new Integer(users.getPageIndex() - 1));
                }
                request.setAttribute("users", users);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        RequestDispatcher rd = request.getRequestDispatcher("admin_modifyuser.ftl");
        rd.forward(request, response);
        return;
    }
}
