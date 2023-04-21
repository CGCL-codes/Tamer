package test2;

import java.io.IOException;
import java.util.HashMap;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.gplugins.jasper.ReportDefinition;
import org.gplugins.jasper.ReportManager;
import org.gplugins.files.ArchivedFile;

public class HelloServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ReportManager reportManager = (ReportManager) new InitialContext().lookup("java:comp/env/GBeanRefName");
            ArchivedFile file = reportManager.generateAndSave("ProductReport", new HashMap());
            response.setContentType("text/html");
            ServletOutputStream out = response.getOutputStream();
            out.println("<html>");
            out.println("<head><title>Hello World</title></head>");
            out.println("<body>");
            out.println("<h1>Report Generated</h1>");
            out.println("<p><b>File Name:</b> " + file.getName());
            out.println("<br /><b>Generated On:</b> " + file.getModifiedDate());
            out.println("<br /><b>Size:</b> " + file.getSize() + " bytes</p>");
            out.println("</body></html>");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String getServletInfo() {
        return "Create a page that says <i>Hello World</i> and send it back";
    }
}
