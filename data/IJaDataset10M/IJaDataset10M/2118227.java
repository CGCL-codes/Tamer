package nz.ac.massey.softwarec.group3.maptools;

import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Natalie
 */
public interface MapInfoServletInterface {

    /**
     * getMapNames - Query the database to get all the names of maps stored in the database
     * @return ResultSet - the result set from the query 
     */
    ResultSet getMapNames();

    /**
     * getMapNames - Query the database to get all the xml representations of maps stored in the database
     * @return ResultSet - the result set from the query 
     */
    ResultSet getMapXML(final String name);

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    void processRequest(final HttpServletRequest request, final HttpServletResponse response);

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    void doGet(final HttpServletRequest request, final HttpServletResponse response);

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    void doPost(final HttpServletRequest request, final HttpServletResponse response);

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    String getServletInfo();
}
