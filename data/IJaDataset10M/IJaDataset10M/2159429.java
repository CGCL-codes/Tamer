package net.sourceforge.jcv.services;

import com.salmonllc.localizer.LanguagePreferences;
import net.sourceforge.jcv.services.reports.resumes.ResumeReportFactory;

public class ReportService {

    private String reportsBaseDir;

    private String imageBaseDir;

    private String appName;

    public ReportService(String appName, String imageBaseDir, String reportsBaseDir) {
        this.appName = appName;
        this.imageBaseDir = imageBaseDir;
        this.reportsBaseDir = reportsBaseDir;
    }

    public byte[] generateResumeReport(String reportFormat, String reportType, int resumeId, LanguagePreferences langPref, boolean onlySeletedSections) throws Exception {
        ResumeReportFactory resumeReportFactory = new ResumeReportFactory(appName, imageBaseDir, reportsBaseDir);
        return resumeReportFactory.getResumeReport(reportFormat, reportType, resumeId, langPref, onlySeletedSections).generateReport();
    }
}
