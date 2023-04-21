package br.revista.report;

import java.util.HashMap;

public class ReportFactory {

    private static final HashMap<Integer, Report> reports = new HashMap();

    private static final ReportFactory factory;

    static {
        factory = new ReportFactory();
    }

    private ReportFactory() {
        reports.put(Report.REPORT_TEMPLATE, new TemplateReport());
        reports.put(Report.REPORT_ARTICLE, new ArticleReport());
        reports.put(Report.REPORT_FULL_ARTICLE, new FullArticleReport());
        reports.put(Report.REPORT_ARTICLE_VERSIONS, new ArticleVersionsReport());
        reports.put(Report.REPORT_USER, new UserReport());
        reports.put(Report.REPORT_REVIEW, new ReviewReport());
        reports.put(Report.REPORT_VOLUME, new VolumeReport());
    }

    public static ReportFactory getInstance() {
        return factory;
    }

    public static Report getReport(int type) {
        return reports.get(type);
    }
}
