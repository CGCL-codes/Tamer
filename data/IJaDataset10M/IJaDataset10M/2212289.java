package com.mycila.testing.plugin.spring;

import com.mycila.testing.core.MycilaTesting;
import static com.mycila.testing.plugin.spring.Bean.Scope.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@SpringContext(locations = "/ctx-autowired.xml")
public final class BeanAutowiredTest {

    @Bean(name = "myBean")
    MyBean myBean = new MyBean("toto");

    @Bean(name = "abean")
    String a = "helloa";

    @Bean(name = "bbean", scope = PROTOTYPE)
    String b = "hellob";

    @Autowired
    ApplicationContext injector;

    @Test
    public void test_bind() {
        MycilaTesting.from(getClass()).createNotifier(this).prepare();
        assertEquals(injector.getBean("abean"), "helloa");
        assertEquals(injector.getBean("bbean"), "hellob");
        assertEquals(injector.getBean("myBean").toString(), "toto");
        assertEquals(injector.getBean("myBean").toString(), "toto");
        AutowiredBean bean = (AutowiredBean) injector.getBean("autowiredBean");
        assertNotNull(bean.mybean);
        assertEquals(bean.mybean, myBean);
        assertNotNull(bean.bbean);
        assertEquals(bean.bbean, b);
        b = "changedb";
        bean = (AutowiredBean) injector.getBean("autowiredBean");
        assertEquals(injector.getBean("bbean"), b);
        assertEquals(bean.bbean, b);
    }
}
