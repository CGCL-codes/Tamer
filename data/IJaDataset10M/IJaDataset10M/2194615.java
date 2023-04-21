package bg.unisofia.emaria.controller;

import com.jcorporate.expresso.services.test.ControllerTestCase;
import com.jcorporate.expresso.services.test.ControllerTestSuite;
import com.jcorporate.expresso.core.ExpressoSchema;
import com.jcorporate.expresso.core.controller.ControllerResponse;

import org.apache.log4j.Logger;
import org.apache.cactus.WebRequest;

import bg.unisofia.emaria.EMariaSchema;

import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.io.*;
import java.util.Properties;

/**
 * @author Ivan Ivanov
 * Date 2004-4-30
 * Time: 14:39:57
 */
public class AdministrativeControllerTest extends ControllerTestCase {

    /**
     * The logger for the test
     */
    private static Logger logger = Logger.getLogger(AdministrativeControllerTest.class);

    /**
     * Constucts an instance of the test
     * @param testName the name of the test
     * @throws Exception if the test cannot be constructed
     */
    public AdministrativeControllerTest(String testName) throws Exception {
        super(testName, AdministrativeController.class);
        if (logger.isDebugEnabled()) {
            logger.debug("Test constructed with name " + testName);
        }
    }

    /**
     * Sets necessary parameters for showAllUsers state in the request
     * @param webRequest the WebRequest where parameters are set
     * @see org.apache.cactus.WebRequest
     */
    public void beginShowAllUsers(WebRequest webRequest) {
        logger.debug("setting request parameters for showAllUsers test");
        try {
            super.logIn(webRequest, "EmrAdmin", "emr123");
            super.setupParameters("showAllUsers", webRequest);
            webRequest.
        } catch (Exception e) {
            logger.error("Test Failed", e);
            fail(e.getMessage());
        }
    }

    /**
     * Starts the test against showAllUsers state
     */
    public void testShowAllUsers() {
        logger.debug("beginning showAllUsers test");
        try {
            ControllerResponse cresponse = super.controllerProcess();
        } catch(Exception e) {
            logger.error("Test Failed", e);
            fail(e.getMessage());
        }
    }

    /**
     * Sets necessary parameters for addUser state in the request
     * @param webRequest the WebRequest where parameters are set
     * @see org.apache.cactus.WebRequest
     */
    public void beginAddUser(WebRequest webRequest) {
        logger.debug("setting request parameters for addUser test");
        String userHome = System.getProperty("user.home");
        String fs = System.getProperty("file.separator");
        File f = new File(userHome + fs + ".idcounter.properties");
        InputStream is = null;
        OutputStream os = null;
        try {
            super.logIn(webRequest, "EmrAdmin", "emr123");
            super.setupParameters("addUser", webRequest);

            is = new FileInputStream(f);
            Properties p = new Properties();
            p.load(is);
            int nextId = Integer.parseInt(p.getProperty("test.adduser.userId")) + 1;
            p.setProperty("test.adduser.userId", "" + nextId);
            os = new FileOutputStream(f);
            p.store(os, "");

            if (logger.isDebugEnabled()) {
                logger.debug("Adding parameters with id " + nextId);
            }

            webRequest.addParameter("loginname", "Rambo" + nextId);
            webRequest.addParameter("password", "Password" + nextId);
            webRequest.addParameter("email", nextId + "rambo@rambo.org");
            webRequest.addParameter("firstname", "Rambo" + nextId);
            webRequest.addParameter("middlename", "Comando" + nextId);
            webRequest.addParameter("lastname", "Terminator" + nextId);
            webRequest.addParameter("address", "Karlovo" + nextId);
            webRequest.addParameter("phone", "03356481" + nextId);
            webRequest.addParameter("mobile", "0888508938" + nextId);
            logger.debug("finished setting params for addUser");
        } catch (Exception e) {
            logger.error("Test Failed", e);
            fail(e.getMessage());
        }
    }

    /**
     * Starts the test against addUser state
     */
    public void testAddUser() {
        logger.debug("beginning addUser test");
        try {
            ControllerResponse response = super.controllerProcess();
        } catch (Exception e) {
            logger.error("Test Failed", e);
            fail(e.getMessage());
        }
    }

    /**
     * Constructs the test suite for this testcase
     * @return the suite with all tests in this testcase
     * @throws Exception
     */
    public static TestSuite suite() throws Exception {
        ControllerTestSuite cts = new ControllerTestSuite();
        cts.addReadOnlySchemaDependency(ExpressoSchema.class);
        cts.addReadOnlySchemaDependency(EMariaSchema.class);
        cts.addTestSuite(AdministrativeControllerTest.class);
        logger.debug("suite constructed");
        return cts;
    }

    public static void main(String[] args) throws Exception {
        logger.debug("suite will be invoked");
        TestRunner.run(suite());
    }
}
