package org.forzaframework.web.servlet.view;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.forzaframework.util.ReportUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.DynamicJasperHelper;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 16:02:53
 */
public class DynamicJasperReportsView extends ModelAndView {

    private static final int OUTPUT_BYTE_ARRAY_INITIAL_SIZE = 4096;

    public DynamicJasperReportsView(JasperPrint jasperPrint) {
        super(createView(jasperPrint));
    }

    public DynamicJasperReportsView(DynamicReport dynamicReport, JRDataSource dataSource) throws JRException {
        super(createView(dynamicReport, dataSource));
    }

    public DynamicJasperReportsView(DynamicReport dynamicReport, JRDataSource dataSource, Map parameters) throws JRException {
        super(createView(dynamicReport, dataSource, parameters));
    }

    protected static boolean useWriter() {
        return false;
    }

    private static AbstractView createView(DynamicReport dynamicReport, JRDataSource dataSource) throws JRException {
        return createView(DynamicJasperHelper.generateJasperPrint(dynamicReport, new ClassicLayoutManager(), dataSource));
    }

    private static AbstractView createView(DynamicReport dynamicReport, JRDataSource dataSource, Map parameters) throws JRException {
        return createView(DynamicJasperHelper.generateJasperPrint(dynamicReport, new ClassicLayoutManager(), dataSource, parameters));
    }

    private static AbstractView createView(final JasperPrint jasperPrint) {
        return new AbstractView() {

            protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                renderReport(jasperPrint, model, response);
            }
        };
    }

    protected static void renderReport(JasperPrint populatedReport, Map model, HttpServletResponse response) throws Exception {
        JRExporter exporter = createExporter();
        if (useWriter()) {
        } else {
            renderReportUsingOutputStream(exporter, populatedReport, response);
        }
    }

    private static JRExporter createExporter() {
        return new JRPdfExporter();
    }

    private static void renderReportUsingOutputStream(JRExporter exporter, JasperPrint populatedReport, HttpServletResponse response) throws Exception {
        response.setContentType(getContentType());
        ByteArrayOutputStream baos = new ByteArrayOutputStream(OUTPUT_BYTE_ARRAY_INITIAL_SIZE);
        JasperReportsUtils.render(exporter, populatedReport, baos);
        response.setContentLength(baos.size());
        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
    }

    private static String getContentType() {
        return "application/pdf";
    }
}
