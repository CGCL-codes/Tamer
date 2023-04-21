package org.opennms.web.userreport;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.log4j.Category;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.config.DatabaseConnectionFactory;
import org.opennms.web.availability.AvailabilityServlet;

/**
 * @author jsartin
 * 
 * First attempt at a pdf report using jasperreports
 * 
 */
public class UserReportServlet extends HttpServlet {

    static Category log = Category.getInstance(AvailabilityServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = this.getServletConfig().getServletContext();
        String formatParam = request.getParameter("format");
        File reportFile = new File("/opt/OpenNMS/lib/reports/notices.jasper");
        if (!reportFile.exists()) throw new JRRuntimeException("File WebappReport.jasper not found. The report design must be compiled first.");
        HashMap parameters = new HashMap();
        parameters.put("StartDay", "10-10-2005");
        parameters.put("StartTime", "07:00");
        parameters.put("EndDay", "11-10-2005");
        parameters.put("EndTime", "06:59");
        byte[] bytes = null;
        Connection m_availConn = null;
        try {
            DatabaseConnectionFactory.init();
            m_availConn = DatabaseConnectionFactory.getInstance().getConnection();
        } catch (MarshalException e1) {
            e1.printStackTrace();
        } catch (ValidationException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, m_availConn);
            try {
                m_availConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (JRException e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>JasperReports - Web Application Sample</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
            out.println("</head>");
            out.println("<body bgcolor=\"white\">");
            out.println("<span class=\"bnew\">JasperReports encountered this error :</span>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
            out.println("</body>");
            out.println("</html>");
            return;
        }
        if (bytes != null && bytes.length > 0) {
            response.setContentType("application/pdf");
            response.setContentLength(bytes.length);
            ServletOutputStream ouputStream = response.getOutputStream();
            ouputStream.write(bytes, 0, bytes.length);
            ouputStream.flush();
            ouputStream.close();
        } else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>JasperReports - Web Application Sample</title>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
            out.println("</head>");
            out.println("<body bgcolor=\"white\">");
            out.println("<span class=\"bold\">Empty response.</span>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
