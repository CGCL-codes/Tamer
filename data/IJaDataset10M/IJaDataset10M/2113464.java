package temp3;

import java.util.Iterator;
import java.util.Vector;
import src.RTPpacket;
import temp.CODE;
import temp.CrossCorrelation;
import temp.WaveformSubstitutionPatternAlgorithm;

public class Receiver {

    byte[] buffer;

    RTPpacket[] packts;

    private int lenghtPacket;

    public byte[] bufferPreviousPacket;

    public int getQuantPacket() {
        return packts.length;
    }

    int lastIndex;

    int maxPackets;

    int lenghtTemplate;

    public byte[] copyPrevius(int n) {
        byte[] copy = new byte[(n - 1) * getLenghtPacket()];
        for (int i = 0; i < lastIndex - 1; i++) {
            copy[i] = bufferPreviousPacket[i];
        }
        return copy;
    }

    public Receiver(byte[] buffer, int lengthPacket, int max, int lenghtTemplate) {
        this.buffer = buffer;
        this.lenghtPacket = lengthPacket;
        packts = generatePackets();
        this.lenghtTemplate = lenghtTemplate;
        this.maxPackets = max;
        this.lastIndex = 0;
        this.bufferPreviousPacket = new byte[this.maxPackets * lengthPacket];
    }

    public byte[] getBufferBytes() {
        byte[] buffer = new byte[getLenghtPacket() * packts.length];
        for (int i = 0; i < packts.length; i++) {
            byte[] data = new byte[packts[i].getpayload_length()];
            packts[i].getpayload(data);
            for (int j = 0; j < data.length; j++) {
                buffer[i * getLenghtPacket() + j] = data[j];
            }
        }
        int tamTotal = buffer.length;
        Vector bom = new Vector();
        for (int i = 0; i < tamTotal; i++) {
            if (buffer[i] != 0) bom.add(buffer[i]);
        }
        Iterator it = bom.iterator();
        byte[] b1 = new byte[bom.size()];
        for (int i = 0; it.hasNext(); i++) {
            b1[i] = (Byte) it.next();
        }
        return b1;
    }

    private RTPpacket[] generatePackets() {
        int quantPacket = buffer.length / getLenghtPacket();
        int resto = buffer.length - quantPacket * getLenghtPacket();
        RTPpacket[] packets = new RTPpacket[quantPacket + 1];
        int k = 0;
        byte[] bufferPacket = new byte[getLenghtPacket()];
        for (int i = 0; i < quantPacket; i++, k++) {
            for (int j = 0; j < getLenghtPacket(); j++) {
                bufferPacket[j] = buffer[i * getLenghtPacket() + j];
            }
            packets[k] = new RTPpacket(bufferPacket, getLenghtPacket());
        }
        bufferPacket = new byte[resto];
        for (int j = 0; j < resto; j++) {
            bufferPacket[j] = buffer[quantPacket * getLenghtPacket() + j];
        }
        packets[k] = new RTPpacket(bufferPacket, bufferPacket.length);
        return packets;
    }

    public RTPpacket getPacket(int n) {
        if (packts[n - 1] == null) {
            byte[] copy = copyPrevius(n);
            CrossCorrelation cc = new CrossCorrelation(copy, (copy.length) - lenghtTemplate - getLenghtPacket(), 0, lenghtTemplate, copy.length - lenghtTemplate, copy.length, CODE.BYTE_UNSIGNED);
            WaveformSubstitutionPatternAlgorithm wspa = new WaveformSubstitutionPatternAlgorithm(copy, 1, 100, (copy.length) - lenghtTemplate - getLenghtPacket(), 0, lenghtTemplate, cc);
            byte[] payload = wspa.getNextPayloadByteArray();
            packts[n - 1] = new RTPpacket(payload, payload.length);
        }
        byte[] data = new byte[packts[n - 1].getlength()];
        packts[n - 1].getpayload(data);
        for (int j = 0; j < data.length; j++) {
            bufferPreviousPacket[lastIndex + j] = data[j];
        }
        lastIndex = lastIndex + data.length;
        return packts[n - 1];
    }

    public void generateLoss(int n) {
        packts[n - 1] = null;
    }

    public void setLenghtPacket(int lenghtPacket) {
        this.lenghtPacket = lenghtPacket;
    }

    public int getLenghtPacket() {
        return lenghtPacket;
    }
}
