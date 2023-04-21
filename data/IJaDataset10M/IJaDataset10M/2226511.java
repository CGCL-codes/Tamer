package controller;

import entity.Gejala;
import entity.KelolaGejala;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jpa.exceptions.NonexistentEntityException;

/**
 *
 * @author Aditya-Z
 */
public class editGejala extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NonexistentEntityException, Exception {
        String u = request.getParameter("u");
        RequestDispatcher dis = null;
        Gejala gejala = new Gejala();
        KelolaGejala ka = new KelolaGejala();
        HttpSession session = request.getSession();
        if (u == null) {
            if (session.getAttribute("gejala1") == null) {
                dis = request.getRequestDispatcher("index.jsp");
                dis.forward(request, response);
            } else {
                gejala = null;
                gejala = (Gejala) session.getAttribute("gejala1");
            }
        } else {
            if (u.equals("gejala_edit")) {
                gejala = null;
                gejala = (Gejala) session.getAttribute("gejala1");
                session.setAttribute("tempU", gejala);
                dis = request.getRequestDispatcher("editGejala.jsp");
                dis.forward(request, response);
            } else if (u.equals("gejala_update")) {
                gejala = null;
                gejala = (Gejala) session.getAttribute("gejala1");
                String namkit = request.getParameter("Nama_penyakit");
                String tingkit = request.getParameter("tingkit");
                String ketgejala = request.getParameter("ketgejala");
                gejala.setNamaPenyakit(namkit);
                gejala.setTingkatPenyakit(tingkit);
                gejala.setGejala(ketgejala);
                session.setAttribute("tempU", gejala);
                ka.edit(gejala);
                session.removeAttribute("tempU");
                session.setAttribute("u", gejala);
                dis = request.getRequestDispatcher("listGejala.jsp");
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
        try {
            processRequest(request, response);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(editGejala.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(editGejala.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(editGejala.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(editGejala.class.getName()).log(Level.SEVERE, null, ex);
        }
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
