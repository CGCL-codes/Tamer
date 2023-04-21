package com.ufnasoft.dms.server;

import com.ufnasoft.dms.server.database.Groups;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import java.io.*;

public class ServerDeleteGroupUser extends InitDMS {

    Logger logger1 = Logger.getLogger(ServerDeleteGroupUser.class);

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        setHeader(res);
        String username = req.getParameter("username");
        String key = req.getParameter("key");
        int groupid = 0;
        int userid = 0;
        try {
            groupid = Integer.parseInt(req.getParameter("groupid"));
        } catch (NumberFormatException e) {
            logger1.info("dms: ServerDeleteGroupUser: groupid is null");
        }
        try {
            userid = Integer.parseInt(req.getParameter("userid"));
        } catch (NumberFormatException e) {
            logger1.info("dms: ServerDeleteGroupUser: userid is null");
        }
        int taskid = 1;
        StringBuffer sb = new StringBuffer("<dms>");
        Groups g = new Groups();
        String rvalue = g.deleteGroupUser(username, key, taskid, groupid, userid);
        sb.append(rvalue);
        sb.append("</dms>");
        out.print(sb.toString());
    }
}
