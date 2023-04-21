package org.t2framework.t2.action.parameter.impl;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import junit.framework.TestCase;
import org.t2framework.commons.meta.impl.MethodDescImpl;
import org.t2framework.commons.mock.MockHttpServletRequest;
import org.t2framework.commons.util.Reflections.MethodUtil;
import org.t2framework.t2.action.parameter.impl.HttpServletRequestParameterResolver;
import org.t2framework.t2.contexts.WebContext;
import org.t2framework.t2.mock.MockActionContextImpl;
import org.t2framework.t2.mock.MockWebContext;
import org.t2framework.t2.spi.Navigation;

public class HttpServletRequestParameterResolverTest extends TestCase {

    public void test1_simpllyGet() throws Exception {
        HttpServletRequestParameterResolver resolver = new HttpServletRequestParameterResolver();
        {
            MockWebContext mock = MockWebContext.createMock("/emp");
            MockHttpServletRequest req = mock.getMockHttpServletRequest();
            MockActionContextImpl actionContext = new MockActionContextImpl(mock);
            Method m = MethodUtil.getDeclaredMethod(Emp.class, "list", new Class[] { HttpServletRequest.class });
            Object resolve = resolver.resolve(actionContext, new MethodDescImpl(m), 0, m.getParameterAnnotations()[0], WebContext.class);
            assertNotNull(resolve);
            assertEquals(req, resolve);
        }
    }

    public static class Emp {

        public Navigation list(HttpServletRequest request) {
            return null;
        }
    }
}
