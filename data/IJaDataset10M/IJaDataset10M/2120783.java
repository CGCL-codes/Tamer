package utils.transport;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UDPComm {

    protected String connectionPhrase = "hello bitten";

    protected Runnable udpThread = new Runnable() {

        public void run() {
            doRun();
        }
    };

    protected DatagramSocket socket = null;

    protected boolean stopped = false;

    protected UDPSerializer serializer;

    protected ObjectReceiver receiver;

    protected int remotePort;

    protected InetAddress remoteip;

    public UDPComm(String remoteip, int remotePort, UDPSerializer serializer) throws IOException {
        try {
            this.remoteip = InetAddress.getByName(remoteip);
            this.remotePort = remotePort;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.socket = new DatagramSocket();
        this.serializer = serializer;
        sendFirstData();
        start();
    }

    public UDPComm(int localPort, UDPSerializer serializer) throws IOException {
        this.socket = new DatagramSocket(localPort);
        this.serializer = serializer;
        waitForConnection();
        start();
    }

    public void setReceiver(ObjectReceiver receiver) {
        this.receiver = receiver;
    }

    public void start() {
        stopped = false;
        Thread t = new Thread(udpThread);
        t.start();
    }

    public void stop() {
        stopped = true;
        socket.close();
    }

    protected void doRun() {
        while (!stopped) {
            byte[] buf = new byte[serializer.getPacketSize()];
            DatagramPacket pk = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(pk);
                if (!(pk.getAddress().equals(remoteip) && pk.getPort() == this.remotePort)) {
                    System.out.println("Bad client connection :" + pk.getAddress() + ":" + pk.getPort());
                    continue;
                }
            } catch (IOException e) {
                if (e instanceof SocketException) {
                    System.out.println("Connection closed");
                    return;
                }
                e.printStackTrace();
            }
            Object obj;
            try {
                obj = serializer.unserialize(pk.getData(), pk.getOffset(), pk.getLength());
                receiver.objectReceived(obj);
            } catch (SerializeException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendFirstData() throws IOException {
        byte[] bytes = connectionPhrase.getBytes();
        byte[] ack = new byte[connectionPhrase.getBytes().length];
        boolean connected = false;
        socket.setSoTimeout(5000);
        while (!connected) {
            sendData(bytes);
            try {
                receiveData(ack);
            } catch (SocketTimeoutException e) {
                System.out.println("Server isn't ready... Trying again.");
                continue;
            }
            String res = new String(ack);
            if (res.equals(connectionPhrase)) {
                System.out.println("connection established");
                connected = true;
                socket.setSoTimeout(0);
            } else {
                System.out.println("invalid server response. Trying again.");
            }
        }
    }

    public void waitForConnection() throws IOException {
        byte[] buf = new byte[connectionPhrase.getBytes().length];
        DatagramPacket pk = new DatagramPacket(buf, buf.length);
        boolean connected = false;
        socket.setSoTimeout(0);
        while (!connected) {
            socket.receive(pk);
            String client = pk.getAddress() + ":" + pk.getPort();
            String result = new String(buf);
            if (result.equals(connectionPhrase)) {
                System.out.println("connection established : " + client);
                remoteip = pk.getAddress();
                remotePort = pk.getPort();
                sendData(connectionPhrase.getBytes());
                connected = true;
            } else {
                System.out.println("Authentification failed for client :" + client);
            }
        }
    }

    public void sendData(byte[] data) throws IOException {
        System.out.println("Send");
        DatagramPacket pk = new DatagramPacket(data, data.length, remoteip, remotePort);
        socket.send(pk);
    }

    public void receiveData(byte[] data) throws IOException {
        DatagramPacket pk = new DatagramPacket(data, data.length);
        socket.receive(pk);
        System.out.println("Receive");
    }

    public void sendObject(Object obj) throws IOException, SerializeException {
        byte[] bytes = serializer.serialize(obj);
        sendData(bytes);
    }
}
