package sf2.vm;

import java.io.Serializable;

public class PortsMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    public int srcPort, srcPortEnd;

    public int dstPort, dstPortEnd;

    public String toString() {
        return srcPort + "-" + srcPortEnd + ":" + dstPort + "-" + dstPortEnd;
    }
}
