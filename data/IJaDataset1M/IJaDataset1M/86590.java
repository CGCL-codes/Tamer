package br.com.BRisa.upnp;

import java.net.*;
import br.com.BRisa.util.Log;

public class Sockets {

    private static DatagramSocket PRISOCK = null;

    private String LOCALHOST = "";

    public Sockets() {
        open();
    }

    public DatagramSocket getPRISOCK() {
        return PRISOCK;
    }

    public String getLocalhost() {
        if (0 < LOCALHOST.length()) {
            return LOCALHOST;
        }
        return PRISOCK.getLocalAddress().getHostAddress();
    }

    public static boolean open() {
        close();
        try {
            PRISOCK = new DatagramSocket();
        } catch (Exception e) {
            Log.Warning("BRisa Warning: ", e);
            return false;
        }
        return true;
    }

    public static boolean close() {
        if (PRISOCK == null) return true;
        try {
            PRISOCK.close();
            PRISOCK = null;
        } catch (Exception e) {
            Log.Warning("BRisa Warning: ", e);
            return false;
        }
        return true;
    }

    public static boolean Send(String Text, String Address, int porta) {
        try {
            InetAddress EndInet = InetAddress.getByName(Address);
            DatagramPacket DTGPacket = new DatagramPacket(Text.getBytes(), Text.length(), EndInet, porta);
            PRISOCK.send(DTGPacket);
        } catch (Exception e) {
            Log.Warning("Endereço" + PRISOCK.getLocalAddress().getHostName());
            Log.Warning("Brisa Adverte", e);
            return false;
        }
        return true;
    }

    public void Recive() {
        byte ReciveBuffer[] = new byte[SSDP.RECV_MESSAGE_BUFSIZE];
    }
}
