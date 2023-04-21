package com.ufnasoft.dms.server;

import com.ufnasoft.dms.server.database.Project;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ServerGetProjectDocuments extends InitDMS {

    Logger logger1 = Logger.getLogger(ServerGetProjectDocuments.class);

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        setHeader(res);
        String username = req.getParameter("username");
        String key = req.getParameter("key");
        String orderby = "";
        orderby = req.getParameter("orderby");
        StringBuffer sb = new StringBuffer("<dms>");
        int projectid = 0;
        try {
            projectid = Integer.parseInt(req.getParameter("projectid"));
        } catch (NumberFormatException e) {
            logger1.info("dms: ServerGetProjectDocuments:  projectid is null:" + e.getMessage());
        }
        if (orderby == "" || orderby == null) orderby = "d.filename";
        Project p = new Project();
        StringBuffer rvalue = p.getProjectDocuments(username, key, projectid, orderby);
        sb.append(rvalue);
        sb.append("</dms>");
        out.print(sb.toString());
    }
}
