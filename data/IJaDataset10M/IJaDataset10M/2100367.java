package net.derquinse.common.reflect;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import org.testng.annotations.Test;

/**
 * Tests for Types.
 * @author Andres Rodriguez
 */
public class ProxiesTest {

    /** Test interface. */
    private interface Type {

        Integer method();
    }

    /**
	 * Test alwaysNull.
	 */
    @Test
    public void testNull() {
        Type t = Proxies.alwaysNull(Type.class);
        assertNotNull(t);
        assertNull(t.method());
    }

    /**
	 * Test unsupported.
	 */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testUnsupported() {
        Type t = Proxies.unsupported(Type.class);
        assertNotNull(t);
        t.method();
    }
}
