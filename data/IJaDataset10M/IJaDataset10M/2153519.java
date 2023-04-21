package org.jquantlib.performance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Q.Boiler
 */
public class PerformanceDriver {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceDriver.class);

    List<PerformanceTest> microscopicTests = new ArrayList<PerformanceTest>();

    List<PerformanceTest> macroscopicTests = new ArrayList<PerformanceTest>();

    List<PerformanceResults> microscopicResults = new ArrayList<PerformanceResults>();

    List<PerformanceResults> macroscopicResults = new ArrayList<PerformanceResults>();

    {
        populateMicroscopicTests();
        populateMacroscopicTests();
    }

    public void populateMicroscopicTests() {
    }

    public void populateMacroscopicTests() {
    }

    public static void main(String args[]) {
        PerformanceDriver instance = new PerformanceDriver();
        if (args.length == 0) {
            logger.info("------Microscopic----------");
            logger.info("---------------------------");
            execute(instance.microscopicTests, instance.microscopicResults);
            printResults(instance.microscopicResults);
            logger.info("------Macroscopic----------");
            logger.info("---------------------------");
            execute(instance.macroscopicTests, instance.macroscopicResults);
            printResults(instance.macroscopicResults);
        } else if (args[0].equalsIgnoreCase("microscopic")) {
            logger.info("------Microscopic----------");
            logger.info("---------------------------");
            execute(instance.microscopicTests, instance.microscopicResults);
            printResults(instance.microscopicResults);
        } else if (args[0].equalsIgnoreCase("macroscopic")) {
            logger.info("------Macroscopic----------");
            logger.info("---------------------------");
            execute(instance.macroscopicTests, instance.macroscopicResults);
            printResults(instance.macroscopicResults);
        } else {
            logger.info("Unrecognized option: " + args[0]);
        }
    }

    public static void execute(List<PerformanceTest> test, List<PerformanceResults> results) {
        for (Iterator<PerformanceTest> it = test.iterator(); it.hasNext(); ) {
            PerformanceTest performanceTest = it.next();
            results.add(performanceTest.execute());
        }
    }

    public static void printResults(List<PerformanceResults> results) {
        for (Iterator<PerformanceResults> it = results.iterator(); it.hasNext(); ) {
            PerformanceResults performanceResults = it.next();
            logger.info(performanceResults.toString());
            if (performanceResults.compositeResults != null) {
                logger.info("\t----------------Composite Tests----------------------------------");
                printResults(performanceResults.compositeResults);
                logger.info("\t----------------END Composite Tests----------------------------------");
            }
        }
    }
}
