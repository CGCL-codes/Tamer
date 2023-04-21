package org.tcpfile.net.protocols;

import java.io.IOException;
import java.util.Vector;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcpfile.net.ByteArray;

/**
 * Static library of tasks common for most protocols.
 * @author Stivo
 *
 */
public class Commons {

    private static Logger log = LoggerFactory.getLogger(Commons.class);

    /**
	 * Creates a Vector with byte arrays of size up to maxSize
	 * Output format:
	 * addBeforeEach,number of bytes used for the sizes, number of this packet, number of all packets
	 * @param in
	 * @param maxSize
	 * @param addBeforeEach
	 * @return
	 */
    public static Vector<byte[]> splitInto(byte[] in, int maxSize, byte[] addBeforeEach) {
        if (maxSize == 0) throw new RuntimeException("maxSize may not be 0.");
        Vector<byte[]> out = new Vector<byte[]>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lastPacket = in.length / maxSize + 1;
        int bytes = findNumberOfBytesNeeded(lastPacket);
        byte[] lastpacket = convertNumberToBytes(lastPacket, bytes);
        for (int i = 1; i <= lastPacket; i++) {
            baos.reset();
            try {
                baos.write(addBeforeEach);
                baos.write(bytes);
                baos.write(convertNumberToBytes(i, bytes));
                baos.write(lastpacket);
                baos.write(ByteArray.copyfromto(in, (i - 1) * maxSize, (i) * maxSize));
                out.add(baos.toByteArray());
            } catch (IOException e) {
                log.warn("", e);
            }
        }
        return out;
    }

    public static boolean isVectorReadyToReconstruct(Vector<byte[]> vec, int ignoreBytes) {
        long l = getNumberOfPackets(vec.get(0), ignoreBytes);
        if (vec.size() == l) return true;
        return false;
    }

    public static long getNumberOfPackets(byte[] b, int ignoreBytes) {
        byte numberofbytes = b[ignoreBytes];
        long l = decodeNumberAt(ignoreBytes + numberofbytes + 1, b, numberofbytes);
        return l;
    }

    public static byte[] reconstruct(Vector<byte[]> in, int ignoreBytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long current = 1;
        int tries = 0;
        int veclength = in.size();
        while (!in.isEmpty()) {
            tries++;
            for (int i = 0; i < in.size(); i++) {
                byte[] cur = in.get(i);
                byte numberofbytes = cur[ignoreBytes];
                long l = decodeNumberAt(ignoreBytes + 1, cur, numberofbytes);
                if (l == current) {
                    try {
                        baos.write(ByteArray.copyfromto(cur, ignoreBytes + numberofbytes * 2 + 1));
                    } catch (IOException e) {
                        log.warn("", e);
                    }
                    current++;
                    in.remove(i);
                    break;
                }
            }
            if (tries > veclength * 5) {
                log.warn("Could not reconstruct this byte array. something is wrong");
                return null;
            }
        }
        return baos.toByteArray();
    }

    public static long decodeNumberAt(int ignoreBytes, byte[] cur, byte numberofbytes) {
        long l = convertBytesToNumber(cur, numberofbytes, ignoreBytes);
        return l;
    }

    public static byte[] insertNumber(byte b) {
        return convertNumberToBytes(b, 1);
    }

    public static byte[] insertNumber(short b) {
        return convertNumberToBytes(b, 2);
    }

    public static byte[] insertNumber(int b) {
        return convertNumberToBytes(b, 4);
    }

    public static byte[] insertNumber(long b) {
        return convertNumberToBytes(b, 4);
    }

    public static int findNumberOfBytesNeeded(int i) {
        int out = 0;
        while (i != 0) {
            i /= 256;
            out++;
        }
        return out;
    }

    public static byte[] intAsBytes(int bla) {
        byte[] b = new byte[4];
        for (int i = 3; i >= 0; i--) {
            b[i] = (byte) bla;
            bla = bla / 256;
        }
        return b;
    }

    public static byte[] convertNumberToBytes(long number, int bytes) {
        byte[] b = new byte[bytes];
        for (int i = bytes - 1; i >= 0; i--) {
            b[i] = (byte) number;
            number = number / 256;
        }
        return b;
    }

    public static long convertBytesToNumber(byte[] b, int bytes, int firstbyte) {
        int bla = 0;
        int blu = 0;
        int mal = 1;
        for (int i = firstbyte + bytes - 1; i >= firstbyte; i--) {
            blu = b[i];
            if (blu < 0) blu = 256 + blu;
            bla += blu * mal;
            mal *= 256;
        }
        return bla;
    }
}
