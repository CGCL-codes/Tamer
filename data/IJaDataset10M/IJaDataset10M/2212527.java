package org.mylabnote.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mylabnote.dao.LabNoteDelegate;
import org.mylabnote.exception.LabNoteException;

/**
 *
 * @author pajanne
 */
public class UserController extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LabNoteDelegate delegate = LabNoteDelegate.getInstance();
        String userId = request.getParameter("userId");
        String userName = request.getParameter("userName");
        String userEmail = request.getParameter("userEmail");
        int userVersion = 1;
        if (null != request.getParameter("userVersion")) {
            userVersion = Integer.parseInt(request.getParameter("userVersion"));
        }
        String submitValue = "";
        if (null != request.getParameter("submit")) {
            submitValue = request.getParameter("submit");
        }
        try {
            if (submitValue.equals("search")) {
                request.setAttribute("user", delegate.getUser(userId));
            } else if (submitValue.equals("add")) {
                request.setAttribute("user", delegate.addUser(userId, userName, userEmail));
                request.setAttribute("info", "User [" + userId + "] added.");
            } else if (submitValue.equals("update")) {
                request.setAttribute("user", delegate.updateUser(userId, userName, userEmail, userVersion));
                request.setAttribute("info", "User [" + userId + "] updated.");
            } else if (submitValue.equals("delete")) {
                delegate.deleteUser(userId);
                request.setAttribute("info", "User [" + userId + "] deleted.");
            }
        } catch (LabNoteException ex) {
            request.setAttribute("message", ex.getMessage());
        }
        try {
            request.setAttribute("users", delegate.getAllUsers());
        } catch (LabNoteException ex) {
            request.setAttribute("message", ex.getMessage());
        }
        RequestDispatcher dispacher = request.getRequestDispatcher("/AllUsers.jsp");
        dispacher.forward(request, response);
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
