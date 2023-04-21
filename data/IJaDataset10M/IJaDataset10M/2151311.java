package br.gov.component.demoiselle.report;

import java.io.OutputStream;
import javax.servlet.ServletRequest;
import net.sf.jasperreports.engine.JasperPrint;

public class ReportExporterStub implements IReportExporter {

    public OutputStream export(Report report, JasperPrint jasperPrint, ServletRequest request) {
        return new ReportExporter().export(report, jasperPrint, request);
    }
}
