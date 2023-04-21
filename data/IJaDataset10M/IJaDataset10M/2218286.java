package org.ws4d.java.communication.connection.udp;

import java.io.IOException;
import java.io.InputStream;
import org.ws4d.java.communication.connection.ip.IPAddress;
import org.ws4d.java.constants.FrameworkConstants;
import org.ws4d.java.structures.Queue;

public class UdpOverTcpListener implements Runnable {

    Thread th = new Thread(this);

    InputStream sos;

    Queue datagramQ;

    private int ip_policy;

    UdpOverTcpListener(InputStream socketsStream, int ip_policy) {
        sos = socketsStream;
        datagramQ = new Queue();
        this.ip_policy = ip_policy;
        th.start();
    }

    public static String intToIpv4(int ip) {
        String adr = "";
        adr += String.valueOf(ip >> 24) + String.valueOf((ip << 8) >> 24) + String.valueOf((ip << 16) >> 24) + String.valueOf((ip << 24) >> 24);
        return adr;
    }

    private Datagram readDatagramFromStream() throws NativeMulticastRouterException {
        byte[] dataBuffer = new byte[3000];
        byte[] lenBuffer = new byte[2];
        byte[] portBuffer = new byte[2];
        byte[] hostBuffer = new byte[16];
        try {
            sos.read(dataBuffer);
            sos.read(lenBuffer);
            int length = 0;
            length |= lenBuffer[0] & 0xFF;
            length <<= 8;
            length |= lenBuffer[1] & 0xFF;
            sos.read(hostBuffer);
            String fromip = "";
            for (int i = 0; i < hostBuffer.length - 1; i++) {
                if (hostBuffer[i] == '\0') {
                    break;
                }
                fromip += (char) hostBuffer[i];
            }
            sos.read(portBuffer);
            int port = 0;
            port |= portBuffer[0] & 0xFF;
            port <<= 8;
            port |= portBuffer[1] & 0xFF;
            byte[] fittingData = new byte[length];
            System.arraycopy(dataBuffer, 0, fittingData, 0, length);
            Datagram datagram = new Datagram(null, fittingData, length);
            datagram.setAddress(new IPAddress(fromip));
            datagram.setPort(port);
            return datagram;
        } catch (IOException e) {
            throw new NativeMulticastRouterException("A problem occured while communicating " + "with the native multicast router! Make sure its up nd running");
        }
    }

    public Datagram receiveDatagram() {
        try {
            while (datagramQ != null && datagramQ.isEmpty()) {
                synchronized (datagramQ) {
                    datagramQ.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (Datagram) datagramQ.get();
    }

    public void run() {
        while (!false) {
            try {
                if (sos.available() >= FrameworkConstants.DGRAM_MAX_SIZE + 2 + ip_policy + 2) {
                    datagramQ.enqueue(this.readDatagramFromStream());
                }
                Thread.sleep(20);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NativeMulticastRouterException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
