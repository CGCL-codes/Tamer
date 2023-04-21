package beans;

import javax.servlet.*;
import javax.servlet.http.*;
import beans.LoginDB;
import beans.User;

public class CreateAccountBean {

    public void createAccount(HttpServletRequest req, ServletContext context) throws ServletException {
        LoginDB loginDB = (LoginDB) context.getAttribute("loginDB");
        String uname = req.getParameter("userName");
        loginDB.addUser(uname, req.getParameter("password"), req.getParameter("password-hint"));
        req.setAttribute("userName", uname);
    }
}
