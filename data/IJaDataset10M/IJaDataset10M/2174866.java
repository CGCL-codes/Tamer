package org.t2framework.lucy.aop.config.impl;

import java.lang.reflect.Method;
import org.t2framework.commons.annotation.composite.SingletonScope;
import org.t2framework.commons.aop.spi.Aspect;
import org.t2framework.commons.aop.spi.Interceptor;
import org.t2framework.commons.aop.spi.Invocation;
import org.t2framework.commons.meta.BeanDesc;
import org.t2framework.lucy.Lucy;
import org.t2framework.lucy.aop.config.AspectConfig;
import org.t2framework.lucy.aop.config.impl.AspectConfigImpl;
import org.t2framework.lucy.aop.impl.PointcutImpl;
import org.t2framework.lucy.ut.LucyTestCase;

public class AspectConfigImplTest extends LucyTestCase {

    public void testCreate() throws Exception {
        Lucy lucy = getLucy();
        lucy.register(HogeInterceptor.class);
        AspectConfigImpl config = new AspectConfigImpl(lucy, new PointcutImpl("^hoge"));
        Aspect aspect = config.createAspect();
        assertNotNull(aspect);
        assertEquals("^hoge", aspect.getPointcut().getPatternString());
    }

    @SingletonScope
    public static class HogeInterceptor implements Interceptor {

        @Override
        public Object intercept(Invocation<Method> invocation) throws Throwable {
            return null;
        }
    }

    public void testExpression() throws Exception {
        getLucy().load("org/t2framework/lucy/aop/config/impl/AspectConfigImplTest.xml");
        assertInterceptorSize("one", 1);
        assertInterceptorSize("list", 2);
        assertInterceptorSize("ary", 2);
        assertInterceptorSize("warn", 1);
        assertInterceptorSize("warn2", 0);
    }

    protected void assertInterceptorSize(String key, int size) {
        Lucy lucy = getLucy();
        BeanDesc<?> desc = lucy.getBeanDesc(key);
        AspectConfig cd = (AspectConfig) desc.getClassDesc().findConfig(AspectConfig.class);
        Aspect a = cd.createAspect();
        assertEquals(size, a.getInterceptors().size());
    }
}
