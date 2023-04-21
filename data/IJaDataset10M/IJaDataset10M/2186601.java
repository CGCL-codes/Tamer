package drcl.intserv;

import drcl.net.traffic.TrafficModel;

public class SpecAd extends drcl.DrclObj {

    public long sender;

    public TrafficModel tspec;

    public double minE2eDelay;

    public double minPropDelay;

    public double minIntPktJitter;

    public int minBW;

    public double pktLossRate;

    public int hop;

    public int minMTU;

    public SpecAd() {
    }

    public SpecAd(long sender_, TrafficModel tspec_) {
        this(sender_, tspec_, 0, 0.0, 0.0, 0.0, Integer.MAX_VALUE, 0.0, tspec_.getMTU());
    }

    public SpecAd(long sender_, TrafficModel tspec_, int hopCount_, double edelay_, double pdelay_, double jitter_, int bandwidth_, double pktLossRate_, int mtu_) {
        super();
        sender = sender_;
        minE2eDelay = edelay_;
        minPropDelay = pdelay_;
        tspec = tspec_;
        minIntPktJitter = jitter_;
        minBW = bandwidth_;
        pktLossRate = pktLossRate_;
        hop = hopCount_;
        minMTU = mtu_;
    }

    public void duplicate(Object source_) {
        if (!(source_ instanceof SpecAd)) return;
        SpecAd that_ = (SpecAd) source_;
        sender = that_.sender;
        minE2eDelay = that_.minE2eDelay;
        minPropDelay = that_.minPropDelay;
        tspec = that_.tspec;
        minIntPktJitter = that_.minIntPktJitter;
        minBW = that_.minBW;
        pktLossRate = that_.pktLossRate;
        hop = that_.hop;
        minMTU = that_.minMTU;
    }

    public boolean check(QoSRequirement qos_) {
        return (qos_.maxE2eDelay >= minE2eDelay + minPropDelay) && (qos_.maxHop >= hop) && (qos_.maxIntPktJitter >= minIntPktJitter) && (qos_.maxPktLossRate >= pktLossRate) && (qos_.minBW <= minBW) && (tspec.getMTU() <= minMTU);
    }

    public String toString() {
        return "sender=" + sender + ",min e2e delay=" + minE2eDelay + ",minPropDelay=" + minPropDelay + ",tspec=" + tspec + ",minIntPktJitter=" + minIntPktJitter + ",minBW=" + minBW + ",pktLossRate=" + pktLossRate + ",hop=" + hop + ",minMTU=" + minMTU;
    }

    static void ___PROPERTY___() {
    }

    public void setSender(long g_) {
        sender = g_;
    }

    public long getSender() {
        return sender;
    }

    public void setMinEndToEndDelay(double d_) {
        minE2eDelay = d_;
    }

    public double getMinEndToEndDelay() {
        return minE2eDelay;
    }

    public void setMinPropDelay(double d_) {
        minPropDelay = d_;
    }

    public double getMinPropDelay() {
        return minPropDelay;
    }

    public void setTspec(TrafficModel d_) {
        tspec = d_;
    }

    public TrafficModel getTspec() {
        return tspec;
    }

    public void setMinIntPktJitter(double d_) {
        minIntPktJitter = d_;
    }

    public double getMinIntPktJitter() {
        return minIntPktJitter;
    }

    public void setMinBW(int d_) {
        minBW = d_;
    }

    public int getMinBW() {
        return minBW;
    }

    public void setPktLossRate(double d_) {
        pktLossRate = d_;
    }

    public double getPktLossRate() {
        return pktLossRate;
    }

    public void setHop(int d_) {
        hop = d_;
    }

    public int getHop() {
        return hop;
    }

    public void setMinMTU(int d_) {
        minMTU = d_;
    }

    public int getMinMTU() {
        return minMTU;
    }
}
