package drcl.inet.mac;

/**
 * This class defines the IEEE802.11 Beacon frame structure.
 *
 * @see Mac_802_11
 * @see Mac_802_11_Packet
 *
 * @author Rong Zheng
 *
 */
public class Mac_802_11_Beacon_Frame extends Mac_802_11_Packet {

    public static final int Mac_802_11_Beacon_Frame_Header_Length = 55;

    long sa;

    long bb_ts;

    /** Creates a Mac_802_11_Beacon_Frame 
    * @param fc_ - MAC frame control
    * @param duration_ - duration
      * @param sa_ - source MAC address
    * @param fcs_ - frame check sequence
    * @param hszie_ - header size
      * @param bb_ts_ - time stamp
    */
    public Mac_802_11_Beacon_Frame(Mac_802_11_Frame_Control fc_, int duration_, long sa_, int fcs_, int hsize_, long bb_ts_) {
        super();
        headerSize = hsize_;
        size = headerSize;
        fc = fc_;
        duration = duration_;
        sa = sa_;
        fcs = fcs_;
        bb_ts = bb_ts_;
    }

    /** Set the source MAC address of the beacon frame */
    public void setSa(long sa_) {
        sa = sa_;
    }

    /** Get the source MAC address of the beacon frame */
    public long getSa() {
        return sa;
    }

    /** Set the TSF timer of the beacon frame */
    public void setTSF(long timer) {
        bb_ts = timer;
    }

    /** Get the TSF timer of the beacon frame */
    public long getTSF() {
        return bb_ts;
    }

    public Object clone() {
        return new Mac_802_11_Beacon_Frame((Mac_802_11_Frame_Control) fc.clone(), duration, sa, fcs, size, bb_ts);
    }

    public String _toString(String separator_) {
        return "Beacon Frame" + separator_ + "sa:" + sa + separator_ + "forcedError:" + forcedError + separator_;
    }
}
