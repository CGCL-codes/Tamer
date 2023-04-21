package semix2.impl.robot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import semix2.robot.DataPacket;
import semix2.robot.DataPacketHandler;

public class UDPPacketReader implements Runnable {

    private final DatagramSocket _socket;

    private final InetAddress _serverAddress;

    private final int _serverReportedUDPPort;

    private final DataPacketHandler _handler;

    private final byte[] _readBuffer;

    private final Logger _logger;

    public UDPPacketReader(DatagramSocket socket, InetAddress serverAddress, int serverPort, DataPacketHandler handler) {
        _socket = socket;
        _serverAddress = serverAddress;
        _serverReportedUDPPort = serverPort;
        _handler = handler;
        _readBuffer = new byte[32020];
        _logger = Logger.getLogger(UDPPacketReader.class.getName());
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            DataPacket packet = null;
            try {
                packet = readPacket();
            } catch (IOException ignore) {
            }
            if (packet != null) {
                _handler.handleDataPacket(packet);
            }
        }
    }

    private DataPacket readPacket() throws IOException {
        DatagramPacket udpPacket = new DatagramPacket(_readBuffer, _readBuffer.length);
        _socket.receive(udpPacket);
        if (!test(udpPacket)) {
            return null;
        }
        int length = (_readBuffer[2] & 0xff) | ((_readBuffer[3] & 0xff) << 8);
        int checkSum = ((_readBuffer[length - 2] & 0xff) << 8) | (_readBuffer[length - 1] & 0xff);
        if (checkSum != CheckSumUtil.getCheckSum(_readBuffer, length)) {
            _logger.log(Level.WARNING, "err checksum");
            return null;
        }
        return new DataPacketImpl(_readBuffer, length);
    }

    private boolean test(DatagramPacket udpPacket) {
        if (!udpPacket.getAddress().equals(_serverAddress) || udpPacket.getPort() != _serverReportedUDPPort) {
            return false;
        }
        if (udpPacket.getLength() <= 0) {
            return false;
        }
        return true;
    }
}
