package za.co.modobo.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import za.co.modobo.common.configurationReader;
import za.co.modobo.common.syslogLogger;
import za.co.modobo.utils.securityDoLogin;

/**
 * Get the logon credentials and authenticate the user
 * @author Susan Mani <susanmani88@gmail.com>
 */
public class doLogin extends HttpServlet {

    private syslogLogger syslog;

    private configurationReader cf;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cf = new configurationReader(getServletContext().getRealPath("/"));
        syslog = new syslogLogger(getServletContext().getRealPath("/") + cf.getHashtableValue("log4j_config/filename"));
        String username = "";
        String password = "";
        Enumeration parameters = request.getParameterNames();
        while (parameters.hasMoreElements()) {
            String parameter = (String) parameters.nextElement();
            if (parameter.compareTo("username") == 0) {
                username = request.getParameter(parameter).toString();
            } else if (parameter.compareTo("password") == 0) {
                password = request.getParameter(parameter).toString();
            }
        }
        syslog.log(this.getClass().toString() + "::processRequest() Extracted username '" + username + "' with password '" + password + "'", "info");
        if ((username.length() < 33) && (password.length() < 33)) {
            try {
                securityDoLogin secDL = new securityDoLogin(syslog, request.getParameter("username"), request.getParameter("password"), getServletContext().getRealPath("/"));
                if (secDL.getResult() == true) {
                    response.addCookie(secDL.getUsrCookie());
                } else {
                    syslog.log(this.getClass().toString() + "::" + Thread.currentThread().getStackTrace()[1].getMethodName() + "(line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + ") Logon attempt FAILED for user '" + request.getParameter("username") + "'", "info");
                }
            } catch (Exception e) {
                syslog.log(this.getClass().toString() + "::" + Thread.currentThread().getStackTrace()[1].getMethodName() + "(line " + Thread.currentThread().getStackTrace()[1].getLineNumber() + ") Exception", "info");
                syslog.log(e.getMessage(), "info");
            }
        }
        response.sendRedirect("/modobo/logonForm");
    }

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
