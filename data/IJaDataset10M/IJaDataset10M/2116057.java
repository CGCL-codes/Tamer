package jbluesman.t;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;

public class Tm extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out;
        response.setContentType("text/html");
        String str = "";
        String nick = "";
        String rcpt = "";
        String text = "";
        String conf = request.getParameter("action");
        out = response.getWriter();
        if (conf != null) {
            if (conf.equals("config")) {
                out.println("<config><t>With this service you can text to T-Mobile(USA) mobiles . A SENDER is required</t><nu>0</nu><np>0</np><nn>1</nn><mr>1</mr><mc>160</mc><mm>30</mm><in>1</in></config>");
                return;
            } else {
                out.println("<num>3</num><res>Servlet Errors</res>");
            }
        } else {
            conf = "";
        }
        try {
            nick = request.getParameter("nick");
            rcpt = request.getParameter("rcpt");
            text = request.getParameter("text");
            str = To.send(rcpt, nick, text);
        } catch (Exception e) {
            out.println("<num>3</num><txt>Servlet Errors</txt>");
        }
        out.println(str);
        out.close();
    }
}
