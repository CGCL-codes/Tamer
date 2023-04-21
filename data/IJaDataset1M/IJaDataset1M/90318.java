package jj2000.j2k.codestream.reader;

/**
 * This class defines an object used to countain informations about a packet
 * to which the current code-block belongs.
 *
 * @see CBlkInfo
 * */
public class PktInfo {

    /** Index of the packet */
    public int packetIdx;

    /** The layer associated with the current code-block in this packet. */
    public int layerIdx;

    /** The code-block offset in the codestream (for this packet) */
    public int cbOff = 0;

    /** The length of the code-block in this packet (in bytes) */
    public int cbLength;

    /** 
     * The length of each terminated segment in the packet. The total is the
     * same as 'cbLength'. It can be null if there is only one terminated
     * segment, in which case 'cbLength' holds the legth of that segment 
     * */
    public int segLengths[];

    /** 
     * The number of truncation points that appear in this packet, and all
     * previous packets, for this code-block. This is the number of passes
     * that can be decoded with the information in this packet and all
     * previous ones. 
     * */
    public int numTruncPnts;

    /** 
     * Classe's constructor.
     *
     * @param lyIdx The layer index for the code-block in this packet
     *
     * @param pckIdx The packet index
     * */
    public PktInfo(int lyIdx, int pckIdx) {
        layerIdx = lyIdx;
        packetIdx = pckIdx;
    }

    /**
     * Object information in a string.
     *
     * @return Object information
     * */
    @Override
    public String toString() {
        return "packet " + packetIdx + " (lay:" + layerIdx + ", off:" + cbOff + ", len:" + cbLength + ", numTruncPnts:" + numTruncPnts + ")\n";
    }
}
