package br.gov.component.demoiselle.report;

import java.io.OutputStream;
import javax.servlet.ServletRequest;
import net.sf.jasperreports.engine.JasperPrint;

public interface IReportExporter {

    /**
	 * Create report in the form of stream as the definitions of the report
	 * @param report
	 * @param jasperPrint
	 * @param request
	 * @return
	 */
    public OutputStream export(Report report, JasperPrint jasperPrint, ServletRequest request);
}
