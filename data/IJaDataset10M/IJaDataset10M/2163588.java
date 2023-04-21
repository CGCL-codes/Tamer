package ow.messaging.timeoutcalc;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import ow.messaging.MessagingAddress;
import ow.messaging.MessagingConfiguration;
import ow.util.Timer;

/**
 * An instance of this class calculates timeout in TCP style.
 * The algorithm is based on what is described in a paper
 * "Congestion Avoidance and Control", Proc. SIGCOMM'88.
 */
public final class RTTBasedTimeoutCalculator implements TimeoutCalculator {

    private static final Logger logger = Logger.getLogger("messaging");

    private final MessagingConfiguration config;

    private Map<MessagingAddress, TargetRecord> targetTable = null;

    private SortedSet<TargetRecord> targetListInLRUOrder = null;

    public RTTBasedTimeoutCalculator(MessagingConfiguration config) {
        this.config = config;
        if (this.config.getDoTimeoutCalculation()) {
            this.targetTable = new HashMap<MessagingAddress, TargetRecord>();
            this.targetListInLRUOrder = new TreeSet<TargetRecord>();
        }
    }

    public int calculateTimeout(MessagingAddress target) {
        if (!this.config.getDoTimeoutCalculation()) {
            return this.config.getStaticTimeout();
        }
        TargetRecord record = null;
        synchronized (this.targetTable) {
            record = this.targetTable.get(target);
        }
        int timeout;
        if (record != null) {
            timeout = record.calculateTimeout();
        } else {
            timeout = this.config.getStaticTimeout();
        }
        return timeout;
    }

    public void updateRTT(MessagingAddress target, int rtt) {
        if (!this.config.getDoTimeoutCalculation()) {
            return;
        }
        TargetRecord record = null;
        synchronized (this.targetTable) {
            record = this.targetTable.get(target);
        }
        if (record != null) {
            synchronized (record) {
                long m = rtt - record.rtt;
                record.rtt += m >> 3;
                if (m < 0) {
                    m = -m;
                    m -= record.mdev;
                    m >>= 3;
                } else {
                    m -= record.mdev;
                }
                record.mdev += m >> 2;
                if (record.mdev > record.mdev_max) {
                    record.mdev_max = record.mdev;
                    if (record.mdev_max > record.rttvar) {
                        record.rttvar = record.mdev_max;
                        record.rttvarKeepingPeriod = config.getRTTKeepingPeriod();
                    }
                }
                if (record.rttvarKeepingPeriod-- <= 0) {
                    if (record.mdev_max < record.rttvar) {
                        record.rttvar -= ((record.rttvar - record.mdev_max + 3) >> 2);
                    }
                    record.mdev_max = config.getTimeoutMin() >> 2;
                    record.rttvarKeepingPeriod = config.getRTTKeepingPeriod();
                }
                record.touch();
            }
        } else {
            record = new TargetRecord(target, rtt);
            synchronized (this.targetListInLRUOrder) {
                this.targetListInLRUOrder.add(record);
                if (this.targetListInLRUOrder.size() > this.config.getRTTTableSize()) {
                    TargetRecord oldestRecord = this.targetListInLRUOrder.last();
                    this.targetListInLRUOrder.remove(oldestRecord);
                    synchronized (this.targetTable) {
                        this.targetTable.remove(oldestRecord.getTarget());
                    }
                }
            }
            synchronized (this.targetTable) {
                this.targetTable.put(target, record);
            }
        }
        logger.log(Level.INFO, "To " + target + ": RTT: " + rtt + ", ave. RTT: " + record.rtt + ", mdev: " + record.mdev + ", mdev_max: " + record.mdev_max + ", rttvar: " + record.rttvar + ", timeout: " + record.calculateTimeout());
    }

    private class TargetRecord implements Comparable<TargetRecord> {

        private MessagingAddress target;

        int rtt;

        int mdev;

        int mdev_max;

        int rttvar;

        private long lastUpdated;

        private long rttvarKeepingPeriod;

        public TargetRecord(MessagingAddress target, int rtt) {
            this.target = target;
            this.rtt = rtt;
            this.mdev = rtt >> 1;
            this.mdev_max = this.rttvar = Math.max(this.mdev, config.getTimeoutMin() >> 2);
            this.lastUpdated = Timer.currentTimeMillis();
            this.rttvarKeepingPeriod = config.getRTTKeepingPeriod();
        }

        public int calculateTimeout() {
            return Math.min(this.rtt + (this.rttvar << 2), config.getTimeoutMax());
        }

        public MessagingAddress getTarget() {
            return this.target;
        }

        public void touch() {
            this.lastUpdated = Timer.currentTimeMillis();
            synchronized (RTTBasedTimeoutCalculator.this.targetListInLRUOrder) {
                RTTBasedTimeoutCalculator.this.targetListInLRUOrder.remove(this);
                RTTBasedTimeoutCalculator.this.targetListInLRUOrder.add(this);
            }
        }

        public int compareTo(TargetRecord o) {
            return (int) (o.lastUpdated - this.lastUpdated);
        }
    }
}
