package com.od.jtimeseries.capture.impl;

import com.od.jtimeseries.capture.TimedCapture;
import com.od.jtimeseries.capture.function.CaptureFunctions;
import com.od.jtimeseries.scheduling.Scheduler;
import com.od.jtimeseries.source.ValueSource;
import com.od.jtimeseries.source.impl.DefaultValueRecorder;
import com.od.jtimeseries.timeseries.TimeSeriesItem;
import com.od.jtimeseries.timeseries.impl.DefaultIdentifiableTimeSeries;
import com.od.jtimeseries.util.time.Time;
import junit.framework.TestCase;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: nick
 * Date: 27-Nov-2009
 * Time: 22:44:44
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractTestTimedCaptureScheduling extends TestCase {

    private Scheduler s;

    public TimedCapture timedCapture;

    public CountDownLatch latch;

    public void setUp() {
        s = createScheduler();
        latch = new CountDownLatch(1);
        ValueSource valueRecorder = new DefaultValueRecorder("test recorder", "test recorder");
        DefaultIdentifiableTimeSeries timeSeries = new DefaultIdentifiableTimeSeries("timeseries", "timeseries") {

            public void addItem(TimeSeriesItem value) {
                latch.countDown();
            }
        };
        timedCapture = new DefaultTimedCapture("test timed capture", valueRecorder, timeSeries, CaptureFunctions.MAX(Time.milliseconds(10)));
    }

    protected abstract Scheduler createScheduler();

    public void tearDown() {
        s.stop();
        s = null;
        latch = null;
        timedCapture = null;
    }

    public void testTimedCaptureFiresWhenCaptureStartedThenSchedulerStarted() {
        timedCapture.start();
        s.addTriggerable(timedCapture);
        s.start();
        try {
            boolean success = latch.await(1000, TimeUnit.MILLISECONDS);
            assertTrue("Timed Out Waiting for capture", success);
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }

    public void testTimedCaptureFiresWhenSchedulerStartedThenCaptureStarted() {
        s.addTriggerable(timedCapture);
        s.start();
        timedCapture.start();
        try {
            boolean success = latch.await(1000, TimeUnit.MILLISECONDS);
            assertTrue("Timed Out Waiting for capture", success);
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }

    public void testTimedCaptureFiresWhenAddedToSchedulerAfterSchedulerStarted() {
        s.start();
        s.addTriggerable(timedCapture);
        timedCapture.start();
        try {
            boolean success = latch.await(1000, TimeUnit.MILLISECONDS);
            assertTrue("Timed Out Waiting for capture", success);
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }

    public void testTimedCaptureDoesNotFiresWhenNotStarted() {
        s.start();
        s.addTriggerable(timedCapture);
        try {
            boolean success = latch.await(100, TimeUnit.MILLISECONDS);
            assertFalse("Capture Triggered", success);
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }
}
