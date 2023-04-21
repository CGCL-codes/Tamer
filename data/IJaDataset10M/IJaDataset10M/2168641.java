package testing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class TestUDPnTCP {

    static final int PORT = 1234;

    public static void main(String[] args) throws IOException, InterruptedException {
        UDPThread udpThread = new UDPThread(new DatagramSocket(PORT));
        TCPThread tcpThread = new TCPThread(new ServerSocket(PORT));
        udpThread.start();
        Thread.sleep(1000);
        tcpThread.start();
    }
}

class TCPThread extends Thread {

    private ServerSocket socket;

    public TCPThread(ServerSocket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            System.out.println(this);
            Socket clientSocket = socket.accept();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class UDPThread extends Thread {

    DatagramSocket socket;

    public UDPThread(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            System.out.println(this);
            DatagramPacket packet = new DatagramPacket(new byte[512], 512);
            socket.receive(packet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
