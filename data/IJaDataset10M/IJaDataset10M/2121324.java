package com.inetmon.jn.decoder.layer4;

import java.io.Serializable;
import jpcap.packet.Packet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import com.inetmon.jn.decoder.ReferenceOnTable;
import com.inetmon.jn.decoder.layer2.EthernetPacket;
import com.inetmon.jn.decoder.layer3.IPv4Packet;
import com.inetmon.jn.decoder.layer3.IPv6Packet;
import com.inetmon.jn.decoder.tools.Activator;
import com.inetmon.jn.decoder.tools.ConstantColor;
import com.inetmon.jn.decoder.tools.DecoderHelper;

/**
 * http://www.ietf.org/rfc/rfc2740.txt
 * OSPF to support version
 *  6 of the Internet Protocol (IPv6).  The fundamental mechanisms of
 *  OSPF (flooding, DR election, area support, SPF calculations, etc.)
 *  remain unchanged. However, some changes have been necessary, either
 *  due to changes in protocol semantics between IPv4 and IPv6, or simply
 *  to handle the increased address size of IPv6
 * 
 *  0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |   Version #   |     Type      |         Packet length         |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                         Router ID                             |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                          Area ID                              |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |          Checksum             |  Instance ID  |      0        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * @author kamel
 *
 */
public class IPv6OSPFPacket extends IPv6Packet {

    /**
	 * Set to 3, the version number of the protocol as documented in this specification.
	 * @uml.property  name="version"
	 */
    int version;

    /**
	 * The type of OSPF packet, such as Link state Update or Hello Packet.
	 * @uml.property  name="type"
	 */
    int type;

    /**
	 * The length of the entire OSPF packet in bytes, including the standard OSPF packet header.
	 * @uml.property  name="packetLength"
	 */
    int packetLength;

    /**
	 * The identity of the router itself (who is originating the packet).
	 * @uml.property  name="routerID"
	 */
    String routerID;

    /**
	 * The OSPF area that the packet is being sent into.
	 * @uml.property  name="areaID"
	 */
    String areaID;

    /**
	 * This checksum is calculated as the 16-bit one's complement of the one's complement sum of all the 16-bit words in the packet, excepting the authentication field. If the packet's length is not an integral number of 16-bit words, the packet is padded with a byte of zero before checksumming.
	 * @uml.property  name="checksum"
	 */
    String checksum;

    /**
	 * The OSPF Instance ID associated with the interface that the packet is being sent out of.
	 * @uml.property  name="instanceID"
	 */
    int instanceID;

    /**
	 * reserved. 8 bits.Must be cleared to 0.
	 * @uml.property  name="reserved"
	 */
    int reserved;

    /**
	 * Message body (it depend of the message type)
	 * @uml.property  name="helloMsg"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="this$0:com.inetmon.jn.decoder.layer4.IPv6OSPFPacket$HelloMessage"
	 */
    HelloMessage helloMsg;

    /**
	 * @uml.property  name="dbDescMsg"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="this$0:com.inetmon.jn.decoder.layer4.IPv6OSPFPacket$DataBaseDescMessage"
	 */
    DataBaseDescMessage dbDescMsg;

    /**
	 * @uml.property  name="lsrMsg"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="this$0:com.inetmon.jn.decoder.layer4.IPv6OSPFPacket$LinkStateRequestMessage"
	 */
    LinkStateRequestMessage lsrMsg;

    /**
	 * @uml.property  name="lsuMsg"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="this$0:com.inetmon.jn.decoder.layer4.IPv6OSPFPacket$LinkStateUpdateMessage"
	 */
    LinkStateUpdateMessage lsuMsg;

    /**
	 * @uml.property  name="lsaMsg"
	 * @uml.associationEnd  readOnly="true" inverse="this$0:com.inetmon.jn.decoder.layer4.IPv6OSPFPacket$LinkStateAcknowledgmentMessage"
	 */
    LinkStateAcknowledgmentMessage lsaMsg;

    /** color representing the packet */
    private static int COLOR = ConstantColor.getColorNumber(32);

    private static final int TYPE_Hello = 1;

    private static final int TYPE_DatabaseDescription = 2;

    private static final int TYPE_LinkStateRequest = 3;

    private static final int TYPE_LinkStateUpdate = 4;

    private static final int TYPE_LinkStateAcknowledgment = 5;

    public IPv6OSPFPacket(Packet packet) {
        super(packet);
        decodeIPv6OSPFPacket();
        setInfo("Message Type : " + getTypeName(type));
        setProtocol1("OSPF_v6");
    }

    public void decodeIPv6OSPFPacket() {
        int offset = EthernetPacket.getEthernetHeaderLength() + IPv6Packet.getIpv6HeaderLength();
        version = DecoderHelper.extractInteger(combined, offset, 1);
        offset += 1;
        type = DecoderHelper.extractInteger(combined, offset, 1);
        offset += 1;
        packetLength = DecoderHelper.extractInteger(combined, offset, 2);
        offset += 2;
        routerID = DecoderHelper.readIpv4Adress(combined, offset);
        offset += 4;
        areaID = DecoderHelper.readIpv4Adress(combined, offset);
        offset += 4;
        checksum = DecoderHelper.readHexValueOnXBytes(combined, offset, 2);
        offset += 2;
        instanceID = DecoderHelper.extractInteger(combined, offset, 1);
        offset += 1;
        reserved = DecoderHelper.extractInteger(combined, offset, 1);
        offset += 1;
        if (type == TYPE_Hello) helloMsg = new HelloMessage(combined, offset); else if (type == TYPE_DatabaseDescription) dbDescMsg = new DataBaseDescMessage(combined, offset); else if (type == TYPE_LinkStateRequest) lsrMsg = new LinkStateRequestMessage(combined, offset); else if (type == TYPE_LinkStateUpdate) lsuMsg = new LinkStateUpdateMessage(combined, offset);
    }

    /** TO PRINT ON THE TABLE VIEW OF ANALYSER */
    public String[] getLine() {
        return new String[] { getTime().substring(11), getSourceMAC(), getDestinationMAC(), getSrcAdd(), getDestAdd(), getProtocol1(), "", getInfo() };
    }

    public void toTree(TreeItem tree) {
        super.toTree(tree);
        int offset = EthernetPacket.getEthernetHeaderLength() + IPv6Packet.getIpv6HeaderLength();
        TreeItem root = new TreeItem(tree, SWT.NONE);
        root.setText("Open Short Path First");
        root.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT2));
        root.setData(new ReferenceOnTable(offset, combined.length - offset));
        TreeItem main = new TreeItem(root, SWT.NONE);
        main.setText("OSPF Header");
        main.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        main.setData(new ReferenceOnTable(offset, 20));
        TreeItem t1 = new TreeItem(main, SWT.NONE);
        t1.setText("Version :" + version);
        t1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t1.setData(new ReferenceOnTable(offset, 1));
        offset += 1;
        TreeItem t2 = new TreeItem(main, SWT.NONE);
        t2.setText("Type :" + type + "(" + getTypeName(type) + ")");
        t2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t2.setData(new ReferenceOnTable(offset, 1));
        offset += 1;
        TreeItem t3 = new TreeItem(main, SWT.NONE);
        t3.setText("Packet Length :" + packetLength);
        t3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t3.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
        TreeItem t4 = new TreeItem(main, SWT.NONE);
        t4.setText("Router ID:" + routerID);
        t4.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t4.setData(new ReferenceOnTable(offset, 4));
        offset += 4;
        TreeItem t5 = new TreeItem(main, SWT.NONE);
        t5.setText("Area ID:" + areaID);
        t5.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t5.setData(new ReferenceOnTable(offset, 4));
        offset += 4;
        TreeItem t6 = new TreeItem(main, SWT.NONE);
        t6.setText("Checksum:" + checksum);
        t6.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t6.setData(new ReferenceOnTable(offset, 2));
        offset += 2;
        TreeItem t7 = new TreeItem(main, SWT.NONE);
        t7.setText("Instance ID:" + instanceID);
        t7.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t7.setData(new ReferenceOnTable(offset, 1));
        offset += 1;
        TreeItem t8 = new TreeItem(main, SWT.NONE);
        t8.setText("Reserved:" + reserved);
        t8.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        t8.setData(new ReferenceOnTable(offset, 1));
        offset += 1;
        TreeItem main2 = new TreeItem(root, SWT.NONE);
        main2.setText("OSPF " + getTypeName(type));
        main2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        main2.setData(new ReferenceOnTable(offset, combined.length - offset));
        if (type == TYPE_Hello) {
            helloMsg.toTree(main2);
        }
    }

    public static boolean isAnalyzable(Packet packet) {
        int codeProto = DecoderHelper.extractInteger(packet.combined, 20, 1);
        if (codeProto == IPv4Packet.IP_PROTO_OSPF) return true; else return false;
    }

    public String getTypeName(int code) {
        String res = "";
        if (code == TYPE_Hello) res = "Hello Packet"; else if (code == TYPE_DatabaseDescription) res = "Database Description"; else if (code == TYPE_LinkStateRequest) res = "Link State Request"; else if (code == TYPE_LinkStateUpdate) res = "Link State Update"; else if (code == TYPE_LinkStateAcknowledgment) res = "Link State Acknowledgment"; else res = "unknown";
        return res;
    }

    public int getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(int color) {
        COLOR = color;
    }

    /**
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                        Interface ID                           |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |    Rtr Pri    |             Options                           |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |        HelloInterval         |        RouterDeadInterval      |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                   Designated Router ID                        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                Backup Designated Router ID                    |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                         Neighbor ID                           |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   @author Kamel
	 */
    public class HelloMessage implements Serializable {

        /**Interface ID
		 32-bit number uniquely identifying this interface among the
		 collection of this router's interfaces. For example, in some
		 implementations it may be possible to use the MIB-II IfIndex
		 ([Ref3]).*/
        private int interfaceID;

        /** Rtr Pri
		 This router's Router Priority.  Used in (Backup) Designated
		 Router election.  If set to 0, the router will be ineligible to
		 become (Backup) Designated Router.*/
        private int rtrPri;

        /** Options
		 The optional capabilities supported by the router, as documented
		 in Section A.2.*/
        private int options;

        private int v6bit;

        private int Ebit;

        private int MCbit;

        private int Nbit;

        private int Rbit;

        private int DCbit;

        /** HelloInterval
		 The number of seconds between this router's Hello packets.*/
        private int helloInterval;

        /** RouterDeadInterval
		 The number of seconds before declaring a silent router down.*/
        private int routerDeadInterval;

        /**Designated Router ID
		 The identity of the Designated Router for this network, in the
		 view of the sending router.  The Designated Router is identified
		 by its Router ID. Set to 0.0.0.0 if there is no Designated
		 Router.*/
        private String designatedRouterID;

        /**  Backup Designated Router ID
		 The identity of the Backup Designated Router for this network, in
		 the view of the sending router.  The Backup Designated Router is
		 identified by its IP Router ID.  Set to 0.0.0.0 if there is no
		 Backup Designated Router.*/
        private String backupDesignatedRouterID;

        /** Neighbor ID
		 The Router IDs of each router from whom valid Hello packets have
		 been seen recently on the network.  Recently means in the last
		 RouterDeadInterval seconds.*/
        private String neighborID;

        /**
		 * this constructor decode the packet
		 * @param combined
		 * @param offset
		 */
        public HelloMessage(byte[] combined, int offset) {
            interfaceID = DecoderHelper.extractInteger(combined, offset, 4);
            offset += 4;
            rtrPri = DecoderHelper.extractInteger(combined, offset, 1);
            options = DecoderHelper.extractInteger(combined, offset, 4);
            options = options & 0x00ffffff;
            DCbit = (options & 0x20) >>> 5;
            Rbit = (options & 0x10) >>> 4;
            Nbit = (options & 0x08) >>> 3;
            ;
            MCbit = (options & 0x04) >>> 2;
            Ebit = (options & 0x02) >>> 1;
            v6bit = (options & 0x01);
            offset += 4;
            helloInterval = DecoderHelper.extractInteger(combined, offset, 2);
            offset += 2;
            routerDeadInterval = DecoderHelper.extractInteger(combined, offset, 2);
            offset += 2;
            designatedRouterID = DecoderHelper.readIpv4Adress(combined, offset);
            offset += 4;
            backupDesignatedRouterID = DecoderHelper.readIpv4Adress(combined, offset);
            offset += 4;
        }

        /**
		 * Add the tree element for this message
		 * @param tree
		 */
        public void toTree(TreeItem tree) {
            TreeItem t1 = new TreeItem(tree, SWT.NONE);
            t1.setText("Interface ID :" + interfaceID);
            t1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t2 = new TreeItem(tree, SWT.NONE);
            t2.setText("rtrPri:" + rtrPri);
            t2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t3 = new TreeItem(tree, SWT.NONE);
            t3.setText("Options (0x" + DecoderHelper.toString(options) + ")");
            t3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t3_1 = new TreeItem(t3, SWT.NONE);
            t3_1.setText("DCbit :" + DCbit);
            t3_1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t3_2 = new TreeItem(t3, SWT.NONE);
            t3_2.setText("Rbit :" + Rbit);
            t3_2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t3_3 = new TreeItem(t3, SWT.NONE);
            t3_3.setText("Nbit :" + Nbit);
            t3_3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t3_4 = new TreeItem(t3, SWT.NONE);
            t3_4.setText("MCbit :" + MCbit);
            t3_4.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t3_5 = new TreeItem(t3, SWT.NONE);
            t3_5.setText("Ebit :" + Ebit);
            t3_5.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t3_6 = new TreeItem(t3, SWT.NONE);
            t3_6.setText("v6bit :" + v6bit);
            t3_6.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t7 = new TreeItem(tree, SWT.NONE);
            t7.setText("Hello Interval :" + helloInterval);
            t7.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t8 = new TreeItem(tree, SWT.NONE);
            t8.setText("Router Dead Interval :" + routerDeadInterval);
            t8.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t9 = new TreeItem(tree, SWT.NONE);
            t9.setText("Designated Router ID :" + designatedRouterID);
            t9.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t10 = new TreeItem(tree, SWT.NONE);
            t10.setText("Backup Designated RouterID :" + backupDesignatedRouterID);
            t10.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t11 = new TreeItem(tree, SWT.NONE);
            t11.setText("Neighbor ID :" + neighborID);
            t11.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        }
    }

    /**
		 * Complement for the DataBase Description Message
		   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		   |       0       |               Options                         |
		   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		   |        Interface MTU         |       0        |0|0|0|0|0|I|M|MS
		   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		   |                    DD sequence number                         |
		   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		   |                                                               |
		   +-                                                             -+
		   |                                                               |
		   +-                     An LSA Header                           -+
		   |                                                               |
		    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-++*/
    public class DataBaseDescMessage implements Serializable {

        /** Options
			 The optional capabilities supported by the router, as documented
			 in Section A.2.*/
        private int options;

        private int v6bit;

        private int Ebit;

        private int MCbit;

        private int Nbit;

        private int Rbit;

        private int DCbit;

        /**
			 *  Interface MTU
			 The size in bytes of the largest IPv6 datagram that can be sent
			 out the    associated interface, without fragmentation. 
			 */
        private int interfaceMTU;

        /**
			 I-bit
			 The Init bit.  When set to 1, this packet is the first in the
			 sequence of Database Description Packets.*/
        private int IBit;

        /** M-bit
			 The More bit.  When set to 1, it indicates that more Database
			 Description Packets are to follow.*/
        private int MBit;

        /**MS-bit
			 The Master/Slave bit.  When set to 1, it indicates that the router
			 is the master during the Database Exchange process.  Otherwise,
			 the router is the slave.*/
        private int MSBit;

        /** DD sequence number
			 Used to sequence the collection of Database Description Packets.
			 The initial value (indicated by the Init bit being set) should be
			 unique.  The DD sequence number then increments until the complete
			 database description has been sent.*/
        private int DDSequenceNumber;

        /**
			 * this constructor decode the packet
			 * @param combined
			 * @param offset
			 */
        public DataBaseDescMessage(byte[] combined, int offset) {
            offset += 1;
            options = DecoderHelper.extractInteger(combined, offset, 4);
            options = options & 0x00ffffff;
            DCbit = (options & 0x20) >>> 5;
            Rbit = (options & 0x10) >>> 4;
            Nbit = (options & 0x08) >>> 3;
            ;
            MCbit = (options & 0x04) >>> 2;
            Ebit = (options & 0x02) >>> 1;
            v6bit = (options & 0x01);
            offset += 4;
            interfaceMTU = DecoderHelper.extractInteger(combined, offset, 2);
            offset += 2;
            offset += 1;
            IBit = (combined[offset] & 0x05) >>> 2;
            MBit = (combined[offset] & 0x02) >>> 1;
            MSBit = (combined[offset] & 0x01);
            offset += 1;
            DDSequenceNumber = DecoderHelper.extractInteger(combined, offset, 4);
        }

        /**
			 * Add the tree element for this message
			 * @param tree
			 */
        public void toTree(TreeItem tree) {
            TreeItem t1 = new TreeItem(tree, SWT.NONE);
            t1.setText("Options (0x" + DecoderHelper.toString(options) + ")");
            t1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t1_1 = new TreeItem(t1, SWT.NONE);
            t1_1.setText("DCbit :" + DCbit);
            t1_1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t1_2 = new TreeItem(t1, SWT.NONE);
            t1_2.setText("Rbit :" + Rbit);
            t1_2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t1_3 = new TreeItem(t1, SWT.NONE);
            t1_3.setText("Nbit :" + Nbit);
            t1_3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t1_4 = new TreeItem(t1, SWT.NONE);
            t1_4.setText("MCbit :" + MCbit);
            t1_4.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t1_5 = new TreeItem(t1, SWT.NONE);
            t1_5.setText("Ebit :" + Ebit);
            t1_5.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t1_6 = new TreeItem(t1, SWT.NONE);
            t1_6.setText("v6bit :" + v6bit);
            t1_6.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t2 = new TreeItem(tree, SWT.NONE);
            t2.setText("interfaceMTU :" + interfaceMTU);
            t2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t3 = new TreeItem(tree, SWT.NONE);
            t3.setText("Flags (0x" + DecoderHelper.toString(options) + ")");
            t3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t3_1 = new TreeItem(t3, SWT.NONE);
            t3_1.setText("IBit :" + IBit);
            t3_1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t3_2 = new TreeItem(t3, SWT.NONE);
            t3_2.setText("MBit :" + MBit);
            t3_2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t3_3 = new TreeItem(t3, SWT.NONE);
            t3_3.setText("MSBit :" + MSBit);
            t3_3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t4 = new TreeItem(tree, SWT.NONE);
            t4.setText("DD Sequence Number :" + DDSequenceNumber);
            t4.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        }
    }

    /**
		   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		   |              0                  |        LS type              |
		   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		   |                         Link State ID                         |
		   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		   |                       Advertising Router                      |
		   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		**/
    public class LinkStateRequestMessage implements Serializable {

        /** type */
        private int LStype;

        /** link state id */
        private int linkStateID;

        /** Adervertising router */
        private int advertisingRouter;

        /**
			 * this constructor decode the packet
			 * @param combined
			 * @param offset
			 */
        public LinkStateRequestMessage(byte[] combined, int offset) {
            offset += 1;
            LStype = DecoderHelper.extractInteger(combined, offset, 1);
            offset += 1;
            linkStateID = DecoderHelper.extractInteger(combined, offset, 4);
            offset += 4;
            advertisingRouter = DecoderHelper.extractInteger(combined, offset, 4);
            offset += 4;
        }

        /**
			 * Add element on the tree
			 * @param tree
			 */
        public void toTree(TreeItem tree) {
            TreeItem t1 = new TreeItem(tree, SWT.NONE);
            t1.setText("LStype:" + LStype);
            t1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t2 = new TreeItem(tree, SWT.NONE);
            t2.setText("Link StateID :" + linkStateID);
            t2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t3 = new TreeItem(tree, SWT.NONE);
            t3.setText("Advertising Router :" + advertisingRouter);
            t3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        }
    }

    /**
		    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   			|                           # LSAs                              |
   			+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   			|                                                               |
   			+-                                                            +-+
   			|                            LSAs                               |
   			+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   			
		**/
    public class LinkStateUpdateMessage implements Serializable {

        /** type */
        private int LSAsNumber;

        /**
			 * this constructor decode the packet
			 * @param combined
			 * @param offset
			 */
        public LinkStateUpdateMessage(byte[] combined, int offset) {
            LSAsNumber = DecoderHelper.extractInteger(combined, offset, 4);
            offset += 4;
        }

        /**
			 * Add element on the tree
			 * @param tree
			 */
        public void toTree(TreeItem tree) {
            TreeItem t1 = new TreeItem(tree, SWT.NONE);
            t1.setText("LSAsNumber:" + LSAsNumber);
            t1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        }
    }

    /**
		   0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |           LS age             |           LS type              |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                       Link State ID                           |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Advertising Router                         |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    LS sequence number                         |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |        LS checksum           |             length             |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		**/
    public class LinkStateAcknowledgmentMessage implements Serializable {

        /** LS age
		      The time in seconds since the LSA was originated.*/
        private int LSage;

        /**LS type
		      The LS type field indicates the function performed by the LSA.
		      The high-order three bits of LS type encode generic properties of
		      the LSA, while the remainder (called LSA function code) indicate
		      the LSA's specific functionality.**/
        private int LStype;

        private int Ubit;

        private int s1;

        private int s2;

        private int LSAFunctionCode;

        /**
			Link State ID
		      Together with LS type and Advertising Router, uniquely identifies
		      the LSA in the link-state database.**/
        private int linkStateID;

        /**Advertising Router
		      The Router ID of the router that originated the LSA.  For example,
		      in network-LSAs this field is equal to the Router ID of the
		      network's Designated Router.**/
        private int advertisingRouter;

        /**LS sequence number
		      Detects old or duplicate LSAs.  Successive instances of an LSA are
		      given successive LS sequence numbers.  See Section 12.1.6 in
		      [Ref1] for more details.**/
        private int LSsequenceNumber;

        /**LS checksum
		      The Fletcher checksum of the complete contents of the LSA,
		      including the LSA header but excluding the LS age field. See
		      Section 12.1.7 in [Ref1] for more details.**/
        private String LSchecksum;

        /**length
		      The length in bytes of the LSA.  This includes the 20 byte LSA
		      header.**/
        private int length;

        public LinkStateAcknowledgmentMessage(byte[] combined, int offset) {
            LSage = DecoderHelper.extractInteger(combined, offset, 2);
            offset += 2;
            LStype = DecoderHelper.extractInteger(combined, offset, 2);
            Ubit = (LStype & 0x80) >>> 15;
            s1 = (LStype & 0x40) >>> 14;
            s2 = (LStype & 0x20) >>> 13;
            LSAFunctionCode = (LStype & 0x1fff);
            offset += 2;
            linkStateID = DecoderHelper.extractInteger(combined, offset, 4);
            offset += 4;
            advertisingRouter = DecoderHelper.extractInteger(combined, offset, 4);
            offset += 4;
            LSsequenceNumber = DecoderHelper.extractInteger(combined, offset, 4);
            offset += 4;
            LSchecksum = DecoderHelper.readHexValueOnXBytes(combined, offset, 2);
            offset += 2;
            length = DecoderHelper.extractInteger(combined, offset, 2);
            offset += 2;
        }

        public void toTree(TreeItem tree) {
            TreeItem t1 = new TreeItem(tree, SWT.NONE);
            t1.setText("LSage:" + LSage);
            t1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t2 = new TreeItem(tree, SWT.NONE);
            t2.setText("LStype (0x" + Integer.toHexString(LStype) + ")" + getDescription(LSAFunctionCode, LStype));
            t2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t2_1 = new TreeItem(t2, SWT.NONE);
            t2_1.setText("Ubit :" + Ubit + "(" + getUbitName(Ubit) + ")");
            t2_1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t2_2 = new TreeItem(t2, SWT.NONE);
            t2_2.setText("S1,S2 : (" + s1 + "," + s2 + ")->" + getSname(s1, s2));
            t2_2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t2_3 = new TreeItem(t2, SWT.NONE);
            t2_3.setText("LSA Function Code :" + LSAFunctionCode);
            t2_3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT5));
            TreeItem t3 = new TreeItem(tree, SWT.NONE);
            t3.setText("LinkStateID : " + linkStateID);
            t3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t4 = new TreeItem(tree, SWT.NONE);
            t4.setText("Advertising Router : " + advertisingRouter);
            t4.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t5 = new TreeItem(tree, SWT.NONE);
            t5.setText("LSsequence Number : " + LSsequenceNumber);
            t5.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t6 = new TreeItem(tree, SWT.NONE);
            t6.setText("LS checksum : 0x" + LSchecksum);
            t6.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
            TreeItem t7 = new TreeItem(tree, SWT.NONE);
            t7.setText("Length : " + length);
            t7.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT4));
        }

        public String getUbitName(int code) {
            if (code == 0) return "Treat the LSA as if it had link-local flooding scope"; else if (code == 1) return "Store and flood the LSA, as if type understood"; else return "Unknown";
        }

        public String getSname(int s1, int s2) {
            if (s1 == 0 && s2 == 0) return "Link-Local Scoping. Flooded only on link it is originated on."; else if (s1 == 0 && s2 == 1) return "Area Scoping. Flooded to all routers in the originating area"; else if (s1 == 1 && s2 == 0) return "AS Scoping. Flooded to all routers in the AS"; else if (s1 == 1 && s2 == 1) return "Reserved"; else return "Unknown";
        }

        public String getDescription(int LSAFunctioncode, int LSType) {
            if (LSAFunctioncode == 1 && LStype == 0x2001) return "Router-LSA"; else if (LSAFunctioncode == 2 && LStype == 0x2002) return " Network-LSA"; else if (LSAFunctioncode == 3 && LStype == 0x2003) return "Inter-Area-Prefix-LSA"; else if (LSAFunctioncode == 4 && LStype == 0x2004) return "Inter-Area-Router-LSA"; else if (LSAFunctioncode == 5 && LStype == 0x4005) return "AS-External-LSA"; else if (LSAFunctioncode == 6 && LStype == 0x2006) return "Group-membership-LSA"; else if (LSAFunctioncode == 7 && LStype == 0x2007) return "Type-7-LSA"; else if (LSAFunctioncode == 8 && LStype == 0x0008) return "Link-LSA"; else if (LSAFunctioncode == 9 && LStype == 0x2009) return "Intra-Area-Prefix-LSA"; else return "Flags on LSAType are not adapted for this Function Code";
        }
    }
}
