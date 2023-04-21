package net.sourceforge.jpcap.net;

import java.util.HashMap;

/**
 * Ethernet protocol utility class.
 *
 * @author Patrick Charles and Jonas Lehmann
 * @version $Revision: 540 $
 * @lastModifiedBy $Author: buchmand $
 * @lastModifiedAt $Date: 2006-06-21 16:40:58 +0200 (Wed, 21 Jun 2006) $
 */
public class EthernetProtocol implements EthernetProtocols, EthernetFields {

    /**
   * Extract the protocol type field from packet data.
   * <p>
   * The type field indicates what type of data is contained in the 
   * packet's data block.
   * @param packetBytes packet bytes.
   * @return the ethernet type code. i.e. 0x800 signifies IP datagram.
   */
    public static int extractProtocol(byte[] packetBytes) {
        return packetBytes[ETH_CODE_POS] << 8 | packetBytes[ETH_CODE_POS + 1];
    }

    private String _rcsid = "$Id: EthernetProtocol.java 540 2006-06-21 16:40:58 +0200 (Wed, 21 Jun 2006) buchmand $";
}
