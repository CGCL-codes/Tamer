package it.unibo.lmc.pjdbc.driver;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;
import junit.framework.Test;
import junit.framework.TestSuite;

public class DriverTests {

    public static Test suite() throws ClassNotFoundException {
        Class.forName("it.unibo.lmc.pjdbc.driver.PrologDriver");
        Properties properties = new Properties();
        String userDir = System.getProperty("user.dir");
        File propFile = new File(userDir + "/target/classes/common.properties");
        if (propFile.exists()) {
            try {
                properties.load(new FileInputStream(propFile));
            } catch (Exception e) {
                System.out.println("><" + e.getLocalizedMessage());
            }
        }
        PropertyConfigurator.configure(properties);
        TestSuite suite = new TestSuite("Test for it.unibo.lmc.pjdbc.driver");
        suite.addTestSuite(PrologConnectionTest.class);
        suite.addTestSuite(PrologResultSetMetaDataTest.class);
        return suite;
    }
}
