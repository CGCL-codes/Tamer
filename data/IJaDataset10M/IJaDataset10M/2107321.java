package com.ufnasoft.dms.server;

import com.ufnasoft.dms.server.database.Project;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import java.io.IOException;

public class ServerGetRootProjects extends InitDMS {

    Logger logger1 = Logger.getLogger(ServerGetRootProjects.class);

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        setHeader(res);
        String username = req.getParameter("username");
        String key = req.getParameter("key");
        StringBuffer sb = new StringBuffer("<dms>");
        Project p = new Project();
        StringBuffer rvalue = p.getRootProjects(username, key);
        sb.append(rvalue);
        sb.append("</dms>");
        out.print(sb.toString());
    }
}
