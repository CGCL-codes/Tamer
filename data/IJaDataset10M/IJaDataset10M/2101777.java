package org.openxava.test.tests;

/**
 * @author Javier Paniza
 */
public class TransportChargesWithDistanceTest extends TransportChargesTestBase {

    public TransportChargesWithDistanceTest(String testName) {
        super(testName, "TransportChargesWithDistance");
    }

    public void testValidValueInSecondLevelInList() throws Exception {
        deleteAll();
        createSome();
        execute("List.filter");
        assertListRowCount(2);
    }
}
