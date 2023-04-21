package com.sendmail.jilter.internal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import org.apache.log4j.Category;

/**
 */
public class JilterServerPacketUtil {

    private static Category log = Category.getInstance(JilterServerPacketUtil.class.getName());

    private static final ByteBuffer zeroBuffer = ByteBuffer.wrap(new byte[0]);

    private JilterServerPacketUtil() {
    }

    public static void sendPacket(WritableByteChannel writeChannel, int command, ByteBuffer dataBuffer) throws IOException {
        ByteBuffer headerBuffer = ByteBuffer.allocate(5);
        int totalDataLength = 0;
        if (dataBuffer == null) {
            dataBuffer = zeroBuffer;
        }
        totalDataLength = (dataBuffer.remaining() + 1);
        log.debug("Sending packet");
        headerBuffer.putInt(totalDataLength);
        headerBuffer.put((byte) command);
        headerBuffer.flip();
        writeChannel.write(headerBuffer);
        writeChannel.write(dataBuffer);
        log.debug("Done sending packet");
    }

    public static int zeroTerminatedStringLength(String string) {
        return (string == null) ? 1 : (string.length() + 1);
    }

    public static void writeZeroTerminatedString(ByteBuffer dataBuffer, String string) throws IOException {
        if (string != null) {
            dataBuffer.put(string.getBytes("ASCII"));
        }
        dataBuffer.put((byte) 0);
    }

    public static void sendAddRcptPacket(WritableByteChannel writeChannel, String recipient) throws IOException {
        ByteBuffer dataBuffer = ByteBuffer.allocate(zeroTerminatedStringLength(recipient));
        writeZeroTerminatedString(dataBuffer, recipient);
        sendPacket(writeChannel, JilterConstants.SMFIR_ADDRCPT, (ByteBuffer) dataBuffer.flip());
    }

    public static void sendDelRcptPacket(WritableByteChannel writeChannel, String recipient) throws IOException {
        ByteBuffer dataBuffer = ByteBuffer.allocate(zeroTerminatedStringLength(recipient));
        writeZeroTerminatedString(dataBuffer, recipient);
        sendPacket(writeChannel, JilterConstants.SMFIR_DELRCPT, (ByteBuffer) dataBuffer.flip());
    }

    public static void sendAddHeaderPacket(WritableByteChannel writeChannel, String header, String value) throws IOException {
        ByteBuffer dataBuffer = ByteBuffer.allocate(zeroTerminatedStringLength(header) + zeroTerminatedStringLength(value));
        writeZeroTerminatedString(dataBuffer, header);
        writeZeroTerminatedString(dataBuffer, value);
        sendPacket(writeChannel, JilterConstants.SMFIR_ADDHEADER, (ByteBuffer) dataBuffer.flip());
    }

    public static void sendChgHeaderPacket(WritableByteChannel writeChannel, int index, String header, String value) throws IOException {
        ByteBuffer dataBuffer = ByteBuffer.allocate(4 + zeroTerminatedStringLength(header) + zeroTerminatedStringLength(value));
        dataBuffer.putInt(index);
        writeZeroTerminatedString(dataBuffer, header);
        writeZeroTerminatedString(dataBuffer, value);
        sendPacket(writeChannel, JilterConstants.SMFIR_CHGHEADER, (ByteBuffer) dataBuffer.flip());
    }

    public static void sendReplBodyPacket(WritableByteChannel writeChannel, ByteBuffer dataBuffer) throws IOException {
        sendPacket(writeChannel, JilterConstants.SMFIR_REPLBODY, dataBuffer);
    }

    public static void sendProgressPacket(WritableByteChannel writeChannel) throws IOException {
        sendPacket(writeChannel, JilterConstants.SMFIR_PROGRESS, null);
    }

    public static void sendReplyCodePacket(WritableByteChannel writeChannel, String reply) throws IOException {
        ByteBuffer dataBuffer = ByteBuffer.allocate(zeroTerminatedStringLength(reply));
        writeZeroTerminatedString(dataBuffer, reply);
        sendPacket(writeChannel, JilterConstants.SMFIR_REPLYCODE, (ByteBuffer) dataBuffer.flip());
    }

    public static String getZeroTerminatedString(ByteBuffer dataBuffer) {
        StringBuffer newString = new StringBuffer();
        while (dataBuffer.remaining() > 0) {
            byte thisByte = dataBuffer.get();
            if (thisByte == 0) {
                break;
            }
            newString.append((char) thisByte);
        }
        return newString.toString();
    }

    public static String[] getZeroTerminatedStringArray(ByteBuffer dataBuffer) {
        ArrayList array = new ArrayList();
        String[] returnArray = null;
        while (dataBuffer.remaining() > 0) {
            array.add(getZeroTerminatedString(dataBuffer));
        }
        return (String[]) array.toArray(new String[array.size()]);
    }
}
