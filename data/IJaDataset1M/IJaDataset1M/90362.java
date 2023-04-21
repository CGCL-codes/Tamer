package org.peaseplate.oldtests;

import org.testng.annotations.Test;

@Test
public class InvocationParameterTest extends AbstractTemplateTestCase {

    public class WorkingObject {

        public String method() {
            return "method()";
        }

        public String method(final String value) {
            return "method(String value)";
        }

        public String method(final String value, final String value2, final String value3) {
            return "method(String value, String value2, String value3)";
        }

        public String method(final String value, final String value2, final String value3, final String... others) {
            return "method(String value, String value2, String value3, String... others)";
        }

        public String method2(final String... parameters) {
            return "method2(String... parameters)";
        }

        public String method3(final String value, final String value2) {
            return "method3(String value, String value2) where the values are " + value + " and " + value2;
        }
    }

    @Test
    public void automatic() throws Exception {
        test("org/peaseplate/language/invocationParameterTest.xml", new WorkingObject());
    }
}
