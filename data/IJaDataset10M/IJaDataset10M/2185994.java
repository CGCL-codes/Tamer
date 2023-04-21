package saapa.test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;
import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;
import saapa.util.SaapaConfig;

/**
 *
 * @author dani
 */
public class BaseSeleniumTestCase extends DBTestCase {

    private static SeleniumServer seleniumServer;

    protected static Selenium selenium;

    protected boolean logged = false;

    private static boolean initiated = false;

    public BaseSeleniumTestCase(String name) {
        super(name);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.postgresql.Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:postgresql://localhost:5432/saapa");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "saapa");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "saapa");
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSet(getClass().getClassLoader().getResourceAsStream("dataset.xml"));
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        if (!SaapaConfig.isInited()) {
            SaapaConfig.init("web/", "WEB-INF/conf");
        }
        if (!initiated) {
            RemoteControlConfiguration rcc = new RemoteControlConfiguration();
            rcc.setPort(44444);
            seleniumServer = new SeleniumServer(false, rcc);
            seleniumServer.start();
            initiated = true;
        }
        selenium = new DefaultSelenium("localhost", 44444, System.getProperty("saapa.selenium.browser"), System.getProperty("saapa.url"));
        selenium.start();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        selenium.stop();
    }

    /**
     * Login into the system with the specified user information.
     *
     * @param login User login to log into the system.
     * @param password User password.
     */
    public void login(String login, String password) {
        selenium.open(SaapaConfig.getProperty("saapa.url"));
        waitForVisible("j_username");
        selenium.type("j_username", login);
        selenium.type("j_password", password);
        selenium.click("ok-login");
        logged = true;
    }

    /**
     * Logout from the system.
     */
    public void logout() {
        if (logged) {
            selenium.open(SaapaConfig.getProperty("saapa.url"));
            waitForVisible("logout");
            selenium.click("logout");
            waitForVisible("j_username");
            logged = false;
        }
    }

    @After
    public void finish() {
        if (logged) {
            logout();
        }
    }

    @Before
    public void init() {
        clearDB("build.xml");
    }

    public void clearDB(String buildPath) {
        File buildFile = new File(buildPath);
        Project project = new Project();
        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
        DefaultLogger consoleLogger = new DefaultLogger();
        consoleLogger.setErrorPrintStream(System.err);
        consoleLogger.setOutputPrintStream(System.out);
        consoleLogger.setMessageOutputLevel(Project.MSG_WARN);
        project.addBuildListener(consoleLogger);
        try {
            project.fireBuildStarted();
            project.init();
            ProjectHelper helper = ProjectHelper.getProjectHelper();
            project.addReference("ant.projectHelper", helper);
            helper.parse(project, buildFile);
            project.executeTarget("clear-db");
            project.fireBuildFinished(null);
        } catch (BuildException e) {
            project.fireBuildFinished(e);
        }
    }

    /**
     * Wait for the specified element to be visible in the page.
     * 
     * @param element Element to look for.
     */
    public void waitForVisible(final String element) {
        Wait wait = new Wait() {

            @Override
            public boolean until() {
                try {
                    return selenium.isVisible(element);
                } catch (Exception e) {
                    return false;
                }
            }
        };
        wait.wait("Couldn't find element with id or xpath='" + element + "'", 10000);
    }

    /**
     * Wait for the specified element to not be visible in the page.
     * 
     * @param element Element to look for.
     */
    public void waitForNotVisible(final String element) {
        Wait wait = new Wait() {

            @Override
            public boolean until() {
                try {
                    return !selenium.isVisible(element);
                } catch (Exception e) {
                    return true;
                }
            }
        };
        wait.wait("element with id or xpath='" + element + "' was visible", 10000);
    }

    /**
     * Wait for the specified element to not present in the page.
     * 
     * @param element Element to look for.
     * @param message Message to be displayed if the element cannot be found.
     */
    public void waitForElementNotPresent(final String element, String message) {
        Wait wait = new Wait() {

            @Override
            public boolean until() {
                try {
                    return !selenium.isElementPresent(element);
                } catch (Exception e) {
                    return true;
                }
            }
        };
        wait.wait(message, 5000);
    }
}
