package com.shared.servlets;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.shared.beans.*;

/**
 *	<code>BaseAppServlet</code> is the foundation Servlet for the 
 *	application. All Servlets and JSPs should eventually inherit from this
 *	class. Therefore, fields, constants, and methods usefull to both the
 *	application's Servlets and JSPs should be placed here.
 *
 *	@author Silas Christiansen
 *	@web.servlet
 *	  name=BaseAppServlet
 *	  load-on-startup="0"
 *
 *	@web.servlet-mapping
 *	  url-pattern="/BaseAppServlet"
 *
 *	@web.resource-ref
 *	  name="jdbc/mydb"
 *	  type="javax.sql.DataSource"
 *	  auth="Container"
 */
public abstract class BaseAppServlet extends javax.servlet.http.HttpServlet {

    public static final int DEV = 0;

    public static final int TEST = 1;

    public static final int PROD = 2;

    public static final int MODE = DEV;

    public static final String NAVIGATE = "NAVIGATE";

    public static final String USER = "USER";

    public static final String PAGE_ERROR = "Error.jsp";

    public static final String PAGE_NOT_LOGGED_IN = "index.jsp";

    public static final String ERROR_MESSAGE = Util.ERROR_MESSAGE;

    public static final String SUCCESS_MESSAGE = Util.SUCCESS_MESSAGE;

    private static String TEMP_DIRECTORY = null;

    private static String IMAGE_DIRECTORY = null;

    public static final String[] ERROR_MESSAGES = { "An Error Occured Please Contact Support", "A Database Problem occured. Please contact support" };

    public static final int ERROR_GENERIC = 0;

    public static final int ERROR_SQL = 1;

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.service(request, response);
    }

    public void doForward(String forward, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (null == forward) {
            forward = "index.jsp";
            Log.msg("BaseAppServlet.doForward()", "forward page was null!");
        }
        response.sendRedirect(forward);
    }

    public boolean doAll(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return true;
    }

    public static boolean isLoggedIn(HttpSession session, ServletContext context) {
        return null != session.getAttribute(SessionBean.LOGGED_IN);
    }

    public static String getTempDirectory(String application) {
        if (null == TEMP_DIRECTORY) {
            String SQL = "SELECT path FROM path where name = 'TEMP_DIRECTORY' and app = '" + application + "'";
            Connection conn = null;
            Statement stmt = null;
            try {
                conn = Database.getConnection();
                stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(SQL);
                if (result.next()) {
                    TEMP_DIRECTORY = result.getString("path");
                } else {
                    Log.msg("BaseAppServlet.getTempDirectory()", "No path found for TEMP_DIRECTORY for application: " + application);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TEMP_DIRECTORY;
    }

    public static String getImageDirectory(String application) {
        if (null == IMAGE_DIRECTORY) {
            String SQL = "SELECT path FROM path where name = 'IMAGE_DIRECTORY' and app = '" + application + "'";
            Connection conn = null;
            Statement stmt = null;
            try {
                conn = Database.getConnection();
                stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(SQL);
                if (result.next()) {
                    IMAGE_DIRECTORY = result.getString("path");
                } else {
                    Log.msg("BaseAppServlet.getImageDirectory()", "No path found for IMAGE_DIRECTORY for application: " + application);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return IMAGE_DIRECTORY;
    }

    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doAll(request, response);
    }

    protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doAll(request, response);
    }
}
