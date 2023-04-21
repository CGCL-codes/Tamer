package org.unitils.mock.example1;

import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.mock.Mock;
import org.unitils.mock.MockUnitils;
import org.unitils.mock.core.MockObject;
import org.unitils.mock.core.proxy.ProxyInvocation;
import org.unitils.mock.mockbehavior.MockBehavior;

public class MyServiceTest extends UnitilsJUnit4 {

    private Mock<MyService> myServiceMock;

    @Before
    public void initialize() {
        myServiceMock = new MockObject<MyService>(MyService.class, this);
    }

    @Test
    public void behavior() {
        myServiceMock.returns("a value").someMethod();
        myServiceMock.raises(RuntimeException.class).someMethod();
        myServiceMock.raises(new RuntimeException()).someMethod();
        myServiceMock.performs(new MockBehavior() {

            public Object execute(ProxyInvocation proxyInvocation) throws Throwable {
                return "a value";
            }
        }).someMethod();
        myServiceMock.onceRaises(RuntimeException.class).someMethod();
        myServiceMock.onceReturns("a value").someMethod();
        myServiceMock.returns("another value").someMethod();
    }

    @Test
    public void assertions() {
        myServiceMock.assertInvoked().someMethod();
        myServiceMock.assertNotInvoked().someMethod();
        myServiceMock.assertInvoked().someMethod();
        myServiceMock.assertNotInvoked().someMethod();
        myServiceMock.assertInvokedInSequence().someMethod();
        myServiceMock.assertInvokedInSequence().anotherMethod();
        myServiceMock.assertInvoked().someMethod();
        MockUnitils.assertNoMoreInvocations();
    }
}
