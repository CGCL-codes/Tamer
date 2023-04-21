package controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Aditya-Z
 */
public class ServletAkun extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String halaman = request.getParameter("halaman");
        RequestDispatcher dis = null;
        if (halaman != null) {
            if (halaman.equals("daftar")) {
                LogikaAkun la = new LogikaAkun(request);
                dis = request.getRequestDispatcher(la.daftar());
            } else if (halaman.equals("home")) {
                LogikaAkun la = new LogikaAkun(request);
                dis = request.getRequestDispatcher(la.openHome());
            } else if (halaman.equals("login")) {
                LogikaAkun la = new LogikaAkun(request);
                dis = request.getRequestDispatcher(la.login());
            } else if (halaman.equals("logout")) {
                LogikaAkun la = new LogikaAkun(request);
                dis = request.getRequestDispatcher(la.logout());
            } else if (halaman.equals("errordaftar")) {
                dis = request.getRequestDispatcher("index.jsp");
            } else if (halaman.equals("godaftar")) {
                dis = request.getRequestDispatcher("index.jsp");
            } else if (halaman.equals("errordaftar")) {
                dis = request.getRequestDispatcher("index.jsp");
            } else if (halaman.equals("gologin")) {
                dis = request.getRequestDispatcher("formLogin.jsp");
            } else if (halaman.equals("searchPenyakit")) {
                LogikaGejala la = new LogikaGejala(request);
                dis = request.getRequestDispatcher(la.search());
            } else if (halaman.equals("searchPenyakit2")) {
                LogikaGejala la = new LogikaGejala(request);
                dis = request.getRequestDispatcher(la.search2());
            }
        } else {
            dis = request.getRequestDispatcher("index.jsp");
        }
        dis.forward(request, response);
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
