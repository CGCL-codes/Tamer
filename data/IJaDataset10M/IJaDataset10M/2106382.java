package com.sample.struts.servlet;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <a href="TestSessionServlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class TestSessionServlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession ses = req.getSession();
        StringBuffer sb = new StringBuffer();
        sb.append("<b>Remote User:</b><br><br>");
        sb.append(req.getRemoteUser());
        sb.append("<br><br>");
        sb.append("<b>Session ID:</b><br><br>");
        sb.append(req.getRequestedSessionId());
        sb.append("<br><br>");
        sb.append("<b>Servlet Session Attributes:</b><br><br>");
        Enumeration enu = ses.getAttributeNames();
        while (enu.hasMoreElements()) {
            String attrName = (String) enu.nextElement();
            Object attrValue = ses.getAttribute(attrName);
            sb.append(attrName);
            sb.append("=");
            sb.append(attrValue);
            sb.append("<br>");
        }
        res.setContentType("text/html");
        res.getOutputStream().print(sb.toString());
        res.getOutputStream().flush();
    }
}
