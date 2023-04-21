package com.entelience.test.test03report;

import org.junit.*;
import static org.junit.Assert.*;
import com.entelience.esis.ReportManager;
import com.entelience.test.DbProcessTestCase;
import com.entelience.util.DateHelper;

/** 
    Run reports on user reports
*/
public class test05NetworkReport extends DbProcessTestCase {

    @Test
    public void test01_NetworkReport() throws Exception {
        String args[] = {};
        ReportManager.runReportRange(db, statusDb, "com.entelience.report.net.NetworkEventsReport", DateHelper.YYYYMMDD_dash("2005-02-01"), DateHelper.YYYYMMDD_dash("2005-03-01"), args);
        ReportManager.runReportRange(db, statusDb, "com.entelience.report.net.NetworkEventsReport", DateHelper.YYYYMMDD_dash("2007-07-22"), DateHelper.YYYYMMDD_dash("2007-07-26"), args);
        ReportManager.runReportRange(db, statusDb, "com.entelience.report.net.NetworkEventsReport", DateHelper.YYYYMMDD_dash("2007-11-25"), DateHelper.YYYYMMDD_dash("2007-12-01"), args);
        ReportManager.runReportRange(db, statusDb, "com.entelience.report.net.NetworkEventsReport", DateHelper.YYYYMMDD_dash("2008-07-13"), DateHelper.YYYYMMDD_dash("2008-07-18"), args);
    }
}
