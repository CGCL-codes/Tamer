package servlet;

import entity.daftarWarna;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fredy
 */
public class warna extends HttpServlet {

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
        RequestDispatcher dis = null;
        String jenisError;
        String message = null;
        Object Message = null;
        String message2 = null;
        Object Message2 = null;
        String nama = request.getParameter("nama");
        String usname = request.getParameter("usname");
        String pass = request.getParameter("psword");
        warna warna = new warna();
        RequestDispatcher page = null;
        daftarWarna daftar = new daftarWarna();
        if (nama.equals("")) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/error_page.jsp");
            message = " ";
            request.setAttribute("message", message);
            requestDispatcher.forward(request, response);
        } else {
            boolean hasilCheck = daftar.checkWarna(usname);
            if (!hasilCheck) {
                warna.setNama(nama);
                daftar.addWarna(warna);
                page = request.getRequestDispatcher("index.jsp");
                page.forward(request, response);
            } else {
                message2 = " ";
                request.setAttribute("message2", message2);
                page = request.getRequestDispatcher("/error_page2.jsp");
                page.forward(request, response);
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

    private void setNama(String nama) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
