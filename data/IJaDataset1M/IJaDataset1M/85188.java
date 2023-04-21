package com.inetmon.jn.decoder.layer3;

import jpcap.packet.Packet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import com.inetmon.jn.decoder.ReferenceOnTable;
import com.inetmon.jn.decoder.layer2.EthernetPacket;
import com.inetmon.jn.decoder.tools.Activator;
import com.inetmon.jn.decoder.tools.ConstantColor;
import com.inetmon.jn.decoder.tools.DecoderHelper;

/**
 * 
 * The IPX WAN protocol operation begins immediately after link establishment.
 *  While IPX is a connectionless datagram protocol, WANs are 
 *  often connection-oriented. Different WANs have different methods of link 
 *  establishment.
 * @author Kamel
 *
 */
public class IPXPacket extends EthernetPacket {

    /**
	 * Checksum. 16 bits.Always set to 0xFFFF.
	 * @uml.property  name="checkSum"
	 */
    String checkSum;

    /**
	 * Packet Length. 16 bits.The total length of the packet header and the data.
	 * @uml.property  name="length"
	 */
    int length;

    /**
	 * Transport control. 8 bits.The hop count.
	 * @uml.property  name="transportControl"
	 */
    int transportControl;

    /**
	 * Type. 8 bits.Specifies the encapsulated protocol.
	 * @uml.property  name="packetType"
	 */
    int packetType;

    /**
	 * Destination network. 32 bits.A subnet identifier of the receiver.
	 * @uml.property  name="destinationNetwork"
	 */
    String destinationNetwork;

    /**
	 * Destination node. 48 bits.The physical address of the receiver.
	 * @uml.property  name="destinationNode"
	 */
    String destinationNode;

    /**
	 * Destination socket. 16 bits.	The socket number of the receiver.
	 * @uml.property  name="destinationSocket"
	 */
    int destinationSocket;

    /**
	 * Source network. 32 bits.Subnet identifier of the sender.
	 * @uml.property  name="sourceNetwork"
	 */
    String sourceNetwork;

    /**
	 * Source node. 48 bits.The physical address of the sender.
	 * @uml.property  name="sourceNode"
	 */
    String sourceNode;

    /**
	 * Source socket. 16 bits.The socket number of the sender.
	 * @uml.property  name="sourceSocket"
	 */
    int sourceSocket;

    /** color representing the packet */
    private static int COLOR = ConstantColor.getColorNumber(13);

    private static final int IPX_PACKET_TYPE_IPX = 0;

    private static final int IPX_PACKET_TYPE_RIP = 1;

    private static final int IPX_PACKET_TYPE_ECHO = 2;

    private static final int IPX_PACKET_TYPE_ERROR = 3;

    private static final int IPX_PACKET_TYPE_PEP = 4;

    private static final int IPX_PACKET_TYPE_SPX = 5;

    private static final int IPX_PACKET_TYPE_NCP = 17;

    private static final int IPX_PACKET_TYPE_WANBCAST = 20;

    private static final int IPX_SOCKET_PING_CISCO = 0x0002;

    private static final int IPX_SOCKET_NCP = 0x0451;

    private static final int IPX_SOCKET_SAP = 0x0452;

    private static final int IPX_SOCKET_IPXRIP = 0x0453;

    private static final int IPX_SOCKET_NETBIOS = 0x0455;

    private static final int IPX_SOCKET_DIAGNOSTIC = 0x0456;

    private static final int IPX_SOCKET_SERIALIZATION = 0x0457;

    private static final int IPX_SOCKET_NWLINK_SMB_SERVER = 0x0550;

    private static final int IPX_SOCKET_NWLINK_SMB_NAMEQUERY = 0x0551;

    private static final int IPX_SOCKET_NWLINK_SMB_REDIR = 0x0552;

    private static final int IPX_SOCKET_NWLINK_SMB_MAILSLOT = 0x0553;

    private static final int IPX_SOCKET_NWLINK_SMB_MESSENGER = 0x0554;

    private static final int IPX_SOCKET_NWLINK_SMB_BROWSE = 0x0555;

    private static final int IPX_SOCKET_ATTACHMATE_GW = 0x055d;

    private static final int IPX_SOCKET_IPX_MESSAGE = 0x4001;

    private static final int IPX_SOCKET_IPX_MESSAGE1 = 0x4003;

    private static final int IPX_SOCKET_ADSM = 0x8522;

    private static final int IPX_SOCKET_EIGRP = 0x85be;

    private static final int IPX_SOCKET_NLSP = 0x9001;

    private static final int IPX_SOCKET_IPXWAN = 0x9004;

    private static final int IPX_SOCKET_SNMP_AGENT = 0x900F;

    private static final int IPX_SOCKET_SNMP_SINK = 0x9010;

    private static final int IPX_SOCKET_PING_NOVELL = 0x9086;

    private static final int IPX_SOCKET_TCP_TUNNEL = 0x9091;

    private static final int IPX_SOCKET_UDP_TUNNEL = 0x9092;

    private static final int SPX_SOCKET_PA = 0x90b2;

    private static final int SPX_SOCKET_BROKER = 0x90b3;

    private static final int SPX_SOCKET_SRS = 0x90b4;

    private static final int SPX_SOCKET_ENS = 0x90b5;

    private static final int SPX_SOCKET_RMS = 0x90b6;

    private static final int SPX_SOCKET_NOTIFY_LISTENER = 0x90b7;

    public IPXPacket(Packet packet) {
        super(packet);
        decodeIPXPacket();
        setInfo("Type : " + getTypeName(packetType) + " , SrcSocket : " + getSocketName(sourceSocket) + " , DstSocket:" + getSocketName(destinationSocket));
        setProtocol1("IPX");
    }

    public void decodeIPXPacket() {
        int offset = getEthernetHeaderLength();
        checkSum = DecoderHelper.readHexValueOnXBytes(combined, offset, 2);
        offset += 2;
        length = DecoderHelper.extractInteger(combined, offset, 2);
        offset += 2;
        transportControl = DecoderHelper.extractInteger(combined, offset, 1);
        offset += 1;
        packetType = DecoderHelper.extractInteger(combined, offset, 1);
        offset += 1;
        destinationNetwork = DecoderHelper.readHexValueOnXBytes(combined, offset, 4);
        offset += 4;
        destinationNode = DecoderHelper.readMacAdress(combined, offset);
        offset += 6;
        destinationSocket = DecoderHelper.extractInteger(combined, offset, 2);
        offset += 2;
        sourceNetwork = DecoderHelper.readHexValueOnXBytes(combined, offset, 4);
        offset += 4;
        sourceNode = DecoderHelper.readMacAdress(combined, offset);
        offset += 6;
        sourceSocket = DecoderHelper.extractInteger(combined, offset, 2);
        offset += 2;
    }

    public String[] getLine() {
        return new String[] { getTime().substring(11), getSourceMAC(), getDestinationMAC(), sourceNetwork + "_" + sourceNode, destinationNetwork + "_" + destinationNode, getProtocol1(), "", getInfo() };
    }

    public void toTree(TreeItem tree) {
        super.toTree(tree);
        int offset = getEthernetHeaderLength();
        TreeItem main = new TreeItem(tree, SWT.NONE);
        main.setText("Internetwork Packet eXchange");
        main.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT2));
        main.setData(new ReferenceOnTable(0, length));
        TreeItem t1 = new TreeItem(main, SWT.NONE);
        t1.setText("CheckSum:   0x" + checkSum);
        t1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t1.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
        TreeItem t2 = new TreeItem(main, SWT.NONE);
        t2.setText("Length:   " + length);
        t2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t2.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
        TreeItem t3 = new TreeItem(main, SWT.NONE);
        t3.setText("Transport Control:   " + transportControl);
        t3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t3.setData(new ReferenceOnTable(offset, 1));
        offset += 1;
        TreeItem t4 = new TreeItem(main, SWT.NONE);
        t4.setText("Packet Type:   " + packetType + "(" + getTypeName(packetType) + ")");
        t4.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t4.setData(new ReferenceOnTable(offset, 1));
        offset += 1;
        TreeItem t5 = new TreeItem(main, SWT.NONE);
        t5.setText("Destination Network:   " + destinationNetwork);
        t5.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t5.setData(new ReferenceOnTable(offset, 4));
        offset += 4;
        TreeItem t6 = new TreeItem(main, SWT.NONE);
        t6.setText("Destination Node:   " + destinationNode);
        t6.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t6.setData(new ReferenceOnTable(offset, 6));
        offset += 6;
        TreeItem t8 = new TreeItem(main, SWT.NONE);
        t8.setText("Destination Socket:   " + destinationSocket + "(" + getSocketName(destinationSocket) + ")");
        t8.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t8.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
        TreeItem t9 = new TreeItem(main, SWT.NONE);
        t9.setText("Source Network:   " + sourceNetwork);
        t9.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t9.setData(new ReferenceOnTable(offset, 4));
        offset += 4;
        TreeItem t10 = new TreeItem(main, SWT.NONE);
        t10.setText("Source Node:   " + sourceNode);
        t10.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t10.setData(new ReferenceOnTable(offset, 6));
        offset += 6;
        TreeItem t11 = new TreeItem(main, SWT.NONE);
        t11.setText("Source Socket:   " + sourceSocket + "(" + getSocketName(sourceSocket) + ")");
        t11.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        t11.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
    }

    public static boolean isAnalyzable(Packet packet) {
        if (DecoderHelper.toString(packet.combined[12]).equals("81") && DecoderHelper.toString(packet.combined[13]).equals("37")) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "CK:" + checkSum + " L:" + length + " TC:" + transportControl + "PT:" + packetType + " DNet:" + destinationNetwork + "Dnode:" + destinationNode + "Dsoc:" + destinationSocket + "SNet:" + sourceNetwork + "Snode:" + sourceNode + "Ssoc:" + sourceSocket;
    }

    public String getSocketName(int code) {
        String res = "";
        if (code == IPX_SOCKET_PING_CISCO) res = "CISCO PING"; else if (code == IPX_SOCKET_NCP) res = "NCP"; else if (code == IPX_SOCKET_SAP) res = "SAP"; else if (code == IPX_SOCKET_IPXRIP) res = "RIP"; else if (code == IPX_SOCKET_NETBIOS) res = "NetBIOS"; else if (code == IPX_SOCKET_DIAGNOSTIC) res = "Diagnostic"; else if (code == IPX_SOCKET_SERIALIZATION) res = "Serialization"; else if (code == IPX_SOCKET_NWLINK_SMB_SERVER) res = "NWLink SMB Server"; else if (code == IPX_SOCKET_NWLINK_SMB_NAMEQUERY) res = "NWLink SMB Name Query"; else if (code == IPX_SOCKET_NWLINK_SMB_REDIR) res = "NWLink SMB Redirector"; else if (code == IPX_SOCKET_NWLINK_SMB_MAILSLOT) res = "NWLink SMB Mailslot Datagram"; else if (code == IPX_SOCKET_NWLINK_SMB_MESSENGER) res = "NWLink SMB Messenger"; else if (code == IPX_SOCKET_NWLINK_SMB_BROWSE) res = "NWLink SMB Browse"; else if (code == IPX_SOCKET_ATTACHMATE_GW) res = "Attachmate Gateway"; else if (code == IPX_SOCKET_IPX_MESSAGE) res = "IPX Message"; else if (code == IPX_SOCKET_IPX_MESSAGE1) res = "IPX Message"; else if (code == IPX_SOCKET_SNMP_AGENT) res = "SNMP Agent"; else if (code == IPX_SOCKET_SNMP_SINK) res = "SNMP Sink"; else if (code == IPX_SOCKET_PING_NOVELL) res = "Novell PING"; else if (code == IPX_SOCKET_UDP_TUNNEL) res = "UDP Tunnel"; else if (code == IPX_SOCKET_TCP_TUNNEL) res = "TCP Tunnel"; else if (code == IPX_SOCKET_TCP_TUNNEL) res = "TCP Tunnel"; else if (code == IPX_SOCKET_ADSM) res = "ADSM"; else if (code == IPX_SOCKET_EIGRP) res = "Cisco EIGRP for IPX"; else if (code == IPX_SOCKET_NLSP) res = "NetWare Link Services Protocol"; else if (code == IPX_SOCKET_IPXWAN) res = "IPX WAN"; else if (code == SPX_SOCKET_PA) res = "NDPS Printer Agent/PSM"; else if (code == SPX_SOCKET_BROKER) res = "NDPS Broker"; else if (code == SPX_SOCKET_SRS) res = "NDPS Service Registry Service"; else if (code == SPX_SOCKET_ENS) res = "NDPS Event Notification Service"; else if (code == SPX_SOCKET_RMS) res = "NDPS Remote Management Service"; else if (code == SPX_SOCKET_NOTIFY_LISTENER) res = "NDPS Notify Listener"; else if (code == 0xE885) res = "NT Server-RPC/GW"; else if (code == 0x400C) res = "HP LaserJet/QuickSilver"; else if (code == 0x907B) res = "SMS Testing and Development"; else if (code == 0x8F83) res = "Powerchute UPS Monitoring"; else if (code == 0x4006) res = "NetWare Directory Server"; else if (code == 0x8104) res = "NetWare 386"; else if (code == 0x0000) res = "NULL"; else res = "unkown";
        return res;
    }

    public String getTypeName(int code) {
        String res = "";
        if (code == IPX_PACKET_TYPE_IPX) res = "IPX"; else if (code == IPX_PACKET_TYPE_RIP) res = "RIP"; else if (code == IPX_PACKET_TYPE_ECHO) res = "ECHO"; else if (code == IPX_PACKET_TYPE_ERROR) res = "ERROR"; else if (code == IPX_PACKET_TYPE_PEP) res = "PEP"; else if (code == IPX_PACKET_TYPE_SPX) res = "SPX"; else if (code == IPX_PACKET_TYPE_NCP) res = "NCP"; else if (code == IPX_PACKET_TYPE_WANBCAST) res = "WANBCAST"; else res = "unknown";
        return res;
    }

    public int getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(int color) {
        COLOR = color;
    }
}
