package examples.payee_to_multiple;

import examples.QuickenExampleTestCase;

/**
 * Test Case for the Payee to Multiple Trasnformation Example.
 *
 * @author <a href="mailto:baerrach@gmail.com">Barrie Treloar</a>
 * @version 0.7.0
 */
public class PayeeToMultipleExampleTest extends QuickenExampleTestCase {

    /**
     * Test for the example.
     *
     * @throws Exception
     *             test failures.
     */
    public void testExample() throws Exception {
        runExample("PayeeToMultipleExampleTest", "/examples/payee_to_multiple/payee_to_multiple.qif", new String[] { "/examples/payee_to_multiple/payee_to_multiple.xsl" }, "/examples/payee_to_multiple/expected_output.qif");
    }
}
