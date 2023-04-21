package com.nhncorp.usf.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link OgnlUtil} Concurrent Test.
 *
 * @author Web Platform Development Team
 */
public class OgnlUtilConcurrentTest {

    private Log log = LogFactory.getLog(OgnlUtilConcurrentTest.class);

    private AtomicInteger totalCount = new AtomicInteger();

    private AtomicInteger failCount = new AtomicInteger();

    private int threadCount = 1000;

    private Random random = new Random();

    private static Map<String, String> map = new HashMap<String, String>();

    @BeforeClass
    public static void beforeClass() {
        map.put("query0", "value");
        map.put("query1", "value");
        map.put("query2", "value");
    }

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
        failCount.set(0);
        totalCount.set(0);
    }

    @Test
    public void testing1() throws Exception {
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int j = 0; j < threadCount; j++) {
            new ThreadWork(latch, 0).start();
        }
        latch.await();
        showResult("testing1", startTime);
    }

    @Test
    public void testing2() throws Exception {
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int j = 0; j < threadCount; j++) {
            new ThreadWork(latch, 1).start();
        }
        latch.await();
        showResult("testing2", startTime);
    }

    private void showResult(String test, long startTime) {
        long endTime = (System.currentTimeMillis() - startTime);
        log.debug("[" + test + "] total time : " + endTime + ", count: " + failCount.get() + "/" + totalCount.get());
    }

    protected class ThreadWork extends Thread {

        private CountDownLatch latch;

        private int type;

        ThreadWork(CountDownLatch latch, int type) {
            this.latch = latch;
            this.type = type;
        }

        public void run() {
            for (int idx = 0; idx < 100; idx++) {
                try {
                    int index = random.nextInt(2);
                    String query = "query" + index;
                    if (type == 0) {
                        OgnlUtil.evaluateAndGet(query, map);
                    } else {
                        OgnlUtil.evaluateAndGet(query, map);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failCount.getAndIncrement();
                } finally {
                    totalCount.getAndIncrement();
                }
            }
            latch.countDown();
        }
    }
}
