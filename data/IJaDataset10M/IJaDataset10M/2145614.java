package com.inetmon.jn.decoder.layer4;

import jpcap.packet.Packet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import com.inetmon.jn.decoder.ReferenceOnTable;
import com.inetmon.jn.decoder.layer3.IPv4Packet;
import com.inetmon.jn.decoder.tools.Activator;
import com.inetmon.jn.decoder.tools.ConstantColor;
import com.inetmon.jn.decoder.tools.DecoderHelper;

/**
 * The Lightweight User Datagram Protocol (UDP-Lite) (RFC 3828), which is 
 * similar to the User Datagram Protocol (UDP) (RFC 768), but can also 
 * serve applications in error-prone network environments that prefer to 
 * have partially damaged payloads delivered rather than discarded. 
 * If this feature is not used, UDP-Lite is semantically identical to UDP.
 * @author Kamel
 *
 */
public class IPv4UDPLitePacket extends IPv4Packet {

    /**
	 * source port
	 * @uml.property  name="srcPort"
	 */
    int srcPort;

    /**
	 * destination port
	 * @uml.property  name="dstPort"
	 */
    int dstPort;

    /**
	 * checksum coverage
	 * @uml.property  name="checkSumCoverage"
	 */
    String checkSumCoverage;

    /**
	 * checksum
	 * @uml.property  name="checkSum"
	 */
    String checkSum;

    /** color representing the packet */
    private static int COLOR = ConstantColor.getColorNumber(30);

    public static final int HEADER_LENGTH = 8;

    /**
	 * decode the packet and initialize field
	 * @param packet
	 */
    public IPv4UDPLitePacket(Packet packet) {
        super(packet);
        decodeIPv4UDPLitePacket();
        setInfo("Ports :" + srcPort + " > " + dstPort);
        setProtocol1("UDP-Lite");
    }

    /**
	 * decode the packet 
	 * @param packet
	 */
    public void decodeIPv4UDPLitePacket() {
        int offset = getEthernetHeaderLength() + getIpv4HeaderLength();
        srcPort = DecoderHelper.extractInteger(combined, offset, 2);
        offset += 2;
        dstPort = DecoderHelper.extractInteger(combined, offset, 2);
        offset += 2;
        checkSumCoverage = DecoderHelper.readHexValueOnXBytes(combined, offset, 2);
        offset += 2;
        checkSum = DecoderHelper.readHexValueOnXBytes(combined, offset, 2);
        offset += 2;
    }

    /** TO PRINT ON THE TABLE VIEW OF ANALYSER */
    public String[] getLine() {
        return new String[] { getTime().substring(11), getSourceMAC(), getDestinationMAC(), getSrcAdd(), getDestAdd(), getProtocol1(), "", getInfo() };
    }

    public void toTree(TreeItem tree) {
        super.toTree(tree);
        int offset = getEthernetHeaderLength() + getIpv4HeaderLength();
        TreeItem main = new TreeItem(tree, SWT.NONE);
        main.setText("Lightweight User Datagram Protocol");
        main.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT2));
        main.setData(new ReferenceOnTable(offset, IPv4UDPLitePacket.HEADER_LENGTH));
        TreeItem t1 = new TreeItem(main, SWT.NONE);
        t1.setText("Source Port : " + srcPort);
        t1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t1.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
        TreeItem t2 = new TreeItem(main, SWT.NONE);
        t2.setText("Destination Port : " + dstPort);
        t2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t2.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
        TreeItem t3 = new TreeItem(main, SWT.NONE);
        t3.setText("CheckSum Coverage : 0x" + checkSumCoverage);
        t3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t3.setData(new ReferenceOnTable(offset, 4));
        offset += 4;
        TreeItem t4 = new TreeItem(main, SWT.NONE);
        t4.setText("CheckSum : 0x" + checkSum);
        t4.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t4.setData(new ReferenceOnTable(offset, 4));
        offset += 4;
    }

    public static boolean isAnalyzable(Packet packet) {
        int codeProto = DecoderHelper.extractInteger(packet.combined, 23, 1);
        if (codeProto == IPv4Packet.IP_PROTO_UDPLITE) return true; else return false;
    }

    public int getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(int color) {
        COLOR = color;
    }

    /**
	 * @return
	 * @uml.property  name="checkSum"
	 */
    public String getCheckSum() {
        return checkSum;
    }

    /**
	 * @param checkSum
	 * @uml.property  name="checkSum"
	 */
    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    /**
	 * @return
	 * @uml.property  name="checkSumCoverage"
	 */
    public String getCheckSumCoverage() {
        return checkSumCoverage;
    }

    /**
	 * @param checkSumCoverage
	 * @uml.property  name="checkSumCoverage"
	 */
    public void setCheckSumCoverage(String checkSumCoverage) {
        this.checkSumCoverage = checkSumCoverage;
    }

    /**
	 * @return
	 * @uml.property  name="dstPort"
	 */
    public int getDstPort() {
        return dstPort;
    }

    /**
	 * @param dstPort
	 * @uml.property  name="dstPort"
	 */
    public void setDstPort(int dstPort) {
        this.dstPort = dstPort;
    }

    /**
	 * @return
	 * @uml.property  name="srcPort"
	 */
    public int getSrcPort() {
        return srcPort;
    }

    /**
	 * @param srcPort
	 * @uml.property  name="srcPort"
	 */
    public void setSrcPort(int srcPort) {
        this.srcPort = srcPort;
    }
}
