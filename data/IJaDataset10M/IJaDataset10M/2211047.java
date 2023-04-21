package fido.servlets.attrcat;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import fido.db.AttributeCategoryTable;

public class Delete extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String context = request.getContextPath();
        try {
            String name = request.getParameter("name");
            AttributeCategoryTable table = new AttributeCategoryTable();
            table.delete(name);
            response.sendRedirect(context + "/attrCatTable");
        } catch (Exception e) {
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            out.println("<HTML>");
            out.println("<HEAD>");
            out.println("<TITLE>Error Deleting Attribute Category</TITLE>");
            out.println("<LINK REL=stylesheet HREF=" + context + "/Border.css>");
            out.println("</HEAD>");
            out.println("<BODY>");
            out.println("Error deleting Attribute Category<P>");
            out.println("<PRE>");
            e.printStackTrace(out);
            out.println("</PRE>");
            out.println("Click <A HREF=" + context + "/attrCatTable>here</A> to return to the table view.");
            out.println("</BODY>");
            out.println("</HTML>");
        }
    }
}
