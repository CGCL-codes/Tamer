package net.sf.sasl.aop.common.grammar.placeholder.resolver;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the
 * {@link net.sf.sasl.aop.common.grammar.placeholder.resolver.ThreadPlaceholderResolver
 * ThreadPlaceholderResolver} class.
 * 
 * @author Philipp Förmer
 * 
 */
public class ThreadPlaceholderResolverTest {

    /**
	 * Unit under test.
	 */
    private ThreadPlaceholderResolver underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new ThreadPlaceholderResolver();
    }

    /**
	 * Weak test cases for the
	 * {@link net.sf.sasl.aop.common.grammar.placeholder.resolver.ThreadPlaceholderResolver#resolve(String, Object[], net.sf.sasl.aop.common.grammar.placeholder.interpreter.IEnvironment)
	 * resolve(...)} method.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testResolve() throws Exception {
        Assert.assertEquals(Thread.currentThread().getId(), underTest.resolve("curThreadId", new Object[0], null));
        Assert.assertEquals(Thread.currentThread().getName(), underTest.resolve("curThreadName", new Object[0], null));
        Assert.assertTrue((Long) underTest.resolve("totalThreadCount", new Object[0], null) > 0);
    }
}
