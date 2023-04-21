package org.proxy4j.core;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.proxy4j.core.testobj.OtherTarget;
import org.proxy4j.core.testobj.Target;
import org.proxy4j.core.testobj.TestMarker;
import org.proxy4j.core.testobj.TestTarget;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * <p>Base test class for all {@link ProxyFactory} implementations.</p>
 * @author Brennan Spies
 */
public abstract class BaseProxyFactoryTest {

    /**
     * Returns the {@code ProxyFactory} implementation used to
     * run the tests.
     *
     * @return The implementation
     */
    protected abstract ProxyFactory getImplementation();

    /**
     * Test method for {@link org.proxy4j.core.ProxyFactory#createProxy(java.lang.Class, javax.inject.Provider)}.
     */
    @Test
    public void testCreateProviderProxy() throws Exception {
        ProxyFactory factory = getImplementation();
        LazyTargetProvider targetProvider = new LazyTargetProvider();
        Target targetProxy = factory.createProxy(Target.class, targetProvider);
        assertFalse("Creating proxy should not create target", targetProvider.isInitialized());
        assertEquals("Target count should be 0", targetProxy.getCount(), 0);
        assertTrue("TestTarget object should be initialized", targetProvider.isInitialized());
        targetProxy.increment();
        assertEquals("Target count should be 1", targetProxy.getCount(), 1);
    }

    /**
	 * Test method for {@link org.proxy4j.core.ProxyFactory#createProxy(Class, org.proxy4j.core.ProxyHandler)}.
	 */
    @Test
    public void testCreateHandlerProxy() throws Exception {
        ProxyFactory factory = getImplementation();
        Target targetProxy = factory.createProxy(Target.class, new TestProxyHandler());
        assertEquals("Target count should be 0", targetProxy.getCount(), 0);
        targetProxy.increment();
        assertEquals("Target count should be 1", targetProxy.getCount(), 1);
    }

    /**
     * Test method for {@link org.proxy4j.core.ProxyFactory#createProxy(Class[], org.proxy4j.core.ProxyHandler)}.
     */
    @Test
    public void testCreateHandlerProxyMultipleInterfaces() throws Exception {
        ProxyFactory factory = getImplementation();
        Object proxy = factory.createProxy(new Class[] { Target.class, OtherTarget.class }, new TestMultiInterfaceProxyHandler());
        String msg = ((OtherTarget) proxy).getMessage();
        assertTrue(msg.startsWith("Hello"));
        Target tproxy = (Target) proxy;
        assertEquals("Count should be 0", tproxy.getCount(), 0);
        tproxy.increment();
        assertEquals("Count should be 1", tproxy.getCount(), 1);
    }

    /**
	 * Test method for {@link org.proxy4j.core.ProxyFactory#createProxy(Class, Object, Class, org.aopalliance.intercept.MethodInterceptor...)} .
	 */
    @Test
    public void testCreateInterceptorProxy() throws Exception {
        ProxyFactory factory = getImplementation();
        final List<String> messages = new ArrayList<String>();
        MethodInterceptor mi1 = new MethodInterceptor() {

            public Object invoke(MethodInvocation call) throws Throwable {
                messages.add("ONE");
                return call.proceed();
            }
        };
        MethodInterceptor mi2 = new MethodInterceptor() {

            public Object invoke(MethodInvocation call) throws Throwable {
                messages.add("TWO");
                return call.proceed();
            }
        };
        Target proxy = factory.createProxy(Target.class, new TestTarget(), TestMarker.class, mi1, mi2);
        assertEquals("Count should be 0", 0, proxy.getCount());
        assertEquals("Should be 2 messages", 2, messages.size());
        assertEquals("First interceptor message should be 'ONE'", messages.get(0), "ONE");
        assertEquals("First interceptor message should be 'TWO'", messages.get(1), "TWO");
        messages.clear();
        proxy.setCount(5);
        assertEquals("Count should be 0", 0, messages.size());
    }
}
