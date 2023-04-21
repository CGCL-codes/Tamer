package com.ufnasoft.dms.server;

import com.ufnasoft.dms.server.database.Project;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import java.io.*;

public class ServerDeleteProjectGroup extends InitDMS {

    Logger logger1 = Logger.getLogger(ServerDeleteProjectGroup.class);

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        setHeader(res);
        String username = req.getParameter("username");
        String key = req.getParameter("key");
        int projectid = 0;
        try {
            projectid = Integer.parseInt(req.getParameter("projectid"));
        } catch (NumberFormatException e) {
            logger1.info("dms: ServerDeleteProjectGroup: projectid is null");
        }
        int groupid = 0;
        try {
            groupid = Integer.parseInt(req.getParameter("groupid"));
        } catch (NumberFormatException e) {
            logger1.info("dms: ServerDeleteProjectGroup:groupid is null");
        }
        StringBuffer sb = new StringBuffer("<dms>");
        int taskid = 1;
        Project p = new Project();
        String rvalue = p.deleteProjectGroup(username, key, taskid, projectid, groupid);
        sb.append("<rvalue>");
        sb.append(rvalue);
        sb.append("</rvalue>");
        sb.append("</dms>");
        out.print(sb.toString());
    }
}
