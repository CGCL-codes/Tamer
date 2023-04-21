package ServerUDP.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mauro
 */
public class ClientWriter {

    private DatagramChannel outputChannel;

    private ByteBuffer output;

    int MTU_MINUS_UDP_HEADER;

    public ClientWriter(SocketAddress address, int port) {
        try {
            outputChannel = DatagramChannel.open();
            outputChannel.configureBlocking(false);
            InetSocketAddress adr = (InetSocketAddress) address;
            adr = new InetSocketAddress(adr.getHostName(), port);
            outputChannel.connect(adr);
            NetworkInterface network = NetworkInterface.getByInetAddress(((InetSocketAddress) outputChannel.socket().getLocalSocketAddress()).getAddress());
            MTU_MINUS_UDP_HEADER = network.getMTU() - 100;
            System.out.println("connected with: " + adr + " " + network.getDisplayName() + " rilevated MTU: " + network.getMTU());
            output = ByteBuffer.allocateDirect(MTU_MINUS_UDP_HEADER);
            output.clear();
        } catch (IOException ex) {
            Logger.getLogger(ClientWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void write(ByteBuffer byteToWrite) throws IOException {
        System.out.println("INFO Written:" + output.position() + " " + byteToWrite.limit() + " " + output.capacity());
        if (output.position() + byteToWrite.limit() > output.capacity()) {
            flush();
        }
        System.out.println("Adding:" + byteToWrite.limit());
        output.put(byteToWrite);
    }

    public void flush() throws IOException {
        output.flip();
        if (output.limit() <= 0) {
            System.out.println("NOT Written:" + output.limit());
            return;
        }
        int byteWritten = outputChannel.write(output);
        if (byteWritten != output.limit()) {
            System.out.println("Written only:" + byteWritten + " byte of " + output.limit());
            output.compact();
        } else {
            System.out.println("Written:" + byteWritten + " byte of " + output.limit());
            output.clear();
        }
    }

    public void writeNow(ByteBuffer t) throws IOException {
        write(t);
        flush();
    }

    public ByteBuffer getBuffer() {
        return ByteBuffer.allocate(MTU_MINUS_UDP_HEADER);
    }
}
