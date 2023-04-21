package net.narusas.daumaccess;

import junit.framework.TestCase;

public class ProxyPoolTest extends TestCase {

    public void testPool() throws InterruptedException {
        ProxyPool pool = new ProxyPool();
        assertEquals(0, pool.size());
        pool.add(new Proxy("0", 0));
        assertEquals(1, pool.size());
        Proxy p = pool.random();
        assertEquals(0, pool.size());
        pool.badProxy(p);
        assertEquals(0, pool.size());
        Thread.sleep(100);
        pool.recoverBad(0);
        assertEquals(1, pool.size());
    }
}
