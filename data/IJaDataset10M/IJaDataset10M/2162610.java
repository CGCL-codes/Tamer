package com.inetmon.jn.decoder.layer5.ipv6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.StringTokenizer;
import jpcap.packet.Packet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import com.inetmon.jn.decoder.layer2.EthernetPacket;
import com.inetmon.jn.decoder.layer4.IPv6TCPPacket;
import com.inetmon.jn.decoder.tools.Activator;
import com.inetmon.jn.decoder.tools.ConstantColor;
import com.inetmon.jn.decoder.tools.DecoderHelper;

/**
 * TODO : create field and decode field
 * 
 * 
 * @author Kamel
 * 
 */
public class IPv6tcpMSNMessengerVoicePacket extends IPv6TCPPacket {

    /**
	 * @uml.property  name="fieldTable"
	 * @uml.associationEnd  qualifier="nameField:java.lang.String java.lang.String"
	 */
    Hashtable fieldTable;

    /** color representing the packet */
    private static int COLOR = ConstantColor.getColorNumber(39);

    public static final int TCP_PORT_MSNvoice = 6901;

    public IPv6tcpMSNMessengerVoicePacket(Packet packet) {
        super(packet);
        decodeIPv6tcpMSNMessengerVoicePacket();
        setInfo("Microsoft Messenger:Voice tcp");
        setProtocol1("MSN Messenger voice v6");
    }

    public void decodeIPv6tcpMSNMessengerVoicePacket() {
        fieldTable = new Hashtable();
        int offset = EthernetPacket.getEthernetHeaderLength() + getIpv6HeaderLength() + getTcpHeaderLength();
        byte[] data = new byte[combined.length - offset];
        System.arraycopy(combined, offset, data, 0, data.length);
        try {
            BufferedReader in = new BufferedReader(new StringReader(new String(data)));
            String l;
            String nameField = "";
            String valueField = "";
            do {
                l = in.readLine();
                if (l != null) {
                    StringTokenizer st = new StringTokenizer(l, ":");
                    int j = 0;
                    while (st.hasMoreTokens()) {
                        if (j == 0) nameField = (String) st.nextElement();
                        if (j >= 1) valueField = (String) st.nextElement();
                        j++;
                    }
                    fieldTable.put(nameField, valueField);
                } else break;
            } while (l.length() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** TO PRINT ON THE TABLE VIEW OF ANALYSER */
    public String[] getLine() {
        return new String[] { getTime().substring(11), getSourceMAC(), getDestinationMAC(), getSrcAdd(), getDestAdd(), getProtocol1(), "", getInfo() };
    }

    public void toTree(TreeItem tree) {
        super.toTree(tree);
        TreeItem root = new TreeItem(tree, SWT.NONE);
        root.setText("MSN Messenger");
        root.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT2));
    }

    public static boolean isAnalyzable(Packet p) {
        int offset = getEthernetHeaderLength();
        int ipv4HeaderLength = (p.combined[offset] & 0x0f) * 4;
        offset += ipv4HeaderLength;
        int srcPort = DecoderHelper.extractInteger(p.combined, offset, 2);
        offset += 2;
        int destPort = DecoderHelper.extractInteger(p.combined, offset, 2);
        offset += 2;
        if (srcPort == TCP_PORT_MSNvoice || destPort == TCP_PORT_MSNvoice) return true;
        return false;
    }

    public int getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(int color) {
        COLOR = color;
    }
}
