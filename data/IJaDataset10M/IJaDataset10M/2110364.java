package com.biviz.jgooglegears.jdbc;

import com.biviz.jgooglegears.JGGStubApplet;
import java.applet.Applet;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.JUnit4TestAdapter;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 *
 * @author Administrator
 */
public class RunTestApplet extends Applet {

    public String getAppletUUID() {
        String uuid = this.getParameter(JGGStubApplet.APPLET_INST_UUID_KEY);
        return uuid;
    }

    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    public void init() {
    }

    public void start() {
        System.out.println("JUnit starting ...");
        junitTesting();
    }

    private void junitTesting() {
        Result result = JUnitCore.runClasses(JGoogleGearsDriverTest.class);
        if (result.wasSuccessful()) {
            System.out.println("Success");
        } else {
            System.out.println("Failed");
            List<Failure> failures = result.getFailures();
            for (Failure eachFailure : failures) {
                System.out.println(eachFailure.toString());
            }
        }
    }

    private void appletContextTesting() {
        try {
            Enumeration<Applet> appletEnum = this.getAppletContext().getApplets();
            Driver driver = (Driver) this.getAppletContext().getApplet("com_biviz_jgooglegears_jdbc_JGoogleGearsDriverApplet");
            Connection conn = driver.connect("jdbc:jgooglegears:database-test", null);
        } catch (SQLException ex) {
            Logger.getLogger(RunTestApplet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.biviz.jgooglegears.jdbc.JGoogleGearsDriverApplet");
        new RunTestApplet().start();
    }
}
