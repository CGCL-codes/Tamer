package org.jperf.examples;

import junit.framework.TestCase;
import org.jperf.PerfTestRunner;
import org.jperf.example.FastDateFormatTest;
import org.jperf.example.NewDateFormatTest;
import org.jperf.example.SynchronizedDateFormatTest;
import org.jperf.example.ThreadLocalDateFormatTest;

/**
 * @author Andy Grove
 */
public class ExamplesTest extends TestCase {

    public ExamplesTest(String s) {
        super(s);
    }

    /**
     * Run performance test against SimpleDateFormatTest.
     *
     * @throws Exception
     */
    public void testNewDateFormatTest() throws Exception {
        runPerfTest(NewDateFormatTest.class);
    }

    /**
     * Run performance test against SimpleDateFormatTest.
     *
     * @throws Exception
     */
    public void testSynchronizedSimpleDateFormat() throws Exception {
        runPerfTest(SynchronizedDateFormatTest.class);
    }

    /**
     * Run performance test against SimpleDateFormatTest.
     *
     * @throws Exception
     */
    public void testThreadLocalDateFormat() throws Exception {
        runPerfTest(ThreadLocalDateFormatTest.class);
    }

    /**
     * Run performance test against FastDateFormat.
     *
     * @throws Exception
     */
    public void testFastDateFormat() throws Exception {
        runPerfTest(FastDateFormatTest.class);
    }

    /**
     * Convenience method for running performance tests.
     * 
     * @param theClass
     * @throws Exception
     */
    private void runPerfTest(Class theClass) throws Exception {
        PerfTestRunner perfTest = new PerfTestRunner();
        perfTest.setMaxClient(10);
        perfTest.setTestPeriod(500);
        perfTest.run(theClass);
    }
}
