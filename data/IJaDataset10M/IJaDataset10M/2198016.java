package v201108.utils;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.lib.utils.ReportCallback;
import com.google.api.ads.dfp.lib.utils.v201108.DateUtils;
import com.google.api.ads.dfp.lib.utils.v201108.ReportUtils;
import com.google.api.ads.dfp.v201108.Column;
import com.google.api.ads.dfp.v201108.DateRangeType;
import com.google.api.ads.dfp.v201108.Dimension;
import com.google.api.ads.dfp.v201108.ExportFormat;
import com.google.api.ads.dfp.v201108.ReportJob;
import com.google.api.ads.dfp.v201108.ReportQuery;
import com.google.api.ads.dfp.v201108.ReportServiceInterface;
import java.io.IOException;

/**
 * This example runs and downloads a report synchronously or asynchronously
 * using the {@link ReportUtils} class.
 *
 * Tags: ReportService.runReportJob
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class RunAndDownloadReportExample {

    public static void main(String[] args) throws Exception {
        DfpServiceLogger.log();
        DfpUser user = new DfpUser();
        ReportServiceInterface reportService = user.getService(DfpService.V201108.REPORT_SERVICE);
        ReportJob reportJob = new ReportJob();
        ReportQuery reportQuery = new ReportQuery();
        reportQuery.setDateRangeType(DateRangeType.CUSTOM_DATE);
        reportQuery.setStartDate(DateUtils.fromString("2011-03-01"));
        reportQuery.setEndDate(DateUtils.today("PST"));
        reportQuery.setDimensions(new Dimension[] { Dimension.ORDER, Dimension.LINE_ITEM });
        reportQuery.setColumns(new Column[] { Column.AD_SERVER_IMPRESSIONS, Column.AD_SERVER_CLICKS, Column.AD_SERVER_CTR, Column.AD_SERVER_REVENUE, Column.AD_SERVER_AVERAGE_ECPM });
        reportJob.setReportQuery(reportQuery);
        System.out.println("Running report job.");
        long reportJobId = reportService.runReportJob(reportJob).getId();
        final ReportUtils reportUtils = new ReportUtils(reportService, reportJobId);
        final String gzCsvPath = "/path/to/filename.csv.gz";
        boolean useSynchronous = true;
        if (useSynchronous) {
            try {
                System.out.println("Waiting for report to finish.");
                if (reportUtils.waitForReportReady()) {
                    System.out.print("Downloading report to " + gzCsvPath + "...");
                    reportUtils.downloadReport(ExportFormat.CSV, gzCsvPath);
                    System.out.println("done.");
                } else {
                    System.out.println("The report failed to schedule.");
                }
            } catch (IOException e) {
                System.out.println("Report did not download for reason: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Thread reportThread = reportUtils.whenReportReady(new ReportCallback() {

                public void onSuccess() {
                    try {
                        System.out.print("Downloading report to " + gzCsvPath + "...");
                        reportUtils.downloadReport(ExportFormat.CSV, gzCsvPath);
                        System.out.println("done.");
                    } catch (IOException e) {
                        System.out.println("Report did not download for reason: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                public void onInterruption() {
                    System.out.println("The report thread was interrupted.");
                }

                public void onFailure() {
                    System.out.println("The report failed to schedule.");
                }

                public void onException(Exception e) {
                    System.out.println("Report did not download for reason: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
        System.out.println("Main function completed.");
    }
}
