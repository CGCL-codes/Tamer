package com.sohlman.profiler.spring;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.sohlman.profiler.HelperReporter;
import com.sohlman.profiler.TestTimer;
import com.sohlman.profiler.ThreadLocalProfiler;
import com.sohlman.profiler.Timer;
import com.sohlman.profiler.Watch;
import com.sohlman.profiler.spring.beans.Service;

public class TestProfilerInterceptor {

    public Service service;

    public TestTimer testTimer = new TestTimer();

    public HelperReporter reporter;

    @Before
    public void setUp() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "/test-applicationContext.xml" });
        service = (Service) applicationContext.getBean("testServiceRoot");
        Timer.setTimer(testTimer);
        reporter = new HelperReporter();
        ThreadLocalProfiler.setReporter(reporter);
    }

    @Test
    public void testOne() throws Exception {
        Watch watch = ThreadLocalProfiler.start();
        service.one();
        ThreadLocalProfiler.stop(watch, "test()");
        Watch[] watches = reporter.getWatches();
        Assert.assertEquals("Total time", 30, watches[0].getElapsedInMillis());
        Assert.assertEquals("Verify that Profiler interceptor sufficient times", 4, watches.length);
        System.out.println(watches.length + " " + watches[0].getElapsedInMillis());
    }

    @Test
    public void testTwo() throws Exception {
        Watch watch = ThreadLocalProfiler.start();
        service.two();
        ThreadLocalProfiler.stop(watch, "test()");
        Watch[] watches = reporter.getWatches();
        Assert.assertEquals("Total time", 90, watches[0].getElapsedInMillis());
        Assert.assertEquals("Verify that Profiler interceptor sufficient times", 7, watches.length);
        System.out.println(watches.length + " " + watches[0].getElapsedInMillis());
    }

    @Test
    public void testThree() throws Exception {
        Watch watch = ThreadLocalProfiler.start();
        service.three();
        ThreadLocalProfiler.stop(watch, "test()");
        Watch[] watches = reporter.getWatches();
        Assert.assertEquals("Total time", 190, watches[0].getElapsedInMillis());
        Assert.assertEquals("Verify that Profiler interceptor sufficient times", 11, watches.length);
        System.out.println(watches.length + " " + watches[0].getElapsedInMillis());
    }
}
