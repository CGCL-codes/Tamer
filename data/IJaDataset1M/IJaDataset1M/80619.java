package com.inetmon.jn.decoder.layer6.ipv6.vlan;

import jpcap.packet.Packet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import com.inetmon.jn.decoder.layer5.vlan.VLANTCPv6Packet;
import com.inetmon.jn.decoder.tools.Activator;
import com.inetmon.jn.decoder.tools.ConstantColor;
import com.inetmon.jn.decoder.tools.DecoderHelper;

public class VLANIPv6POP3Packet extends VLANTCPv6Packet {

    /** color representing the packet */
    private static int COLOR = ConstantColor.getColorNumber(6);

    public static int TCP_PORT_POP3 = 110;

    public VLANIPv6POP3Packet(Packet packet) {
        super(packet);
        decodeVLANIPv6POP3Packet();
        setInfo("Post Office Protocol version 3 (POP3), an application-layer " + "Internet standard protocol, to retrieve e-mail from a remote server " + "over a TCP/IP connection. ");
        setProtocol1("VLAN POP3 v6");
    }

    public void decodeVLANIPv6POP3Packet() {
    }

    /** TO PRINT ON THE TABLE VIEW OF ANALYSER */
    public String[] getLine() {
        return new String[] { getTime().substring(11), getSourceMAC(), getDestinationMAC(), getSrcAdd(), getDestAdd(), getProtocol1(), getVLANFieldID(), getInfo() };
    }

    public void toTree(TreeItem tree) {
        super.toTree(tree);
        TreeItem main = new TreeItem(tree, SWT.NONE);
        main.setText("Post Office Protocol version 3");
        main.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT2));
    }

    public static boolean isAnalyzable(Packet p) {
        int offset = getEthernetHeaderLength() + getVLANHeaderLength() + getIpv6HeaderLength();
        int srcPort = DecoderHelper.extractInteger(p.combined, offset, 2);
        offset += 2;
        int destPort = DecoderHelper.extractInteger(p.combined, offset, 2);
        if (srcPort == TCP_PORT_POP3 || destPort == TCP_PORT_POP3) return true;
        return false;
    }

    public int getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(int color) {
        COLOR = color;
    }
}
