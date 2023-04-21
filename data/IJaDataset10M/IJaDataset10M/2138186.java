package com.hp.hpl.MeetingMachine.servlets;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.catalina.HttpRequest;
import org.apache.catalina.HttpResponse;
import java.net.InetAddress;

public class WebPresenceServlet extends HttpServlet {

    String room_name = null;

    public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("text/html");
        InetAddress thisIP = InetAddress.getLocalHost();
        String client_ip = request.getRemoteAddr();
        String server_ip = request.getServerName();
        boolean local = client_ip.endsWith(server_ip) || client_ip.endsWith("127.0.0.1");
        String wpm_addr = "http://" + server_ip + ":" + String.valueOf(request.getServerPort());
        out.println("<html><head><title>MeetingMachine Web Presence</title><base href=\"" + wpm_addr + "/\"></head>");
        out.println("<body bgcolor=\"#FFFFCC\" text=\"#000000\"><br>");
        String today = (new Date()).toString();
        out.println("<p><center>" + today + "</center></p><h1>MeetingMachine Ready!");
        if (room_name != null) {
            out.println("for " + room_name + "</h1>");
        } else {
            out.println("</h1>");
        }
        if (local) {
            out.println("<h2>Laptop Users: set your web browser to <a href=" + wpm_addr + ">" + wpm_addr + "</a></h2>");
        } else {
            out.println("<h2><a href=\"Win32/mm-waltz.zip\">Download Win32 client</a></h2>");
            out.println("<h2><a href=\"EventHeap/configure.pr2\">Connect to the MeetingMachine!</a></h2>");
        }
        out.println("<h2><a href=\"eTable/\">View eTable</a></h2>");
        out.println("<h2><a href=\"Help/\">Help</a></h2>");
        out.println("<h2>Please send suggestions to John_Barton@hpl.hp.com</h2>");
        out.println("<br>");
        out.println("<hr>");
        out.println("<a href=\"Debug/\">Debug</a>");
        out.println("<br>");
        out.println("</body></html>");
        log("Sent MM home page");
    }

    public String getServletInfo() {
        return "eTable WebPresence servlet";
    }
}
