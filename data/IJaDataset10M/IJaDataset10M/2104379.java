package de.objectcode.time4u.servernew.util;

import de.objectcode.time4u.servernew.report.ReportProject;
import de.objectcode.time4u.servernew.report.ReportView;

public class ParameterClass {

    private String endDate;

    private String startDate;

    private ReportProject.Report report;

    private ReportView.RowReport rowReport;

    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    public void setReport(final ReportProject.Report report) {
        this.report = report;
    }

    public void setRowReport(final ReportView.RowReport rowReport) {
        this.rowReport = rowReport;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public ReportProject.Report getReport() {
        return this.report;
    }

    public ReportView.RowReport getRowReport() {
        return this.rowReport;
    }

    public String getStartDate() {
        return this.startDate;
    }
}
