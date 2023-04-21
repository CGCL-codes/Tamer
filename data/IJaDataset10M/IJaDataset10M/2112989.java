package guiTrace;

import jist.swans.Constants;

public class EnqueueTraceEvent extends TraceEvent {

    private String sourceID;

    private String destinationID;

    private Short packetType;

    private Integer sizeOfPacket;

    private Integer packetAttribute = 0;

    private Short packetID;

    public EnqueueTraceEvent() {
        super(TraceEvent.EnqueueTraceEvent);
    }

    public EnqueueTraceEvent(String SrcID, String destID, Short pcktType, Integer sizeOfPacket, Short pcktID) {
        super(TraceEvent.EnqueueTraceEvent);
        this.sourceID = SrcID;
        this.destinationID = destID;
        this.packetType = pcktType;
        this.sizeOfPacket = sizeOfPacket;
        this.packetID = pcktID;
    }

    @Override
    public String showYourself() {
        String s = "";
        s += "+ -t " + getTime() / (float) Constants.SECOND + " ";
        s += "-s " + this.sourceID + " ";
        s += "-d " + this.destinationID + " ";
        s += "-p " + getProtocolName(new Integer(this.packetType)) + " ";
        s += "-e " + this.sizeOfPacket + " ";
        s += "-i " + this.packetID + " ";
        s += "-a " + this.packetAttribute + " ";
        return s;
    }
}
