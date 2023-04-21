package org.hamcrest.examples.junit3;

import static org.hamcrest.JMock1Matchers.*;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * Demonstrates how HamCrest matchers can be used from jMock with JUnit 3.8.x.
 *
 * @author Joe Walnes
 */
public class ExampleWithJMock1 extends MockObjectTestCase {

    /**
     * A sample interface to be mocked.
     */
    public static interface AnInterface {

        void doStuff(String string);
    }

    private Mock mock = mock(AnInterface.class);

    private AnInterface anInterface = (AnInterface) mock.proxy();

    /**
     * This examples shows using a mock with a standard jMock constraint.
     * Hamcrest is not used here.
     */
    public void testUsingAJMockConstraint() {
        mock.expects(atLeastOnce()).method("doStuff").with(stringContains("cheese"));
        anInterface.doStuff("i like cheese and stuff");
    }

    /**
     * This examples shows using a mock with a Hamcrest matcher, adapted
     * to jMock.
     */
    public void testUsingAHamcrestMatcher() {
        mock.expects(atLeastOnce()).method("doStuff").with(equalTo("xx"));
        anInterface.doStuff("xx");
    }

    /**
     * This examples shows using the standard jMock assertThat() method
     * with both jMock Constraints and Hamcrest Matchers.
     */
    public void testUsingAssertThat() {
        assertThat("i like cheese", stringContains("cheese"));
        assertThat("xx", equalTo("xx"));
        assertThat("yy", not(equalTo("xx")));
    }
}
