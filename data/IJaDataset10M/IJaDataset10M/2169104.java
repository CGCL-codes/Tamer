package egs.comms;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.net.*;
import egs.packets.*;

public abstract class Connection {

    protected ByteBuffer rcvBuf;

    protected DataOutputStream outs;

    protected DataInputStream ins;

    protected Socket socket;

    protected int SO_TIMEOUT = 30000;

    public Connection(int bufSize) {
        rcvBuf = ByteBuffer.allocate(bufSize);
    }

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.socket.setSoLinger(true, SO_TIMEOUT);
        setStreams();
    }

    public void setStreams() throws IOException {
        this.outs = new DataOutputStream(socket.getOutputStream());
        this.ins = new DataInputStream(socket.getInputStream());
    }

    public void setStreams(DataOutputStream outs, DataInputStream ins) {
        this.outs = outs;
        this.ins = ins;
    }

    public void sendPacket(Packet p) throws EGSCommunicationException {
        boolean retVal = true;
        byte[] bytesToSend = p.getRaw();
        try {
            this.outs.write(bytesToSend, 0, bytesToSend.length);
        } catch (IOException ioe) {
            System.out.println("Could not send data " + ioe);
            retVal = false;
            throw new EGSCommunicationException(ioe.getMessage());
        }
    }

    public Packet getPacket() throws InvalidPacketException {
        byte[] packetData = null;
        Packet packet = null;
        byte[] header = new byte[Packet.HEADER_SIZE];
        int dataSize = 0;
        int dataBytesRead = 0;
        try {
            this.ins.read(header, 0, Packet.HEADER_SIZE);
            dataSize = ((header[8] & 0xff) << 32) | ((header[9] & 0xff) << 16) | ((header[10] & 0xff) << 8) | (header[11] & 0xff);
            packetData = new byte[Packet.HEADER_SIZE + dataSize];
            for (int i = 0; i < Packet.HEADER_SIZE; i++) {
                packetData[i] = header[i];
            }
            dataBytesRead = this.ins.read(packetData, Packet.HEADER_SIZE, dataSize);
            if (dataBytesRead != dataSize) {
                throw new InvalidPacketException("Packet lenght does not match data received");
            }
        } catch (IOException e) {
            throw new InvalidPacketException("Could not create packet, exception: " + e);
        }
        packet = Packet.getNextPacket(packetData);
        return packet;
    }

    public void closeConnection() {
        try {
            this.ins.close();
            this.outs.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBytes(byte[] data) throws IOException {
        this.outs.write(data);
    }
}
