package org.efs.openreports.actions.admin;

import java.util.List;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.ReportLogProvider;
import com.opensymphony.xwork2.ActionSupport;

public class AnalyzeReportLogsAction extends ActionSupport {

    private static final long serialVersionUID = -6462788985617724220L;

    private static final String TOP_REPORTS = "topReports";

    private static final String TOP_REPORTS_BYUSER = "topReportsByUser";

    private static final String TOP_FAILURES = "topFailures";

    private static final String TOP_EMPTY = "topEmpty";

    private static final String TOP_REPORTS_30_DAYS = "topReports30Days";

    private static final String TOP_REPORTS_60_DAYS = "topReports60Days";

    private static final String TOP_REPORTS_90_DAYS = "topReports90Days";

    private static final String TOP_ALERTS = "topAlerts";

    private static final String TOP_ALERTS_BYUSER = "topAlertsByUser";

    private static final String TOP_TRIGGERED_ALERTS = "topTriggeredAlerts";

    private static final String TOP_NOT_TRIGGERED_ALERTS = "topNotTriggeredAlerts";

    private List<Object[]> results;

    private String queryName;

    private ReportLogProvider reportLogProvider;

    @Override
    public String execute() {
        try {
            if (queryName == null) return SUCCESS;
            if (queryName.equals(TOP_REPORTS_BYUSER)) {
                results = reportLogProvider.getTopReportsByUser();
            } else if (queryName.equals(TOP_REPORTS)) {
                results = reportLogProvider.getTopReports();
            } else if (queryName.equals(TOP_FAILURES)) {
                results = reportLogProvider.getTopFailures();
            } else if (queryName.equals(TOP_EMPTY)) {
                results = reportLogProvider.getTopEmptyReports();
            } else if (queryName.equals(TOP_REPORTS_30_DAYS)) {
                results = reportLogProvider.getTopReportsForPeriod(30);
            } else if (queryName.equals(TOP_REPORTS_60_DAYS)) {
                results = reportLogProvider.getTopReportsForPeriod(60);
            } else if (queryName.equals(TOP_REPORTS_90_DAYS)) {
                results = reportLogProvider.getTopReportsForPeriod(90);
            } else if (queryName.equals(TOP_ALERTS_BYUSER)) {
                results = reportLogProvider.getTopAlertsByUser();
            } else if (queryName.equals(TOP_ALERTS)) {
                results = reportLogProvider.getTopAlerts();
            } else if (queryName.equals(TOP_TRIGGERED_ALERTS)) {
                results = reportLogProvider.getTopTriggeredAlerts();
            } else if (queryName.equals(TOP_NOT_TRIGGERED_ALERTS)) {
                results = reportLogProvider.getTopNotTriggeredAlerts();
            }
        } catch (ProviderException pe) {
            addActionError(pe.getMessage());
            return ERROR;
        }
        return SUCCESS;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public List<Object[]> getResults() {
        return results;
    }

    public void setReportLogProvider(ReportLogProvider reportLogProvider) {
        this.reportLogProvider = reportLogProvider;
    }
}
