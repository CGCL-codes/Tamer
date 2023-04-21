package com.mouseoverstudio.test;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HSRTest extends javax.servlet.http.HttpServlet implements Servlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int counter;
        HttpSession session = request.getSession();
        if (session.isNew()) {
            counter = 0;
        } else {
            counter = (Integer) session.getAttribute("counter");
        }
        counter = counter + 1;
        session.setAttribute("counter", counter);
        response.getWriter().write(counter + "");
    }
}
