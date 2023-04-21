package org.bing.engine.controller.preference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath*:/META-INF/bing-controller-spring-*.xml", "classpath*:/META-INF/bing-controller-*-spring.xml" })
public class PreferenceManagerTest extends AbstractJUnit4SpringContextTests {

    private PreferenceManager manager;

    @Before
    public void setUp() throws Exception {
        manager = (PreferenceManager) this.applicationContext.getBean("controller.PreferenceManager");
    }

    @Test
    public void testGetControllerGuid() {
        String guid = manager.getControllerGuid();
        String guid2 = manager.getControllerGuid();
        Assert.assertNotNull(guid);
        Assert.assertNotNull(guid2);
        Assert.assertTrue(guid.trim().length() > 0);
        Assert.assertEquals(guid, guid2);
    }
}
