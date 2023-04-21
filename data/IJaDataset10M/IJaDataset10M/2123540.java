package org.impalaframework.web.listener;

import static org.easymock.EasyMock.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.impalaframework.util.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;
import junit.framework.TestCase;

public class ServletContextListenerFactoryBeanTest extends TestCase {

    private ServletContextListenerFactoryBean bean;

    private ServletContext servletContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bean = new ServletContextListenerFactoryBean();
        servletContext = createMock(ServletContext.class);
        bean.setServletContext(servletContext);
        bean.setListenerClass(TestListener.class.getName());
        bean.setBeanClassLoader(ClassUtils.getDefaultClassLoader());
    }

    public void testAfterPropertiesSet() throws Exception {
        replay(servletContext);
        bean.afterPropertiesSet();
        bean.destroy();
        ServletContextListener listener = bean.getListener();
        assertTrue(listener instanceof TestListener);
        TestListener testListener = ObjectUtils.cast(listener, TestListener.class);
        assertEquals(1, testListener.getInitializedCount());
        assertEquals(1, testListener.getDestroyedCount());
        assertTrue(testListener.isAppContextAwareCalled());
        verify(servletContext);
    }
}

class TestListener implements ServletContextListener, ApplicationContextAware {

    private int initializedCount;

    private int destroyedCount;

    private boolean appContextAwareCalled;

    public void contextInitialized(ServletContextEvent event) {
        initializedCount++;
    }

    public void contextDestroyed(ServletContextEvent event) {
        destroyedCount++;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContextAwareCalled = true;
    }

    public boolean isAppContextAwareCalled() {
        return appContextAwareCalled;
    }

    public int getDestroyedCount() {
        return destroyedCount;
    }

    public int getInitializedCount() {
        return initializedCount;
    }
}
