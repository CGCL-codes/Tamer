package events.openflow;

import drivers.OFPConstants;
import events.Event;

/**
 * @author Zheng Cai
 */
public class SwitchJoinEvent extends Event {

    public static class PhysicalPort {

        public int portNo;

        public short[] hwAddr;

        public byte[] name;

        public long config;

        public long state;

        public long curr;

        public long advertised;

        public long supported;

        public long peer;

        public PhysicalPort() {
            hwAddr = new short[OFPConstants.OfpConstants.OFP_ETH_ALEN];
            name = new byte[OFPConstants.OfpConstants.OFP_MAX_PORT_NAME_LEN];
        }
    }

    public long dpid;

    public long nBuffers;

    public short nTables;

    public long capabilities;

    public long actions;

    public int nPorts;

    public PhysicalPort[] ports;

    @Override
    public String toString() {
        return String.format("Switch %d: nBuffer=%d, nTables=%d, capabilities=%d, actions=%d, nPorts=%d", dpid, nBuffers, nTables, capabilities, actions, nPorts);
    }

    public PhysicalPort findPort(int portNo) {
        for (PhysicalPort p : ports) {
            if (p.portNo == portNo) return p;
        }
        return null;
    }

    @Override
    public int convertToBytes(byte[] buf, int index) {
        return 0;
    }
}
