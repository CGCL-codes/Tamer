package com.parfumball.file;

import java.io.IOException;
import com.parfumball.pcap.Packet;
import com.parfumball.pcap.PcapException;

/**
 * A LibPcap2InputStream is a concrete implementation of a 
 * PcapInputStream for reading modified libpcap packet capture 
 * files. 
 * 
 * The modified libpcap file has a header The format of this header 
 * is as follows:<p>
 * 
 * 1. Magic number (0xa1b2cd34 or 0x34cdb2a1 depending on the 
 *    byte order of the host on which the packets were captured).
 *    There are other (modified) magic numbers, but we don't support
 *    them for now.<br>
 *  
 * 2. Major version (2 bytes).<br>
 * 
 * 3. Minor version (2 bytes).<br>
 * 
 * 4. Time zone (4 bytes).<br>
 * 
 * 5. Sigfigs? (libcap.h reports this as accuracy of time stamps - 4 bytes).<br>
 * 
 * 6. Snaplen (The packet capture length. 4 bytes)<br>
 * 
 * 7. Network Type (The data link type. 4 bytes)<p>
 * 
 * The format of a single packet is as follows:<p>
 * 
 * 1. Timestamp seconds (4 bytes)<br>
 * 
 * 2. Timestamp microseconds (4 bytes)<br>
 * 
 * 3. Captured length (4 bytes) <br>
 * 
 * 4. Real length (4 bytes) <br>
 * 
 * 5. Packet data (The actual bytes in the packet - CapturedLength bytes).<br>
 * 
 * 6. The interface index (a 4 byte integer).<br>
 * 
 * 7. The Ethernet packet type (a 2 byte integer).<br>
 * 
 * 8. The packet type (broadcast/multicast etc. - a 1 byte integer).<br>
 * 
 * 9. 2 bytes of CPU information. (I haven't the faintest 
 *    idea what this is about).
 * 
 * 10. A 3 byte pad.
 *  
 * @author prasanna
 */
public class LibPcap2InputStream extends LibPcapInputStream {

    /**
     * The magic number for the mofified libpcap files.
     */
    public final String LIBPCAP2_MAGIC = "A1B2CD34";

    /**
     * The swapped magic number for modified libpcap files.
     */
    public final String LIBPCAP2_SWAPPED_MAGIC = "34CDB2A1";

    /**
     * Does nothing.
     */
    public LibPcap2InputStream() {
        super();
    }

    /** 
     * Reads the magic identifier which must be one of <code>
     * LIBPCAP2_MAGIC</code> or <code>LIBPCAP2_SWAPPED_MAGIC</code>. Throws 
     * a PcapException if this is not the case. Will throw an IOException 
     * if an I/O error occurs.
     * 
     * @see com.parfumball.file.PcapInputStream#readMagic()
     */
    protected void readMagic() throws IOException, PcapException {
        int myMagic = in.readInt();
        magic = Integer.toHexString(myMagic).toUpperCase();
        if (magic.equalsIgnoreCase(LIBPCAP2_SWAPPED_MAGIC)) {
            isSwapped = true;
        } else if (!magic.equalsIgnoreCase(LIBPCAP2_MAGIC)) {
            throw new PcapException("This file is not a Libpcap file.");
        }
    }

    /**
     * Reads the next packet's record header from the underlying
     * InputStream.
     * 
     * @return
     * @throws IOException
     * @throws PcapException
     */
    public Packet readRecordHeader() throws IOException, PcapException {
        Packet p = super.readRecordHeader();
        if (p != null) {
            in.skipBytes(12);
        }
        return p;
    }
}
