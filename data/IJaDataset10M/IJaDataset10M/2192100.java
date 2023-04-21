package drcl.net.traffic;

/**
 * This class describes an On/Off traffic model.
 * <i>On</i> and <i>off</i> time intervals are exponentially distributed.
 * It defines the following parameters:
 * <dl>
 * <dt>On Time
 * <dd>Fixed length of <i>on</i> time (burst) intervals (second).
 *
 * <dt>Off Time
 * <dd>Fixed length of <i>off</i> time (idle) intervals (second).
 *
 * <dt>Average Sending Rate
 * <dd>Sending rate over the period of the <i>on</i> time plus <i>off</i> time (bps).
 *
 * <dt>Packet Size
 * <dd>Size of packets (byte).
 * </dl>
 */
public class traffic_OnOff extends TrafficModel implements TrafficPeriodic {

    public int packetSize;

    public int rate;

    public double OnTime;

    public double OffTime;

    public traffic_OnOff() {
    }

    public traffic_OnOff(int mtu_, int rate_, double ontime_, double offtime_) {
        set(mtu_, rate_, ontime_, offtime_);
    }

    public void set(int mtu_, int rate_, double ontime_, double offtime_) {
        packetSize = mtu_;
        rate = rate_;
        OnTime = ontime_;
        OffTime = offtime_;
    }

    public double getPeriod() {
        return OnTime + OffTime;
    }

    public double getLoad() {
        return rate;
    }

    public int getBurst() {
        return (int) Math.ceil(rate * (OnTime + OffTime));
    }

    public TrafficModel merge(TrafficModel that_) {
        if (!(that_ instanceof traffic_OnOff)) return null;
        traffic_OnOff thatTraffic_ = (traffic_OnOff) that_;
        packetSize = Math.max(packetSize, thatTraffic_.packetSize);
        if (thatTraffic_.rate > rate) rate = thatTraffic_.rate;
        OnTime = Math.min(OnTime, thatTraffic_.OnTime);
        OffTime = Math.min(OffTime, thatTraffic_.OffTime);
        return this;
    }

    public void duplicate(Object source_) {
        if (!(source_ instanceof traffic_OnOff)) return;
        traffic_OnOff that_ = (traffic_OnOff) source_;
        packetSize = that_.packetSize;
        rate = that_.rate;
        OnTime = that_.OnTime;
        OffTime = that_.OffTime;
    }

    public String oneline() {
        return getClass().getName() + ":packetSize=" + packetSize + ", rate=" + rate + ", OnTime=" + OnTime + ", OffTime=" + OffTime;
    }

    private void ___PROPERTY___() {
    }

    public void setPacketSize(int size_) {
        packetSize = size_;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public void setAvgRate(int rate_) {
        rate = rate_;
    }

    public int getAvgRate() {
        return rate;
    }

    public void setOnTime(double ontime_) {
        OnTime = ontime_;
    }

    public double getOnTime() {
        return OnTime;
    }

    public void setOffTime(double offtime_) {
        OffTime = offtime_;
    }

    public double getOffTime() {
        return OffTime;
    }

    public int getMTU() {
        return packetSize;
    }
}
