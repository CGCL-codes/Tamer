package orbisoftware.aquarius.pdu_generator;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class PacketGeneratorData {

    private static PacketGeneratorData instance = null;

    private byte[] datagramData = null;

    private String ipAddress = null;

    private Boolean generatorActive = false;

    private int port = 3000;

    protected PacketGeneratorData() {
        findBroadcastAddress();
    }

    public static PacketGeneratorData getInstance() {
        if (instance == null) {
            instance = new PacketGeneratorData();
        }
        return instance;
    }

    public void setGeneratorActive(Boolean newValue) {
        generatorActive = newValue;
    }

    public Boolean getGeneratorActive() {
        return generatorActive;
    }

    public Boolean datagramDataValid() {
        Boolean isValid = false;
        if (datagramData != null) {
            if (datagramData.length > 0) isValid = true;
        }
        return isValid;
    }

    public void setDatagramData(byte[] newDatagramData) {
        datagramData = new byte[(newDatagramData.length)];
        datagramData = newDatagramData;
    }

    public byte[] getDatagramData() {
        return datagramData;
    }

    public void setIPAddress(String newIPAddress) {
        ipAddress = newIPAddress;
    }

    public String getIPAddress() {
        return ipAddress;
    }

    public void setPort(int newPort) {
        port = newPort;
    }

    public int getPort() {
        return port;
    }

    private void findBroadcastAddress() {
        Enumeration<NetworkInterface> interfaces;
        InetAddress broadcast = null;
        boolean foundAddress = false;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements() && foundAddress == false) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback()) continue;
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) continue; else foundAddress = true;
                }
            }
        } catch (SocketException e) {
        }
        if (broadcast != null) ipAddress = broadcast.getHostAddress(); else ipAddress = "127.0.0.1";
    }
}
