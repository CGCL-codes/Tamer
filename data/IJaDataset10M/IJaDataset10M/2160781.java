package networkcontroller.packet;

import networkcontroller.xml.XMLGet;

/**
 * class for get packet - which gets segment of file from given host
 * 
 * @author maciek
 * 
 */
public class GetPacket extends Packet {

    /**
	 * constructs get packet
	 * 
	 * @param sum
	 *            hash of file to get
	 * @param size
	 *            size of file
	 * @param segment
	 *            segment of file
	 */
    public GetPacket(String sum, long size, int segment) {
        super(true, true);
        type = Packet.GET_CODE;
        xml = new XMLGet(sum, size, segment);
        createPacket();
    }
}
