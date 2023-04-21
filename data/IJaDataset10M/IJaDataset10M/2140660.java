package net.sf.sasl.aop.common.grammar.placeholder.resolver;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the
 * {@link net.sf.sasl.aop.common.grammar.placeholder.resolver.MemoryPlaceholderResolver
 * MemoryPlaceholderResolver} class.
 * 
 * @author Philipp Förmer
 * 
 */
public class MemoryPlaceholderResolverTest {

    /**
	 * The unit under test.
	 */
    private MemoryPlaceholderResolver underTest;

    @Before
    public void setUp() {
        underTest = new MemoryPlaceholderResolver();
    }

    /**
	 * Weak test cases for the
	 * {@link net.sf.sasl.aop.common.grammar.placeholder.resolver.MemoryPlaceholderResolver#resolve(String, Object[], net.sf.sasl.aop.common.grammar.placeholder.interpreter.IEnvironment)
	 * resolve(...)} method.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testResolve() throws Exception {
        Assert.assertNotNull(underTest.resolve("freeMemory", new Object[] { "MB" }, null));
        Assert.assertNotNull(underTest.resolve("freeMemory", new Object[] {}, null));
        Assert.assertNotNull(underTest.resolve("maxMemory", new Object[] { "MB" }, null));
        Assert.assertNotNull(underTest.resolve("maxMemory", new Object[] {}, null));
        Assert.assertNotNull(underTest.resolve("totalMemory", new Object[] { "MB" }, null));
        Assert.assertNotNull(underTest.resolve("totalMemory", new Object[] {}, null));
    }

    /**
	 * Test cases for the
	 * {@link net.sf.sasl.aop.common.grammar.placeholder.resolver.MemoryPlaceholderResolver#format(String, long)
	 * format(String, long)} procedure.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testFormat() throws Exception {
        Assert.assertEquals(Double.valueOf(1), MemoryPlaceholderResolver.format("KB", 1024));
        Assert.assertEquals(Double.valueOf(1), MemoryPlaceholderResolver.format("MB", 1024 * 1024));
        Assert.assertEquals(Double.valueOf(1), MemoryPlaceholderResolver.format("GB", 1024 * 1024 * 1024));
        Assert.assertEquals(Double.valueOf(1), MemoryPlaceholderResolver.format("TB", 1024L * 1024 * 1024 * 1024));
    }
}
