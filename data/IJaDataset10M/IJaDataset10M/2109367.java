package org.apache.hadoop.hdfs.server.datanode;

/** 
 * a class to throttle the block transfers.
 * This class is thread safe. It can be shared by multiple threads.
 * The parameter bandwidthPerSec specifies the total bandwidth shared by
 * threads.
 */
class BlockTransferThrottler {

    private long period;

    private long periodExtension;

    private long bytesPerPeriod;

    private long curPeriodStart;

    private long curReserve;

    private long bytesAlreadyUsed;

    /** Constructor 
   * @param bandwidthPerSec bandwidth allowed in bytes per second. 
   */
    BlockTransferThrottler(long bandwidthPerSec) {
        this(500, bandwidthPerSec);
    }

    /**
   * Constructor
   * @param period in milliseconds. Bandwidth is enforced over this
   *        period.
   * @param bandwidthPerSec bandwidth allowed in bytes per second. 
   */
    BlockTransferThrottler(long period, long bandwidthPerSec) {
        this.curPeriodStart = System.currentTimeMillis();
        this.period = period;
        this.curReserve = this.bytesPerPeriod = bandwidthPerSec * period / 1000;
        this.periodExtension = period * 3;
    }

    /**
   * @return current throttle bandwidth in bytes per second.
   */
    synchronized long getBandwidth() {
        return bytesPerPeriod * 1000 / period;
    }

    /**
   * Sets throttle bandwidth. This takes affect latest by the end of current
   * period.
   * 
   * @param bytesPerSecond 
   */
    synchronized void setBandwidth(long bytesPerSecond) {
        if (bytesPerSecond <= 0) {
            throw new IllegalArgumentException("" + bytesPerSecond);
        }
        bytesPerPeriod = bytesPerSecond * period / 1000;
    }

    /** Given the numOfBytes sent/received since last time throttle was called,
   * make the current thread sleep if I/O rate is too fast
   * compared to the given bandwidth.
   *
   * @param numOfBytes
   *     number of bytes sent/received since last time throttle was called
   */
    synchronized void throttle(long numOfBytes) {
        if (numOfBytes <= 0) {
            return;
        }
        curReserve -= numOfBytes;
        bytesAlreadyUsed += numOfBytes;
        while (curReserve <= 0) {
            long now = System.currentTimeMillis();
            long curPeriodEnd = curPeriodStart + period;
            if (now < curPeriodEnd) {
                try {
                    wait(curPeriodEnd - now);
                } catch (InterruptedException ignored) {
                }
            } else if (now < (curPeriodStart + periodExtension)) {
                curPeriodStart = curPeriodEnd;
                curReserve += bytesPerPeriod;
            } else {
                curPeriodStart = now;
                curReserve = bytesPerPeriod - bytesAlreadyUsed;
            }
        }
        bytesAlreadyUsed -= numOfBytes;
    }
}
