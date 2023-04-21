package UDP;

import java.net.*;

public class UDP_Connection implements Runnable {

    private DatagramSocket dsock;

    private UDP_Callbacks callbacks;

    private int maxSize;

    private Thread thrd;

    public UDP_Connection(int prt, UDP_Callbacks calls, int maxPacketSize) throws SocketException {
        dsock = new DatagramSocket(prt);
        callbacks = calls;
        maxSize = maxPacketSize;
        thrd = new Thread(this);
        thrd.start();
    }

    public void run() {
        while (!thrd.isInterrupted()) {
            DatagramPacket pack = new DatagramPacket(new byte[maxSize], maxSize);
            try {
                dsock.receive(pack);
                callbacks.PacketReceived(pack);
            } catch (Exception e) {
                callbacks.ReceiveException(e);
                return;
            }
        }
    }

    public DatagramSocket getSocket() {
        return dsock;
    }
}
