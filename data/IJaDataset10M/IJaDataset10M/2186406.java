package Servlets;

import Beans.Connection;
import Beans.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Kana
 */
public class AddNews extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String module = request.getParameter("module");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        try {
            Connection conn = (Connection) this.getServletContext().getAttribute("connection");
            if (conn.getConn() == null) response.sendError(1001);
            ResultSet rs = null;
            conn.setQuery("SELECT level FROM login JOIN user USING (userid) WHERE username='" + user.getUsername() + "'");
            rs = conn.fetch();
            String count = rs.getString(1);
            if (count.equals("99") && action.equals("add")) {
                String title = request.getParameter("act_title");
                String body = request.getParameter("act_details");
                if (title == null && body == null) {
                    request.setAttribute("error", "Addition of News failed. Please try again.");
                } else {
                    if (title.isEmpty() && body.isEmpty()) {
                        out.println("Addition of News failed. Please try again.");
                    } else {
                        conn.setQuery("INSERT INTO news (title, details, date) VALUES ('" + title + "','" + body + "', DATE(NOW()))");
                        conn.update();
                        out.println("<redirect url='/home?module=news'>Adding of the news was succesful!</redirect>");
                    }
                }
            }
            request.getRequestDispatcher("/WEB-INF/base/" + module + "/index.jsp").include(request, response);
        } catch (NullPointerException e) {
            out.println("Invalid Action! Either login or contact the administrator to do such action.");
        } catch (Exception e) {
            out.println("Error: " + e.toString());
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
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
     * Handles the HTTP
     * <code>POST</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
