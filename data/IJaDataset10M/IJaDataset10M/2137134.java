package org.openje.http.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SessionManagementTest extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commands[] = request.getParameterValues("command");
        String keys[] = request.getParameterValues("key");
        String values[] = request.getParameterValues("value");
        HttpSession session = request.getSession(true);
        if (commands != null && commands.length == 1) {
            if ("put".equals(commands[0])) {
                if (keys != null && values != null && keys.length == 1 && values.length == 1) {
                    session.setAttribute(keys[0], values[0]);
                }
            } else if ("remove".equals(commands[0])) {
                if (keys != null && keys.length == 1) {
                    session.removeAttribute(keys[0]);
                }
            }
        }
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head><title>Session Management Test</title></head>");
        writer.println("<body bgcolor=\"white\">");
        writer.println("<form method=\"POST\">");
        writer.println("<table>");
        writer.println("<tr><td>");
        writer.println("KEY:");
        writer.println("</td><td>");
        writer.println("<input type=\"TEXT\" name=\"key\">");
        writer.println("</td></tr>");
        writer.println("<tr><td>");
        writer.println("VALUE:");
        writer.println("</td><td>");
        writer.println("<input type=\"TEXT\" name=\"value\">");
        writer.println("</td></tr>");
        writer.println("<tr><td colspan=\"2\">");
        writer.println("<input type=\"SUBMIT\" value=\"Add Session\">");
        writer.println("</td></tr>");
        writer.println("</table>");
        writer.println("<input type=\"HIDDEN\" name=\"command\" value=\"put\">");
        writer.println("</form>");
        Enumeration e = session.getAttributeNames();
        if (e.hasMoreElements()) {
            writer.println("<table border>");
            writer.println("<tr><td colspan=\"3\" align=\"center\">Sessions</td></tr>");
            do {
                String skey = (String) e.nextElement();
                String svalue = (String) session.getAttribute(skey);
                writer.println("<tr>");
                writer.println("<td>" + skey + "</td>");
                writer.println("<td>" + svalue + "</td>");
                writer.println("<td>");
                writer.println("<form method=\"POST\">");
                writer.println("<input type=\"SUBMIT\" value=\"REMOVE\"/>");
                writer.println("<input type=\"HIDDEN\" name=\"command\" value=\"remove\"/>");
                writer.println("<input type=\"HIDDEN\" name=\"key\" value=\"" + skey + "\"/>");
                writer.println("</form>");
                writer.println("</tr>");
            } while (e.hasMoreElements());
            writer.println("<tr><td>ID</td><td>" + session.getId() + "</td></tr>");
            writer.println("<tr><td>Creation Time</td><td>" + session.getCreationTime() + "(" + new Date(session.getCreationTime()) + ")</td></tr>");
            writer.println("<tr><td>Last Accessed Time</td><td>" + session.getLastAccessedTime() + "(" + new Date(session.getLastAccessedTime()) + ")</td></tr>");
            Date now = new Date();
            writer.println("<tr><td>Current Time</td><td>" + now.getTime() + "(" + now + ")</td></tr>");
            Date diff = new Date(now.getTime() - session.getLastAccessedTime());
            writer.println("<tr><td>Current Time - Last Accessed Time</td><td>" + diff.getTime() + "(milisecs)</td></tr>");
            writer.println("<tr><td>Max Inactive Interval</td><td>" + session.getMaxInactiveInterval() + "(secs)</td></tr>");
            writer.println("<table/>");
        } else {
            writer.println("<p>No session data.</p>");
        }
        writer.println("<form>");
        writer.println("<input type=\"SUBMIT\" value=\"Update\" />");
        writer.println("</form>");
        writer.println("</body>");
        writer.println("</html>");
    }
}
