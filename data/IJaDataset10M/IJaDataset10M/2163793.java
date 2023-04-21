package authenticator;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import utility.MacAddress;

/**
 *
 * @author Igor
 */
public class Authenticator {

    private static final int DEFAULT_PORT = 8080;

    private static final Map<MacAddress, byte[]> macToPmk = new HashMap<MacAddress, byte[]>();

    static {
        try {
            macToPmk.put(new MacAddress("70-f1-a1-3f-a0-f7"), "1234567812345678".getBytes());
            macToPmk.put(new MacAddress("00-23-4d-d3-63-b0"), "1234567812345678".getBytes());
            macToPmk.put(new MacAddress("00-1f-16-43-e0-db"), "1234567812345678".getBytes());
        } catch (Exception ex) {
            Logger.getLogger(Authenticator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                Logger.getLogger(Authenticator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Authenticator is running...");
        ServerSocket servSocket;
        try {
            servSocket = new ServerSocket(port);
            while (true) {
                Socket supplicantSocket = servSocket.accept();
                System.out.println("New supplicant connection started...");
                MacAddress aMac = new MacAddress(NetworkInterface.getByInetAddress(supplicantSocket.getLocalAddress()).getHardwareAddress());
                MacAddress sMac = new MacAddress(NetworkInterface.getByInetAddress(supplicantSocket.getInetAddress()).getHardwareAddress());
                byte[] pmk = macToPmk.get(sMac);
                (new SupplicantHandler(supplicantSocket, aMac, sMac, pmk)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(Authenticator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
