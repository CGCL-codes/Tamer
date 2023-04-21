package test;

import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Core helper for tests.
 * 
 * @author Julien Chable
 * @version 1.0
 */
public class TestCore {

    private String testRootPath;

    /**
	 * All sample document are normally located at this place.
	 */
    private static String pathRootProject;

    /**
	 * Demo logger
	 */
    private static Logger logger = Logger.getLogger("org.openxml4j.test");

    static {
        pathRootProject = System.getProperty("user.dir") + File.separator + "bin";
        PropertyConfigurator.configure(pathRootProject + File.separator + "config.log4j");
    }

    /**
	 * Constructor. Initialize the demo.
	 * 
	 */
    public TestCore(Class cl) {
        init(cl);
    }

    /**
	 * Initialize the test root path
	 */
    public void init(Class cl) {
        String packageName = cl.getPackage().getName();
        String sep = File.separator;
        if (sep.equals("\\")) {
            sep = "\\\\";
        }
        testRootPath = pathRootProject + File.separator + packageName.replaceAll("\\.", sep) + File.separator;
    }

    /**
	 * Gets the test root path.
	 * 
	 * @return The test root path.
	 */
    public String getTestRootPath() {
        return testRootPath;
    }

    /**
	 * Sets the test root path.
	 * 
	 * @param testRoot
	 */
    public void setTestRootPath(String testRoot) {
        this.testRootPath = testRoot;
    }

    /**
	 * @return the logger
	 */
    public static Logger getLogger() {
        return logger;
    }
}
