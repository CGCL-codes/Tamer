package mortiforo.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ibatis.sqlmap.client.SqlMapClient;
import mortiforo.beans.ForumUser;
import mortiforo.beans.UserPermissions;
import mortiforo.util.LDAPUsers;
import mortiforo.util.MD5;

/**
 * @author espectro
 *
 * Gets the username and email of all the LDAP users, and creates
 * accounts for them on the database
 */
public class MigrateLDAPUsers extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        String logged_user = null;
        boolean[] permissions;
        permissions = new boolean[UserPermissions.permission_count];
        if (session != null) {
            logged_user = (String) session.getAttribute("logged_user_name");
            permissions = (boolean[]) session.getAttribute("permission");
        }
        if (logged_user == null || logged_user.equals("") || permissions[UserPermissions.perm_administration] == false) {
            request.setAttribute("not_admin", "");
            RequestDispatcher rd = request.getRequestDispatcher("migrateldapusers.ftl");
            rd.forward(request, response);
            return;
        }
        SqlMapClient sqlMap = (SqlMapClient) getServletContext().getAttribute("sqlmap");
        Properties p = (Properties) getServletContext().getAttribute("config");
        String languages[] = p.getProperty("installed_languages").split(",");
        LDAPUsers ldapusers = new LDAPUsers();
        List users = new ArrayList();
        try {
            ldapusers.init(getServletContext());
            users = ldapusers.getDirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List added_users = new ArrayList();
        List failed_users = new ArrayList();
        Iterator it = users.iterator();
        while (it.hasNext()) {
            ForumUser user = (ForumUser) it.next();
            user.setPassword(MD5.CreateHash("password"));
            if (languages != null && languages.length > 0) user.setLanguage(languages[0].trim()); else user.setLanguage("en");
            try {
                sqlMap.insert("insertLDAPuser", user);
                sqlMap.insert("InsertNewUserGroup", new Integer(user.getId()));
                added_users.add(user);
            } catch (SQLException e1) {
                failed_users.add(user);
                e1.printStackTrace();
            }
        }
        if (added_users.size() > 0) {
            request.setAttribute("added_users", added_users);
        }
        if (failed_users.size() > 0) {
            request.setAttribute("failed_users", failed_users);
        }
        if (added_users.size() == 0 && failed_users.size() == 0) {
            request.setAttribute("ldap_no_users", "");
        }
        RequestDispatcher rd = request.getRequestDispatcher("migrateldapusers.ftl");
        rd.forward(request, response);
    }
}
