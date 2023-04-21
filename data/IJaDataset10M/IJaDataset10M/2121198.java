package servlet.util;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  Administrator
 * @version
 */
public class HolidayServelet extends HttpServlet {

    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /** Destroys the servlet.
     */
    public void destroy() {
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        System.out.println("netwrok library name is" + request.getParameter("holidaylibrary"));
        String libraryname;
        try {
            if (request.getParameter("holidaylibrary") == null) {
                libraryname = session.getAttribute("LibraryId").toString();
            } else {
                libraryname = request.getParameter("holidaylibrary");
            }
            System.out.println("libraryname is" + libraryname);
            java.util.Vector v = (new beans.sm.Holidayslist()).getholidays(libraryname, session.getAttribute("NetworkLibraryName").toString());
            for (int i = 0; i < v.size(); i = i + 2) {
                System.out.println("this is vec1" + v.elementAt(i));
                System.out.println("this is vec2" + v.elementAt(i + 1));
            }
            System.out.println("this enter the holiday page");
            request.setAttribute("page", "1");
            request.setAttribute("vector", v);
            request.setAttribute("name", libraryname);
            javax.servlet.RequestDispatcher rd = null;
            if (v.size() > 0) {
                rd = request.getRequestDispatcher("/jsp/sm/Holidays.jsp");
                rd.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
}
