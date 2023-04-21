package samples.junit4.constructorargs;

import org.easymock.ConstructorArgs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import samples.constructorargs.ConstructorArgsDemo;
import java.lang.reflect.Constructor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.powermock.api.easymock.PowerMock.*;

/**
 * This test demonstrates the ability to invoke a specific constructor after
 * creating the mock object.
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ConstructorArgsDemo.class)
public class ConstructorArgsDemoTest {

    @Test
    public void testGetTheSecret_noConstructor() throws Exception {
        ConstructorArgsDemo tested = createMock(ConstructorArgsDemo.class);
        assertNull(Whitebox.getInternalState(tested, "secret", ConstructorArgsDemo.class));
    }

    @Test
    public void testGetTheSecret_defaultConstructor() throws Exception {
        final Constructor<ConstructorArgsDemo> constructor = ConstructorArgsDemo.class.getConstructor((Class<?>[]) null);
        ConstructorArgsDemo tested = createMock(ConstructorArgsDemo.class, new ConstructorArgs(constructor));
        assertEquals("default", Whitebox.getInternalState(tested, "secret", ConstructorArgsDemo.class));
    }

    @Test
    public void testGetTheSecret_stringConstructor() throws Exception {
        final String expected = "my own secret";
        ConstructorArgsDemo tested = createMock(ConstructorArgsDemo.class, expected);
        assertEquals(expected, Whitebox.getInternalState(tested, "secret", ConstructorArgsDemo.class));
    }

    @Test
    public void testGetTheSecret_stringConstructorAndMockedPrivateSecret() throws Exception {
        final String originalSecret = "my own secret";
        ConstructorArgsDemo tested = createPartialMock(ConstructorArgsDemo.class, new String[] { "theSecretIsPrivate" }, originalSecret);
        assertEquals(originalSecret, Whitebox.getInternalState(tested, "secret", ConstructorArgsDemo.class));
        final String myNewSecret = "my new secret";
        expectPrivate(tested, "theSecretIsPrivate").andReturn(myNewSecret);
        replay(tested);
        final String actual = tested.getTheSecret();
        verify(tested);
        assertEquals(myNewSecret, actual);
    }
}
