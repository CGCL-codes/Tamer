package temp2;

import src.RTPpacket;
import temp.CODE;
import temp.CrossCorrelation;
import temp.WaveformSubstitutionPatternAlgorithm;

public class Receiver {

    byte[] buffer;

    RTPpacket[] packts;

    int lenghtPacket;

    byte[] bufferPreviousPacket;

    int lastIndex;

    int maxPackets;

    int lenghtTemplate;

    public Receiver(byte[] buffer, int lengthPacket, int max, int lenghtTemplate) {
        this.buffer = buffer;
        this.lenghtPacket = lengthPacket;
        packts = generatePackets();
        this.lenghtTemplate = lenghtTemplate;
        this.maxPackets = max;
        this.lastIndex = 0;
        this.bufferPreviousPacket = new byte[this.maxPackets * lengthPacket];
    }

    private RTPpacket[] generatePackets() {
        byte bufferPacket[] = new byte[this.lenghtPacket];
        RTPpacket[] packets = new RTPpacket[buffer.length / lenghtPacket];
        for (int i = 0, k = 0; i < buffer.length; i += lenghtPacket, k++) {
            for (int j = 0; j < buffer.length; j++) {
                bufferPacket[j] = buffer[i + j];
            }
            packets[k] = new RTPpacket(bufferPacket, lenghtPacket);
        }
        return packets;
    }

    public RTPpacket getPacket(int n) {
        byte[] data = new byte[lenghtPacket];
        byte[] template = new byte[lenghtTemplate];
        if (packts[n - 1] == null) {
            for (int i = 0; i < template.length; i++) {
                template[lenghtTemplate - 1 - i] = bufferPreviousPacket[lastIndex - 1 - i];
            }
            CrossCorrelation cc = new CrossCorrelation(bufferPreviousPacket, bufferPreviousPacket.length, 0, template.length, 0, bufferPreviousPacket.length, CODE.BYTE_UNSIGNED);
            WaveformSubstitutionPatternAlgorithm wspa = new WaveformSubstitutionPatternAlgorithm(bufferPreviousPacket, bufferPreviousPacket.length, 0, template.length, 0, cc);
        }
        packts[n - 1].getpayload(data);
        for (int j = 0; j < data.length; j++) {
            bufferPreviousPacket[lastIndex + j] = data[j];
        }
        lastIndex = lastIndex + data.length;
        return packts[n - 1];
    }
}
