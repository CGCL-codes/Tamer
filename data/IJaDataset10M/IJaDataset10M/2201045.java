package org.nexopenframework.cache.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.nexopenframework.cache.Cache;
import org.nexopenframework.cache.providers.CacheManager;
import org.nexopenframework.cache.providers.ehcache.EhCache;
import org.nexopenframework.cache.service.CacheService;
import org.nexopenframework.cache.service.SimpleService;
import org.nexopenframework.module.kernel.Kernel;
import org.nexopenframework.test.context.junit4.AbstractJUnit4NexOpenContextTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Simple JUnit TestCase for {@link CacheManagerBeanDefinitionParser}</p>
 * 
 * @deprecated This component has been deprecated and removed in future releases
 * @see org.nexopenframework.cache.config.CacheManagerBeanDefinitionParser
 * @see org.nexopenframework.cache.providers.CacheManager
 * @see org.springframework.test.AbstractDependencyInjectionSpringContextTests
 * @author Francesc Xavier Magdaleno
 * @version $Revision $,$Date: 2009-09-01 21:31:44 +0100 $ 
 * @since 2.0.0.GA
 */
@ContextConfiguration(locations = "/openfrwk-module-cache-injection.xml")
public class CacheInjectionBeanDefinitionParserTest extends AbstractJUnit4NexOpenContextTests {

    @Autowired
    CacheManager manager;

    @Autowired
    Kernel kernel;

    @Autowired
    CacheService service;

    @Autowired
    SimpleService simple;

    @Test
    public void checkServiceInjection() {
        assertNotNull(service.getCache());
        service.doBusiness();
        assertTrue(service.getCache().size() > 0);
        assertNotNull(simple.getCache());
        simple.doSimpleBusiness();
        assertTrue(simple.getCache().size() > 0);
        assertEquals(service.getCache(), simple.getCache());
    }

    @Test
    public void kernelNotNull() {
        assertNotNull(kernel);
    }

    @Test
    public void testProcessEhCache() {
        assertEquals(this.applicationContext.getBean("openfrwk.cache.manager"), manager);
        assertTrue(manager.getDefaultCache() instanceof EhCache);
        final Cache cache = manager.getDefaultCache();
        cache.put("some.key", "some.value");
    }
}
