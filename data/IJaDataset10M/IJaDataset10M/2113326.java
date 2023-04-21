package com.inetmon.jn.decoder.layer5.vlan;

import jpcap.packet.Packet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import com.inetmon.jn.decoder.ReferenceOnTable;
import com.inetmon.jn.decoder.layer4.VLANIPv4Packet;
import com.inetmon.jn.decoder.layer4.VLANIPv6Packet;
import com.inetmon.jn.decoder.tools.Activator;
import com.inetmon.jn.decoder.tools.ConstantColor;
import com.inetmon.jn.decoder.tools.DecoderHelper;

/**
 * http://www.faqs.org/rfcs/rfc1883.html
 * 
 * The Hop-by-Hop Options header is used to carry optional information
   that must be examined by every node along a packet's delivery path.
   The Hop-by-Hop Options header is identified by a Next Header value of
   0 in the IPv6 header, and has the following format:

   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |  Next Header  |  Hdr Ext Len  |                               |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                               +
   |                                                               |
   .                                                               .
   .                            Options                            .
   .                                                               .
   |                                                               |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * @author Kamel
 *
 */
public class VLANIPv6HopbyHopOptionPacket extends VLANIPv6Packet {

    /**
	 * 8-bit selector.  Identifies the type of header immediately following the Hop-by-Hop Options header.  Uses the same values as the IPv4 Protocol field [RFC-1700 et seq.].
	 * @uml.property  name="nextHeader"
	 */
    private int nextHeader;

    /**
	 * 8-bit unsigned integer.  Length of the Hop-by-Hop Options header in 8-octet units, not including the first 8 octets.
	 * @uml.property  name="hdrExtLen"
	 */
    private int HdrExtLen;

    /**
	 * Variable-length field, of length such that the complete Hop-by-Hop Options header is an integer multiple of 8 octets long.  Contains one or more TLV-encoded options, as described in section 4.2.
	 * @uml.property  name="options"
	 */
    private String Options;

    /** color representing the packet */
    private static int COLOR = ConstantColor.getColorNumber(23);

    public VLANIPv6HopbyHopOptionPacket(Packet packet) {
        super(packet);
        decodeVLANIPv6HopbyHopOptionPacket();
        setInfo("NextHeader :" + VLANIPv4Packet.getProtocolName(nextHeader));
        setProtocol1("VLAN Hop-by-Hop Options Header v6");
    }

    public void decodeVLANIPv6HopbyHopOptionPacket() {
        int offset = getEthernetHeaderLength() + getVLANHeaderLength() + VLANIPv6Packet.getIpv6HeaderLength();
        nextHeader = DecoderHelper.extractInteger(combined, offset, 1);
        offset += 1;
        HdrExtLen = DecoderHelper.extractInteger(combined, offset, 1);
        offset += 1;
        Options = DecoderHelper.readHexValueOnXBytes(combined, offset, ((HdrExtLen + 1) * 8) - 2);
    }

    /** TO PRINT ON THE TABLE VIEW OF ANALYSER */
    public String[] getLine() {
        return new String[] { getTime().substring(11), getSourceMAC(), getDestinationMAC(), getSrcAdd(), getDestAdd(), getProtocol1(), getVLANFieldID(), getInfo() };
    }

    public void toTree(TreeItem tree) {
        super.toTree(tree);
        int offset = getEthernetHeaderLength() + getVLANHeaderLength() + VLANIPv6Packet.getIpv6HeaderLength();
        TreeItem main = new TreeItem(tree, SWT.NONE);
        main.setText("Hop-by-Hop Options Header");
        main.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT2));
        main.setData(new ReferenceOnTable(offset, (HdrExtLen + 1) * 8));
        TreeItem t1 = new TreeItem(main, SWT.NONE);
        t1.setText("nextHeader : " + nextHeader + "(" + VLANIPv4Packet.getProtocolName(nextHeader) + ")");
        t1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t1.setData(new ReferenceOnTable(offset, 1));
        offset += 1;
        TreeItem t2 = new TreeItem(main, SWT.NONE);
        t2.setText("HdrExtLen : " + HdrExtLen);
        t2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t2.setData(new ReferenceOnTable(offset, 1));
        offset += 1;
        TreeItem t3 = new TreeItem(main, SWT.NONE);
        t3.setText("Options: " + Options);
        t3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t3.setData(new ReferenceOnTable(offset, ((HdrExtLen + 1) * 8) - 2));
        offset += ((HdrExtLen + 1) * 8) - 2;
    }

    public static boolean isAnalyzable(Packet packet) {
        int codeProto = DecoderHelper.extractInteger(packet.combined, 24, 1);
        if (codeProto == VLANIPv4Packet.IP_PROTO_HOPOPTS) return true; else return false;
    }

    public int getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(int color) {
        COLOR = color;
    }

    /**
	 * @return
	 * @uml.property  name="hdrExtLen"
	 */
    public int getHdrExtLen() {
        return HdrExtLen;
    }

    /**
	 * @param hdrExtLen
	 * @uml.property  name="hdrExtLen"
	 */
    public void setHdrExtLen(int hdrExtLen) {
        HdrExtLen = hdrExtLen;
    }

    /**
	 * @return
	 * @uml.property  name="nextHeader"
	 */
    public int getNextHeader() {
        return nextHeader;
    }

    /**
	 * @param nextHeader
	 * @uml.property  name="nextHeader"
	 */
    public void setNextHeader(int nextHeader) {
        this.nextHeader = nextHeader;
    }

    /**
	 * @return
	 * @uml.property  name="options"
	 */
    public String getOptions() {
        return Options;
    }

    /**
	 * @param options
	 * @uml.property  name="options"
	 */
    public void setOptions(String options) {
        Options = options;
    }
}
