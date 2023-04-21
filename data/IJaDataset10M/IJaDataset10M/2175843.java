package org.archive.hcc.client;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.archive.hcc.util.JmxUtils;

public class ClusterControllerClientImplSelfTest extends ClusterControllerClientSelfTestBase {

    public void testListCrawlers() {
        try {
            Collection<Crawler> crawlers = cc.listCrawlers();
            assertNotNull(crawlers);
        } catch (ClusterException e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    public void testCrawlerLifecycleListener() {
        AllPurposeTestListener l = new AllPurposeTestListener();
        try {
            cc.addCrawlerLifecycleListener(l);
            Crawler c = cc.createCrawler();
            try {
                assertTrue(l.crawlerCreatedLatch.await(20 * 1000, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals(c, l.c);
            c.destroy();
            try {
                assertTrue(l.crawlerDestroyedLatch.await(20 * 1000, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals(c, l.c);
        } catch (ClusterException e) {
            e.printStackTrace();
            assertFalse(true);
        } finally {
            cc.removeCrawlerLifecycleListener(l);
        }
    }

    public void testGetCurrentCrawlJob() {
        AllPurposeTestListener l = new AllPurposeTestListener();
        try {
            cc.addCrawlerLifecycleListener(l);
            cc.addCrawlJobListener(l);
            cc.createCrawler();
            try {
                assertTrue(l.crawlerCreatedLatch.await(10 * 1000, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            l.c.addJob(new JobOrder("test1", "", getTestJar()));
            l.c.startPendingJobQueue();
            try {
                assertTrue(l.crawlJobStartedLatch.await(10 * 1000, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CurrentCrawlJob ccj = cc.getCurrentCrawlJob(l.c);
            assertTrue(ccj.equals(l.j));
            l.c.destroy();
            try {
                assertTrue(l.crawlerDestroyedLatch.await(10 * 1000, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (ClusterException e) {
            e.printStackTrace();
            assertFalse(true);
        } finally {
            cc.removeCrawlerLifecycleListener(l);
            cc.removeCrawlJobListener(l);
        }
    }

    public void testGetSetMaxInstances() {
        try {
            Crawler c = cc.createCrawler();
            InetSocketAddress a = JmxUtils.extractRemoteAddress(c.getName());
            int max = cc.getMaxInstances(a.getHostName(), a.getPort());
            assertTrue(max > 0);
            cc.setMaxInstances(a.getHostName(), a.getPort(), ++max);
            int newMax = cc.getMaxInstances(a.getHostName(), a.getPort());
            assertEquals(max, newMax);
            c.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }
}
