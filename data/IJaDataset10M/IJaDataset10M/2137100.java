package org.newsclub.net.unix.demo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketException;

/**
 * A simple demo client.
 * 
 * @author Christian Kohlschütter
 * @see SimpleTestServer
 */
public class SimpleTestClient {

    public static void main(String[] args) throws IOException {
        final File socketFile = new File(new File(System.getProperty("java.io.tmpdir")), "junixsocket-test.sock");
        AFUNIXSocket sock = AFUNIXSocket.newInstance();
        try {
            sock.connect(new AFUNIXSocketAddress(socketFile));
        } catch (AFUNIXSocketException e) {
            System.out.println("Cannot connect to server. Have you started it?");
            System.out.flush();
            throw e;
        }
        System.out.println("Connected");
        InputStream is = sock.getInputStream();
        OutputStream os = sock.getOutputStream();
        byte[] buf = new byte[128];
        int read = is.read(buf);
        System.out.println("Server says: " + new String(buf, 0, read));
        System.out.println("Replying to server...");
        os.write("Hello Server".getBytes());
        os.flush();
        os.close();
        is.close();
        sock.close();
        System.out.println("End of communication.");
    }
}
