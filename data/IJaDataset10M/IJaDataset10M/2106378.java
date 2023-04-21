package org.efs.openreports.providers;

import java.util.List;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportTemplate;

public interface ReportProvider {

    public List<String> getReportFileNames() throws ProviderException;

    public Report getReport(Integer id) throws ProviderException;

    public Report getReport(String reportName) throws ProviderException;

    public List<Report> getReports() throws ProviderException;

    public Report insertReport(Report report) throws ProviderException;

    public void updateReport(Report report) throws ProviderException;

    public void deleteReport(Report report) throws ProviderException;

    public ReportTemplate getReportTemplate(String templateName) throws ProviderException;

    public List<ReportTemplate> getReportTemplates() throws ProviderException;
}
