package com.soramaki.fna.paymentsimulation;

import java.util.Comparator;
import java.util.Vector;
import com.soramaki.fna.paymentsimulation.events.Event;
import com.soramaki.fna.utils.DateTimeUtils;

public class PaymentRecord {

    public static class Key {

        public long id;

        public long splitid;

        public int priority;

        public Key() {
            this.priority = 0;
            this.id = 0;
            this.splitid = 0;
        }

        public String toString() {
            String res = String.valueOf(id);
            if (splitid > 0) {
                res = res + "_" + String.valueOf(id);
            }
            res = res + "_P" + priority;
            return res;
        }

        public Key copy() {
            Key newId = new Key();
            newId.id = this.id;
            newId.splitid = this.splitid;
            newId.priority = this.priority;
            return newId;
        }
    }

    public static class KeyComparator implements Comparator<Key> {

        @Override
        public int compare(Key o1, Key o2) {
            Integer p;
            Long c;
            p = o1.priority - o2.priority;
            if (p != 0) return Integer.signum(p);
            c = o1.id - o2.id;
            if (c != 0) return Long.signum(c);
            c = o1.splitid - o2.splitid;
            return Long.signum(c);
        }
    }

    public static KeyComparator keyComparator() {
        return new KeyComparator();
    }

    long id;

    long splitid;

    String datestring;

    public long timestamp;

    double value;

    double settlementValue;

    Bank sender;

    Bank receiver;

    int originalPriority;

    public Event event;

    int priority;

    String settlement_method;

    public long offsetTime;

    long timesettled;

    int senderNumberInQueue;

    int receiverNumberInQueue;

    double senderRtgsBalance;

    double receiverRtgsBalance;

    double senderRrgsBalance;

    double receiverRrgsBalance;

    double bilateralbalance;

    long settlementOrderId;

    double overdraftLimit;

    double bilateralLimit;

    public long split;

    public long offset;

    public byte qopt;

    public byte forcednet;

    public boolean forcesettlement;

    double valueOffset;

    double valueNetted;

    PaymentRecord parent = null;

    int split_count;

    LogString lastReason;

    public PaymentRecord(String id, long timestamp, double value, Bank sender, Bank receiver, int priority) {
        this.id = Long.valueOf(id);
        this.timestamp = timestamp;
        this.value = value;
        this.settlementValue = value;
        this.sender = sender;
        this.receiver = receiver;
        this.priority = priority;
        this.originalPriority = priority;
        this.timesettled = -1;
        this.bilateralbalance = 0;
        this.senderRtgsBalance = 0;
        this.receiverRtgsBalance = 0;
        this.senderRrgsBalance = 0;
        this.receiverRrgsBalance = 0;
        this.senderNumberInQueue = 0;
        this.receiverNumberInQueue = 0;
        this.split_count = 0;
        this.forcesettlement = false;
    }

    private PaymentRecord() {
    }

    /**
	 * @return Whether this payment is product of a split operation
	 */
    public boolean isSplitProduct() {
        return (this.splitid > 0);
    }

    /**
	 * Given a value threshold, generates as many split payments as needed
	 * in order to cover up the same value of this payment. Each of the
	 * payments is equal or less in value than threshold.
	 * Returns the split payments or null if no split may occur.
	 * @param threshold
	 * @return
	 */
    public Vector<PaymentRecord> splitByThreshold(double threshold) {
        if (this.settlementValue <= threshold) {
            return null;
        }
        int t = (int) threshold;
        int n = 2;
        int val = (int) (this.settlementValue / n);
        while (val > t) {
            n++;
            val = (int) (this.settlementValue / n);
        }
        PaymentRecord parent = this.isSplitProduct() ? this.parent : this;
        Vector<PaymentRecord> rs = new Vector<PaymentRecord>();
        for (int i = 0; i < n; i++) {
            PaymentRecord pi = this.copy();
            parent.split_count++;
            pi.value = settlementValue;
            pi.settlementValue = val;
            pi.parent = parent;
            pi.splitid = parent.split_count;
            rs.add(pi);
        }
        double roundingerror = this.settlementValue - n * val;
        rs.get(n - 1).settlementValue += roundingerror;
        if (this.sender.canSettle(rs.get(0))) {
            return rs;
        }
        return null;
    }

    /**
     * Given a fund amount and a minimum amount threshold, generates 2 split
     * payments that sum up the same amount as this payment. The method
     * doesn't generate split payment below the given threshold. The first
     * payment will cover the same amount as the funds parameter.
     * Returns null when no split may occur.
     * @param freeLiquidity
     * @param splitThreshold
     * @return
     */
    public Vector<PaymentRecord> splitByLiquidity(double freeLiquidity, double splitThreshold) {
        if (freeLiquidity <= 0 || freeLiquidity < splitThreshold || this.settlementValue < splitThreshold || freeLiquidity >= this.settlementValue) {
            return null;
        }
        PaymentRecord parent = this.isSplitProduct() ? this.parent : this;
        Vector<PaymentRecord> rs = new Vector<PaymentRecord>();
        PaymentRecord r0 = this.copy();
        PaymentRecord r1 = this.copy();
        r0.settlementValue = freeLiquidity;
        r1.settlementValue = this.settlementValue - freeLiquidity;
        if (settlementValue < 0) {
            settlementValue = 0;
        }
        r0.parent = parent;
        r1.parent = parent;
        r0.splitid = ++parent.split_count;
        r1.splitid = ++parent.split_count;
        rs.add(r0);
        rs.add(r1);
        return rs;
    }

    public PaymentRecord copy() {
        PaymentRecord ret = new PaymentRecord();
        ret.id = this.id;
        ret.splitid = this.splitid;
        ret.datestring = this.datestring;
        ret.timestamp = this.timestamp;
        ret.value = this.settlementValue;
        ret.settlementValue = this.settlementValue;
        ret.sender = this.sender;
        ret.receiver = this.receiver;
        ret.priority = this.priority;
        ret.event = this.event;
        ret.timesettled = this.timesettled;
        ret.senderNumberInQueue = this.senderNumberInQueue;
        ret.receiverNumberInQueue = this.receiverNumberInQueue;
        ret.senderRtgsBalance = this.senderRtgsBalance;
        ret.receiverRtgsBalance = this.receiverRtgsBalance;
        ret.senderRrgsBalance = this.senderRrgsBalance;
        ret.receiverRrgsBalance = this.receiverRrgsBalance;
        ret.bilateralbalance = this.bilateralbalance;
        ret.settlementOrderId = this.settlementOrderId;
        ret.overdraftLimit = this.overdraftLimit;
        ret.bilateralLimit = this.bilateralLimit;
        ret.split = this.split;
        ret.offset = this.offset;
        ret.qopt = this.qopt;
        ret.forcednet = this.forcednet;
        ret.forcesettlement = this.forcesettlement;
        ret.valueOffset = this.valueOffset;
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String sep = ModelParams.separator;
        sb.append(id).append(sep);
        sb.append(splitid).append(sep);
        String date[] = DateTimeUtils.canonicalFormat.formatTimeStamp(this.timestamp);
        sb.append(date[0]).append(sep);
        sb.append(date[1]).append(sep);
        sb.append(ModelParams.decimalformat.format(value)).append(sep);
        sb.append(ModelParams.decimalformat.format(valueOffset)).append(sep);
        sb.append(ModelParams.decimalformat.format(valueNetted)).append(sep);
        sb.append(ModelParams.decimalformat.format(settlementValue)).append(sep);
        sb.append(sender.id).append(sep);
        sb.append(receiver.id).append(sep);
        sb.append(originalPriority).append(sep);
        sb.append(priority).append(sep);
        String settled[] = DateTimeUtils.canonicalFormat.formatTimeStamp(this.timesettled);
        sb.append(settled[1]).append(sep);
        long delay = (timesettled - timestamp) / 1000;
        sb.append("" + delay).append(sep);
        sb.append(senderNumberInQueue).append(sep);
        sb.append(receiverNumberInQueue).append(sep);
        sb.append(ModelParams.decimalformat.format(senderRtgsBalance)).append(sep);
        sb.append(ModelParams.decimalformat.format(senderRrgsBalance)).append(sep);
        sb.append(overdraftLimit).append(sep);
        sb.append(ModelParams.decimalformat.format(receiverRtgsBalance)).append(sep);
        sb.append(ModelParams.decimalformat.format(receiverRrgsBalance)).append(sep);
        sb.append(ModelParams.decimalformat.format(bilateralbalance)).append(sep);
        sb.append(bilateralLimit).append(sep);
        sb.append(settlementOrderId).append(sep);
        sb.append(this.split).append(sep);
        sb.append(this.offset).append(sep);
        String osdate[] = DateTimeUtils.canonicalFormat.formatTimeStamp(this.offsetTime);
        sb.append(osdate[1]).append(sep);
        sb.append(this.qopt).append(sep);
        sb.append(this.forcednet).append(sep);
        sb.append("\n");
        return sb.toString();
    }

    public Key getKey() {
        Key k = new Key();
        k.priority = this.priority;
        k.id = this.id;
        k.splitid = this.splitid;
        return k;
    }
}
