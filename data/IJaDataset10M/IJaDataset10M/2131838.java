package gnu.classpath.examples.CORBA.swing.x5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The main executable class of the game manager server.
 * 
 * The manager address server returns the IOR address string of the game
 * manager. Hence the user does not need to enter the rather long IOR address
 * string and only needs to specify the host and port of the machine where the
 * game manager is running.
 * 
 * The manager address server starts the main game manager as well.
 * 
 * This server acts as a HTTP server that always returns the same response. This
 * primitive functionality is sufficient for its task.
 * 
 * The more complex CORBA applications should use the name service instead. We
 * do not use the name service as this would require to start additional
 * external application, specific for the different java platforms.
 * 
 * @author Audrius Meskauskas (AudriusA@Bioinformatics.org) 
 */
public class X5Server {

    /**
   * Start the game manager.
   */
    public static void main(String[] args) {
        OrbStarter.startManager(args);
        if (!GameManagerImpl.ok) {
            System.out.println("Unable to start the game manager:");
            System.exit(1);
        }
        System.out.println(GameManagerImpl.ior);
        String manager_address = null;
        ServerSocket nameServer = null;
        try {
            nameServer = new ServerSocket(OrbStarter.MANAGER_NAMER_PORT);
            System.out.println("The game manager is listening at:");
            manager_address = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + nameServer.getLocalPort();
            System.out.println(manager_address);
            System.out.println("Enter this address to the " + "input field of the game client.");
            System.out.println("Use ^C to stop the manager.");
        } catch (Exception ex) {
            System.out.println("The port " + OrbStarter.MANAGER_NAMER_PORT + " is not available. The game manager namer will not start.");
            System.exit(1);
        }
        if (OrbStarter.WRITE_URL_TO_FILE != null) {
            try {
                File gmf = new File(OrbStarter.WRITE_URL_TO_FILE);
                FileWriter f = new FileWriter(gmf);
                BufferedWriter b = new BufferedWriter(f);
                b.write(manager_address);
                b.close();
            } catch (IOException e) {
                System.out.println("Local filesystem not accessible." + "Read IOR from console.");
            }
        }
        while (true) {
            try {
                Socket socket = nameServer.accept();
                System.out.println("Connected.");
                socket.setSoTimeout(1000 * 120);
                OutputStream out = socket.getOutputStream();
                int length = GameManagerImpl.ior.length();
                StringBuffer b = new StringBuffer();
                b.append("HTTP/1.0 200 OK\r\n");
                b.append("Content-Length: " + length + "\r\n");
                b.append("Connection: close\r\n");
                b.append("Content-Type: text/plain; charset=UTF-8\r\n");
                b.append("\r\n");
                b.append(GameManagerImpl.ior);
                out.write(b.toString().getBytes("UTF-8"));
                socket.shutdownOutput();
                if (!socket.isClosed()) socket.close();
                System.out.println("Completed.");
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("Network problem.");
            }
        }
    }
}
