package pgr.sample.pgrtest.client;

import com.google.gwt.core.client.EntryPoint;
import com.allen_sauer.gwt.log.client.Log;
import simpatico.gwtlib.client.test.TestCase;
import simpatico.gwtlib.client.test.TestSuiteListener;
import java.util.Date;

/**
 * Entry point for test sample.
 *
 * Date: 2008-09-30
 * Time: 14:18:06
 *
 * @author Pawel Majewski
 */
public final class PgrTest implements EntryPoint, TestSuiteListener {

    private int cases = 0;

    private int failed = 0;

    private Date startDate;

    private static final double MILES_IN_SECOND = 1000;

    /**
     * {@inheritDoc}
     */
    public void onModuleLoad() {
        Log.setUncaughtExceptionHandler();
        startDate = new Date();
        Log.info("Pgr test started");
        Log.info("Tests of primitives, strings and null values");
        PrimitiveStringNullTestSuite primitiveStringNullTestSuite = new PrimitiveStringNullTestSuite();
        primitiveStringNullTestSuite.setTestListener(this);
        primitiveStringNullTestSuite.startTest();
    }

    /**
     * {@inheritDoc}
     */
    public void testStart() {
    }

    /**
     * {@inheritDoc}
     */
    public void testCaseStart(final TestCase tCase) {
        cases++;
    }

    /**
     * {@inheritDoc}
     */
    public void testCaseSuccess(final TestCase tCase) {
        Log.info(" - " + tCase.getName() + " -  success!");
    }

    /**
     * {@inheritDoc}
     */
    public void testCaseFailed(final TestCase tCase, final Throwable th) {
        Log.error(" - " + tCase.getName() + " - failed!", th);
        failed++;
    }

    /**
     * {@inheritDoc}
     */
    public void testFinished() {
        Date now = new Date();
        double period = ((double) (now.getTime() - startDate.getTime())) / MILES_IN_SECOND;
        Log.info("Test finished in " + period + " seconds.");
        Log.info("Failed " + failed + " of " + cases + " cases.");
    }
}
