package org.lightframework.mvc.plugin.spring;

import java.util.LinkedList;
import org.lightframework.mvc.Action;
import org.lightframework.mvc.Plugin;
import org.lightframework.mvc.PluginManager;
import org.lightframework.mvc.RouteManager;
import org.lightframework.mvc.test.MockModule;
import org.lightframework.mvc.test.MvcTestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * test case of {@link SpringPlugin}
 * 
 * @author fenghm (fenghm@bingosoft.net)
 * @since 1.0.0
 */
public class TestSpringPlugin extends MvcTestCase {

    private static ApplicationContext spring;

    private static MockModule root;

    @Override
    protected void setUpOnlyOnce() throws Exception {
        spring = new ClassPathXmlApplicationContext("classpath*:spring/lightmvc.xml");
    }

    @Override
    protected void setUpEveryTest() throws Exception {
        if (null != root) {
            module = root;
            request.setModule(module);
        }
        module.setPackagee(packagee);
    }

    @Override
    protected void tearDownEveryTest() throws Exception {
        if (null == root) {
            root = module;
        }
    }

    public void tesrRouteRegister() throws Throwable {
        assertEquals(4, RouteManager.table().size());
    }

    public void testPluginRegister() throws Throwable {
        LinkedList<Plugin> plugins = PluginManager.getPlugins();
        assertEquals(2, plugins.size());
        PluginRegistry registry = spring.getBean(PluginRegistry.class);
        assertEquals(2, registry.getPlugins().size());
    }

    public void testPluginResolve1() throws Throwable {
        request("/test1");
        Object controller = spring.getBean("test1Controller");
        Action action = request.getAction();
        assertEquals(controller, action.getControllerObject());
        assertNotNull(action.getControllerObject());
        assertNotNull(action.getControllerClass());
        request("/test1Controller");
        action = request.getAction();
        assertEquals(controller, action.getControllerObject());
        request("/test1/list");
        action = request.getAction();
        assertEquals(controller, action.getControllerObject());
        assertNotNull(action.getMethod());
        assertEquals("list", action.getMethod().getName());
        request("/test_manage/list");
        action = request.getAction();
        assertEquals(controller, action.getControllerObject());
        assertNotNull(action.getMethod());
        assertEquals("list", action.getMethod().getName());
    }

    public void testPluginResolve2() throws Throwable {
        request("/test2");
        Object controller = spring.getBean("test2Service");
        Action action = request.getAction();
        assertEquals(controller, action.getControllerObject());
        assertNotNull(action.getControllerObject());
        assertNotNull(action.getControllerClass());
        request("/test2Service");
        action = request.getAction();
        assertEquals(controller, action.getControllerObject());
    }

    public void testPluginResolve3() throws Throwable {
        request("/test3");
        Object controller = spring.getBean("test3");
        Action action = request.getAction();
        assertEquals(controller, action.getControllerObject());
        assertNotNull(action.getControllerObject());
        assertNotNull(action.getControllerClass());
    }
}
