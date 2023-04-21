package org.codecompany.jeha.example.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codecompany.jeha.ExceptionHandler;
import org.codecompany.jeha.example.ejb.JehaTestInterface;
import org.codecompany.jeha.example.web.handler.WebHandler;

public class JehaTestServlet extends HttpServlet {

    private static final long serialVersionUID = -4858811491383133750L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doHandle(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doHandle(req, resp);
    }

    @ExceptionHandler(handler = WebHandler.class)
    private void doHandle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String testType = req.getParameter("testType");
        if ("generalServlet".equals(testType)) {
            throw new RuntimeException("Runtime exception");
        } else if ("specificServlet".equals(testType)) {
            throw new ArrayIndexOutOfBoundsException("Array index out of bound exception");
        } else {
            JehaTestInterface jehaTest = ServiceLocator.getInstance().getEJBInterface();
            jehaTest.execute(testType);
        }
        req.getRequestDispatcher("/response.jsp").forward(req, resp);
    }
}
