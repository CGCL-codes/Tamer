package net.bgraham.collectionbatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;
import net.bgraham.collectionbatcher.ArrayListBatcher;
import net.bgraham.collectionbatcher.IBatchListener;
import net.bgraham.collectionbatcher.Log;
import org.junit.Before;
import org.junit.Test;

public class ArrayListBatcherTestForIntegers extends TestCase {

    private static long maxTimeToWaitBetweenBatches = 900;

    private static long maxTimeToWaitBetweenUpdates = 100;

    static long count = 0;

    static long batches = 0;

    static long fullTimeoutCount = 0;

    static long earlyTimeoutCount = 0;

    @Override
    @Before
    public void setUp() {
        count = 0;
        batches = 0;
        fullTimeoutCount = 0;
        earlyTimeoutCount = 0;
    }

    @Test
    public static void testBatcherCallsCallbackIfExitIsCalledWithOutstandingData() {
        count = 0;
        Log.log("testBatcherCallsCallbackIfExitIsCalledWithOutstandingData()");
        final List<Integer> receivedList = new ArrayList<Integer>();
        List<Integer> sentList = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            sentList.add(new Integer(i));
        }
        ArrayListBatcher<Integer> batcher = new ArrayListBatcher<Integer>(new IBatchListener<Integer>() {

            public void batchCollected(Collection<Integer> batch, boolean fullTimeoutHit) {
                receivedList.addAll(batch);
                batches++;
                if (fullTimeoutHit) {
                    fullTimeoutCount++;
                } else {
                    earlyTimeoutCount++;
                }
            }
        }, maxTimeToWaitBetweenBatches, maxTimeToWaitBetweenUpdates);
        for (Integer val : sentList) {
            batcher.add(val);
        }
        batcher.exit();
        Util.doSleep(1000);
        Util.printBatchStats(batches, fullTimeoutCount, earlyTimeoutCount);
        Util.printResults(sentList.size(), receivedList.size());
        assertTrue("Received values are different than sent values", sentList.equals(receivedList));
        Log.log("\n");
    }

    @Test
    public static void testBatcherWithTenThousandIntegersAndDelaysBetweenAddsShouldHaveNoLosses() {
        count = 0;
        Log.log("testBatcherWithTenThousandIntegersAndDelaysBetweenAddsShouldHaveNoLosses()");
        final List<Integer> receivedList = new ArrayList<Integer>();
        List<Integer> sentList = new ArrayList<Integer>();
        for (int i = 0; i < 10000; i++) {
            sentList.add(new Integer(i));
        }
        ArrayListBatcher<Integer> batcher = new ArrayListBatcher<Integer>(new IBatchListener<Integer>() {

            public void batchCollected(Collection<Integer> batch, boolean fullTimeoutHit) {
                receivedList.addAll(batch);
                if (++count % 25 == 0) {
                    Log.log("size = " + receivedList.size());
                    Util.printBatchStats(batches, fullTimeoutCount, earlyTimeoutCount);
                }
                batches++;
                if (fullTimeoutHit) {
                    fullTimeoutCount++;
                } else {
                    earlyTimeoutCount++;
                }
            }
        }, maxTimeToWaitBetweenBatches, maxTimeToWaitBetweenUpdates);
        for (Integer val : sentList) {
            batcher.add(val);
            if (val.intValue() % 100 == 0) {
                Util.doSleep((long) (Math.random() * 150));
            }
        }
        Util.doSleep(1000);
        batcher.exit();
        Util.printBatchStats(batches, fullTimeoutCount, earlyTimeoutCount);
        Util.printResults(sentList.size(), receivedList.size());
        assertTrue("Received values are different than sent values", sentList.equals(receivedList));
        Log.log("\n");
    }

    @Test
    public static void testBatcherWithOneMillionIntegersAndNoDelayBetweenAddsShouldHaveNoLosses() {
        count = 0;
        Log.log("testBatcherWithOneMillionIntegersAndNoDelayBetweenAddsShouldHaveNoLosses()");
        final List<Integer> receivedList = new ArrayList<Integer>();
        List<Integer> sentList = new ArrayList<Integer>();
        for (int i = 0; i < 1000000; i++) {
            sentList.add(new Integer(i));
        }
        ArrayListBatcher<Integer> batcher = new ArrayListBatcher<Integer>(new IBatchListener<Integer>() {

            public void batchCollected(Collection<Integer> batch, boolean fullTimeoutHit) {
                receivedList.addAll(batch);
                batches++;
                if (fullTimeoutHit) {
                    fullTimeoutCount++;
                } else {
                    earlyTimeoutCount++;
                }
            }
        }, maxTimeToWaitBetweenBatches, maxTimeToWaitBetweenUpdates);
        for (Integer val : sentList) {
            batcher.add(val);
        }
        Util.doSleep(1000);
        batcher.exit();
        Util.printBatchStats(batches, fullTimeoutCount, earlyTimeoutCount);
        Util.printResults(sentList.size(), receivedList.size());
        assertTrue("Received values are different than sent values", sentList.equals(receivedList));
        Log.log("\n");
    }

    @Test
    public static void testBatcherWithOneMillionIntegersAndNoDelayBetweenAddsWithOneForcedLoss() {
        count = 0;
        Log.log("testBatcherWithOneMillionIntegersAndNoDelayBetweenAddsWithOneForcedLoss()");
        final List<Integer> receivedList = new ArrayList<Integer>();
        List<Integer> sentList = new ArrayList<Integer>();
        for (int i = 0; i < 1000000; i++) {
            sentList.add(new Integer(i));
        }
        ArrayListBatcher<Integer> batcher = new ArrayListBatcher<Integer>(new IBatchListener<Integer>() {

            public void batchCollected(Collection<Integer> batch, boolean fullTimeoutHit) {
                receivedList.addAll(batch);
                batches++;
                if (fullTimeoutHit) {
                    fullTimeoutCount++;
                } else {
                    earlyTimeoutCount++;
                }
            }
        }, maxTimeToWaitBetweenBatches, maxTimeToWaitBetweenUpdates);
        for (Integer val : sentList) {
            if (val.intValue() != 100) batcher.add(val);
        }
        Util.doSleep(1000);
        batcher.exit();
        Util.printBatchStats(batches, fullTimeoutCount, earlyTimeoutCount);
        Util.printResults(sentList.size(), receivedList.size());
        assertTrue("Should have lost exactly one value", receivedList.size() == sentList.size() - 1);
        Log.log("\n");
    }
}
