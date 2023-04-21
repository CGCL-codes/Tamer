package com.google.code.ssm.test.counter;

import static org.junit.Assert.assertEquals;
import com.google.code.ssm.test.svc.TestSvc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * 
 * @author Jakub Białek
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath*:META-INF/test-context.xml", "classpath*:simplesm-context.xml" })
public class UpdateCounterInCacheTest {

    @Autowired
    private TestSvc test;

    @Test
    public void test() throws InterruptedException {
        String key = "my2-counter-key";
        test.increment(key);
        test.getCounter(key);
        long value = 777L;
        test.update(key, value);
        Thread.sleep(100);
        assertEquals(value, test.getCounter(key));
        assertEquals(value, test.getCounter(key));
        assertEquals(value, test.getCounter(key));
        test.increment(key);
        test.increment(key);
        test.increment(key);
        assertEquals((long) value + 3, (long) test.getCounter(key));
        String key2 = "my3-counter-key2";
        value = 123L;
        test.update(key2, value);
        test.increment(key2);
        test.getCounter(key2);
        Thread.sleep(100);
        assertEquals((long) value + 1, (long) test.getCounter(key2));
        assertEquals((long) value + 1, (long) test.getCounter(key2));
        assertEquals((long) value + 1, (long) test.getCounter(key2));
        String key3 = "my3-counter-key3";
        value = 333L;
        test.increment(key3);
        test.getCounter(key3);
        Thread.sleep(100);
        test.update(key3, value);
        assertEquals(value, test.getCounter(key3));
        assertEquals(value, test.getCounter(key3));
        assertEquals(value, test.getCounter(key3));
    }
}
