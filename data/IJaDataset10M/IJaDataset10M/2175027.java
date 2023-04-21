package com.ufnasoft.dms.server;

import com.ufnasoft.dms.server.database.Groups;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import java.io.*;

public class ServerUpdateGroup extends InitDMS {

    Logger logger1 = Logger.getLogger(ServerUpdateGroup.class);

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        setHeader(res);
        String username = req.getParameter("username");
        String key = req.getParameter("key");
        String groupname = req.getParameter("groupname");
        int groupid = 0;
        try {
            groupid = Integer.parseInt(req.getParameter("groupid"));
        } catch (NumberFormatException e) {
            logger1.info("dms: ServerUpdateGroup: groupid is null:" + e.getMessage());
        }
        StringBuffer sb = new StringBuffer("<dms>");
        int taskid = 1;
        Groups g = new Groups();
        String rvalue = g.updateGroup(username, key, taskid, groupid, groupname);
        sb.append("<rvalue>");
        sb.append(rvalue);
        sb.append("</rvalue>");
        sb.append("</dms>");
        out.print(sb.toString());
    }
}
