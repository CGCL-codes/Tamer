package mobile;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author HuoLin
 */
public class SocketTest {

    public static void main(String[] args) throws Exception {
        SocketTest socket = new SocketTest();
        PrintWriter out = null;
        BufferedReader in = null;
        String fromServer;
        Socket server = new Socket("61.144.205.110", 9070);
        System.out.println("Server is connected: " + server.isConnected());
        out = new PrintWriter(server.getOutputStream(), true);
        in = new BufferedReader((new InputStreamReader(server.getInputStream())));
        out.println("KST0961");
        while ((fromServer = in.readLine()) != null) {
            System.out.println(fromServer);
        }
        if (server != null) {
            server.close();
        }
    }

    public byte[] printLocalHost() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("host name: " + address.getHostName());
            return address.getAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SocketTest.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
