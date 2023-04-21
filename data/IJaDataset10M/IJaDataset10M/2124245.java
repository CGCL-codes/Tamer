package org.snmp4ant.snmp;

import java.io.*;
import java.net.*;

/**
*    The class SNMPInformRequestSenderInterface implements an interface for sending SNMPv2 inform request 
* 	 messages to a remote SNMP manager. The approach is that from version 2c of SNMP, using no encryption of data. 
*    Communication occurs via UDP, using port 162, the standard SNMP trap and inform request port, as the destination port.
*/
public class SNMPInformRequestSenderInterface {

    public static final int SNMP_TRAP_PORT = 162;

    private DatagramSocket dSocket;

    /**
    *    Construct a new inform request sender object to send inform requests to remote SNMP hosts.
    */
    public SNMPInformRequestSenderInterface() throws SocketException {
        dSocket = new DatagramSocket();
    }

    /**
    *    Construct a new inform request sender object to send inform requests to remote SNMP hosts, binding to
    *   the specified local port.
    */
    public SNMPInformRequestSenderInterface(int localPort) throws SocketException {
        dSocket = new DatagramSocket(localPort);
    }

    /**
    *    Send the supplied SNMPv2 inform request pdu to the specified host, using the supplied version number
    *    and community name. 
    */
    public void sendInformRequest(int version, InetAddress hostAddress, String community, SNMPv2InformRequestPDU pdu) throws IOException {
        SNMPMessage message = new SNMPMessage(version, community, pdu);
        byte[] messageEncoding = message.getBEREncoding();
        DatagramPacket outPacket = new DatagramPacket(messageEncoding, messageEncoding.length, hostAddress, SNMP_TRAP_PORT);
        dSocket.send(outPacket);
    }

    /**
    *    Send the supplied inform request pdu to the specified host, using the supplied community name and
    *    using 1 for the version field in the SNMP message.
    */
    public void sendInformRequest(InetAddress hostAddress, String community, SNMPv2InformRequestPDU pdu) throws IOException {
        int version = 1;
        sendInformRequest(version, hostAddress, community, pdu);
    }

    @SuppressWarnings("unused")
    private String hexByte(byte b) {
        int pos = b;
        if (pos < 0) pos += 256;
        String returnString = new String();
        returnString += Integer.toHexString(pos / 16);
        returnString += Integer.toHexString(pos % 16);
        return returnString;
    }

    @SuppressWarnings("unused")
    private String getHex(byte theByte) {
        int b = theByte;
        if (b < 0) b += 256;
        String returnString = new String(Integer.toHexString(b));
        if (returnString.length() % 2 == 1) returnString = "0" + returnString;
        return returnString;
    }
}
