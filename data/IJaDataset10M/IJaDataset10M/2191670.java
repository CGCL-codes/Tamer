package com.qspin.qtaste.ui.reporter;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.SwingUtilities;
import com.qspin.qtaste.reporter.campaign.CampaignReportManager;
import com.qspin.qtaste.reporter.testresults.TestResult;

/**
 *
 * @author vdubois
 */
public class TestCaseReporter {

    private static TestCaseReporter instance = null;

    private static ArrayList<TestCaseReportTable> reportTableList = new ArrayList<TestCaseReportTable>();

    public static synchronized TestCaseReporter getInstance() {
        if (instance == null) {
            instance = new TestCaseReporter();
        }
        return instance;
    }

    public static void addTestCaseReportTableListener(TestCaseReportTable table) {
        reportTableList.add(table);
    }

    public static void removeTestCaseReportTableListener(TestCaseReportTable table) {
        if (reportTableList.contains(table)) reportTableList.remove(table);
    }

    public void putEntry(final TestResult tr) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                String reportName = CampaignReportManager.getInstance().getReportName();
                Iterator<TestCaseReportTable> it = reportTableList.iterator();
                while (it.hasNext()) it.next().putEntry(tr, reportName);
            }
        });
    }
}
