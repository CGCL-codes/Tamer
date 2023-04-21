package examples.basic;

import examples.QuickenExampleTestCase;

/**
 * Test Case for the Basic Example.
 *
 * @author <a href="mailto:baerrach@gmail.com">Barrie Treloar</a>
 * @version 0.7.0
 */
public class BasicExampleTest extends QuickenExampleTestCase {

    /**
     * Test for the example.
     *
     * @throws Exception
     *             test failures.
     */
    public void testExample() throws Exception {
        runExample("BasicExampleTest", "/examples/basic/basic.qif", null, "/examples/basic/expected_output.qif");
    }
}
