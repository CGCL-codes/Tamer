package it.hakvoort.bdf2tcp;

import it.hakvoort.bdf.BDFDataRecord;
import it.hakvoort.bdf.BDFListener;
import it.hakvoort.bdf.BDFReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author Gido Hakvoort (gido@hakvoort.it)
 *
 */
public class BDFNetworkServer implements Runnable {

    private ServerSocket serverSocket;

    private BDFReader reader;

    private String HOST;

    private int PORT;

    private boolean listening = true;

    private AtomicInteger connectedClients = new AtomicInteger(0);

    private Thread serverThread;

    public BDFNetworkServer(BDFReader reader, int PORT) {
        this.reader = reader;
        this.PORT = PORT;
    }

    public synchronized void start() {
        if (this.serverThread == null) {
            this.serverThread = new Thread(this);
            this.serverThread.start();
        }
    }

    @Override
    public void run() {
        if (!reader.isRunning()) {
            reader.start();
        }
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(PORT));
            HOST = InetAddress.getLocalHost().getHostAddress();
            System.out.println(String.format("BDFServer ready and listening for connections on: %s:%s.", HOST, PORT));
            System.out.println(String.format("Sending data at %s records/second with %s channels/record.", reader.getFrequency(), reader.getHeader().getNumChannels()));
            while (listening) {
                Socket socket = serverSocket.accept();
                BDFClientHandler handler = new BDFClientHandler(socket);
                reader.addListener(handler);
                synchronized (connectedClients) {
                    connectedClients.incrementAndGet();
                }
                String name = String.format("BDFClient_%s", connectedClients);
                System.out.println(String.format("%s connected from '%s'.", name, socket.getInetAddress().getHostAddress()));
                Thread handlerThread = new Thread(handler);
                handlerThread.setName(name);
                handlerThread.setDaemon(true);
                handlerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(String.format("Could not bind socket to address %s:%s", HOST, PORT));
        }
    }

    public synchronized void stop() {
        listening = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println(String.format("Disconnect error"));
        }
        if (reader.isRunning()) {
            reader.stop();
        }
    }

    public int getConnectedClients() {
        return connectedClients.get();
    }

    /**
	 * BDFClientHandler handles unique client connections. The handler receives BDFDataRecords from the BDFReader and 
	 * convert these into bytes before sending them over the network, mimicing Biosemi's ActiView network connection.
	 */
    public class BDFClientHandler implements Runnable, BDFListener {

        private InputStream in;

        private OutputStream out;

        private boolean connected = true;

        public BDFClientHandler(Socket socket) throws IOException {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        }

        @Override
        public void run() {
            try {
                while (connected && in.read() != -1) {
                }
            } catch (IOException e) {
                connected = false;
            }
            synchronized (connectedClients) {
                connectedClients.decrementAndGet();
            }
            reader.removeListener(this);
            System.out.println(String.format("%s disconnected.", Thread.currentThread().getName()));
        }

        @Override
        public void receivedRecord(BDFDataRecord record) {
            byte[] data = new byte[record.channelCount * 3];
            for (int i = 0; i < record.channelCount; i++) {
                int sample = record.samples[i];
                data[i * 3 + 0] = (byte) (sample & 0xFF);
                data[i * 3 + 1] = (byte) ((sample >> 8) & 0xFF);
                data[i * 3 + 2] = (byte) ((sample >>> 16) & 0xFF);
            }
            try {
                out.write(data);
            } catch (IOException e) {
                connected = false;
            }
        }
    }
}
