package fhj.itm05.seminarswe.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import fhj.itm05.seminarswe.database.UserDAOHsqldb;
import fhj.itm05.seminarswe.domain.UserData;
import fhj.itm05.seminarswe.web.utils.*;

/**
 * @author crusty2000
 * @version 1.1
 */
public class LoginController implements Controller {

    public Map<String, Object> handleRequest(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute("userSession");
        String action = request.getParameter("action");
        if (userSession.getLoggedIn()) response.sendRedirect(context.getContextPath() + "/Dispatcher/" + ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.home"));
        if ("dologin".equalsIgnoreCase(action)) {
            String username = request.getParameter("user");
            String pwd = request.getParameter("pwd");
            if (userSession.getLoggedIn() == false) {
                if (username == null | pwd == null | username.isEmpty() | pwd.isEmpty()) params.put("login_params", "Please provide a username and a password to login"); else {
                    username = username.toLowerCase();
                    PasswordEncryption pe = PasswordEncryption.getInstance();
                    if (UserDAOHsqldb.getInstance().checkUserLogin(username, pe.encrypt(pwd))) {
                        System.out.println(username + " is logged in.");
                        UserData userData = UserDAOHsqldb.getInstance().getUser(username);
                        userSession.setLoggedIn(true, userData);
                        response.sendRedirect(context.getContextPath() + "/Dispatcher/" + ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.home"));
                    } else {
                        params.put("login_failed", "Username or password wrong.");
                    }
                }
            } else {
                params.put("login_params", "You are already logged in!");
            }
        }
        return params;
    }
}
