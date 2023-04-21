package com.manning.junitbook.ch12.selenium;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.server.SeleniumServer;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * Subclasses inherit a managed Selenium server.
 * <p>
 * Maven will not pick up this class because it does not start or end with
 * "Test" or end with "TestCase".
 * </p>
 * 
 * @author <a href="mailto:ggregory@apache.org">Gary Gregory</a>
 * @version $Id$
 */
public class ManagedSeleniumServer {

    protected static Selenium selenium;

    private static SeleniumServer seleniumServer;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        ManagedSeleniumServer.startSeleniumServer();
        ManagedSeleniumServer.startSeleniumClient();
    }

    public static void startSeleniumClient() throws Exception {
        selenium = new DefaultSelenium("localhost", 4444, "*iexplore", "http://www.google.com/");
        selenium.start();
    }

    public static void startSeleniumServer() throws Exception {
        seleniumServer = new SeleniumServer();
        seleniumServer.start();
    }

    public static void stopSeleniumClient() throws Exception {
        if (selenium != null) {
            selenium.stop();
            selenium = null;
        }
    }

    public static void stopSeleniumServer() throws Exception {
        if (seleniumServer != null) {
            seleniumServer.stop();
            seleniumServer = null;
        }
    }

    @AfterClass
    public static void tearDownOnce() throws Exception {
        ManagedSeleniumServer.stopSeleniumClient();
        ManagedSeleniumServer.stopSeleniumServer();
    }

    protected void captureScreenshot(Throwable t) throws Throwable {
        if (selenium != null) {
            String filename = this.getClass().getName() + ".png";
            try {
                selenium.captureScreenshot(filename);
                System.err.println("Saved screenshot " + filename + " for " + t.toString());
            } catch (Exception e) {
                System.err.println("Exception saving screenshot " + filename + " for " + t.toString() + ": " + e.toString());
                e.printStackTrace();
            }
            throw t;
        }
    }
}
