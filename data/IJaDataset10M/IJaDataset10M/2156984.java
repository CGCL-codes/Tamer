package simpatico.gwtlib.client.test;

/**
 * Logger interface.
 */
public interface TestSuiteListener {

    /**
     * Call on start.
     */
    void testStart();

    /**
     * Call on case start.
     * @param tCase test case
     */
    void testCaseStart(TestCase tCase);

    /**
     * Call on case finish succesfull.
     * @param tCase test case
     */
    void testCaseSuccess(TestCase tCase);

    /**
     * Call on  case finish fail.
     * @param tCase test case
     * @param th optional exception
     */
    void testCaseFailed(TestCase tCase, Throwable th);

    /**
     * Call on test finish.
     */
    void testFinished();
}
