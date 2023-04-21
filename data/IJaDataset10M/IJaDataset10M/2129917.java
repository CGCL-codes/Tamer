package org.easystub;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;
import org.easystub.exceptions.TestException;
import org.easystub.exceptions.TestExceptionWithStringArgument;
import org.easystub.exceptions.TestExceptionWithThrowableArgument;
import org.easystub.exceptions.TestExceptionWithLotsOfArguments;
import static org.hamcrest.CoreMatchers.instanceOf;

public class ThrowableFactoryTest {

    private ThrowableFactory testObj = ThrowableFactory.instance();

    @Test
    public void testCreateNewThrowableInstance() throws Throwable {
        Throwable instance = testObj.create(TestException.class);
        try {
            throw instance;
        } catch (TestException expected) {
        }
    }

    @Test
    public void testCreateNewThrowableInstanceWithStringConstructorArgument() throws Throwable {
        Throwable instance = testObj.create(TestExceptionWithStringArgument.class);
        try {
            throw instance;
        } catch (TestExceptionWithStringArgument expected) {
            assertEquals("Default Throwable created by EasyStub", expected.getMessage());
        }
    }

    @Test
    public void testCreateNewThrowableInstanceWithThrowableConstructorArgument() throws Throwable {
        Throwable instance = testObj.create(TestExceptionWithThrowableArgument.class);
        try {
            throw instance;
        } catch (TestExceptionWithThrowableArgument expected) {
            assertThat(expected.getCause(), instanceOf(EasyStubCreatedException.class));
        }
    }

    @Test
    public void testCreateNewThrowableInstanceWithLotsOfArguments() throws Throwable {
        Throwable instance = testObj.create(TestExceptionWithLotsOfArguments.class);
        try {
            throw instance;
        } catch (TestExceptionWithLotsOfArguments expected) {
            assertEquals("Default Throwable created by EasyStub", expected.getMessage());
            assertThat(expected.getCause(), instanceOf(EasyStubCreatedException.class));
            assertNull(expected.getSomethingElse());
        }
    }
}
