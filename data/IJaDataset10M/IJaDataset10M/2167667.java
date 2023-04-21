package drcl.inet.sensorsim;

import drcl.data.*;
import drcl.comp.*;
import drcl.net.*;
import drcl.inet.*;
import drcl.inet.contract.*;
import java.util.*;
import drcl.comp.Port;
import drcl.comp.Contract;
import drcl.util.random.*;

/** This class implements the sensor physical layer.
*
* @author Ahmed Sobeih
* @version 1.0, 12/19/2003
*/
public class SensorPhy extends drcl.net.Module {

    public static final double TARGET_STRENGTH = 50000.0;

    public static final double NOISE_STRENGTH = 100.0;

    public static final double NOISE_MEAN = 0.0;

    public static final double NOISE_VAR = 0.5;

    public static final long SEED_RNG = 10;

    long nid;

    double Radius;

    double Pt;

    double noiseStrength;

    double lastNoisePower;

    GaussianDistribution gen;

    double RxThresh;

    public static final String CONFIG_PORT_ID = ".config";

    public static final String CHANNEL_PORT_ID = ".channel";

    public static final String PROPAGATION_PORT_ID = ".propagation";

    public static final String MOBILITY_PORT_ID = ".mobility";

    public static final String TO_AGENT_PORT_ID = ".toAgent";

    protected Port configPort = addPort(CONFIG_PORT_ID, false);

    protected Port channelPort = addPort(CHANNEL_PORT_ID, false);

    protected Port propagationPort = addPort(PROPAGATION_PORT_ID, false);

    protected Port mobilityPort = addPort(MOBILITY_PORT_ID, false);

    protected Port toAgentPort = addPort(TO_AGENT_PORT_ID, false);

    public SensorPhy(long nid_, double Radius_) {
        super();
        nid = nid_;
        Radius = Radius_;
        Pt = TARGET_STRENGTH;
        noiseStrength = NOISE_STRENGTH;
        lastNoisePower = 1.0;
        gen = null;
        RxThresh = 200.0;
    }

    public SensorPhy() {
        super();
        Pt = TARGET_STRENGTH;
        noiseStrength = NOISE_STRENGTH;
        lastNoisePower = 1.0;
        gen = null;
        RxThresh = 200.0;
    }

    public String getName() {
        return "SensorPhy";
    }

    public void duplicate(Object source_) {
        super.duplicate(source_);
        SensorPhy that_ = (SensorPhy) source_;
        nid = that_.nid;
        Radius = that_.Radius;
        Pt = that_.Pt;
        noiseStrength = that_.noiseStrength;
        lastNoisePower = that_.lastNoisePower;
        gen = that_.gen;
        RxThresh = that_.RxThresh;
    }

    /** Gets the port that connects to the sensor channel */
    public Port getChannelPort() {
        return channelPort;
    }

    /** Sets the ID of the node */
    public void setNid(long nid_) {
        nid = nid_;
    }

    /** Gets the ID of the node */
    public long getNid(long nid_) {
        return nid;
    }

    /** Sets the transmission radius  */
    public void setRadius(double Radius_) {
        Radius = Radius_;
    }

    /** Gets the transmission radius  */
    public double getRadius() {
        return Radius;
    }

    /** Sets the receiving threshold  */
    public void setRxThresh(double RxThresh_) {
        RxThresh = RxThresh_;
    }

    /** Gets the receiving threshold  */
    public double getRxThresh() {
        return RxThresh;
    }

    /** Gets the noise power  */
    public double getNoisePower() {
        return lastNoisePower;
    }

    /** Sets the noise strength */
    public void setNoiseStrength(double noiseStrength_) {
        noiseStrength = noiseStrength_;
    }

    /** Sets the target power  */
    public void setTargetPower(double Pt_) {
        setPt(Pt_);
    }

    public void setPt(double Pt_) {
        Pt = Pt_;
    }

    /** Gets the target power  */
    public double getTargetPower() {
        return getPt();
    }

    public double getPt() {
        return Pt;
    }

    /** Handles data arriving at UP port */
    protected synchronized void dataArriveAtUpPort(Object data_, drcl.comp.Port upPort_) {
        if (!(data_ instanceof TargetPacket)) {
            error(data_, "dataArriveAtUpPort()", upPort_, "unknown object");
            return;
        }
        SensorPositionReportContract.Message msg = new SensorPositionReportContract.Message();
        msg = (SensorPositionReportContract.Message) mobilityPort.sendReceive(msg);
        double Xc = msg.getX();
        double Yc = msg.getY();
        double Zc = msg.getZ();
        System.out.println("Target " + nid + ": generating signal at time " + getTime() + " loc: " + Xc + ", " + Yc + ", " + Zc);
        downPort.doSending(new SensorNodeChannelContract.Message(nid, Xc, Yc, Zc, Pt, Radius, new TargetPacket(((TargetPacket) data_).size, ((TargetPacket) data_).data)));
    }

    /** Handles data arriving at Channel port  */
    protected synchronized void dataArriveAtChannelPort(Object data_) {
        long s;
        if (gen == null) {
            s = SEED_RNG;
            gen = new GaussianDistribution(NOISE_MEAN, NOISE_VAR, s);
        }
        SensorPositionReportContract.Message msg = new SensorPositionReportContract.Message();
        msg = (SensorPositionReportContract.Message) mobilityPort.sendReceive(msg);
        double Xc = msg.getX();
        double Yc = msg.getY();
        double Zc = msg.getZ();
        double Xs, Ys, Zs;
        SensorNodeChannelContract.Message msg2 = (SensorNodeChannelContract.Message) data_;
        Xs = msg2.getX();
        Ys = msg2.getY();
        Zs = msg2.getZ();
        double Pt_received;
        Pt_received = msg2.getPt();
        long target_nid = msg2.getNid();
        SensorRadioPropagationQueryContract.Message msg3 = (SensorRadioPropagationQueryContract.Message) propagationPort.sendReceive(new SensorRadioPropagationQueryContract.Message(Pt_received, Xs, Ys, Zs, Xc, Yc, Zc));
        double Pr = msg3.getPr();
        if (Pr < RxThresh) {
            System.out.println("SensorPhy nid=" + nid + " Packet was discarded because Pr = " + Pr + " < " + RxThresh);
        } else {
            double af = Pr;
            int size = ((TargetPacket) msg2.getPkt()).size;
            TargetPacket sensorPkt = new TargetPacket(size, ((TargetPacket) msg2.getPkt()).data);
            lastNoisePower = 0.0;
            double rd;
            for (int k = 0; k < sensorPkt.size; k++) {
                sensorPkt.data[k] = sensorPkt.data[k] * af;
                rd = gen.nextDouble();
                double noise = noiseStrength * rd;
                sensorPkt.data[k] = sensorPkt.data[k] + noise;
                lastNoisePower = lastNoisePower + (noise * noise);
            }
            lastNoisePower = lastNoisePower / ((double) size);
            toAgentPort.doSending(new SensorAgentPhyContract.Message(lastNoisePower, new TargetPacket(size, sensorPkt.data), target_nid));
        }
    }

    protected synchronized void processOther(Object data_, Port inPort_) {
        String portid_ = inPort_.getID();
        if (portid_.equals(CHANNEL_PORT_ID)) {
            if (!(data_ instanceof SensorNodeChannelContract.Message)) {
                error(data_, "processOther()", inPort_, "unknown object");
                return;
            }
            dataArriveAtChannelPort(data_);
            return;
        }
        super.processOther(data_, inPort_);
    }
}
