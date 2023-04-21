package com.myapp.struts;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

/**
 *
 * @author Tomek
 * @version
 */
public class NewServlet extends HttpServlet {

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet NewServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement prpStmt = null;
        ResultSet rs = null;
        StringBuffer resultString;
        DataSource dataSource;
        try {
            dataSource = (DataSource) this.getServletContext().getAttribute("empTable");
            conn = dataSource.getConnection("ztsr", "corba");
            String sqlQuery = "SELECT * FROM Ksiazki";
            prpStmt = conn.prepareStatement(sqlQuery);
            rs = prpStmt.executeQuery();
            while (rs.next()) {
                out.println(rs.getString("Autor") + "  " + rs.getString("Tytul"));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("SQL Exception occured while accessing the table");
            e.printStackTrace(out);
        } catch (Exception e) {
            e.printStackTrace(out);
        }
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
}
