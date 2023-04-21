package edu.uga.dawgpack.aggregate;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import cern.colt.list.LongArrayList;

/**
 * @author Juber Ahamad Patel
 * 
 */
public class DistributionThread extends Thread {

    private BlockingQueue<long[]> distributionQueue;

    private LongArrayList[] updateRanges;

    private BlockingQueue[] updateQueues;

    private long regionLength;

    private int numberOfUpdaters;

    DistributionThread(BlockingQueue<long[]> distributionQueue, BlockingQueue[] updateQueues, long regionLength) {
        this.distributionQueue = distributionQueue;
        this.updateQueues = updateQueues;
        this.regionLength = regionLength;
        this.numberOfUpdaters = updateQueues.length;
        this.updateRanges = new LongArrayList[numberOfUpdaters];
        for (int i = 0; i < updateRanges.length; i++) {
            updateRanges[i] = new LongArrayList(100 * 2 / numberOfUpdaters);
        }
    }

    @Override
    public void run() {
        long[] updateRangesArray = null;
        int size;
        int startRegion;
        int endRegion;
        long updateStart;
        long updateEnd;
        long[] ranges = null;
        while (true) {
            try {
                ranges = distributionQueue.take();
                if (ranges.length == 0) {
                    System.out.println(new Date() + " distribution thread " + Thread.currentThread().getName() + " received stop signal, returning");
                    distributionQueue.put(ranges);
                    return;
                }
            } catch (InterruptedException e) {
                System.out.println(new Date() + " problem in Distribution Thread");
                e.printStackTrace();
            }
            for (int i = 0; i < ranges.length; i += 2) {
                updateStart = ranges[i];
                updateEnd = ranges[i + 1];
                startRegion = (int) (updateStart / regionLength);
                if (startRegion > numberOfUpdaters - 1) startRegion = numberOfUpdaters - 1;
                endRegion = (int) (updateEnd / regionLength);
                if (endRegion > numberOfUpdaters - 1) endRegion = numberOfUpdaters - 1;
                if (startRegion == endRegion) {
                    updateRanges[startRegion].add(updateStart);
                    updateRanges[startRegion].add(updateEnd);
                } else {
                    updateRanges[startRegion].add(updateStart);
                    updateRanges[startRegion].add(endRegion * regionLength - 1);
                    updateRanges[endRegion].add(endRegion * regionLength);
                    updateRanges[endRegion].add(updateEnd);
                }
            }
            for (int i = 0; i < updateRanges.length; i++) {
                size = updateRanges[i].size();
                if (size == 0) continue;
                updateRangesArray = Arrays.copyOf(updateRanges[i].elements(), size);
                updateRanges[i].clear();
                try {
                    if (updateQueues[i].remainingCapacity() == 0) {
                        System.out.println(new Date() + " update queue is full");
                    }
                    updateQueues[i].put(updateRangesArray);
                } catch (InterruptedException e) {
                    System.out.println(new Date() + " problem in Distribution Thread while putting update ranges");
                    e.printStackTrace();
                }
            }
        }
    }
}
