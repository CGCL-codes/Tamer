package org.volume4.alliedbridge.model.events;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  Schalk
 * @version
 */
public class DeletePendingEvent extends HttpServlet {

    public String DRIVER, URL, USER, PASS;

    public String app_root, messageSuccess, messageFail, event_id, series, comp_logo, comp_logo_alt, comp_name, lb_comp_logo, lb_comp_logo_alt, town, region, comp_date, comp_time, entry_form, entry_form_alt;

    public void init() throws ServletException {
        ServletContext context = getServletContext();
        DRIVER = context.getInitParameter("driver");
        URL = context.getInitParameter("mc_dburl");
        USER = context.getInitParameter("mc_dbuser");
        PASS = context.getInitParameter("mc_dbpass");
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Connection con = null;
        messageSuccess = "Update of event completed successfuly.";
        messageFail = "Update of event failed.";
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = con.createStatement();
            String query = "DELETE FROM ab_events_pending " + "WHERE event_id= '" + event_id + "'";
            int result = stmt.executeUpdate(query);
            app_root = request.getContextPath();
            response.sendRedirect(app_root + "/application/application_messages/process_result.jsp?message=" + messageSuccess);
        } catch (SQLException ex) {
            out.println("\nERROR:------ SQLException -----\n");
            while (ex != null) {
                out.println("Message: " + ex.getMessage() + "<br>");
                out.println("SQLState: " + ex.getSQLState() + "<br>");
                out.println("ErrorCode :" + ex.getErrorCode());
                ex = ex.getNextException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException ex) {
                out.println("\nERROR:------ SQLException -----\n");
                out.println("Message: " + ex.getMessage());
                out.println("SQLState: " + ex.getSQLState());
                out.println("ErrorCode :" + ex.getErrorCode());
            }
        }
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
}
