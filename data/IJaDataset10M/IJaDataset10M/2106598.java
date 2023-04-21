package xpusp.controller;

import xpusp.model.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ProfessorHistoryHandler extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        res.sendRedirect(res.encodeRedirectURL("professor/view_history.jsp"));
    }
}
