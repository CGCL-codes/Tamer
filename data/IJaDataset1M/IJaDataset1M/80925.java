package com.ufnasoft.dms.server;

import com.ufnasoft.dms.server.database.Users;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import java.io.*;

public class ServerAddUser extends InitDMS {

    Logger logger1 = Logger.getLogger(ServerAddUser.class);

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        setHeader(res);
        String username = req.getParameter("username");
        String key = req.getParameter("key");
        String machinename = req.getParameter("machinename");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        int defaultmessagemethodid = 0;
        try {
            defaultmessagemethodid = Integer.parseInt(req.getParameter("defaultmessagemethodid"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer("<dms>");
        int taskid = 1;
        Users u = new Users();
        String rvalue = u.addUser(username, key, taskid, machinename, email, password, firstname, lastname, defaultmessagemethodid);
        sb.append("<rvalue>");
        sb.append(rvalue);
        sb.append("</rvalue>");
        sb.append("</dms>");
        out.print(sb.toString());
    }
}
