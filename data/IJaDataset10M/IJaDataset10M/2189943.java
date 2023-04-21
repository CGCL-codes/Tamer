package org.springframework.util;

import java.util.Date;
import junit.framework.TestCase;

/**
 * @author Alef Arendsen
 */
public class ResponseTimeMonitorTests extends TestCase {

    public void testGetAccessCount() {
        ResponseTimeMonitorImpl impl = new ResponseTimeMonitorImpl();
        assertEquals(impl.getAverageResponseTimeMillis(), 0);
        impl.recordResponseTime(100);
        impl.recordResponseTime(100);
        impl.recordResponseTime(100);
        impl.recordResponseTime(300);
        impl.recordResponseTime(200);
        assertEquals(impl.getAccessCount(), 5);
    }

    public void testGetUptime() {
        long begin = System.currentTimeMillis();
        ResponseTimeMonitorImpl impl = new ResponseTimeMonitorImpl();
        long upTime = impl.getUptimeMillis();
        assertEquals(upTime, System.currentTimeMillis() - begin, 300);
    }

    public void testGetLoadDate() {
        Date now = new Date();
        ResponseTimeMonitorImpl impl = new ResponseTimeMonitorImpl();
        Date d = impl.getLoadDate();
        assertEquals(now.getTime(), d.getTime(), 300);
    }

    public void testGetAverageResponseTimeMillis() {
        ResponseTimeMonitorImpl impl = new ResponseTimeMonitorImpl();
        assertEquals(impl.getAverageResponseTimeMillis(), 0);
        impl.recordResponseTime(100);
        impl.recordResponseTime(100);
        impl.recordResponseTime(100);
        assertEquals(impl.getAverageResponseTimeMillis(), 100);
        impl.recordResponseTime(300);
        impl.recordResponseTime(200);
        assertEquals(impl.getAverageResponseTimeMillis(), 160);
    }

    public void testGetBestResponseTimeMillis() {
        ResponseTimeMonitorImpl impl = new ResponseTimeMonitorImpl();
        assertEquals(impl.getAverageResponseTimeMillis(), 0);
        impl.recordResponseTime(100);
        impl.recordResponseTime(100);
        impl.recordResponseTime(100);
        assertEquals(impl.getAverageResponseTimeMillis(), 100);
        impl.recordResponseTime(300);
        impl.recordResponseTime(200);
        impl.recordResponseTime(60);
        impl.recordResponseTime(500);
        assertEquals(impl.getBestResponseTimeMillis(), 60);
    }

    public void testGetWorstResponseTimeMillis() {
        ResponseTimeMonitorImpl impl = new ResponseTimeMonitorImpl();
        assertEquals(impl.getAverageResponseTimeMillis(), 0);
        impl.recordResponseTime(100);
        impl.recordResponseTime(100);
        impl.recordResponseTime(100);
        assertEquals(impl.getAverageResponseTimeMillis(), 100);
        impl.recordResponseTime(300);
        impl.recordResponseTime(200);
        impl.recordResponseTime(60);
        impl.recordResponseTime(500);
        assertEquals(impl.getWorstResponseTimeMillis(), 500);
    }
}
