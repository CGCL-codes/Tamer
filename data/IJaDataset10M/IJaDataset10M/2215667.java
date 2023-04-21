package cs544.group6.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import cs544.group6.shared.FTPacket;

/**
 * @author group 6
 * 
 */
public class FTClientListeningThread extends Thread {

    DatagramSocket socket;

    DatagramPacket packet;

    private boolean running = true;

    private static final int BUFFER_SIZE = 8192;

    /**
	 * 
	 * @param socket
	 */
    public FTClientListeningThread(DatagramSocket socket) {
        super("FTClientListeningThread");
        this.socket = socket;
    }

    @Override
    public void run() {
        FTPacket ftPacket;
        FTClientProtocolHandler clientHandler = new FTClientProtocolHandler();
        while (running) {
            packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
            try {
                socket.receive(packet);
                ftPacket = new FTPacket(packet.getData(), packet.getLength());
                clientHandler.processFTPacket(ftPacket);
            } catch (IOException e) {
                e.printStackTrace();
                shutdown();
            }
        }
        socket.close();
    }

    /**
	 * Method to shutdown thread
	 */
    public void shutdown() {
        running = false;
    }
}
