package org.fest.swing.edt;

import org.testng.annotations.Test;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.test.core.CommonAssertions.failWhenExpectingException;
import static org.fest.util.Strings.concat;

/**
 * Tests for <a href="http://code.google.com/p/fest/issues/detail?id=247" target="_blank">Bug 247</a>.
 *
 * @author Alex Ruiz
 */
@Test
public class Bug247_NotEnoughInfoInFailureInEDTTest {

    private static String TEST_NAME = Bug247_NotEnoughInfoInFailureInEDTTest.class.getName();

    public void shouldShowMethodCallInCurrentThreadWhenFailingInEDT() {
        boolean testClassInStackTrace = false;
        try {
            execute(new GuiTask() {

                protected void executeInEDT() {
                    throw new RuntimeException("Thrown on purpose");
                }
            });
            failWhenExpectingException();
        } catch (RuntimeException e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            StackTraceElement first = stackTrace[0];
            assertThat(first.getClassName()).isEqualTo(concat(TEST_NAME, "$1"));
            assertThat(first.getMethodName()).isEqualTo("executeInEDT");
            String expected = Bug247_NotEnoughInfoInFailureInEDTTest.class.getName();
            for (StackTraceElement element : e.getStackTrace()) {
                if (!expected.equals(element.getClassName())) continue;
                testClassInStackTrace = true;
                break;
            }
        }
        assertThat(testClassInStackTrace).as("test class in stack trace").isTrue();
    }
}
