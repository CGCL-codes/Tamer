package org.powermock.modules.junit4.singleton;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.powermock.PowerMock.expectPrivate;
import static org.powermock.PowerMock.mockStatic;
import static org.powermock.PowerMock.mockStaticMethod;
import static org.powermock.PowerMock.replay;
import static org.powermock.PowerMock.verify;
import static org.junit.Assert.assertEquals;
import org.powermock.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import samples.singleton.StupidSingleton;
import samples.singleton.StupidSingletonHelper;

/**
 * Test class to demonstrate static, static+final, static+native and
 * static+final+native methods mocking.
 * 
 * @author Johan Haleby
 * @author Jan Kronquist
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(StupidSingleton.class)
public class StupidSingletonTest {

    @Test
    public void testSay() throws Exception {
        mockStatic(StupidSingleton.class);
        String expected = "Hello altered World";
        expect(StupidSingleton.say("hello")).andReturn("Hello altered World");
        replay(StupidSingleton.class);
        String actual = StupidSingleton.say("hello");
        verify(StupidSingleton.class);
        assertEquals("Expected and actual did not match", expected, actual);
        String actual2 = StupidSingleton.say("world");
        assertEquals("Hello world", actual2);
    }

    @Test
    public void testSayFinal() throws Exception {
        mockStatic(StupidSingleton.class);
        String expected = "Hello altered World";
        expect(StupidSingleton.sayFinal("hello")).andReturn("Hello altered World");
        replay(StupidSingleton.class);
        String actual = StupidSingleton.sayFinal("hello");
        verify(StupidSingleton.class);
        assertEquals("Expected and actual did not match", expected, actual);
        String actual2 = StupidSingleton.sayFinal("world");
        assertEquals("Hello world", actual2);
    }

    @Test
    public void testSayNative() throws Exception {
        mockStatic(StupidSingleton.class);
        String expected = "Hello altered World";
        expect(StupidSingleton.sayNative("hello")).andReturn("Hello altered World");
        replay(StupidSingleton.class);
        String actual = StupidSingleton.sayNative("hello");
        verify(StupidSingleton.class);
        assertEquals("Expected and actual did not match", expected, actual);
    }

    @Test
    public void sayFinalNative() throws Exception {
        mockStatic(StupidSingleton.class);
        String expected = "Hello altered World";
        expect(StupidSingleton.sayFinalNative("hello")).andReturn("Hello altered World");
        replay(StupidSingleton.class);
        String actual = StupidSingleton.sayFinalNative("hello");
        verify(StupidSingleton.class);
        assertEquals("Expected and actual did not match", expected, actual);
    }

    @Test
    public void mockAStaticMethod() throws Exception {
        mockStatic(StupidSingleton.class);
        String expected = "qwe";
        expect(StupidSingleton.doStatic(5)).andReturn(expected);
        replay(StupidSingleton.class);
        String actual = StupidSingleton.doStatic(5);
        assertEquals(expected, actual);
        verify(StupidSingleton.class);
    }

    @Test
    public void mockSayHello() throws Exception {
        mockStatic(StupidSingletonHelper.class);
        StupidSingletonHelper.sayHelloHelper();
        expectLastCall().times(2);
        replay(StupidSingletonHelper.class);
        StupidSingleton.sayHello();
        verify(StupidSingletonHelper.class);
    }

    @Test
    public void mockSayHelloAgain() throws Exception {
        mockStatic(StupidSingletonHelper.class);
        StupidSingletonHelper.sayHelloAgain();
        expectLastCall().times(2);
        replay(StupidSingletonHelper.class);
        StupidSingleton.sayHelloAgain();
        verify(StupidSingletonHelper.class);
    }

    @Test
    public void testSayPrivateStatic() throws Exception {
        mockStaticMethod(StupidSingleton.class, "sayPrivateStatic", String.class);
        final String expected = "Hello world";
        expectPrivate(StupidSingleton.class, "sayPrivateStatic", "name").andReturn(expected);
        replay(StupidSingleton.class);
        String actual = (String) Whitebox.invokeMethod(StupidSingleton.class, "sayPrivateStatic", "name");
        verify(StupidSingleton.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testSayPrivateFinalStatic() throws Exception {
        mockStaticMethod(StupidSingleton.class, "sayPrivateFinalStatic", String.class);
        final String expected = "Hello world";
        expectPrivate(StupidSingleton.class, "sayPrivateFinalStatic", "name").andReturn(expected);
        replay(StupidSingleton.class);
        String actual = (String) Whitebox.invokeMethod(StupidSingleton.class, "sayPrivateFinalStatic", "name");
        verify(StupidSingleton.class);
        assertEquals(expected, actual);
    }

    @Test
    public void innerClassesWork() {
        assertEquals(17, StupidSingleton.getNumberFromInner());
    }

    @Test
    public void innerInstanceClassesWork() {
        assertEquals(23, StupidSingleton.getNumberFromInnerInstance());
    }
}
