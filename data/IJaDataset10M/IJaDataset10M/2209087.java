package com.inetmon.jn.decoder.layer6.ipv6.vlan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import jpcap.packet.Packet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;
import com.inetmon.jn.decoder.ReferenceOnTable;
import com.inetmon.jn.decoder.layer5.vlan.VLANTCPv6Packet;
import com.inetmon.jn.decoder.layer6.ipv4.vlan.VLANIPv4HTTPPacket;
import com.inetmon.jn.decoder.tools.Activator;
import com.inetmon.jn.decoder.tools.ConstantColor;
import com.inetmon.jn.decoder.tools.DecoderHelper;

/**
 * 
 * Class used to store http packet and analyse
 * a packet(pcap).
 * It store field of http packet on a hashtable.
 * we can use constante value to get the value of the field
 *  @author kamel
 *
 */
public class VLANIPv6HTTPPacket extends VLANTCPv6Packet {

    /**
	 * @uml.property  name="hostName"
	 */
    private String hostName = "";

    /** color representing the packet */
    private static int COLOR = ConstantColor.getColorNumber(1);

    /**
	 * contains the type of the http request (post,get...)
	 * @uml.property  name="method"
	 */
    String method;

    /**
	 * @uml.property  name="fieldTable"
	 * @uml.associationEnd  qualifier="nameField:java.lang.String java.lang.String"
	 */
    Hashtable fieldTable;

    private static final String[] valueNames = { "Method", "Header" };

    public static String HOST = "Host";

    public static String USER_AGENT = "User-Agent";

    public static String ACCEPT = "Accept";

    public static String ACCEPT_LANGUAGE = "Accept-Language";

    public static String ACCEPT_ENCODING = "Accept-Encoding";

    public static String ACCEPT_CHARSET = "Accept-Charset";

    public static String KEEP_ALIVE = "Keep-Alive";

    public static String CONNECTION = "Connection";

    public static String COOKIE = "Cookie";

    public static String CACHE_CONTROL = "Cache-Control";

    public static String CONTENT_TYPE = "Content-Type";

    public static String CONTENT_ENCODING = "Content-Encoding";

    public static String SERVER = "Server";

    public static String CONTENT_LENGTH = "Content-Length";

    public static String DATE = "Date";

    public static int TCP_PORT_HTTP = 80;

    public VLANIPv6HTTPPacket(Packet packet) {
        super(packet);
        decodeVLANIPv6HTTPPacket(packet);
        if (getDestinationPort() == VLANIPv4HTTPPacket.proxyPort || getSourcePort() == VLANIPv4HTTPPacket.proxyPort) setProtocol1("VLAN HTTP IPv6 over Proxy "); else setProtocol1("VLAN HTTP v6");
        if (isHTTPRequest()) {
            setInfo("REQUEST Method : " + getTypeRequest() + ",Host:" + fieldTable.get(HOST));
        } else {
            setInfo(getInfoHTTP());
        }
    }

    /**
	 * analyse a row packet (if the packet is an http packet)
	 * we get all field we need and put them on a hashtable
	 * 
	 * @param p
	 */
    public void decodeVLANIPv6HTTPPacket(Packet p) {
        method = "";
        fieldTable = new Hashtable();
        int sizeheader = getEthernetHeaderLength() + getVLANHeaderLength() + getIpv6HeaderLength() + getTcpHeaderLength();
        byte[] data = new byte[p.combined.length - sizeheader];
        System.arraycopy(p.combined, sizeheader, data, 0, data.length);
        try {
            BufferedReader in = new BufferedReader(new StringReader(new String(data)));
            method = in.readLine();
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
                    if (nameField == this.HOST) this.hostName = valueField;
                    fieldTable.put(nameField, valueField);
                } else break;
            } while (l.length() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** TO PRINT ON THE TABLE VIEW OF ANALYSER */
    public String[] getLine() {
        return new String[] { getTime().substring(11), getSourceMAC(), getDestinationMAC(), getSrcAdd(), getDestAdd(), getProtocol1(), getVLANFieldID(), getInfo() };
    }

    public void toTree(TreeItem tree) {
        super.toTree(tree);
        int offset = getEthernetHeaderLength() + getVLANHeaderLength() + getIpv6HeaderLength() + getTcpHeaderLength();
        TreeItem main = new TreeItem(tree, SWT.NONE);
        main.setText("HTTP Protocol");
        main.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT2));
        main.setData(new ReferenceOnTable(offset, combined.length - offset));
        TreeItem t1 = new TreeItem(main, SWT.NONE);
        t1.setText(HOST + ": " + getField(HOST));
        t1.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t2 = new TreeItem(main, SWT.NONE);
        t2.setText(USER_AGENT + ": " + this.getField(USER_AGENT));
        t2.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t3 = new TreeItem(main, SWT.NONE);
        t3.setText(ACCEPT + ": " + this.getField(ACCEPT));
        t3.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t4 = new TreeItem(main, SWT.NONE);
        t4.setText(ACCEPT_LANGUAGE + ": " + this.getField(ACCEPT_LANGUAGE));
        t4.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t5 = new TreeItem(main, SWT.NONE);
        t5.setText(ACCEPT_ENCODING + ": " + this.getField(ACCEPT_ENCODING));
        t5.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t6 = new TreeItem(main, SWT.NONE);
        t6.setText(ACCEPT_CHARSET + ": " + this.getField(ACCEPT_CHARSET));
        t6.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t7 = new TreeItem(main, SWT.NONE);
        t7.setText(KEEP_ALIVE + ": " + this.getField(KEEP_ALIVE));
        t7.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t8 = new TreeItem(main, SWT.NONE);
        t8.setText(CONNECTION + ": " + this.getField(CONNECTION));
        t8.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t9 = new TreeItem(main, SWT.NONE);
        t9.setText(COOKIE + ": " + this.getField(COOKIE));
        t9.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t10 = new TreeItem(main, SWT.NONE);
        t10.setText(CACHE_CONTROL + ": " + this.getField(CACHE_CONTROL));
        t10.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t11 = new TreeItem(main, SWT.NONE);
        t11.setText(CONTENT_TYPE + ": " + this.getField(CONTENT_TYPE));
        t11.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t12 = new TreeItem(main, SWT.NONE);
        t12.setText(CONTENT_ENCODING + ": " + this.getField(CONTENT_ENCODING));
        t12.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t13 = new TreeItem(main, SWT.NONE);
        t13.setText(SERVER + ": " + this.getField(SERVER));
        t13.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t14 = new TreeItem(main, SWT.NONE);
        t14.setText(CONTENT_LENGTH + ": " + this.getField(CONTENT_LENGTH));
        t14.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
        TreeItem t15 = new TreeItem(main, SWT.NONE);
        t15.setText(DATE + ": " + this.getField(DATE));
        t15.setImage(Activator.getDefault().getImage(Activator.IMG_ROOT3));
    }

    public static boolean isAnalyzable(Packet p) {
        int offset = getEthernetHeaderLength() + getVLANHeaderLength();
        int ipv4HeaderLength = (p.combined[offset] & 0x0f) * 4;
        offset += ipv4HeaderLength;
        int sizeheader = offset;
        int srcPort = DecoderHelper.extractInteger(p.combined, offset, 2);
        offset += 2;
        int destPort = DecoderHelper.extractInteger(p.combined, offset, 2);
        if (srcPort == TCP_PORT_HTTP || destPort == TCP_PORT_HTTP || srcPort == VLANIPv4HTTPPacket.proxyPort || destPort == VLANIPv4HTTPPacket.proxyPort) {
            byte[] data = new byte[p.combined.length - sizeheader];
            System.arraycopy(p.combined, sizeheader, data, 0, data.length);
            BufferedReader in = new BufferedReader(new StringReader(new String(data)));
            String method;
            try {
                method = in.readLine();
                if (method.indexOf("HTTP") != -1) return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String getProtocolName() {
        return "HTTP (Hyper Text Transfer Protocol)";
    }

    public String[] getValueNames() {
        return valueNames;
    }

    /**
	 * Function used to get the field specified by the parameter.
	 * to pass the parameter we can use constant values specified 
	 * by this static member var on this class.
	 * @param nameField
	 * @return
	 */
    public String getField(String nameField) {
        String res = "";
        if (fieldTable.size() > 0) res = (String) fieldTable.get(nameField);
        return res;
    }

    /**
	 * Test if the HTTP packet is a request
	 * @return
	 */
    public boolean isHTTPRequest() {
        boolean res = false;
        if (method.contains("GET") || method.contains("POST")) res = true;
        return res;
    }

    /**
	 *Get Type of Method Request
	 * @return
	 */
    public String getTypeRequest() {
        boolean res = false;
        if (method.contains("GET")) return "GET"; else if (method.contains("POST")) return "POST"; else return "";
    }

    public void printPacketHTTP() {
        Iterator i = fieldTable.keySet().iterator();
        while (i.hasNext()) {
            String s = (String) i.next();
            System.out.println(s + " : " + fieldTable.get(s));
        }
        System.out.println("--------------------");
    }

    public String getInfoHTTP() {
        String r = USER_AGENT + " " + fieldTable.get(USER_AGENT) + " , " + ACCEPT_LANGUAGE + " " + fieldTable.get(USER_AGENT) + " , " + ACCEPT_ENCODING + " " + fieldTable.get(USER_AGENT) + " , " + CONTENT_LENGTH + " " + fieldTable.get(USER_AGENT) + " , " + DATE + " " + fieldTable.get(USER_AGENT);
        return r;
    }

    /**
	 * @return
	 * @uml.property  name="hostName"
	 */
    public String getHostName() {
        return hostName;
    }

    /**
	 * @param hostName
	 * @uml.property  name="hostName"
	 */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
	 * @return
	 * @uml.property  name="method"
	 */
    public String getMethod() {
        return method;
    }

    /**
	 * @param method
	 * @uml.property  name="method"
	 */
    public void setMethod(String method) {
        this.method = method;
    }

    public int getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(int color) {
        COLOR = color;
    }
}
