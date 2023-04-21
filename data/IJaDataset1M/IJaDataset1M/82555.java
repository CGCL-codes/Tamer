package admin;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import db.DBConnection;

/**
 * Servlet implementation class for Servlet: Admin
 * 
 */
public class Admin extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    private String dbHost;

    private String dbUser;

    private String dbPassword;

    private String dbName;

    private DBConnection dbConnection;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dbHost = getInitParameter("DB-Host");
        dbUser = getInitParameter("DB-User");
        dbPassword = getInitParameter("DB-Password");
        dbName = getInitParameter("DB-Name");
        dbConnection = new DBConnection(dbHost, dbUser, dbPassword, dbName);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (session.getAttribute("user") != null) {
            onReloadPage(request, response);
            return;
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
        out.println("<script type=\"text/javascript\" src=\"js/admin/LoadBar.js\"></script>");
        out.println("<script type=\"text/javascript\" src=\"js/admin/FlomailAdmin.js\"></script>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/adminId.css\">");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/adminClass.css\">");
        out.println("<title>flomail :: Admin Login</title>");
        out.println("</head>");
        out.println("<body onload=\"init();\">");
        out.println("</body>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String task = request.getParameter("task");
        if (task == null) {
            doGet(request, response);
        } else {
            if (task.equals("login")) {
                new Logon(request, response, dbConnection);
            }
            if (task.equals("createDomain")) {
                Domain domain = new Domain(request, response, dbConnection);
                domain.createDomain();
            }
        }
    }

    public void onReloadPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
        out.println("<script type=\"text/javascript\" src=\"js/admin/LoadBar.js\"></script>");
        out.println("<script type=\"text/javascript\" src=\"js/admin/FlomailAdmin.js\"></script>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/adminId.css\">");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/adminClass.css\">");
        out.println("<title>flomail :: Admin Login</title>");
        out.println("</head>");
        out.println("<body onload=\"initConsole();\">");
        out.println("</body>");
    }

    public DBConnection getDbConnection() {
        return dbConnection;
    }
}
