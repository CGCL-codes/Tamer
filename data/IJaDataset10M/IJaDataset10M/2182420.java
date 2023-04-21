package net.sf.ussrp.web.servlets;

import java.util.Map;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import net.sf.ussrp.bus.Report;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: XlsServlet.java,v 1.3 2006/01/21 23:37:34 tomkast Exp $
 */
public class XlsServlet extends HttpServlet {

    /**
	 *
	 */
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        OutputStream out = null;
        Log logger = LogFactory.getLog(getClass());
        try {
            Map model = (Map) request.getAttribute("model");
            Report report = (Report) model.get("report");
            JasperPrint jasperPrint = (JasperPrint) model.get("jasperPrint");
            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            out = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            response.setContentType("application/msexcel");
            exporter.exportReport();
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("service(), " + e.getMessage());
            e.printStackTrace();
            try {
                if (out != null) out.close();
            } catch (Exception e2) {
                logger.error("service(), " + e2.getMessage());
                e2.printStackTrace();
            }
        }
    }
}
