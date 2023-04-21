package br.com.yaw.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GeralActionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tokens[] = request.getRequestURI().split("/");
        String action = tokens[2];
        if ("login".equals(action)) {
            String mail = request.getUserPrincipal().getName();
            request.getSession().setAttribute("logged", mail);
            response.sendRedirect("/fx/lancamento.jsp");
        } else if ("username".equals(action)) {
            response.getWriter().write(request.getUserPrincipal().getName());
        }
    }
}
