package web;

import ejb.BookEntity;
import ejb.BookEntityFacadeLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Администратор
 */
public class Show extends HttpServlet {

    @EJB
    private BookEntityFacadeLocal bookEntityFacade;

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
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Show</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Show at " + request.getContextPath() + "</h1>");
            out.println("<table><tr><td>Name</td><td>Author</td><td>Date</td></tr>");
            List books = bookEntityFacade.findAll();
            for (Iterator it = books.iterator(); it.hasNext(); ) {
                out.println("<tr>");
                BookEntity elem = (BookEntity) it.next();
                out.println("<td>" + elem.getName() + "</td>");
                out.println("<td>" + elem.getAuthor() + "</td>");
                out.println("<td>" + elem.getBookdate() + "</td>");
                out.println("<td><a href=\"Delete?id=" + elem.getId() + "\">Delete</a></td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("<a href='AddNew'>Add new book</a><br/>");
            out.println("<a href='Oldest'>Find oldest</a>");
            out.println("</body>");
            out.println("</html>");
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
}
