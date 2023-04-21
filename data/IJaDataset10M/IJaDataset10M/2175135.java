package com.googlecode.pinthura.bean;

import com.googlecode.pinthura.data.Authentication;
import com.googlecode.pinthura.data.Employee;
import static junit.framework.Assert.fail;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public final class APathEvaluatorWithExceptionsUnderTest {

    private final IMocksControl mockControl = EasyMock.createControl();

    private PathEvaluator pathEvaluator;

    private PropertyFinder mockPropertyFinder;

    @Before
    public void setup() {
        mockPropertyFinder = mockControl.createMock(PropertyFinder.class);
        pathEvaluator = new PathEvaluatorImpl(mockPropertyFinder);
    }

    @SuppressWarnings({ "ThrowableInstanceNeverThrown", "unchecked" })
    @Test
    public void shouldThrowAnExceptionIfAPropertyFinderExceptionIsThrown() throws NoSuchMethodException {
        expectProperty("authentication", Employee.class, "getAuthentication");
        EasyMock.expect(mockPropertyFinder.findMethodFor("boohoo", Authentication.class)).andThrow(new PropertyFinderException("test"));
        mockControl.replay();
        try {
            pathEvaluator.evaluate("authentication.boohoo", new Employee());
            fail();
        } catch (PathEvaluatorException e) {
            assertThat((Class<PropertyFinderException>) e.getCause().getClass(), equalTo(PropertyFinderException.class));
            assertThat(e.getCause().getMessage(), equalTo("test"));
        }
    }

    @SuppressWarnings({ "ThrowableInstanceNeverThrown", "unchecked" })
    @Test
    public void shouldThrowAnExceptionIfAnyExceptionIsThrown() throws NoSuchMethodException {
        EasyMock.expect(mockPropertyFinder.findMethodFor("boohoo", Authentication.class)).andThrow(new NullPointerException());
        mockControl.replay();
        try {
            pathEvaluator.evaluate("boohoo", new Authentication());
            fail();
        } catch (PathEvaluatorException e) {
            assertThat((Class<NullPointerException>) e.getCause().getClass(), equalTo(NullPointerException.class));
        }
    }

    private void expectProperty(final String property, final Class<?> parentClass, final String methodName) throws NoSuchMethodException {
        EasyMock.expect(mockPropertyFinder.findMethodFor(property, parentClass)).andReturn(parentClass.getMethod(methodName));
    }
}
