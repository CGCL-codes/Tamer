package com.microstream.user;

import java.io.IOException;
import java.util.Date;
import java.io.*;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.Response;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.microstream.dao.MsUser;
import com.microstream.lift.guestbook.Greeting;
import com.microstream.lift.guestbook.PMF;

public class RegisterUserServlet extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=gb2312");
        PersistenceManager pm = PMF.get().getPersistenceManager();
        String userName = "";
        String userPassword = "";
        String eMail = "";
        userName = req.getParameter("user_name").toString();
        userPassword = req.getParameter("user_password").toString();
        eMail = req.getParameter("e_mail").toString();
        PrintWriter pw = resp.getWriter();
        if (UserUtility.getOneUser(pm, userName) != null) {
            pw.println("�û��Ѿ�����");
            return;
        }
        MsUser msUser = new MsUser(userName, userPassword, eMail, new Date());
        pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(msUser);
            resp.sendRedirect("/user_man/register_success.jsp");
        } finally {
            pm.close();
        }
    }
}
