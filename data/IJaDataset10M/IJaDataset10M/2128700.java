package web;

import fachlogik.Eintrag;
import fachlogik.GaesteBuch;
import fachlogik.GaesteBuchException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Rudi
 */
public class NeuerEintrag extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            GaesteBuch buch = (GaesteBuch) request.getSession().getServletContext().getAttribute("gaestebuch");
            if (buch == null) {
                throw new ServletException("keine Instanz von GaesteBuch");
            }
            String text = request.getParameter("text");
            Eintrag eintrag = new Eintrag();
            eintrag.setText(text);
            eintrag.setDatum(new Date());
            eintrag.setErsteller((String) request.getSession().getAttribute("name"));
            buch.neu(eintrag);
            ListenAuswahl.auswahl(request.getSession());
            request.getRequestDispatcher("gaestebuch.jsp").forward(request, response);
        } catch (GaesteBuchException ex) {
            Logger.getLogger(Anmeldung.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
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
