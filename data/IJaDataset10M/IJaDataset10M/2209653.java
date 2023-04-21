package test.config;

import junit.framework.TestCase;
import net.sf.joyaop.AroundAdvice;
import net.sf.joyaop.Interceptor;
import net.sf.joyaop.config.Configuration;
import net.sf.joyaop.config.XmlConfiguration;
import net.sf.joyaop.framework.AspectFactory;
import net.sf.joyaop.framework.AspectRuntime;
import net.sf.joyaop.framework.InterceptorAspect;
import net.sf.joyaop.framework.InterfaceAspect;
import net.sf.joyaop.framework.MixinAspect;
import net.sf.joyaop.framework.RunnableAspect;
import net.sf.joyaop.impl.Pointcuts;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shen Li
 */
public class ConfigTest extends TestCase {

    public void testConfig() {
        MockAspectRuntime aspectRuntime = new MockAspectRuntime();
        Configuration config = new XmlConfiguration("test/config/test-config.xml");
        config.configure(aspectRuntime);
        aspectRuntime.verify();
    }

    public static class MockAspectRuntime implements AspectRuntime {

        private final List interceptors = new ArrayList();

        private final List mixins = new ArrayList();

        private final List interfaces = new ArrayList();

        public void addInterceptor(InterceptorAspect interceptorAspect) {
            interceptors.add(interceptorAspect);
        }

        public void addMixin(MixinAspect mixinAspect) {
            mixins.add(mixinAspect);
        }

        public void addInterface(InterfaceAspect interfaceAspect) {
            interfaces.add(interfaceAspect);
        }

        public void verify() {
            assertEquals(2, interceptors.size());
            InterceptorAspect interceptorAspect1 = (InterceptorAspect) interceptors.get(0);
            InterceptorAspect interceptorAspect2 = (InterceptorAspect) interceptors.get(1);
            assertEquals(MockInterceptor.class, interceptorAspect1.getAspectClass());
            assertEquals(MockInterceptor2.class, interceptorAspect2.getAspectClass());
            assertEquals(RunnableAspect.THREAD_SCOPE, interceptorAspect1.getScope());
            assertEquals(RunnableAspect.JVM_SCOPE, interceptorAspect2.getScope());
            assertEquals(Pointcuts.byRegexp("aaa", null), interceptorAspect1.getPointcut());
            assertEquals(Pointcuts.byRegexp("aaa", null), interceptorAspect2.getPointcut());
            assertEquals(2, interceptorAspect1.getPrecedence());
            assertEquals(1, interceptorAspect2.getPrecedence());
            assertEquals(1, interceptorAspect1.getParameters().size());
            assertEquals("value1", interceptorAspect1.getParameters().get("param1"));
            assertEquals(1, interceptorAspect2.getParameters().size());
            assertEquals("value1", interceptorAspect2.getParameters().get("param1"));
            assertEquals(2, mixins.size());
            MixinAspect mixinAspect1 = (MixinAspect) mixins.get(0);
            MixinAspect mixinAspect2 = (MixinAspect) mixins.get(1);
            assertEquals(MockMixin.class, mixinAspect1.getInterfaceClass());
            assertEquals(MockMixin.class, mixinAspect2.getInterfaceClass());
            assertEquals(MockMixinImpl.class, mixinAspect1.getAspectClass());
            assertEquals(MockMixinImpl2.class, mixinAspect2.getAspectClass());
            assertEquals(RunnableAspect.JVM_SCOPE, mixinAspect1.getScope());
            assertEquals(RunnableAspect.INSTANCE_SCOPE, mixinAspect2.getScope());
            assertEquals(Pointcuts.byRegexp("aaa", null), mixinAspect1.getPointcut());
            assertEquals(Pointcuts.byRegexp("aaa", null), mixinAspect2.getPointcut());
            assertEquals(1, mixinAspect1.getParameters().size());
            assertEquals("value2", mixinAspect1.getParameters().get("param2"));
            assertEquals(1, mixinAspect2.getParameters().size());
            assertEquals("value2", mixinAspect2.getParameters().get("param2"));
        }

        public AspectFactory getAspectFactory() {
            return null;
        }

        public void setAspectFactory(AspectFactory aspectFactory) {
        }

        public Object newInstance(Class clazz) {
            return null;
        }

        public Object newInstance(Class clazz, Class[] argTypes, Object[] args) {
            return null;
        }

        public Object newInstance(String className) {
            return null;
        }

        public Object newInstance(String className, Class[] argTypes, Object[] args) {
            return null;
        }
    }

    public abstract static class MockInterceptor implements AroundAdvice {
    }

    public abstract static class MockInterceptor2 implements Interceptor {
    }

    public static interface MockMixin {
    }

    public static class MockMixinImpl implements MockMixin {
    }

    public static class MockMixinImpl2 implements MockMixin {
    }
}
