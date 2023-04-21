package ipspoofer;

import java.math.BigInteger;

public class UDP {

    private byte[] ethType;

    private byte ipVersion;

    private byte ipTos;

    private byte[] ipOffset;

    private byte ipTTL;

    private byte ipProt;

    private byte[] ipTotalLength;

    private byte[] ipChecksum;

    private byte[] udpLength;

    private byte[] udpChecksum;

    private byte[] udpPackage;

    public byte[] getIpChecksum() {
        return ipChecksum;
    }

    public void setIpChecksum(byte[] ipChecksum) {
        this.ipChecksum = ipChecksum;
    }

    public byte[] getUdpLength() {
        return udpLength;
    }

    public void setUdpLength(byte[] udpLength) {
        this.udpLength = udpLength;
    }

    public byte[] getUdpChecksum() {
        return udpChecksum;
    }

    public void setUdpChecksum(byte[] udpChecksum) {
        this.udpChecksum = udpChecksum;
    }

    public byte[] getUdpPackage() {
        return udpPackage;
    }

    public void setUdpPackage(byte[] udpPackage) {
        this.udpPackage = udpPackage;
    }

    public UDP(byte[] sourceMAC, byte[] destMAC, byte[] sourceIP, byte[] destIP, byte[] sourcePort, byte[] destPort, byte[] data, byte[] ipId) {
        ethType = new byte[] { (byte) 8, (byte) 0 };
        ipVersion = (byte) 69;
        ipTos = (byte) 0;
        ipOffset = new byte[] { (byte) 0, (byte) 0 };
        ipTTL = (byte) 128;
        ipProt = (byte) 17;
        ipTotalLength = calculateIpTotalLength(data);
        udpLength = calculateUdpLength(data);
        ipChecksum = Checksum.calculate(calculateIPChecksumData(ipVersion, ipTos, ipTotalLength, ipId, ipOffset, ipTTL, ipProt, sourceIP, destIP));
        udpChecksum = Checksum.calculate(calculateUDPChecksumData(sourceIP, destIP, udpLength, sourcePort, destPort, data));
        udpPackage = createUDPArray(sourceMAC, destMAC, sourceIP, destIP, sourcePort, destPort, data, ethType, ipVersion, ipTos, ipId, ipTotalLength, ipOffset, ipTTL, ipProt, udpLength, ipChecksum, udpChecksum);
    }

    private static byte[] calculateUdpLength(byte[] data) {
        byte[] result = BigInteger.valueOf(data.length + 8).toByteArray();
        if (result.length == 1) return new byte[] { 0, result[0] }; else return result;
    }

    private static byte[] calculateIpTotalLength(byte[] data) {
        byte[] result = BigInteger.valueOf(data.length + 28).toByteArray();
        if (result.length == 1) return new byte[] { 0, result[0] }; else return result;
    }

    private static int[] calculateIPChecksumData(byte ipVersion, byte ipTos, byte[] ipTotalLength, byte[] ipId, byte[] ipOffset, byte ipTTL, byte ipProt, byte[] sourceIP, byte[] destIP) {
        int[] result = new int[20];
        int position = 0;
        result[position] = positiveByteValue(ipVersion);
        position++;
        result[position] = positiveByteValue(ipTos);
        position++;
        result = fillChecksumDataArray(result, ipTotalLength, position);
        position += ipTotalLength.length;
        result = fillChecksumDataArray(result, ipId, position);
        position += ipId.length;
        result = fillChecksumDataArray(result, ipOffset, position);
        position += ipOffset.length;
        result[position] = positiveByteValue(ipTTL);
        position++;
        result[position] = positiveByteValue(ipProt);
        position++;
        result = fillChecksumDataArray(result, sourceIP, position);
        position += sourceIP.length;
        result = fillChecksumDataArray(result, destIP, position);
        return result;
    }

    private int[] calculateUDPChecksumData(byte[] sourceIP, byte[] destIP, byte[] udpLength, byte[] sourcePort, byte[] destPort, byte[] data) {
        int length = 20 + data.length;
        int padd = 0;
        if (length % 2 == 1) padd = 1;
        int[] result = new int[length + padd];
        int position = 0;
        result = fillChecksumDataArray(result, sourceIP, position);
        position += sourceIP.length;
        result = fillChecksumDataArray(result, destIP, position);
        position += destIP.length;
        byte[] prot = new byte[] { (byte) 0, (byte) positiveByteValue(ipProt) };
        result = fillChecksumDataArray(result, prot, position);
        position += prot.length;
        result = fillChecksumDataArray(result, udpLength, position);
        position += udpLength.length;
        result = fillChecksumDataArray(result, sourcePort, position);
        position += sourcePort.length;
        result = fillChecksumDataArray(result, destPort, position);
        position += destPort.length;
        result = fillChecksumDataArray(result, data, position);
        position += data.length;
        if (padd == 1) position++;
        result = fillChecksumDataArray(result, udpLength, position);
        return result;
    }

    private static int[] fillChecksumDataArray(int[] result, byte[] fill, int position) {
        for (int i = position; i < position + fill.length; i++) {
            result[i] = positiveByteValue(fill[i - position]);
        }
        return result;
    }

    private static byte[] fillUDPDataArray(byte[] result, byte[] fill, int position) {
        for (int i = position; i < position + fill.length; i++) {
            result[i] = fill[i - position];
        }
        return result;
    }

    private static int positiveByteValue(int initial) {
        if (initial < 0) return initial + 256; else return initial;
    }

    private static byte[] createUDPArray(byte[] sourceMAC, byte[] destMAC, byte[] sourceIP, byte[] destIP, byte[] sourcePort, byte[] destPort, byte[] data, byte[] ethType, byte ipVersion, byte ipTos, byte[] ipId, byte[] ipTotalLength, byte[] ipOffset, byte ipTTL, byte ipProt, byte[] udpLength, byte[] ipChecksum, byte[] udpChecksum) {
        byte[] result = new byte[42 + data.length];
        int position = 0;
        result = fillUDPDataArray(result, sourceMAC, position);
        position += sourceMAC.length;
        result = fillUDPDataArray(result, destMAC, position);
        position += destMAC.length;
        result = fillUDPDataArray(result, ethType, position);
        position += ethType.length;
        result[position] = ipVersion;
        position++;
        result[position] = ipTos;
        position++;
        result = fillUDPDataArray(result, ipTotalLength, position);
        position += ipTotalLength.length;
        result = fillUDPDataArray(result, ipId, position);
        position += ipId.length;
        result = fillUDPDataArray(result, ipOffset, position);
        position += ipOffset.length;
        result[position] = ipTTL;
        position++;
        result[position] = ipProt;
        position++;
        result = fillUDPDataArray(result, ipChecksum, position);
        position += ipChecksum.length;
        result = fillUDPDataArray(result, sourceIP, position);
        position += sourceIP.length;
        result = fillUDPDataArray(result, destIP, position);
        position += destIP.length;
        result = fillUDPDataArray(result, sourcePort, position);
        position += sourcePort.length;
        result = fillUDPDataArray(result, destPort, position);
        position += destPort.length;
        result = fillUDPDataArray(result, udpLength, position);
        position += udpLength.length;
        result = fillUDPDataArray(result, udpChecksum, position);
        position += udpChecksum.length;
        result = fillUDPDataArray(result, data, position);
        return result;
    }
}
