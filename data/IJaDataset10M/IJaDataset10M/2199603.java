package corner.study.proxy;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.testng.Assert;
import org.testng.annotations.Test;
import corner.study.proxy.model.Foo;
import corner.study.proxy.model.FooImpl;

/**
 * 基于Cglib的代理类
 * 官方网站 : http://cglib.sourceforge.net/
 * 相关资料 : http://www.google.com/search?q=cglib&ie=utf-8&oe=utf-8
 * @author <a href="mailto:jun.tsai@bjmaxinfo.com">Jun Tsai</a>
 * @version $Revision: 2333 $
 * @since 2.3
 */
public class CglibProxyTest extends Assert {

    public static class CglibInvocationHandler implements MethodInterceptor {

        public Object intercept(Object o, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            System.out.println("------------------------now to execute method [" + method.getName() + "]-----------------------------");
            System.out.println("catch before method from cglib proxy");
            Object result = proxy.invokeSuper(o, args);
            System.out.println("catch after method from cglib proxy");
            return result;
        }
    }

    public static class CglibProxy {

        public static <T> T createProxy(Class<T> clazz) throws Throwable {
            Enhancer enhancer = new Enhancer();
            CglibInvocationHandler handler = new CglibInvocationHandler();
            enhancer.setSuperclass(clazz);
            enhancer.setCallback(handler);
            return (T) enhancer.create();
        }
    }

    @Test
    public void test_cglib_proxy() throws Throwable {
        Foo foo = CglibProxy.createProxy(FooImpl.class);
        assertEquals(foo.getSayHello(), "hello");
    }
}
