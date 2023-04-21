package jsystem.extensions.reporter;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import jsystem.framework.FrameworkOptions;
import jsystem.framework.JSystemProperties;
import jsystem.framework.report.Reporter.EnumReportLevel;
import junit.framework.Assert;
import junit.framework.SystemTestCase4;

/**
 * This class is a test for method getCurrentTestFileName 
 * in HtmlReporterUtils utility class
 * 
 * @see HtmlReporterUtils
 * 
 * @author optier
 *
 */
public class TestReportForGetCurrentTestFileName extends SystemTestCase4 {

    public TestReportForGetCurrentTestFileName() {
        super();
    }

    /**
	 * Check that report file start with report6.html
	 */
    @Test
    public void checkGetCurrentTestFileName1() {
        String currentHtmlFileName = "report6.html";
        assertCurrentTestFileName(currentHtmlFileName);
    }

    /**
	 * Check that next report file is report8.html
	 */
    @Test
    public void checkGetCurrentTestFileName2() {
        String currentHtmlFileName = "report8.html";
        assertCurrentTestFileName(currentHtmlFileName);
    }

    /**
	 * Check that start level open new report html file, but method
	 * getCurrentTestFileName still point to main html test file.
	 * 
	 * @throws IOException
	 */
    @Test
    public void checkGetCurrentTestFileName3() throws IOException {
        String currentHtmlFileName = "report10.html";
        assertCurrentTestFileName(currentHtmlFileName);
        report.startLevel("Level 1", EnumReportLevel.CurrentPlace);
        assertCurrentTestFileName(currentHtmlFileName);
        report.report("report 1");
        assertCurrentTestFileName(currentHtmlFileName);
        report.stopLevel();
        assertCurrentTestFileName(currentHtmlFileName);
    }

    /**
	 * Check that start level open new report html file, but method
	 * getCurrentTestFileName still point to main html test file.
	 * 
	 * This time test is using 2 leveling.
	 * 
	 * @throws IOException
	 */
    @Test
    public void checkGetCurrentTestFileName4() throws IOException {
        String currentHtmlFileName = "report13.html";
        assertCurrentTestFileName(currentHtmlFileName);
        report.startLevel("Level 1", EnumReportLevel.CurrentPlace);
        assertCurrentTestFileName(currentHtmlFileName);
        report.report("report 1");
        assertCurrentTestFileName(currentHtmlFileName);
        report.stopLevel();
        assertCurrentTestFileName(currentHtmlFileName);
        report.startLevel("Level 2", EnumReportLevel.CurrentPlace);
        assertCurrentTestFileName(currentHtmlFileName);
        report.report("report 2");
        assertCurrentTestFileName(currentHtmlFileName);
        report.stopLevel();
        assertCurrentTestFileName(currentHtmlFileName);
    }

    /**
	 * Check that html file name is still correct after using 
	 * leveling in previous tests.
	 *  
	 */
    @Test
    public void checkGetCurrentTestFileName5() {
        String currentHtmlFileName = "report17.html";
        assertCurrentTestFileName(currentHtmlFileName);
        report.report("report 1");
        assertCurrentTestFileName(currentHtmlFileName);
    }

    /**
	 * Assert current running test html file with an expected html
	 * file name.
	 * 
	 * @param expectedfileName expected report html file
	 */
    private void assertCurrentTestFileName(String expectedfileName) {
        String baseLogFolder = JSystemProperties.getInstance().getPreference(FrameworkOptions.LOG_FOLDER) + File.separator + "current";
        String expectedCurrentTestFileName = baseLogFolder + File.separator + expectedfileName;
        String actualCurrentTestFileName = HtmlReporterUtils.getCurrentTestFileName();
        report.report("Expected Current Test File Name: " + expectedCurrentTestFileName);
        report.report("Actual Current Test File Name: " + actualCurrentTestFileName);
        Assert.assertEquals(expectedCurrentTestFileName, actualCurrentTestFileName);
    }
}
