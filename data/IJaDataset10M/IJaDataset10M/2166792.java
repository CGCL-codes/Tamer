package wyklad;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Pozdrawiak extends javax.servlet.http.HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf8");
        PrintWriter pw = response.getWriter();
        String imie = request.getParameter("imie");
        pw.println("<html>");
        pw.println("<head>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("Witaj " + imie);
        pw.println("</body>");
        pw.println("</html>");
        pw.close();
    }
}
