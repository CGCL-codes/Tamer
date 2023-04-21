package com.aqua.sanity.report.publish;

import jsystem.extensions.analyzers.text.FindText;
import jsystem.extensions.analyzers.text.TextNotFound;
import jsystem.guiMapping.JsystemMapping;
import jsystem.utils.UploadRunner;
import org.openqa.selenium.server.SeleniumServer;
import utils.ScenarioUtils;
import com.aqua.general.JSysTestCaseOld;
import com.aqua.jsystemobject.CreateEnvFixtureOld;
import com.aqua.jsystemobject.ReportSystem;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * This class run the runner and the selenium objects.<br> 
 * adds report functionality tests,<br>
 * publish them with the "add publish result event" tab and checks the
 * result in the report server with the help of the selenium object.
 * 
 * @author chen n.
 * 
 */
public class PublishEventTest extends JSysTestCaseOld {

    ReportSystem reportSystem;

    int minutes = 1200;

    int runs = 3;

    String templateUserMonitorName = "t";

    String publisherUserMonitorName = "u";

    final String classWithTests = "ReporterTestsToPublish";

    int deleteEachNRuns = 1;

    String description = "DetailedDescription";

    String version = "DetailedVersion";

    String build = "DetailedBuild";

    JsystemMapping jmap = new JsystemMapping();

    private int numOfPublishEventsToRun = 5;

    private static final String MAX_WAIT_TIME_IN_MS = "60000";

    private Selenium selenium;

    private SeleniumServer seleniumServer;

    public PublishEventTest() throws Exception {
        super();
        setFixture(CreateEnvFixtureOld.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        String foxBinPath = " ";
        if ("Linux".equalsIgnoreCase(System.getProperty("os.name"))) {
            foxBinPath = sut().getValue("/sut/firefoxPath/text()");
            selenium = new DefaultSelenium("localhost", 4444, "*firefox " + foxBinPath, reportsUrl());
        } else {
            selenium = new DefaultSelenium("localhost", 4444, "*firefox", reportsUrl());
        }
        jsystem.launch();
        jsystem.initReporters();
        seleniumServer = new SeleniumServer();
        seleniumServer.start();
        sleep(2000);
        report.report(reportsUrl());
        selenium.start();
    }

    public void tearDown() throws Exception {
        sleep(2000);
        selenium.stop();
        seleniumServer.stop();
        sleep(2000);
        super.tearDown();
    }

    /**
	 * test publish event - This test check that the publish event is really
	 * publish one test result with publish parameters<br>
	 * 1. creates a scenario and run it. <br>
	 * 2. check if the publish has been done.<br>
	 * 3. verify correct data on JReporter.
	 * 
	 * @throws Exception
	 * @params.exclude	numOfPublishEventsToRun
	 */
    public void testAddPublishEventWithParameters() throws Exception {
        jsystem.launch();
        jsystem.initReporters();
        deleteRuns();
        ScenarioUtils.createAndCleanScenario(jsystem, "ReportScenario");
        report.report("Running simple Publish Test");
        report.report("Parameters description=" + description + " version=" + version + " build=" + build);
        report.report("Add test - testReportWithMessage");
        jsystem.addTest("testReportWithMessage", "ReporterTests");
        jsystem.selectTest(1);
        jsystem.addPublishResultEvent();
        jsystem.setTestParameter(2, "General", "ActionType", "publish", true);
        jsystem.setTestParameter(2, "General", "InitReporter", "false", true);
        jsystem.setTestParameter(2, "General", "Build", build, false);
        jsystem.setTestParameter(2, "General", "Version", version, false);
        jsystem.setTestParameter(2, "Publish", "Description", description, false);
        jsystem.play();
        jsystem.waitForRunEnd();
        openReportPublishResultAndSetTestAgainst();
        analyzer.analyze(new FindText(version));
        analyzer.analyze(new FindText(build));
        analyzer.analyze(new FindText(description));
    }

    /**
	 * test publish event - This test check that the publish event is really
	 * publish one test result with empty parameters<br>
	 * 1. creates a scenarlocalio and run it.<br>
	 * 2. check if the publish has been done.<br>
	 * 3. verify all parameters are empty.
	 * 
	 * @throws Exception
	 * @params.exclude	numOfPublishEventsToRun
	 */
    public void testAddPublishEventWithoutParameters() throws Exception {
        jsystem.initReporters();
        deleteRuns();
        ScenarioUtils.createAndCleanScenario(jsystem, "ReportScenario");
        report.report("Running simple Publish Test");
        report.report("Add test - testReportWithMessage");
        jsystem.addTest("testReportWithMessage", "ReporterTests");
        jsystem.selectTest(1);
        jsystem.addPublishResultEvent();
        jsystem.setTestParameter(2, "General", "ActionType", "publish", true);
        jsystem.setTestParameter(2, "General", "InitReporter", "false", true);
        jsystem.setTestParameter(2, "General", "Build", "", false);
        jsystem.setTestParameter(2, "General", "Version", "", false);
        jsystem.setTestParameter(2, "Publish", "Description", "", false);
        jsystem.play();
        jsystem.waitForRunEnd();
        openReportPublishResultAndSetTestAgainst();
        analyzer.analyze(new TextNotFound(version));
        analyzer.analyze(new TextNotFound(build));
        analyzer.analyze(new TextNotFound(description));
    }

    /**
	 * open JReporter using the selenium, press main button and set the HTML source as testAgainst
	 * 
	 * @throws Exception
	 */
    public void openReportPublishResultAndSetTestAgainst() throws Exception {
        selenium.open(reportsUrl());
        selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
        analyzer.setTestAgainstObject(selenium.getHtmlSource());
        analyzer.analyze(new FindText("www.aquasw.com"));
        selenium.selectWindow("null");
        selenium.click("//a/img");
        selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
        analyzer.setTestAgainstObject(selenium.getHtmlSource());
    }

    /**
	 * This test check that the publish event "InitReporter" cleans the main xml file (reports.0.xml).<br>
	 * 1. The test creates a scenario and runs it, this is done to make sure that the report
	 * is not empty.<br>
	 * 2. The test then activates the init report and verify that the
	 * report is empty.
	 * 
	 * @params.exclude	numOfPublishEventsToRun
	 */
    public void testPublishEventWithInit() throws Exception {
        deleteRuns();
        ScenarioUtils.createAndCleanScenario(jsystem, jsystem.getCurrentScenario());
        jsystem.addTest("testShouldPass", "GenericBasic");
        jsystem.selectTest(1);
        jsystem.addPublishResultEvent();
        jsystem.setTestParameter(2, "General", "ActionType", "publish", true);
        jsystem.setTestParameter(2, "General", "InitReporter", "true", true);
        jsystem.setTestParameter(2, "General", "Build", build, false);
        jsystem.setTestParameter(2, "General", "Version", version, false);
        jsystem.setTestParameter(2, "Publish", "Description", description, false);
        jsystem.play();
        jsystem.waitForRunEnd();
        if (jsystem.checkReportIsEmpty()) {
            report.report("Init report succeeded ,the log report.0.xml is empty from tests");
        } else {
            report.report("Init report failed", false);
        }
    }

    /**
	 * This test check that the publish event is really
	 * publish ${numOfPublishEventsToRun} times.<br>
	 * 1. creates a scenario and put ${numOfPublishEventsToRun} and after every test put publish event 
	 * and run it. <br>
	 * 2. check if the publish has been done ${numOfPublishEventsToRun} times with different publish parameters
	 * 
	 * @throws Exception
	 * 
	 */
    public void testThatNumberOfPublishedEventsHaveBeenPublished() throws Exception {
        jsystem.initReporters();
        deleteRuns();
        ScenarioUtils.createAndCleanScenario(jsystem, "ReportScenario");
        report.report("Running simple Publish Test");
        report.report("Parameters description=" + description + " version=" + version + " build=" + build);
        report.report("Add test - testReportWithMessage");
        for (int i = 0; i < getNumOfPublishEventsToRun(); i++) {
            report.step("publish run " + i);
            jsystem.addTest("testReportWithMessage", "ReporterTests");
            jsystem.selectTest(i * 2 + 1);
            jsystem.addPublishResultEvent();
            jsystem.setTestParameter(i * 2 + 2, "General", "ActionType", "publish", true);
            jsystem.setTestParameter(i * 2 + 2, "General", "InitReporter", "false", true);
            jsystem.setTestParameter(i * 2 + 2, "General", "Build", build + i, false);
            jsystem.setTestParameter(i * 2 + 2, "General", "Version", version + i, false);
            jsystem.setTestParameter(i * 2 + 2, "Publish", "Description", description + i, false);
            report.step("publish run end " + (i * 2 + 2));
        }
        jsystem.play();
        jsystem.waitForRunEnd();
        openReportPublishResultAndSetTestAgainst();
        for (int i = 0; i < getNumOfPublishEventsToRun(); i++) {
            analyzer.analyze(new FindText(version + i));
            analyzer.analyze(new FindText(build + i));
            analyzer.analyze(new FindText(description + i));
        }
    }

    /**
	 * This test check that add a property to summary with properties file<br> 
	 * add "Summary.getInstance().setProperty(key,value)" to a test code,<br>
	 * when key can be "Version","Description","build".<br>
	 * publish event is really publish one test result .<br>
	 * 1. creates a scenario with "testAddSummaryValues" and run it.<br> 
	 * 2. check if the publish has been done.<br>
	 * 3. verify parameters values.
	 * 
	 * @throws Exception
	 * @params.exclude numOfPublishEventsToRun
	 */
    public void testAddPublishEventWithParametersUsingSummary() throws Exception {
        jsystem.initReporters();
        deleteRuns();
        ScenarioUtils.createAndCleanScenario(jsystem, "ReportScenario");
        report.report("Running simple Publish Test");
        report.report("Parameters description=" + description + " version=" + version + " build=" + build);
        report.report("Add test - testReportWithMessage");
        jsystem.addTest("testReportWithMessage", "ReporterTests");
        jsystem.selectTest(1);
        jsystem.addTest("testAddSummaryValues", "AddPublishSummaryValues");
        jsystem.selectTest(2);
        jsystem.addPublishResultEvent();
        jsystem.setTestParameter(3, "General", "ActionType", "publish", true);
        jsystem.setTestParameter(3, "General", "InitReporter", "false", true);
        jsystem.play();
        jsystem.waitForRunEnd();
        openReportPublishResultAndSetTestAgainst();
        analyzer.analyze(new FindText("SummaryVersion"));
        analyzer.analyze(new FindText("SummaryBuild"));
        analyzer.analyze(new FindText("SummaryDescription"));
        jsystem.exit();
    }

    /**
	 * This method set a combo option. The method also checks if the combo was successfully set
	 * and if not will activate a retry
	 * @param combo
	 * @param value
	 * @throws Exception
	 */
    public void setCombo(String combo, String value) throws Exception {
        int i = 0;
        report.report("Seting value -" + value + "-to combo-" + combo);
        selenium.getBodyText();
        selenium.select(combo, "label=" + value);
        while (!value.equals(selenium.getSelectedValue(combo)) && i < 2) {
            selenium.select(combo, "label=" + value);
            Thread.sleep(1000);
            i++;
        }
    }

    /**
	 * deletes all runs in JReporter (clears DB)
	 * 
	 * @throws Exception
	 */
    private void deleteRuns() throws Exception {
        selenium.open(reportsUrl());
        selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
        analyzer.setTestAgainstObject(selenium.getHtmlSource());
        analyzer.analyze(new FindText("www.aquasw.com"));
        selenium.selectWindow("null");
        selenium.click("//a/img");
        selenium.waitForPageToLoad(MAX_WAIT_TIME_IN_MS);
        selenium.click("checkBoxControl");
        this.setCombo(jmap.getRunsOptionCombo(), jmap.getRunsDeleteComboOption());
        selenium.click(jmap.getRunsSubmitButton());
        selenium.stop();
        selenium.start();
        selenium.open(reportsUrl());
        analyzer.setTestAgainstObject(selenium.getHtmlSource());
        analyzer.analyze(new TextNotFound(description));
    }

    public int getNumOfPublishEventsToRun() {
        return numOfPublishEventsToRun;
    }

    /**
	 * the number of tests and publish events that will be added to scenario
	 * 
	 * @param numOfPublishEventsToRun
	 */
    public void setNumOfPublishEventsToRun(int numOfPublishEventsToRun) {
        this.numOfPublishEventsToRun = numOfPublishEventsToRun;
    }

    /**
	  * get the report url
     */
    private String reportsUrl() throws Exception {
        return UploadRunner.getReportsApplicationUrl() + "/";
    }
}
