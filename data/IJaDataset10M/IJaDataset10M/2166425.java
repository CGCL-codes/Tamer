package com.jes.classfinder.junit.suite;

import java.io.File;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.apache.log4j.Logger;
import org.hsqldb.Server;
import com.jes.classfinder.persistence.ClassDAOTest;
import com.jes.classfinder.persistence.ClassInJarDAOTest;
import com.jes.classfinder.persistence.DirectoryLocationDAOTest;
import com.jes.classfinder.persistence.JarDAOTest;

public class HsqlDBAllTestsOneTimeSetup {

    public static Logger logger = Logger.getLogger(HsqlDBAllTestsOneTimeSetup.class);

    protected static Server hsqlDatabaseServer;

    public static Test suite() {
        logger.debug("In suite() of HsqlDBAllTestsOneTimeSetup");
        TestSuite suite = new TestSuite();
        suite.addTest(ClassDAOTest.suite());
        suite.addTest(ClassInJarDAOTest.suite());
        suite.addTest(JarDAOTest.suite());
        suite.addTest(DirectoryLocationDAOTest.suite());
        TestSetup wrapper = new TestSetup(suite) {

            protected void setUp() {
                oneTimeSetUp();
            }

            protected void tearDown() {
                oneTimeTearDown();
            }
        };
        return wrapper;
    }

    public static Server startHsqlDatabase() {
        if (hsqlDatabaseServer != null) {
            hsqlDatabaseServer.stop();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
        String userDir = System.getProperty("user.home");
        File userDirFile = new File(userDir);
        File databaseDir = new File(userDirFile, "classfinder/hsqldb");
        if (databaseDir.exists()) {
            if (!databaseDir.delete()) {
                logger.error("Could not delete database");
            } else {
                if (!databaseDir.mkdir()) {
                    logger.error("Could not make database dir " + databaseDir.getAbsolutePath());
                }
            }
        }
        int databaseNumber = 0;
        String databasePath = (new File(databaseDir, "classfinder")).getAbsolutePath();
        String databaseName = "classfinder";
        hsqlDatabaseServer = new Server();
        hsqlDatabaseServer.setDatabasePath(databaseNumber, databasePath);
        hsqlDatabaseServer.setDatabaseName(0, databaseName);
        hsqlDatabaseServer.setTrace(true);
        hsqlDatabaseServer.start();
        return hsqlDatabaseServer;
    }

    public static Server stopHsqlDatabase() {
        hsqlDatabaseServer.stop();
        return hsqlDatabaseServer;
    }

    public static void oneTimeSetUp() {
        startHsqlDatabase();
    }

    public static void oneTimeTearDown() {
        stopHsqlDatabase();
    }

    public static void main(String args[]) {
        System.setProperty("log4j.configuration", "D:/workspace/ClassFinder/trunk/SVN_LOCAL/log4j/log4j.xml");
        TestRunner.run(HsqlDBAllTestsOneTimeSetup.suite());
    }
}
