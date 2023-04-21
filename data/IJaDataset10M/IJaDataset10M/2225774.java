package net.sf.magicmap.client.plugin.udpscanner;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;
import java.util.StringTokenizer;
import net.sf.magicmap.client.measurement.interfaces.AbstractScanner;

/**
 * Wrapper f�r externe Scansoftware, die ihre Daten via UDP versenden kann. Dies
 * sind z.B. Netstumbler und die Wirless-Tools unter Linux in Zusammenhang mit
 * einem Perlskript, welches die Daten via UDP verschickt.
 * 
 * @author thuebner
 */
public class UDPScanner extends AbstractScanner {

    public static final String MESSAGE_RESULT = "1";

    public static final String MESSAGE_COMPLETE = "2";

    private DatagramSocket socket;

    private boolean isRunning;

    private int port;

    /**
     * Erstellt einen UDP-Socket der Daten von einem Scanskript entgegennimmt
     * und diese zur Applikation weiterreicht
     * 
     * @param port
     */
    public UDPScanner(int port) {
        this.port = port;
    }

    public void start() {
        try {
            this.socket = new DatagramSocket(port);
            this.isRunning = true;
            super.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (this.isRunning && !thread.isInterrupted()) {
            try {
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                this.socket.receive(packet);
                if (isRunning) {
                    tokenize(packet);
                }
            } catch (SocketException se) {
                System.out.println("Socket geschlossen?");
            } catch (IOException e) {
                e.printStackTrace();
                this.isRunning = false;
            }
        }
    }

    @Override
    public void stop() {
        this.isRunning = false;
        try {
            this.socket.close();
        } catch (Exception e) {
            System.out.println("Socket unsauber geschlossen.");
        }
        this.socket = null;
    }

    public static String trim(String text) {
        String result = text;
        if (result != null) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * @param packet
     */
    private void tokenize(DatagramPacket packet) {
        try {
            String recievedStr = new String(packet.getData());
            StringTokenizer st = new StringTokenizer(recievedStr, "|");
            String ssid;
            String bssid;
            String signal;
            String noise;
            String lastSeen;
            String hostName;
            String messageType;
            String clientAddress;
            String foundNew;
            String seenBefore;
            String lostContact;
            String bestSNR;
            String clientMac;
            messageType = trim(st.nextToken());
            if (MESSAGE_RESULT.equals(messageType)) {
                ssid = trim(st.nextToken());
                bssid = trim(st.nextToken());
                signal = trim(st.nextToken());
                noise = trim(st.nextToken());
                lastSeen = trim(st.nextToken());
                hostName = trim(st.nextToken());
                clientMac = trim(st.nextToken());
                clientAddress = packet.getAddress().toString();
                Date lastSeenDate = new Date(System.currentTimeMillis());
                try {
                    lastSeenDate = WifiScanResult.df.parse(lastSeen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                double signalLevel = 0.0;
                double noised = 0.0;
                try {
                    signalLevel = Double.parseDouble(signal);
                    noised = Double.parseDouble(noise);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                WifiScanResult message = new WifiScanResult(bssid.toUpperCase(), signalLevel, lastSeenDate, noised, ssid);
                this.notifyScan(message);
            } else if (MESSAGE_COMPLETE.equals(messageType)) {
                foundNew = trim(st.nextToken());
                seenBefore = trim(st.nextToken());
                lostContact = trim(st.nextToken());
                bestSNR = trim(st.nextToken());
                hostName = trim(st.nextToken());
                clientMac = trim(st.nextToken());
                clientAddress = packet.getAddress().toString();
                this.notifyRoundComplete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
