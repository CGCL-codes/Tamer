package fr.upemlv.transfile.server.threads;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.logging.Logger;
import fr.upemlv.transfile.server.FileServer;
import fr.upemlv.transfile.server.UdpManager;

public class UdpSender implements Runnable {

    public final int UDP_DATA_LENGTH = 512;

    private final long filesize;

    private final String filename;

    private final int port;

    private final String multicastAddress;

    private final MulticastSocket ms;

    private final long maxPosition;

    private long nextTrunkToSend = 0;

    private long lastTrunk;

    public UdpSender(String filename, long filesize, int port, String multicastAddress) throws IOException {
        this.filename = filename;
        this.filesize = filesize;
        this.port = port;
        this.multicastAddress = multicastAddress;
        this.ms = new MulticastSocket(port);
        this.maxPosition = filesize / UDP_DATA_LENGTH + 1;
        ms.joinGroup(InetAddress.getByName(multicastAddress));
        Logger.getLogger("fslogger").info("Creation of Multicast group for file : " + filename + " on :" + multicastAddress + ":" + port);
    }

    /**
	 * redefines value for stopping the transmission loop UDP
	 */
    public void adjustTrunkParameters() {
        synchronized (UdpManager.getInstance()) {
            lastTrunk = (lastTrunk == 0) ? maxPosition : lastTrunk - 1;
        }
    }

    @Override
    public void run() {
        Logger.getLogger("fslogger").info("UDP Sender started for file " + filename);
        FileChannel fc;
        MappedByteBuffer bb;
        try {
            fc = new FileInputStream(FileServer.getInstance().getRoot() + filename).getChannel();
            bb = fc.map(MapMode.READ_ONLY, 0, filesize);
            lastTrunk = 0;
            DatagramPacket d = new DatagramPacket(new byte[0], 0, InetAddress.getByName(multicastAddress), port);
            while (true) {
                byte[] dataArray = new byte[UDP_DATA_LENGTH];
                int dataLength;
                ByteBuffer dataBuf = ByteBuffer.allocate(UDP_DATA_LENGTH + 8);
                dataBuf.putLong(nextTrunkToSend);
                if (bb.remaining() < UDP_DATA_LENGTH) {
                    dataLength = bb.remaining() + 8;
                    d.setLength(dataLength);
                    bb.get(dataArray, 0, bb.remaining());
                    bb.clear();
                    nextTrunkToSend = 0;
                } else {
                    bb.get(dataArray, 0, UDP_DATA_LENGTH);
                    nextTrunkToSend++;
                }
                dataBuf.put(dataArray);
                d.setData(dataBuf.array());
                ms.send(d);
                synchronized (UdpManager.getInstance()) {
                    if (nextTrunkToSend == lastTrunk) {
                        UdpManager.getInstance().removeTransmission(filename);
                        bb.clear();
                        fc.close();
                        Logger.getLogger("fslogger").info("UDP Sender finished for file " + filename);
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger("fslogger").warning(filename + "was not found for client : " + e.getCause());
        } catch (IOException e) {
            Logger.getLogger("fslogger").warning("error during mapping or sending file " + filename + " : " + e.getCause());
        }
    }

    public long getNextTrunkToSend() {
        return nextTrunkToSend;
    }

    public long getLastTrunk() {
        return lastTrunk;
    }

    public void setLastTrunk(long lastTrunk) {
        this.lastTrunk = lastTrunk;
    }

    public long getFilesize() {
        return filesize;
    }

    public String getFilename() {
        return filename;
    }

    public int getPort() {
        return port;
    }

    public String getMulticastAddress() {
        return multicastAddress;
    }
}
