package servlets;

import entity.Principal;
import entity.School;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import remote.SchoolFacadeRemote;

/**
 *
 * @author Administrator
 */
public class servlet_login extends HttpServlet {

    InitialContext c;

    remote.SchoolFacadeRemote school;

    public servlet_login() {
        try {
            c = new InitialContext();
            school = (SchoolFacadeRemote) c.lookup("remote.SchoolFacadeRemote");
        } catch (NamingException ex) {
            Logger.getLogger(servlet_login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String sUserName = request.getParameter("un");
            String sPassword = request.getParameter("pw");
            String sSchool = request.getParameter("school");
            String sLoginType = request.getParameter("type");
            System.out.println(sUserName);
            System.out.println(sPassword);
            System.out.println(sSchool);
            System.out.println(sLoginType);
            if (sLoginType.equalsIgnoreCase("admin")) {
                out.println("log admin");
                adminLoginCheck(sUserName, sPassword, sSchool, request, response);
            } else if (sLoginType.equalsIgnoreCase("principle")) {
                out.println("log principle");
                principalLoginCheck(sUserName, sPassword, sSchool, request, response);
            } else if (sLoginType.equalsIgnoreCase("student")) {
                out.println("log student");
            } else if (sLoginType.equalsIgnoreCase("teacher")) {
                out.println("log teacher");
            } else if (sLoginType.equalsIgnoreCase("parent")) {
                out.println("log parent");
            } else {
                out.println(sLoginType);
            }
        } finally {
            out.close();
        }
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

    private void adminLoginCheck(String sUserName, String sPassword, String sSchool, HttpServletRequest request, HttpServletResponse response) {
        try {
            School sch = school.find(Integer.parseInt(sSchool));
            System.out.println(sch);
            if (sch.getSysadmin().getSaUn().equals(sUserName) && sch.getSysadmin().getSaPw().equals(sPassword)) {
                System.out.println("LOGIN ADMIN OK");
                request.getSession().setAttribute("school", sch);
                request.getSession().setAttribute("login_type", "ADMIN");
                RequestDispatcher d = getServletContext().getRequestDispatcher("/admin_home.jsp");
                try {
                    d.forward(request, response);
                } catch (ServletException ex) {
                    Logger.getLogger(servlet_login.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(servlet_login.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Error");
            }
        } catch (Exception ex) {
            Logger.getLogger(servlet_login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void principalLoginCheck(String sUserName, String sPassword, String sSchool, HttpServletRequest request, HttpServletResponse response) {
        School sch = school.find(Integer.parseInt(sSchool));
        Collection<Principal> prin = sch.getPrincipalCollection();
        for (Principal principal : prin) {
            if (principal.getPUn().equals(sUserName) && principal.getPPw().equals(sPassword)) {
                try {
                    System.out.println("OK pina hw");
                    request.getSession().setAttribute("school", sch);
                    request.getSession().setAttribute("login_type", "PRINCIPAL");
                    RequestDispatcher d = getServletContext().getRequestDispatcher("/principal_home.jsp");
                    d.forward(request, response);
                    return;
                } catch (ServletException ex) {
                    Logger.getLogger(servlet_login.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(servlet_login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
