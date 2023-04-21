package net.sourceforge.unitth.integrationtest;

import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MainPageStatisticsIT {

    private static String OUTPUT_FOLDER = "report.th";

    private String[] reports = { "report-001", "report-002", "report-003", "report-004", "report-005", "report-006", "report-007", "report-008", "report-009", "report-010" };

    private String[] dFlags = { "unitth.generate.exectimegraphs=true", "unitth.html.report.path=.", "unitth.report.dir=" + OUTPUT_FOLDER, "unitth.xml.report.filter=TEST-", "unitth.use.absolsute.paths=true" };

    private static WebDriver driver = null;

    @BeforeClass
    public static void setupTestClass() {
        driver = new FirefoxDriver();
    }

    @AfterClass
    public static void tearDownClass() {
        driver.close();
    }

    @Test
    public void TC_shallCheckReportReportSummary_basedOnAllReferenceReports() {
        ArrayList<String> aReports = new ArrayList<String>();
        for (String report : reports) {
            aReports.add(this.getClass().getResource("/test-reports/junit.reference/" + report).getFile().toString());
        }
        RunUnitTHUtils.runUnitTHWithJunitReports(aReports.toArray(new String[0]), dFlags);
        driver.get("file://" + System.getProperty("user.dir") + "/" + OUTPUT_FOLDER + "/main.html");
        HtmlReportAPI hrapi = new HtmlReportAPI(driver);
        assertEquals("10", hrapi.getMainPageSummaryNumberOfRuns());
        assertEquals("9", hrapi.getMainPageSummaryNumberOfPackages());
        assertEquals("22", hrapi.getMainPageSummaryNumberOfTestClasses());
        assertEquals("91", hrapi.getMainPageSummaryNumberOfTestCases());
        assertEquals("80.23%", hrapi.getMainPageSummaryBestRun());
        assertEquals("76.74%", hrapi.getMainPageSummaryWorstRun());
        assertEquals("78.10%", hrapi.getMainPageSummaryAveragePassRate());
    }

    @Test
    public void TC_shallCheckReportReportSummary_basedOnSingleReferenceReporWithIgnores() {
        ArrayList<String> aReports = new ArrayList<String>();
        aReports.add(this.getClass().getResource("/test-reports/junit.reference/" + reports[8]).getFile().toString());
        RunUnitTHUtils.runUnitTHWithJunitReports(aReports.toArray(new String[0]), dFlags);
        driver.get("file://" + System.getProperty("user.dir") + "/" + OUTPUT_FOLDER + "/main.html");
        HtmlReportAPI hrapi = new HtmlReportAPI(driver);
        assertEquals("1", hrapi.getMainPageSummaryNumberOfRuns());
        assertEquals("9", hrapi.getMainPageSummaryNumberOfPackages());
        assertEquals("22", hrapi.getMainPageSummaryNumberOfTestClasses());
        assertEquals("86", hrapi.getMainPageSummaryNumberOfTestCases());
        assertEquals("76.74%", hrapi.getMainPageSummaryBestRun());
        assertEquals("76.74%", hrapi.getMainPageSummaryWorstRun());
        assertEquals("76.74%", hrapi.getMainPageSummaryAveragePassRate());
    }

    @Test
    public void TC_shallVerifyRunList() throws Exception {
        ArrayList<String> aReports = new ArrayList<String>();
        for (String report : reports) {
            aReports.add(this.getClass().getResource("/test-reports/junit.reference/" + report).getFile().toString());
        }
        RunUnitTHUtils.runUnitTHWithJunitReports(aReports.toArray(new String[0]), dFlags);
        driver.get("file://" + System.getProperty("user.dir") + "/" + OUTPUT_FOLDER + "/main.html");
        HtmlReportAPI hrapi = new HtmlReportAPI(driver);
        int rowIndex = 1;
        int noTests = hrapi.getNoTestCasesStatsForMainPageRunListForRowWithIndex(rowIndex);
        int noPassed = hrapi.getNoPassedTestCasesStatsForMainPageRunListForRowWithIndex(rowIndex);
        int noFailed = hrapi.getNoFailedTestCasesStatsForMainPageRunListForRowWithIndex(rowIndex);
        int noError = hrapi.getNoErrorTestCasesStatsForMainPageRunListForRowWithIndex(rowIndex);
        int noIgnored = hrapi.getNoIgnoredTestCasesStatsForMainPageRunListForRowWithIndex(rowIndex);
        double passRate = hrapi.getPassRateForMainPageRunListForRowWithIndex(rowIndex);
        assertEquals("checking no rows in run list", 10, hrapi.getNoRunRowsInRunList());
        assertEquals("checking no test cases in a run list row ", 86, noTests);
        assertEquals("checking no passed test cases in a run list row ", 66, noPassed);
        assertEquals("checking no error test cases in a run list row ", 0, noError);
        assertEquals("checking no failed test cases in a run list row ", 20, noFailed);
        assertEquals("checking no ignored test cases in a run list row ", 5, noIgnored);
        assertEquals("checking pass rate in a run list row ", 76.74, passRate, 0.01);
        assertEquals("no executed tests shall be equal to passed+fail+error", noTests, noPassed + noFailed + noError);
    }

    @Test
    public void TC_shallVerifyTestPackageList() throws Exception {
        ArrayList<String> aReports = new ArrayList<String>();
        for (String report : reports) {
            aReports.add(this.getClass().getResource("/test-reports/junit.reference/" + report).getFile().toString());
        }
        RunUnitTHUtils.runUnitTHWithJunitReports(aReports.toArray(new String[0]), dFlags);
        driver.get("file://" + System.getProperty("user.dir") + "/" + OUTPUT_FOLDER + "/main.html");
        HtmlReportAPI hrapi = new HtmlReportAPI(driver);
        int rowIndex = 1;
        int noUniqueTests = hrapi.getNoUniqueTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noExecTests = hrapi.getNoExecutedTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noRuns = hrapi.getNoRunsForMainPagePackageListForRowWithIndex(rowIndex);
        int noPassed = hrapi.getNoPassedTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noFailed = hrapi.getNoFailedTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noError = hrapi.getNoErrorTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        assertEquals("checking no rows in package list", 9, hrapi.getNoPackageRowsInPackageList());
        assertEquals("checking no runs in a package list row ", 10, noRuns);
        assertEquals("checking no unique test classes in a package list row ", 22, hrapi.getNoUniqueClassesStatsForMainPagePackageListForRowWithIndex(rowIndex));
        assertEquals("checking no unique test cases in a package list row ", 91, noUniqueTests);
        assertEquals("checking no executed test cases in a package list row ", 854, noExecTests);
        assertEquals("checking no passed test cases in a package list row ", 667, noPassed);
        assertEquals("checking no error test cases in a package list row ", 0, noError);
        assertEquals("checking no failed test cases in a package list row ", 187, noFailed);
        assertEquals("checking no ignored test cases in a package list row ", 91, hrapi.getNoIgnoredTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex));
        assertEquals("checking pass rate in a package list row ", 78.10, hrapi.getPassRateForMainPagePackageListForRowWithIndex(rowIndex), 0.01);
        assertEquals("no executed tests shall be equal to passed+fail+error", noExecTests, noPassed + noFailed + noError);
    }

    @Test
    public void TC_shallVerifyTestPackageList_forSubPackageLevel1() throws Exception {
        ArrayList<String> aReports = new ArrayList<String>();
        for (String report : reports) {
            aReports.add(this.getClass().getResource("/test-reports/junit.reference/" + report).getFile().toString());
        }
        RunUnitTHUtils.runUnitTHWithJunitReports(aReports.toArray(new String[0]), dFlags);
        driver.get("file://" + System.getProperty("user.dir") + "/" + OUTPUT_FOLDER + "/main.html");
        HtmlReportAPI hrapi = new HtmlReportAPI(driver);
        int rowIndex = 2;
        int noUniqueTests = hrapi.getNoUniqueTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noExecTests = hrapi.getNoExecutedTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noRuns = hrapi.getNoRunsForMainPagePackageListForRowWithIndex(rowIndex);
        int noPassed = hrapi.getNoPassedTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noFailed = hrapi.getNoFailedTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noError = hrapi.getNoErrorTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        assertEquals("checking no runs in a package list row ", 10, noRuns);
        assertEquals("checking no unique test classes in a package list row ", 10, hrapi.getNoUniqueClassesStatsForMainPagePackageListForRowWithIndex(rowIndex));
        assertEquals("checking no unique test cases in a package list row ", 41, noUniqueTests);
        assertEquals("checking no executed test cases in a package list row ", 410, noExecTests);
        assertEquals("checking no passed test cases in a package list row ", 299, noPassed);
        assertEquals("checking no error test cases in a package list row ", 0, noError);
        assertEquals("checking no failed test cases in a package list row ", 111, noFailed);
        assertEquals("checking no ignored test cases in a package list row ", 40, hrapi.getNoIgnoredTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex));
        assertEquals("checking pass rate in a package list row ", 72.93, hrapi.getPassRateForMainPagePackageListForRowWithIndex(rowIndex), 0.01);
        assertEquals("no executed tests shall be egual to runs*tests", noExecTests, noRuns * noUniqueTests);
        assertEquals("no executed tests shall be equal to passed+fail+error", noExecTests, noPassed + noFailed + noError);
    }

    @Test
    public void TC_shallVerifyTestPackageList_forSubPackageLevel2() throws Exception {
        ArrayList<String> aReports = new ArrayList<String>();
        for (String report : reports) {
            aReports.add(this.getClass().getResource("/test-reports/junit.reference/" + report).getFile().toString());
        }
        RunUnitTHUtils.runUnitTHWithJunitReports(aReports.toArray(new String[0]), dFlags);
        driver.get("file://" + System.getProperty("user.dir") + "/" + OUTPUT_FOLDER + "/main.html");
        HtmlReportAPI hrapi = new HtmlReportAPI(driver);
        int rowIndex = 3;
        int noUniqueTests = hrapi.getNoUniqueTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noExecTests = hrapi.getNoExecutedTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noRuns = hrapi.getNoRunsForMainPagePackageListForRowWithIndex(rowIndex);
        int noPassed = hrapi.getNoPassedTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noFailed = hrapi.getNoFailedTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noError = hrapi.getNoErrorTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        assertEquals("checking no runs in a package list row ", 10, noRuns);
        assertEquals("checking no unique test classes in a package list row ", 6, hrapi.getNoUniqueClassesStatsForMainPagePackageListForRowWithIndex(rowIndex));
        assertEquals("checking no unique test cases in a package list row ", 25, noUniqueTests);
        assertEquals("checking no executed test cases in a package list row ", 250, noExecTests);
        assertEquals("checking no passed test cases in a package list row ", 196, noPassed);
        assertEquals("checking no error test cases in a package list row ", 0, noError);
        assertEquals("checking no failed test cases in a package list row ", 54, noFailed);
        assertEquals("checking no ignored test cases in a package list row ", 40, hrapi.getNoIgnoredTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex));
        assertEquals("checking pass rate in a package list row ", 78.40, hrapi.getPassRateForMainPagePackageListForRowWithIndex(rowIndex), 0.01);
        assertEquals("no executed tests shall be egual to runs*tests", noExecTests, noRuns * noUniqueTests);
        assertEquals("no executed tests shall be equal to passed+fail+error", noExecTests, noPassed + noFailed + noError);
    }

    @Test
    public void TC_shallVerifyTestPackageList_forSubPackageLevel3() throws Exception {
        ArrayList<String> aReports = new ArrayList<String>();
        for (String report : reports) {
            aReports.add(this.getClass().getResource("/test-reports/junit.reference/" + report).getFile().toString());
        }
        RunUnitTHUtils.runUnitTHWithJunitReports(aReports.toArray(new String[0]), dFlags);
        driver.get("file://" + System.getProperty("user.dir") + "/" + OUTPUT_FOLDER + "/main.html");
        HtmlReportAPI hrapi = new HtmlReportAPI(driver);
        int rowIndex = 4;
        int noUniqueTests = hrapi.getNoUniqueTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noExecTests = hrapi.getNoExecutedTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noRuns = hrapi.getNoRunsForMainPagePackageListForRowWithIndex(rowIndex);
        int noPassed = hrapi.getNoPassedTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noFailed = hrapi.getNoFailedTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        int noError = hrapi.getNoErrorTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex);
        assertEquals("checking no runs in a package list row ", 10, noRuns);
        assertEquals("checking no unique test classes in a package list row ", 2, hrapi.getNoUniqueClassesStatsForMainPagePackageListForRowWithIndex(rowIndex));
        assertEquals("checking no unique test cases in a package list row ", 9, noUniqueTests);
        assertEquals("checking no executed test cases in a package list row ", 90, noExecTests);
        assertEquals("checking no passed test cases in a package list row ", 79, noPassed);
        assertEquals("checking no error test cases in a package list row ", 0, noError);
        assertEquals("checking no failed test cases in a package list row ", 11, noFailed);
        assertEquals("checking no ignored test cases in a package list row ", 10, hrapi.getNoIgnoredTestCasesStatsForMainPagePackageListForRowWithIndex(rowIndex));
        assertEquals("checking pass rate in a package list row ", 87.78, hrapi.getPassRateForMainPagePackageListForRowWithIndex(rowIndex), 0.01);
        assertEquals("no executed tests shall be egual to runs*tests", noExecTests, noRuns * noUniqueTests);
        assertEquals("no executed tests shall be equal to passed+fail+error", noExecTests, noPassed + noFailed + noError);
    }

    @Test
    public void TC_shallVerifyTestModuleList_forTestClassWithFailsAndIgnores() throws Exception {
        ArrayList<String> aReports = new ArrayList<String>();
        for (String report : reports) {
            aReports.add(this.getClass().getResource("/test-reports/junit.reference/" + report).getFile().toString());
        }
        RunUnitTHUtils.runUnitTHWithJunitReports(aReports.toArray(new String[0]), dFlags);
        driver.get("file://" + System.getProperty("user.dir") + "/" + OUTPUT_FOLDER + "/main.html");
        HtmlReportAPI hrapi = new HtmlReportAPI(driver);
        int rowIndex = 7;
        int noUniqueTests = hrapi.getNoUniqueTestCasesStatsForMainPageModuleListForRowWithIndex(rowIndex);
        int noExecTests = hrapi.getNoExecutedTestCasesStatsForMainPageModuleListForRowWithIndex(rowIndex);
        int noRuns = hrapi.getNoRunsForMainPageModuleListForRowWithIndex(rowIndex);
        int noPassed = hrapi.getNoPassedTestCasesStatsForMainPageModuleListForRowWithIndex(rowIndex);
        int noFailed = hrapi.getNoFailedTestCasesStatsForMainPageModuleListForRowWithIndex(rowIndex);
        int noError = hrapi.getNoErrorTestCasesStatsForMainPageModuleListForRowWithIndex(rowIndex);
        assertEquals("checking no rows in module list", 22, hrapi.getNoModuleRowsInModuleList());
        assertEquals("checking no runs in a module list row ", 10, noRuns);
        assertEquals("checking no unique test cases in a module list row ", 4, noUniqueTests);
        assertEquals("checking no executed test cases in a module list row ", 40, noExecTests);
        assertEquals("checking no passed test cases in a module list row ", 30, noPassed);
        assertEquals("checking no error test cases in a module list row ", 0, noError);
        assertEquals("checking no failed test cases in a module list row ", 10, noFailed);
        assertEquals("checking no ignored test cases in a module list row ", 10, hrapi.getNoIgnoredTestCasesStatsForMainPageModuleListForRowWithIndex(rowIndex));
        assertEquals("checking pass rate in a module list row ", 75.00, hrapi.getPassRateForMainPageModuleListForRowWithIndex(rowIndex), 0.01);
        assertEquals("no executed tests shall be egual to runs*tests", noExecTests, noRuns * noUniqueTests);
        assertEquals("no executed tests shall be equal to passed+fail+error", noExecTests, noPassed + noFailed + noError);
        assertEquals("no unique test modules in module list shall match amount given in summary", Integer.parseInt(hrapi.getMainPageSummaryNumberOfTestClasses()), hrapi.getNoModuleRowsInModuleList());
    }

    @Test
    public void TC_shallVerifyTestModuleList_forTestClassThatHasBeenIgnored() throws Exception {
        ArrayList<String> aReports = new ArrayList<String>();
        for (String report : reports) {
            aReports.add(this.getClass().getResource("/test-reports/junit.reference/" + report).getFile().toString());
        }
        RunUnitTHUtils.runUnitTHWithJunitReports(aReports.toArray(new String[0]), dFlags);
        driver.get("file://" + System.getProperty("user.dir") + "/" + OUTPUT_FOLDER + "/main.html");
        HtmlReportAPI hrapi = new HtmlReportAPI(driver);
        int rowIndex = 18;
        int noUniqueTests = hrapi.getNoUniqueTestCasesStatsForMainPageModuleListForRowWithIndex(rowIndex);
        int noExecTests = hrapi.getNoExecutedTestCasesStatsForMainPageModuleListForRowWithIndex(rowIndex);
        int noRuns = hrapi.getNoRunsForMainPageModuleListForRowWithIndex(rowIndex);
        int noPassed = hrapi.getNoPassedTestCasesStatsForMainPageModuleListForRowWithIndex(rowIndex);
        int noFailed = hrapi.getNoFailedTestCasesStatsForMainPageModuleListForRowWithIndex(rowIndex);
        int noError = hrapi.getNoErrorTestCasesStatsForMainPageModuleListForRowWithIndex(rowIndex);
        assertEquals("checking no runs in a module list row ", 10, noRuns);
        assertEquals("checking no unique test cases in a module list row ", 5, noUniqueTests);
        assertEquals("checking no executed test cases in a module list row ", 16, noExecTests);
        assertEquals("checking no passed test cases in a module list row ", 14, noPassed);
        assertEquals("checking no error test cases in a module list row ", 0, noError);
        assertEquals("checking no failed test cases in a module list row ", 2, noFailed);
        assertEquals("checking no ignored test cases in a module list row ", 8, hrapi.getNoIgnoredTestCasesStatsForMainPageModuleListForRowWithIndex(rowIndex));
        assertEquals("checking pass rate in a module list row ", 87.50, hrapi.getPassRateForMainPageModuleListForRowWithIndex(rowIndex), 0.01);
        assertEquals("no executed tests shall be equal to passed+fail+error", noExecTests, noPassed + noFailed + noError);
    }

    @Test
    public void TC_shallVerifyTotals() throws Exception {
        ArrayList<String> aReports = new ArrayList<String>();
        for (String report : reports) {
            aReports.add(this.getClass().getResource("/test-reports/junit.reference/" + report).getFile().toString());
        }
        RunUnitTHUtils.runUnitTHWithJunitReports(aReports.toArray(new String[0]), dFlags);
        driver.get("file://" + System.getProperty("user.dir") + "/" + OUTPUT_FOLDER + "/main.html");
        HtmlReportAPI hrapi = new HtmlReportAPI(driver);
        int noExecTests = hrapi.getNoExecutedTestCasesStatsForMainPageTotals();
        int noPassed = hrapi.getNoPassedTestCasesStatsForMainPageTotals();
        int noFailed = hrapi.getNoFailedTestCasesStatsForMainPageTotals();
        int noError = hrapi.getNoErrorTestCasesStatsForMainPageTotals();
        assertEquals("checking no executed test cases in a package list row ", 854, noExecTests);
        assertEquals("checking no passed test cases in a package list row ", 667, noPassed);
        assertEquals("checking no error test cases in a package list row ", 0, noError);
        assertEquals("checking no failed test cases in a package list row ", 187, noFailed);
        assertEquals("checking no ignored test cases in a package list row ", 91, hrapi.getNoIgnoredTestCasesStatsForMainPageTotals());
        assertEquals("checking pass rate in a package list row ", 78.10, hrapi.getPassRateStatsForMainPageTotals(), 0.01);
        assertEquals("no executed tests shall be equal to passed+fail+error", noExecTests, noPassed + noFailed + noError);
    }
}
