package org.springframework.aop.support;

import javax.servlet.ServletException;
import junit.framework.TestCase;
import org.springframework.util.SerializationTestUtils;

/**
 * @author Rod Johnson
 * @author Dmitriy Kopylenko
 * @since 1.1
 */
public abstract class AbstractRegexpMethodPointcutTests extends TestCase {

    private AbstractRegexpMethodPointcut rpc;

    protected void setUp() {
        rpc = getRegexpMethodPointcut();
    }

    protected abstract AbstractRegexpMethodPointcut getRegexpMethodPointcut();

    public void testNoPatternSupplied() throws Exception {
        noPatternSuppliedTests(rpc);
    }

    public void testSerializationWithNoPatternSupplied() throws Exception {
        rpc = (AbstractRegexpMethodPointcut) SerializationTestUtils.serializeAndDeserialize(rpc);
        noPatternSuppliedTests(rpc);
    }

    protected void noPatternSuppliedTests(AbstractRegexpMethodPointcut rpc) throws Exception {
        assertFalse(rpc.matches(Object.class.getMethod("hashCode", (Class[]) null), String.class));
        assertFalse(rpc.matches(Object.class.getMethod("wait", (Class[]) null), Object.class));
        assertEquals(0, rpc.getPatterns().length);
    }

    public void testExactMatch() throws Exception {
        rpc.setPattern("java.lang.Object.hashCode");
        exactMatchTests(rpc);
        rpc = (AbstractRegexpMethodPointcut) SerializationTestUtils.serializeAndDeserialize(rpc);
        exactMatchTests(rpc);
    }

    protected void exactMatchTests(AbstractRegexpMethodPointcut rpc) throws Exception {
        assertTrue(rpc.matches(Object.class.getMethod("hashCode", (Class[]) null), String.class));
        assertFalse(rpc.matches(Object.class.getMethod("wait", (Class[]) null), Object.class));
    }

    public void testWildcard() throws Exception {
        rpc.setPattern(".*Object.hashCode");
        assertTrue(rpc.matches(Object.class.getMethod("hashCode", (Class[]) null), Object.class));
        assertFalse(rpc.matches(Object.class.getMethod("wait", (Class[]) null), Object.class));
    }

    public void testWildcardForOneClass() throws Exception {
        rpc.setPattern("java.lang.Object.*");
        assertTrue(rpc.matches(Object.class.getMethod("hashCode", (Class[]) null), String.class));
        assertTrue(rpc.matches(Object.class.getMethod("wait", (Class[]) null), String.class));
    }

    public void testMatchesObjectClass() throws Exception {
        rpc.setPattern("java.lang.Object.*");
        assertTrue(rpc.matches(Exception.class.getMethod("hashCode", (Class[]) null), ServletException.class));
        assertFalse(rpc.matches(Exception.class.getMethod("getMessage", (Class[]) null), Exception.class));
    }
}
