package org.t2framework.t2.action.parameter.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.t2framework.commons.meta.impl.MethodDescImpl;
import org.t2framework.commons.util.Reflections.MethodUtil;
import org.t2framework.t2.annotation.core.ActionParam;
import org.t2framework.t2.annotation.core.Index;
import org.t2framework.t2.exception.InvalidRequestParameterTypeRuntimeException;
import org.t2framework.t2.mock.MockActionContextImpl;
import org.t2framework.t2.mock.MockWebContext;

public class IndexParameterResolverTest extends TestCase {

    public void testResolve() throws Exception {
        MockWebContext context = MockWebContext.createMock("/hoge", "examples");
        context.getMockHttpServletRequest().addParameter("hoge[1]", "111");
        Method m = MethodUtil.getDeclaredMethod(Hoge.class, "aaa", new Class[] { Integer.class });
        Annotation[][] params = m.getParameterAnnotations();
        IndexParameterResolver resolver = new IndexParameterResolver();
        MockActionContextImpl actionContext = new MockActionContextImpl(context);
        Object resolve = resolver.resolve(actionContext, new MethodDescImpl(m), 0, params[0], Integer.class);
        assertNotNull(resolve);
        System.out.println(resolve);
        assertEquals(1, resolve);
    }

    public void testResolve2() throws Exception {
        MockWebContext context = MockWebContext.createMock("/hoge", "examples");
        context.getMockHttpServletRequest().addParameter("hoge[1][2]", "111");
        Method m = MethodUtil.getDeclaredMethod(Hoge.class, "bbb", new Class[] { int.class, int.class });
        Annotation[][] params = m.getParameterAnnotations();
        IndexParameterResolver resolver = new IndexParameterResolver();
        MockActionContextImpl actionContext = new MockActionContextImpl(context);
        Object resolve = resolver.resolve(actionContext, new MethodDescImpl(m), 1, params[1], Integer.class);
        assertNotNull(resolve);
        System.out.println(resolve);
        assertEquals(2, resolve);
    }

    public void testResolve3() throws Exception {
        MockWebContext context = MockWebContext.createMock("/hoge", "examples");
        context.getMockHttpServletRequest().addParameter("hoge[][]", "111");
        Method m = MethodUtil.getDeclaredMethod(Hoge.class, "bbb", new Class[] { int.class, int.class });
        Annotation[][] params = m.getParameterAnnotations();
        IndexParameterResolver resolver = new IndexParameterResolver();
        MockActionContextImpl actionContext = new MockActionContextImpl(context);
        Object resolve = resolver.resolve(actionContext, new MethodDescImpl(m), 1, params[1], int.class);
        assertNotNull(resolve);
        System.out.println(resolve);
        assertTrue(((Integer) resolve).intValue() == 0);
    }

    public void testResolve4() throws Exception {
        MockWebContext context = MockWebContext.createMock("/hoge", "examples");
        context.getMockHttpServletRequest().addParameter("hoge[][", "111");
        Method m = MethodUtil.getDeclaredMethod(Hoge.class, "bbb", new Class[] { int.class, int.class });
        Annotation[][] params = m.getParameterAnnotations();
        IndexParameterResolver resolver = new IndexParameterResolver();
        MockActionContextImpl actionContext = new MockActionContextImpl(context);
        try {
            resolver.resolve(actionContext, new MethodDescImpl(m), 1, params[1], null);
            fail();
        } catch (InvalidRequestParameterTypeRuntimeException expected) {
        }
    }

    public void testResolve5() throws Exception {
        MockWebContext context = MockWebContext.createMock("/hoge", "examples");
        context.getMockHttpServletRequest().addParameter("hoge[1]", "111");
        Method m = MethodUtil.getDeclaredMethod(Hoge.class, "ccc", new Class[] { String.class });
        Annotation[][] params = m.getParameterAnnotations();
        IndexParameterResolver resolver = new IndexParameterResolver();
        MockActionContextImpl actionContext = new MockActionContextImpl(context);
        Object resolve = resolver.resolve(actionContext, new MethodDescImpl(m), 0, params[0], String.class);
        assertNotNull(resolve);
        assertEquals("1", resolve);
    }

    public static class Hoge {

        @ActionParam("hoge[{index}]")
        public void aaa(@Index Integer a) {
        }

        @ActionParam("hoge[{index}][{nestindex}]")
        public void bbb(@Index int a, @Index("nestindex") int b) {
        }

        @ActionParam("hoge[{index}]")
        public void ccc(@Index String a) {
        }
    }
}
