package com.ibm.realtime.rtmb.tests.util.RTSJSpecificTestUtils;

import javax.realtime.Clock;
import javax.realtime.RealtimeThread;
import javax.realtime.RelativeTime;
import com.ibm.realtime.rtmb.tests.util.BenchMarkTimer;
import com.ibm.realtime.rtmb.util.RTSJSpecificTest;

@RTSJSpecificTest
public class RTBenchMarkTimer extends BenchMarkTimer {

    Clock c = Clock.getRealtimeClock();

    @Override
    public void sleep(long milliseconds) throws InterruptedException {
        RelativeTime r = new RelativeTime(milliseconds, 0);
        RealtimeThread.sleep(c, r);
    }

    @Override
    public void sleep(long milliseconds, int nanoseconds) throws InterruptedException {
        RealtimeThread.sleep(c, new RelativeTime(milliseconds, nanoseconds));
    }
}
