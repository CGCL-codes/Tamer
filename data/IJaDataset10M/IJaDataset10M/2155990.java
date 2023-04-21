package org.t2framework.t2.action.parameter.impl;

import java.lang.reflect.Method;
import java.util.Map;
import junit.framework.TestCase;
import org.t2framework.commons.meta.MethodDesc;
import org.t2framework.commons.meta.impl.MethodDescImpl;
import org.t2framework.t2.action.parameter.impl.RequestHeaderResolver;
import org.t2framework.t2.annotation.core.Page;
import org.t2framework.t2.annotation.core.RequestHeader;
import org.t2framework.t2.mock.MockActionContextImpl;
import org.t2framework.t2.mock.MockWebContext;
import org.t2framework.t2.spi.Navigation;

public class RequestHeaderResolverTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void test1_getHeadersAsMap() throws Exception {
        RequestHeaderResolver resolver = new RequestHeaderResolver();
        {
            MockWebContext mock = MockWebContext.createMock("aaa");
            mock.getMockHttpServletRequest().addHeader("hoge1", "foo");
            mock.getMockHttpServletRequest().addHeader("hoge2", "123");
            MockActionContextImpl actionContext = new MockActionContextImpl(mock);
            final Method m = HogePage.class.getDeclaredMethod("execute", new Class[] { Map.class });
            final MethodDesc md = new MethodDescImpl(m);
            Object resolve = resolver.resolve(actionContext, md, 0, m.getParameterAnnotations()[0], m.getParameterTypes()[0]);
            assertNotNull(resolve);
            Map<String, Object> map = (Map<String, Object>) resolve;
            assertEquals("foo", map.get("hoge1"));
            assertEquals("123", map.get("hoge2"));
        }
    }

    public void test2_getHeaderByKey() throws Exception {
        RequestHeaderResolver resolver = new RequestHeaderResolver();
        {
            MockWebContext mock = MockWebContext.createMock("aaa");
            mock.getMockHttpServletRequest().addHeader("hoge1", "foo");
            mock.getMockHttpServletRequest().addHeader("hoge2", "123");
            MockActionContextImpl actionContext = new MockActionContextImpl(mock);
            final Method m = HogePage.class.getDeclaredMethod("execute2", new Class[] { String.class });
            final MethodDesc md = new MethodDescImpl(m);
            Object resolve = resolver.resolve(actionContext, md, 0, m.getParameterAnnotations()[0], m.getParameterTypes()[0]);
            assertNotNull(resolve);
            assertEquals("123", resolve);
        }
    }

    public void test3_getHeadersByKey() throws Exception {
        RequestHeaderResolver resolver = new RequestHeaderResolver();
        {
            MockWebContext mock = MockWebContext.createMock("aaa");
            mock.getMockHttpServletRequest().addHeader("hoge3", "foo");
            mock.getMockHttpServletRequest().addHeader("hoge3", "123");
            MockActionContextImpl actionContext = new MockActionContextImpl(mock);
            final Method m = HogePage.class.getDeclaredMethod("execute3", new Class[] { String[].class });
            final MethodDesc md = new MethodDescImpl(m);
            Object resolve = resolver.resolve(actionContext, md, 0, m.getParameterAnnotations()[0], m.getParameterTypes()[0]);
            assertNotNull(resolve);
            String[] s = (String[]) resolve;
            assertEquals("foo", s[0]);
            assertEquals("123", s[1]);
        }
    }

    public void test4_getHeader_illegalType() throws Exception {
        RequestHeaderResolver resolver = new RequestHeaderResolver();
        {
            MockWebContext mock = MockWebContext.createMock("aaa");
            mock.getMockHttpServletRequest().addHeader("hoge1", "foo");
            mock.getMockHttpServletRequest().addHeader("hoge2", "123");
            MockActionContextImpl actionContext = new MockActionContextImpl(mock);
            final Method m = HogePage.class.getDeclaredMethod("illegalType_youCanNotGetAsMapWhenKeyIsSpecified", new Class[] { Map.class });
            final MethodDesc md = new MethodDescImpl(m);
            Object resolve = resolver.resolve(actionContext, md, 0, m.getParameterAnnotations()[0], m.getParameterTypes()[0]);
            assertNull(resolve);
        }
    }

    @Page("hoge")
    public static class HogePage {

        public Navigation execute(@RequestHeader Map<String, String> headers) {
            return null;
        }

        public Navigation execute2(@RequestHeader(key = "hoge2") String s) {
            return null;
        }

        public Navigation execute3(@RequestHeader(key = "hoge3") String[] s) {
            return null;
        }

        public Navigation illegalType_youCanNotGetAsMapWhenKeyIsSpecified(@RequestHeader(key = "hoge2") Map<String, String> s) {
            return null;
        }
    }
}
