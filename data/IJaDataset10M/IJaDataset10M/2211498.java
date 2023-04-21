package org.frameworkset.web.servlet.view.jasperreports;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

/**
 * Implementation of <code>AbstractJasperReportsSingleFormatView</code>
 * that renders report results in XLS format.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.1.3
 */
public class JasperReportsXlsView extends AbstractJasperReportsSingleFormatView {

    public JasperReportsXlsView() {
        setContentType("application/vnd.ms-excel");
    }

    @Override
    protected JRExporter createExporter() {
        return new JRXlsExporter();
    }

    @Override
    protected boolean useWriter() {
        return false;
    }
}
