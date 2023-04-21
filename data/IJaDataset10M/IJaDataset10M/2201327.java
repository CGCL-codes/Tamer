package Servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.DaftarUser;
import model.User;

/**
 *
 * @author dila
 */
public class Register extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dis = null;
        HttpSession session = request.getSession();
        session.removeAttribute("error");
        User user = new User();
        DaftarUser du = new DaftarUser();
        String username = request.getParameter("username");
        String nama = request.getParameter("nama");
        String sekolah = request.getParameter("sekolah");
        String alamat = request.getParameter("alamat");
        String email = request.getParameter("email");
        String password = request.getParameter("pass");
        user.setAlamat(alamat);
        user.setUsername(username);
        user.setEmail(email);
        user.setNama(nama);
        user.setPass(password);
        user.setSekolah(sekolah);
        session.setAttribute("register", user);
        user.setLoginStat(true);
        if (alamat.equals("") || email.equals("") || nama.equals("") || sekolah.equals("") || username.equals("") || password.equals("")) {
            session.setAttribute("error", "* masih ada field kosong");
            dis = request.getRequestDispatcher("register.jsp");
            dis.forward(request, response);
        } else {
            boolean hasilPeriksa = du.check(username);
            if (!hasilPeriksa) {
                if (password.length() >= 6) {
                    du.create(user);
                    session.removeAttribute("error");
                    dis = request.getRequestDispatcher("index.jsp");
                    dis.forward(request, response);
                } else {
                    session.setAttribute("error", "* password minimal harus 6 digit");
                    dis = request.getRequestDispatcher("register.jsp");
                    dis.forward(request, response);
                }
            } else {
                session.setAttribute("error", "* username sudah ada dalam database");
                dis = request.getRequestDispatcher("register.jsp");
                dis.forward(request, response);
            }
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
}
