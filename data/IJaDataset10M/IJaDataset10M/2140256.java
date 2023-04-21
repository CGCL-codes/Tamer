package org.powermock.modules.junit4.newmocking;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.powermock.PowerMock.mockConstruction;
import static org.powermock.PowerMock.replay;
import static org.powermock.PowerMock.verify;
import static org.junit.Assert.assertEquals;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import samples.newmocking.MyClass;
import samples.newmocking.StupidNew;

/**
 * Test class to demonstrate new instance mocking.
 * 
 * @author Johan Haleby
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ MyClass.class, StupidNew.class })
public class StupidNewTest {

    @Test
    public void testGetMessage() throws Exception {
        StupidNew tested = new StupidNew();
        MyClass myClassMock = mockConstruction(MyClass.class);
        String expected = "Hello altered World";
        expect(myClassMock.getMessage()).andReturn("Hello altered World");
        replay(myClassMock);
        String actual = tested.getMessage();
        verify(myClassMock);
        assertEquals("Expected and actual did not match", expected, actual);
    }

    @Test
    public void testGetMessageWithArgument() throws Exception {
        StupidNew tested = new StupidNew();
        MyClass myClassMock = mockConstruction(MyClass.class);
        String expected = "Hello altered World";
        expect(myClassMock.getMessage("test")).andReturn("Hello altered World");
        replay(myClassMock);
        String actual = tested.getMessageWithArgument();
        verify(myClassMock);
        assertEquals("Expected and actual did not match", expected, actual);
    }

    @Test
    public void testInvokeVoidMethod() throws Exception {
        StupidNew tested = new StupidNew();
        MyClass myClassMock = mockConstruction(MyClass.class);
        myClassMock.voidMethod();
        expectLastCall().times(1);
        replay(myClassMock);
        tested.invokeVoidMethod();
        verify(myClassMock);
    }
}
