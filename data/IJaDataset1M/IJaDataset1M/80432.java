package com.ufnasoft.dms.server;

import com.ufnasoft.dms.server.database.Message;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import java.io.IOException;

public class ServerGetMessage extends InitDMS {

    Logger logger1 = Logger.getLogger(ServerGetMessage.class);

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        setHeader(res);
        String username = req.getParameter("username");
        String key = req.getParameter("key");
        String messagetype = "inbox";
        if (req.getParameter("messagetype") == null) messagetype = "inbox"; else messagetype = req.getParameter("messagetype");
        int messageid = 0;
        try {
            messageid = Integer.parseInt(req.getParameter("messageid"));
        } catch (NumberFormatException e) {
            logger1.info("dms: ServerGetMessage: userid is null:" + e.getMessage());
        }
        StringBuffer sb = new StringBuffer("<dms>");
        Message m = new Message();
        StringBuffer rvalue = m.getMessageById(username, key, messagetype, messageid);
        sb.append(rvalue);
        sb.append("</dms>");
        out.print(sb.toString());
    }
}
