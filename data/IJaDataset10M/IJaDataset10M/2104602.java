package org.callbackparams.internal;

import org.callbackparams.ParameterizedValue;
import org.callbackparams.internal.template.SuperClassWithMultipleConstructors;
import org.callbackparams.internal.template.TestrunCallbacks;
import org.callbackparams.junit4.CallbackParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * Test makes sure that super-class constructor is accessed as expected.
 *
 * @author Henrik Kaipe
 */
@RunWith(CallbackParamsRunner.class)
public class TestSuperClassWithMultipleConstructors extends SuperClassWithMultipleConstructors {

    static final String TEST_STRING = "test-string ...";

    @ParameterizedValue
    boolean dummyParam;

    public TestSuperClassWithMultipleConstructors() {
        super(TEST_STRING);
    }

    @Test
    public void test() {
        assertEquals("Super-class name", TestrunCallbacks.class.getName(), TestSuperClassWithMultipleConstructors.class.getSuperclass().getName());
        assertEquals("sValue", TEST_STRING, getsValue());
    }
}
