package pub.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import pub.db.*;
import pub.utils.StringUtils;
import pub.servlets.Login;

public abstract class UpdateTable extends PubServlet {

    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String return_url = request.getParameter("return_url");
        if (return_url == null) {
            throw new ServletException("return_url is a required parameter");
        }
        String user_id = Login.getUserId(request);
        if (user_id == null) {
            Login.redirectToLoginServlet(request, response);
            return;
        }
        HashMap params = readParameters(request);
        updateTable(conn, params);
        response.sendRedirect(return_url);
    }

    protected abstract void updateTable(Connection conn, HashMap params);

    protected abstract HashMap readParameters(HttpServletRequest request) throws ServletException;
}
