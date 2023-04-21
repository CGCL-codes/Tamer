package org.nexopenframework.cache.treecache;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertTrue;
import java.lang.management.ManagementFactory;
import javax.management.JMException;
import javax.transaction.TransactionManager;
import org.jboss.cache.TransactionManagerLookup;
import org.jboss.cache.TreeCache;
import org.jboss.cache.loader.CacheLoader;
import org.jboss.system.ServiceController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>test for {@link TreeCacheFactoryBean}</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version $Revision $,$Date: 2009-09-01 21:31:44 +0100 $ 
 * @since 2.0.0.GA
 */
public class TreeCacheFactoryBeanTest {

    @Test
    public void treeCacheFactoryBeanWithTM() throws Exception {
        final TreeCacheFactoryBean tcfb = new TreeCacheFactoryBean();
        tcfb.setBeanName("treeCache");
        tcfb.setClusterName("testCluster");
        tcfb.setCacheMode("LOCAL");
        tcfb.setServer(ManagementFactory.getPlatformMBeanServer());
        final TransactionManager tm = createMock(TransactionManager.class);
        final CacheLoader cacheLoader = createMock(CacheLoader.class);
        cacheLoader.create();
        cacheLoader.start();
        cacheLoader.stop();
        cacheLoader.destroy();
        replay(tm, cacheLoader);
        final TransactionManagerLookup tml = new TransactionManagerLookup() {

            public TransactionManager getTransactionManager() {
                return tm;
            }
        };
        tcfb.setCacheLoader(cacheLoader);
        tcfb.setTransactionManagerLookup(tml);
        tcfb.afterPropertiesSet();
        final Object cache = tcfb.getObject();
        assertTrue(cache instanceof TreeCache);
        tcfb.destroy();
        reset(tm);
    }

    @Test
    public void treeCacheFactoryBeanWithCacheLoader() throws Exception {
        final TreeCacheFactoryBean tcfb = new TreeCacheFactoryBean();
        tcfb.setBeanName("treeCache");
        tcfb.setClusterName("testCluster");
        tcfb.setCacheMode("LOCAL");
        tcfb.setServer(ManagementFactory.getPlatformMBeanServer());
        final CacheLoader cacheLoader = createMock(CacheLoader.class);
        cacheLoader.create();
        cacheLoader.start();
        cacheLoader.stop();
        cacheLoader.destroy();
        replay(cacheLoader);
        tcfb.setCacheLoader(cacheLoader);
        tcfb.afterPropertiesSet();
        Object cache = tcfb.getObject();
        assertTrue(cache instanceof TreeCache);
        tcfb.destroy();
        reset(cacheLoader);
    }

    @Before
    public void start() throws JMException {
        ManagementFactory.getPlatformMBeanServer().registerMBean(new ServiceController(), ServiceController.OBJECT_NAME);
    }

    @After
    public void stop() throws JMException {
        ManagementFactory.getPlatformMBeanServer().unregisterMBean(ServiceController.OBJECT_NAME);
    }
}
