package com.wwwc.index.web.servlet;

import java.io.*;
import java.security.*;
import javax.servlet.http.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.ServletRequestWrapper;
import java.net.*;
import com.wwwc.util.web.*;
import com.wwwc.index.web.ejb.database.*;

public class UserRegisterServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            HttpSession session = request.getSession(true);
            String user_ip = request.getRemoteAddr();
            String call_path = request.getContextPath() + request.getServletPath();
            String mid = (String) request.getAttribute("mid");
            if (mid == null) {
                System.out.println("UserRegisterServlet:Error:101");
                return;
            }
            String user_name = null;
            int user_age = 0;
            int user_level = 0;
            String user_fname = null;
            String user_since = null;
            String user_edu = null;
            String user_zip = null;
            String user_sex = null;
            String screen_w = null;
            String screen_h = null;
            Hashtable user_info = (Hashtable) session.getAttribute("user_info");
            if (user_info == null) {
                System.out.println("UserRegisterServlet:Error:104");
                return;
            }
            try {
                user_name = (String) user_info.get("user_name");
                user_age = Integer.parseInt((String) user_info.get("user_age"));
                user_level = Integer.parseInt((String) user_info.get("user_level"));
                user_fname = (String) user_info.get("user_fname");
                user_since = (String) user_info.get("user_since");
                user_edu = (String) user_info.get("user_edu");
                user_zip = (String) user_info.get("user_zip");
                user_sex = (String) user_info.get("user_sex");
            } catch (Exception e) {
                System.out.println("UserRegisterServlet:Error:105" + e);
            }
            String action = request.getParameter("aid");
            out.println(MyJavaScript.disableHistory());
            UserRegisterBean ubean = new UserRegisterBean(request, mid);
            if (action == null || action.equals("0")) {
                out.println(ubean.registerUser((String) session.getAttribute("img_key"), "0", "1"));
            }
            if (action != null && action.equals("1")) {
                if (ubean.activeUserAccount(request)) {
                    out.println(MyMessage.htmlMessage("<BR><B>Your account has been actived.</B><BR><BR>Please sign in.<BR>" + "<BR><BR><B>Thank You!</B>"));
                } else {
                    out.println("<TABLE>");
                    out.println("<TR><TD align=center style='font-size: 12pt' heigth=200>");
                    out.println("Your activation code has been sent to you through e-mail.</TD></TR>");
                    out.println("<TR><TD align=center>");
                    out.println("<img src=/images/star.gif border=0><I>Activation error.</I>");
                    out.println("</TD></TR>");
                    out.println("<TR><TD align=center height=200>" + ubean.activationForm(call_path, mid, "1") + "</TD></TR>");
                    out.println("</TABLE>");
                }
            }
        } catch (Exception e) {
            System.out.println("UserRegisterServlet:Error:401:" + e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}
