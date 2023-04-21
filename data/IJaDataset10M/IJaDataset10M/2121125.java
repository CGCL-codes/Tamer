package org.simpleframework.servlet.test;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DispatchServlet extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/snoop/");
        dispatcher.forward(request, response);
    }
}
