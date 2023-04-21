package com.pureperfect.purview.validators.text;

import java.lang.reflect.Method;
import junit.framework.TestCase;
import com.pureperfect.purview.Purview;

/**
 * Unit test for {@link AllowOnly} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.3
 * @since 1.3
 */
public class AllowOnlyTest extends TestCase {

    public class Stub {

        @AllowOnly({ "Male", "Female" })
        private String value;

        @AllowOnly({ "Male", "Female" })
        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void foo(@AllowOnly({ "Male", "Female" }) String bar) {
        }
    }

    public class RequiredStub {

        @AllowOnly(value = "foo", required = true)
        public String value;
    }

    public class IgnoreCaseStub {

        @AllowOnly(value = "foo", ignoreCase = true)
        public String value;
    }

    public void testField() throws Exception {
        Stub mock = new Stub();
        assertEquals(0, Purview.validateFields(mock).getProblems().size());
        mock.setValue("woot");
        assertEquals(1, Purview.validateFields(mock).getProblems().size());
        mock.setValue("Male");
        assertEquals(0, Purview.validateFields(mock).getProblems().size());
    }

    public void testMethod() throws Exception {
        Stub mock = new Stub();
        assertEquals(0, Purview.validateMethods(mock).getProblems().size());
        mock.setValue("woot");
        assertEquals(1, Purview.validateMethods(mock).getProblems().size());
        mock.setValue("Female");
        assertEquals(0, Purview.validateMethods(mock).getProblems().size());
    }

    public void testParameters() throws Exception {
        Stub mock = new Stub();
        Method m = mock.getClass().getMethod("foo", new Class[] { String.class });
        assertEquals(0, Purview.validateParameters(mock, m, new Object[] { null }).getProblems().size());
        assertEquals(1, Purview.validateParameters(mock, m, new Object[] { "woot" }).getProblems().size());
        assertEquals(0, Purview.validateParameters(mock, m, new Object[] { "Female" }).getProblems().size());
    }

    public void testRequired() {
        RequiredStub stub = new RequiredStub();
        assertEquals(1, Purview.validateFields(stub).getProblems().size());
        stub.value = "woot";
        assertEquals(1, Purview.validateFields(stub).getProblems().size());
        stub.value = "foo";
        assertEquals(0, Purview.validateFields(stub).getProblems().size());
    }

    public void testIgnoreCase() {
        IgnoreCaseStub stub = new IgnoreCaseStub();
        assertEquals(0, Purview.validateFields(stub).getProblems().size());
        stub.value = "foo";
        assertEquals(0, Purview.validateFields(stub).getProblems().size());
        stub.value = "Foo";
        assertEquals(0, Purview.validateFields(stub).getProblems().size());
        stub.value = "FOO";
        assertEquals(0, Purview.validateFields(stub).getProblems().size());
        stub.value = "bar";
        assertEquals(1, Purview.validateFields(stub).getProblems().size());
    }
}
