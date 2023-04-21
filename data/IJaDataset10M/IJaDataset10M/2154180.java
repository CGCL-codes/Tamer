package nl.BobbinWork.testutils.parameterized;

import static nl.BobbinWork.testutils.Assert.*;
import java.util.Arrays;
import org.junit.Ignore;
import org.junit.runners.Parameterized;

/**
 * Represents the test parameters fed to one run of a {@link Parameterized}
 * test.
 * 
 * @author J. Pol
 */
@Ignore("This is a test tool, not a tester")
public class HappyTestRunParameters extends TestRunParameters {

    private final Object[] expectedResult;

    private final Double[] deltas;

    /**
   * Constructs a an object for a test run that does <i>not</i> throw an
   * exception.
   * 
   * @param description
   *          a description and/or unique tag of this test run
   * @param input
   *          the inputs of the test run
   * @param expectedResult
   *          the expected result of the test run
   * @param deltas
   *          each non null element triggers an assertEquals of doubles with a
   *          tolerance
   */
    HappyTestRunParameters(final String description, final Object[] input, final Object[] expectedResult, final Double... deltas) {
        super(description, input);
        this.expectedResult = expectedResult;
        this.deltas = deltas;
    }

    @Override
    public String toString() {
        final StringBuffer message = new StringBuffer();
        message.append(super.toString());
        if (deltas != null && deltas.length > 0) {
            message.append(NEW_LINE);
            message.append("tolerance: ");
            message.append(Arrays.deepToString(deltas));
        }
        message.append(NEW_LINE);
        message.append("expected: ");
        message.append(Arrays.deepToString(expectedResult));
        return message.toString();
    }

    @Override
    void assertResult(Object[] actual) {
        String message = toString() + NEW_LINE + "got: " + Arrays.deepToString(actual);
        assertArraysEqualsTolerant(message, expectedResult, actual, deltas);
    }

    @Override
    void assertResult(Exception exception) {
        fail(toString(), exception);
    }
}
