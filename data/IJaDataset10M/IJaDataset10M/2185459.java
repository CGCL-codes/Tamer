package org.openemergency;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A simple harness to run our tests.  Configures Log4J,
 * creates an ApplicationContext, retrieves a bean from Spring
 *
 * @author Timothy O'Brien
 */
public class TestRunner {

    private static Logger log;

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure(TestRunner.class.getResource("/log4j.properties"));
        log = Logger.getLogger(TestRunner.class);
        log.info("Initializing TestRunner...");
        log.info("Loading Spring Configuration...");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        String testName = args[0];
        log.info("Running test: " + testName);
        Test test = (Test) context.getBean(testName);
        test.run();
    }
}
