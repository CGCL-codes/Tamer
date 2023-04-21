package net.sf.javareflector.function;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Created: 4/7/11
 */
public class DefaultIfNullFunctionTest {

    private DefaultIfNullFunction<Integer> createFunction() {
        return DefaultIfNullFunction.create(42);
    }

    @Test
    public void returnsDefaultValueIfNull() {
        assertEquals(42, (int) createFunction().apply(null));
    }

    @Test
    public void returnsValueIfNotNull() {
        assertEquals(1, (int) createFunction().apply(1));
    }

    @Test
    public void convertsNullToEmptyString() {
        assertEquals("", DefaultIfNullFunction.toStringFunction().apply(null));
    }

    @Test
    public void twoInstancesAreEqual() {
        assertTrue(createFunction().equals(createFunction()));
    }
}
