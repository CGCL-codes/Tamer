package com.aurorasoftworks.signal.tools.core.context.generator.source.java;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertSame;
import static org.testng.AssertJUnit.assertTrue;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Executor;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import com.aurorasoftworks.signal.runtime.core.context.IContext;
import com.aurorasoftworks.signal.runtime.core.context.proxy.AbstractMethodInterceptor;
import com.aurorasoftworks.signal.runtime.core.context.proxy.IInvocationHandler;
import com.aurorasoftworks.signal.runtime.core.context.proxy.IMethodHandler;
import com.aurorasoftworks.signal.runtime.core.context.proxy.IMethodInterceptor;
import com.aurorasoftworks.signal.runtime.core.context.proxy.IProxyClass;
import com.aurorasoftworks.signal.runtime.core.context.proxy.IProxyFactory;
import com.aurorasoftworks.signal.runtime.core.context.proxy.IProxyTarget;
import com.aurorasoftworks.signal.tools.core.context.loader.Child;
import com.aurorasoftworks.signal.tools.core.context.loader.ClassHolder;
import com.aurorasoftworks.signal.tools.core.context.loader.ContextAware;
import com.aurorasoftworks.signal.tools.core.context.loader.Eager;
import com.aurorasoftworks.signal.tools.core.context.loader.HashtableHolder;
import com.aurorasoftworks.signal.tools.core.context.loader.ILazyController;
import com.aurorasoftworks.signal.tools.core.context.loader.IProxyTargetChild;
import com.aurorasoftworks.signal.tools.core.context.loader.IProxyTargetParent;
import com.aurorasoftworks.signal.tools.core.context.loader.IProxyTargetRoot;
import com.aurorasoftworks.signal.tools.core.context.loader.IProxyTargetSecondaryParent;
import com.aurorasoftworks.signal.tools.core.context.loader.IProxyTargetSibling;
import com.aurorasoftworks.signal.tools.core.context.loader.Initializing;
import com.aurorasoftworks.signal.tools.core.context.loader.Lazy;
import com.aurorasoftworks.signal.tools.core.context.loader.LazyController;
import com.aurorasoftworks.signal.tools.core.context.loader.LazyProxyTarget;
import com.aurorasoftworks.signal.tools.core.context.loader.Parent;
import com.aurorasoftworks.signal.tools.core.context.loader.PrimitiveArgs;
import com.aurorasoftworks.signal.tools.core.context.loader.PrimitiveProperties;
import com.aurorasoftworks.signal.tools.core.context.loader.PrimitivePropertiesProcessor;
import com.aurorasoftworks.signal.tools.core.context.loader.ProxyTarget;
import com.aurorasoftworks.signal.tools.core.context.loader.Simple;
import com.aurorasoftworks.signal.tools.core.context.loader.SimpleFactory;
import com.aurorasoftworks.signal.tools.core.context.loader.VectorHolder;
import com.aurorasoftworks.signal.tools.core.context.loader.WrapperArgs;

@Test
public class JavaSourceContextGeneratorTest extends AbstractSourceContextGeneratorTest {

    @Test
    public void testInstantiation() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/simple.xml");
        assertNotNull((Simple) ctx.getBean("simple"));
    }

    @Test(dependsOnMethods = "testInstantiation")
    public void testStaticFactoryInstantiation() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/simple-static-factory.xml");
        assertNotNull((Simple) ctx.getBean("simple"));
    }

    @Test(dependsOnMethods = "testStaticFactoryInstantiation")
    public void testFactoryInstantiation() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/simple-factory.xml");
        assertNotNull((Simple) ctx.getBean("simple"));
        assertNotNull((SimpleFactory) ctx.getBean("simple-factory"));
    }

    @Test(dependsOnMethods = "testFactoryInstantiation")
    public void testInstantiationArgs() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/primitive-args.xml");
        assertNotNull((PrimitiveArgs) ctx.getBean("args"));
    }

    @Test(dependsOnMethods = "testInstantiationArgs")
    public void testInstantiationArgs_ClassLiteral() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/class-literal.xml");
        assertEquals(Collection.class, ((ClassHolder) ctx.getBean("holder")).getTargetClass());
    }

    @Test(dependsOnMethods = "testInstantiationArgs")
    public void testWrappedInstantiationArgs() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/wrapper-args.xml");
        assertNotNull((WrapperArgs) ctx.getBean("args"));
    }

    @Test(dependsOnMethods = "testInstantiationArgs")
    public void testFactoryInstantiationArgs() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/primitive-args-factory.xml");
        assertNotNull((PrimitiveArgs) ctx.getBean("args"));
    }

    @Test(dependsOnMethods = "testFactoryInstantiationArgs")
    public void testFactoryInstantiationArgs2() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/primitive-args-factory2.xml");
        assertTrue(ctx.getBean("executor") instanceof Executor);
    }

    @Test(dependsOnMethods = "testInstantiationArgs")
    public void testInstantiationProperties() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/primitive-properties.xml");
        assertNotNull((PrimitiveProperties) ctx.getBean("props"));
    }

    @Test(dependsOnMethods = { "testInstantiationArgs", "testInstantiationProperties" })
    public void testInstantiationWithReferenceArg() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/reference-arg.xml");
        assertNotNull((Parent) ctx.getBean("parent"));
        assertNotNull((Child) ctx.getBean("child"));
    }

    @Test(dependsOnMethods = "testInstantiationWithReferenceArg")
    public void testInstantiationWithReferenceProperty() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/reference-property.xml");
        assertNotNull((Parent) ctx.getBean("parent"));
        assertNotNull((Child) ctx.getBean("child"));
    }

    @Test(dependsOnMethods = "testInstantiation")
    public void testInitialization() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/initializing.xml");
        assertFalse(new Initializing().isInitialized());
        assertNotNull((Initializing) ctx.getBean("init"));
        assertTrue(((Initializing) ctx.getBean("init")).isInitialized());
    }

    @Test(dependsOnMethods = "testInitialization")
    public void testContextAware() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/context-aware.xml");
        assertNotNull((ContextAware) ctx.getBean("contextAware"));
        AssertJUnit.assertSame(ctx, ((ContextAware) ctx.getBean("contextAware")).getContext());
    }

    @Test(dependsOnMethods = "testInstantiationArgs")
    public void testListArg() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/list-arg.xml");
        List<?> list = (List<?>) ctx.getBean("list");
        assertNotNull(list);
        List<String> expected = Arrays.asList(new String[] { "abc", "def" });
        assertEquals(expected, list);
    }

    @SuppressWarnings("unchecked")
    @Test(dependsOnMethods = "testListArg")
    public void testListArgReference() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/list-ref.xml");
        Object element = ctx.getBean("obj");
        assertNotNull(element);
        List<Object> list = (List<Object>) ctx.getBean("list");
        assertNotNull(list);
        assertEquals(1, list.size());
        assertSame(element, list.get(0));
    }

    @Test(dependsOnMethods = { "testInstantiationArgs", "testListArg" })
    public void testSetArg() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/set-arg.xml");
        Set<?> set = (Set<?>) ctx.getBean("set");
        assertNotNull(set);
        Set<String> expected = new HashSet<String>(Arrays.asList(new String[] { "abc", "def" }));
        assertEquals(expected, set);
    }

    @SuppressWarnings("unchecked")
    @Test(dependsOnMethods = { "testSetArg" })
    public void testSetArgReference() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/set-ref.xml");
        Object element1 = ctx.getBean("element1");
        assertNotNull(element1);
        Object element2 = ctx.getBean("element2");
        assertNotNull(element2);
        assertFalse(element1.equals(element2));
        Set<Object> set = (Set<Object>) ctx.getBean("set");
        assertNotNull(set);
        assertEquals(2, set.size());
        Set<Object> expected = new HashSet<Object>(Arrays.asList(new Object[] { element1, element2 }));
        assertEquals(expected, set);
    }

    @Test(dependsOnMethods = "testListArg")
    public void testVectorListArg() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/vector-list-arg.xml");
        VectorHolder ht = (VectorHolder) ctx.getBean("holder");
        Vector<?> v = ht.getVector();
        assertNotNull(v);
        List<String> expected = Arrays.asList(new String[] { "abc", "def" });
        assertEquals(expected, v);
    }

    @Test(dependsOnMethods = "testSetArg")
    public void testVectorSetArg() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/vector-set-arg.xml");
        VectorHolder ht = (VectorHolder) ctx.getBean("holder");
        Vector<?> v = ht.getVector();
        assertNotNull(v);
        List<String> expected = Arrays.asList(new String[] { "abc", "def" });
        assertEquals(expected, v);
    }

    @Test(dependsOnMethods = { "testInstantiationArgs", "testSetArg" })
    public void testMapArg() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/map-arg.xml");
        Map<?, ?> map = (Map<?, ?>) ctx.getBean("map");
        assertNotNull(map);
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("1", "one");
        expected.put("2", "two");
        assertEquals(expected, map);
    }

    @Test(dependsOnMethods = { "testMapArg" })
    public void testMapArgReference() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/map-ref.xml");
        Object element1 = ctx.getBean("element1");
        assertNotNull(element1);
        Object element2 = ctx.getBean("element2");
        assertNotNull(element2);
        assertFalse(element1.equals(element2));
        Map<?, ?> map = (Map<?, ?>) ctx.getBean("map");
        assertNotNull(map);
        Map<Object, Object> expected = new HashMap<Object, Object>();
        expected.put("1", element1);
        expected.put(element2, "two");
        assertEquals(expected, map);
    }

    @Test(dependsOnMethods = "testMapArg")
    public void testHashtableArg() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/hashtable-arg.xml");
        HashtableHolder ht = (HashtableHolder) ctx.getBean("holder");
        Hashtable<?, ?> map = ht.getHashtable();
        assertNotNull(map);
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("1", "one");
        expected.put("2", "two");
        assertEquals(expected, map);
    }

    @Test(dependsOnMethods = "testInstantiation")
    public void testLazyInit() throws Exception {
        Eager.resetInstanceCount();
        Lazy.resetInstanceCount();
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/lazy-init.xml");
        assertEquals(1, Eager.getInstanceCount());
        assertEquals(0, Lazy.getInstanceCount());
        ctx.getBean(Eager.class);
        assertEquals(1, Eager.getInstanceCount());
        assertEquals(0, Lazy.getInstanceCount());
        ctx.getBean("lazy");
        assertEquals(1, Eager.getInstanceCount());
        assertEquals(1, Lazy.getInstanceCount());
    }

    @Test(dependsOnMethods = "testInstantiationWithReferenceArg")
    public void testAutowireByType() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/autowire-type.xml");
        ctx.getBean("holder");
    }

    @Test(dependsOnMethods = "testInstantiation")
    public void testProxyTarget() throws Exception {
        IInvocationHandler invoker = mock(IInvocationHandler.class);
        generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/proxy-target.xml");
        IProxyFactory proxyFactory = createProxyFactory();
        ProxyTarget target = new ProxyTarget();
        IProxyTargetChild proxy = (IProxyTargetChild) proxyFactory.createProxy(target, invoker);
        assertTrue(proxy instanceof IProxyTargetRoot);
        assertTrue(proxy instanceof IProxyTargetParent);
        assertTrue(proxy instanceof IProxyTargetChild);
        assertTrue(proxy instanceof IProxyTargetSibling);
        assertTrue(proxy instanceof IProxyTargetSecondaryParent);
        assertTrue(proxy instanceof IProxyTarget);
        IProxyClass proxyClass = proxyFactory.getProxyClass(target.getClass());
        proxy.first();
        proxy.second();
        proxy.third();
        ((IProxyTargetSibling) proxy).fourth(1, 2, (short) 3, (byte) 4, 'x', 5.0f, 6.0, "abc");
        verify(invoker).invoke(target, proxyClass.getMethodHandler("first", new Class[0]), new Object[0]);
        verify(invoker).invoke(target, proxyClass.getMethodHandler("second", new Class[0]), new Object[0]);
        verify(invoker).invoke(target, proxyClass.getMethodHandler("third", new Class[0]), new Object[0]);
        verify(invoker).invoke(target, proxyClass.getMethodHandler("fourth", new Class[] { Integer.class, Long.class, Short.class, Byte.class, Character.class, Float.class, Double.class, String.class }), new Object[] { Integer.valueOf(1), Long.valueOf(2), Short.valueOf((short) 3), Byte.valueOf((byte) 4), Character.valueOf('x'), Float.valueOf(5.0f), Double.valueOf(6.0), "abc" });
    }

    @Test(dependsOnMethods = "testProxyTarget")
    public void testProxyOfProxy() throws Exception {
        generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/proxy-target.xml");
        final StringBuilder testBuf = new StringBuilder();
        ProxyTarget target = new ProxyTarget();
        IProxyFactory proxyFactory = createProxyFactory();
        IProxyTargetRoot proxy = (IProxyTargetRoot) proxyFactory.createProxy(target, new AbstractMethodInterceptor() {

            public Object invoke(IProxyTarget target, IMethodHandler handler, Object[] args) throws Exception {
                testBuf.append("<second>");
                Object result = handler.invoke(target, args);
                testBuf.append("</second>");
                return result;
            }
        });
        IProxyTargetChild proxyOfProxy = (IProxyTargetChild) proxyFactory.createProxy((IProxyTarget) proxy, new AbstractMethodInterceptor() {

            public Object invoke(IProxyTarget target, IMethodHandler handler, Object[] args) throws Exception {
                testBuf.append("<first>");
                Object result = handler.invoke(target, args);
                testBuf.append("</first>");
                return result;
            }
        });
        assertEquals("Second", proxyOfProxy.second());
        assertEquals("<first><second></second></first>", testBuf.toString());
    }

    @Test(dependsOnMethods = "testProxyTarget", enabled = true)
    public void testLazyProxyTarget() throws Exception {
        LazyProxyTarget.resetInstanceCount();
        assertEquals(0, LazyProxyTarget.getInstanceCount());
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/lazy-proxy-target.xml");
        assertEquals(0, LazyProxyTarget.getInstanceCount());
        IProxyTargetChild p = (IProxyTargetChild) ctx.getBean("proxy");
        assertEquals(0, LazyProxyTarget.getInstanceCount());
        assertEquals("2nd", p.second());
        assertEquals(1, LazyProxyTarget.getInstanceCount());
        assertEquals("3rd", p.third());
        assertEquals(1, LazyProxyTarget.getInstanceCount());
    }

    @Test(dependsOnMethods = "testProxyTarget")
    public void testProxyTargetInvoker() throws Exception {
        generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/proxy-target.xml");
        IProxyFactory proxyFactory = createProxyFactory();
        ProxyTarget target = new ProxyTarget();
        IProxyClass proxyClass = proxyFactory.getProxyClass(target.getClass());
        assertEquals("Second", proxyClass.invoke(target, proxyClass.getMethodHandler("second", new Class[0]), new Object[0]));
        assertEquals(10, proxyClass.invoke(target, proxyClass.getMethodHandler("fifth", new Class[0]), new Object[0]));
        assertEquals("1 2 3 4 a 1.5 2.5 xyz", proxyClass.invoke(target, proxyClass.getMethodHandler("fourth", new Class[] { Integer.class, Long.class, Short.class, Byte.class, Character.class, Float.class, Double.class, String.class }), new Object[] { 1, 2l, (short) 3, (byte) 4, 'a', 1.5f, 2.5, "xyz" }));
    }

    @Test(dependsOnMethods = "testProxyTargetInvoker")
    public void testProxyTargetWithInterceptors() throws Exception {
        generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/proxy-target.xml");
        IProxyFactory proxyFactory = createProxyFactory();
        ProxyTarget target = new ProxyTarget();
        IProxyTargetChild proxy = (IProxyTargetChild) proxyFactory.createProxy(target, new IMethodInterceptor[] { new AbstractMethodInterceptor() {

            public Object invoke(IProxyTarget target, IMethodHandler handler, Object[] args) throws Exception {
                return ((String) nextHandler.invoke(target, handler, args)).toUpperCase();
            }
        } });
        assertEquals("SECOND", proxy.second());
        proxy = (IProxyTargetChild) proxyFactory.createProxy(target, new IMethodInterceptor[] { new AbstractMethodInterceptor() {

            public Object invoke(IProxyTarget target, IMethodHandler handler, Object[] args) throws Exception {
                args[0] = -7;
                return nextHandler.invoke(target, handler, args);
            }
        } });
        assertEquals("-7 2 3 4 a 1.5 2.5 xyz", ((IProxyTargetSibling) proxy).fourth(1, 2l, (short) 3, (byte) 4, 'a', 1.5f, 2.5, "xyz"));
    }

    @Test(dependsOnMethods = "testFactoryInstantiation")
    public void testBeanProcessor() throws Exception {
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/post-processor.xml");
        PrimitiveProperties p = (PrimitiveProperties) ctx.getBean("props");
        assertEquals(PrimitivePropertiesProcessor.INT_VALUE, p.getI());
        assertEquals(PrimitivePropertiesProcessor.LONG_VALUE, p.getL());
    }

    @Test(dependsOnMethods = "testLazyProxyTarget")
    public void testLazyController() throws Exception {
        LazyController.resetCounters();
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/lazy-controller.xml");
        ctx.getBean("dispatcher");
        ILazyController ctl = (ILazyController) ctx.getBean("ctl");
        assertEquals(0, LazyController.getInstanceCount());
        assertEquals(0, LazyController.getActivationCount());
        assertEquals(0, LazyController.getDeactivationCount());
        ctl.doSomething();
        assertEquals(1, LazyController.getInstanceCount());
        assertEquals(1, LazyController.getActivationCount());
        assertEquals(0, LazyController.getDeactivationCount());
    }

    @Test(dependsOnMethods = "testLazyProxyTarget")
    public void testEagerController() throws Exception {
        LazyController.resetCounters();
        IContext ctx = generate("/com/aurorasoftworks/signal/tools/core/context/loader/spring/eager-controller.xml");
        ctx.getBean("dispatcher");
        ILazyController ctl = (ILazyController) ctx.getBean("ctl");
        assertEquals(1, LazyController.getInstanceCount());
        assertEquals(0, LazyController.getActivationCount());
        assertEquals(0, LazyController.getDeactivationCount());
        ctl.doSomething();
        assertEquals(1, LazyController.getInstanceCount());
        assertEquals(1, LazyController.getActivationCount());
        assertEquals(0, LazyController.getDeactivationCount());
    }
}
