package se.ytterman.jpcap.io;

import java.lang.*;
import java.io.*;
import java.util.*;
import se.ytterman.jpcap.jni.PcapSocket;
import se.ytterman.jpcap.pcap.PcapPacket;

public class PcapPacketOutputStream extends ByteArrayOutputStream {

    public PcapPacketOutputStream(PcapSocket outputSocket) {
        this.outputSocket = outputSocket;
    }

    public void flush() throws IOException {
        super.flush();
        this.addPadding(64);
        this.checkFrame();
        PcapPacket outputPacket = new PcapPacket(this.toByteArray());
        this.outputSocket.write(outputPacket);
    }

    private void addPadding(int total_length) {
        int current_length = this.size();
        if (total_length < 0) {
        } else if (total_length < current_length) {
        } else if (total_length == current_length) {
        } else if (total_length > current_length) {
            byte[] padding = new byte[total_length - current_length];
            int length = padding.length;
            for (int i = 0; i < length; i++) padding[i] = (byte) 0x0;
            this.write(padding, 0, length);
        }
    }

    private void checkFrame() {
        int current_length = this.size();
        if (current_length < 64) {
        } else if (current_length >= 64 && current_length <= 1514) {
        } else if (current_length <= 1514) {
        }
    }

    private PcapSocket outputSocket;
}
