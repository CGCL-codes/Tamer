package samples.junit4.privatefield;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import samples.Service;
import samples.privatefield.MockSelfPrivateFieldServiceClass;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.easymock.PowerMock.*;
import static org.powermock.reflect.Whitebox.setInternalState;

/**
 * A test class that demonstrate how to test classes that uses a private field
 * for a service and has no corresponding setter and at the same time mocking a
 * method of the actual test class. This is approach is common in DI frameworks
 * like Guice and Wicket IoC.
 * 
 * @author Johan Haleby
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MockSelfPrivateFieldServiceClass.class)
public class MockSelfPrivateFieldServiceClassTest {

    @Test
    public void testGetCompositeMessage() throws Exception {
        MockSelfPrivateFieldServiceClass tested = createPartialMock(MockSelfPrivateFieldServiceClass.class, "getOwnMessage");
        Service serviceMock = createMock(Service.class);
        setInternalState(tested, "service", serviceMock, MockSelfPrivateFieldServiceClass.class);
        final String expected = "Hello world";
        expectPrivate(tested, "getOwnMessage").andReturn("Hello");
        expect(serviceMock.getServiceMessage()).andReturn(" world");
        replay(serviceMock);
        replay(tested);
        final String actual = tested.getCompositeMessage();
        verify(serviceMock);
        verify(tested);
        assertEquals(expected, actual);
    }
}
