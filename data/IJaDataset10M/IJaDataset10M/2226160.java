package networkcontroller.packet;

import networkcontroller.xml.XMLSearch;

/**
 * class for search packet - search query on network
 * 
 * @author maciek
 * 
 */
public class SearchPacket extends Packet {

    /**
	 * constructs packet and sends it if it's not a tcp packet
	 * 
	 * @param tcpPacket
	 *            if it is a tcp packet
	 * @param host
	 *            destination host to send search - if it is tcp search, else
	 *            null
	 * @param port
	 *            destination port to send seatch - if it is tcp search, else
	 *            null
	 * @param id
	 *            id of search
	 * @param name
	 *            name criteria, can be null
	 * @param sizeMin
	 *            minimal size criteria, can be null
	 * @param sizeMax
	 *            minimal size criteria, can be null
	 * @param sum
	 *            file hash criteria, can be null
	 */
    public SearchPacket(boolean tcpPacket, String host, int port, int id, String name, int sizeMin, int sizeMax, String sum) {
        super(tcpPacket, false);
        type = Packet.SEARCH_CODE;
        xml = new XMLSearch(host, port, id, name, sizeMin, sizeMax, sum);
        createPacket();
        if (tcpPacket == false) sendPacket();
    }
}
