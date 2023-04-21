package net.cimd.packets;

import net.cimd.packets.parameters.CimdParameter;
import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * @author <a href="mailto:dpoleakovski@gmail.com">Dmitri Poleakovski</a>
 * @version $Revision: 1.1 $ $Date: 2007/03/14 14:15:08 $
 */
public class Encoder {

    public static final byte STX = 0x02;

    public static final byte ETX = 0x03;

    public static final byte TAB = 0x09;

    public static final byte COLON = 0x3A;

    public static void encode(CimdPacket packet, ByteBuffer buffer) throws InvalidContentException {
        packet.validate();
        int startInd = buffer.position();
        buffer.put(STX);
        writeByteDecimal(buffer, packet.getOpCode(), 2, 10);
        buffer.put(COLON);
        writeByteDecimal(buffer, packet.getPacketNumber(), 3, 10);
        Iterator iter = packet.getParameters().iterator();
        while (iter.hasNext()) {
            CimdParameter param = (CimdParameter) iter.next();
            buffer.put(TAB);
            writeString(buffer, param.getType());
            buffer.put(COLON);
            writeString(buffer, param.getValue());
        }
        buffer.put(TAB);
        writeByteDecimal(buffer, calculateChecksum(buffer, startInd, buffer.position() - 1), 2, 16);
        buffer.put(ETX);
        printBuf(buffer, startInd);
    }

    public static byte calculateChecksum(ByteBuffer buf, int stxPos, int lastTabPos) {
        int cs = 0;
        for (int i = stxPos; i <= lastTabPos; i++) {
            cs += (buf.get(i) & 0x00ff);
            cs &= 0x00ff;
        }
        return (byte) cs;
    }

    private static void writeByteDecimal(ByteBuffer buf, byte b, int noOfDigits, int radix) {
        int exp = (int) Math.pow(radix, noOfDigits);
        int val = b & 0x00ff;
        for (int i = noOfDigits; i > 0; i--, exp /= radix) {
            byte dig = (byte) ((val % exp) / (exp / radix));
            buf.put((byte) Character.forDigit(dig, radix));
        }
    }

    private static void writeString(ByteBuffer buf, String str) {
        for (int i = 0; i < str.length(); i++) {
            buf.put((byte) str.charAt(i));
        }
    }

    private static void printBuf(ByteBuffer buf, int offset) {
        for (int i = offset; i < buf.position(); i++) {
            byte b = buf.get(i);
            String value;
            switch(b) {
                case STX:
                    value = "<STX>";
                    break;
                case ETX:
                    value = "<ETX>";
                    break;
                case TAB:
                    value = "<TAB>";
                    break;
                default:
                    value = String.valueOf((char) b);
            }
            System.out.print(" " + value);
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        Login login = new Login();
        login.setUserIdentity("dima");
        login.setPassword("1234");
        login.setSubaddress((byte) 1);
        login.setWindowSize((byte) 10);
        login.setPacketNumber((byte) 0xfe);
        encode(login, buf);
        System.out.println("??????????? " + buf.limit());
    }
}
