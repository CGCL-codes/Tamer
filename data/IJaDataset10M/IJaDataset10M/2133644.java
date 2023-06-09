package org.red5.server.net.rtmp;

import org.apache.mina.common.ByteBuffer;
import org.red5.server.net.rtmp.message.Constants;

/**
 *
 * @author The Red5 Project (red5@osflash.org)
 * @author Luke Hubbard, Codegent Ltd (luke@codegent.com)
 */
public class RTMPUtils implements Constants {

    public static void writeReverseIntOld(ByteBuffer out, int value) {
        byte[] bytes = new byte[4];
        ByteBuffer rev = ByteBuffer.allocate(4);
        rev.putInt(value);
        rev.flip();
        bytes[3] = rev.get();
        bytes[2] = rev.get();
        bytes[1] = rev.get();
        bytes[0] = rev.get();
        rev.release();
        out.put(bytes);
    }

    public static void writeReverseInt(ByteBuffer out, int value) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) (0xFF & (value >> 24));
        bytes[2] = (byte) (0xFF & (value >> 16));
        bytes[1] = (byte) (0xFF & (value >> 8));
        bytes[0] = (byte) (0xFF & value);
        out.put(bytes);
    }

    public static void writeMediumInt(ByteBuffer out, int value) {
        byte[] bytes = new byte[3];
        bytes[0] = (byte) (0xFF & (value >> 16));
        bytes[1] = (byte) (0xFF & (value >> 8));
        bytes[2] = (byte) (0xFF & (value >> 0));
        out.put(bytes);
    }

    public static int readUnsignedMediumInt(ByteBuffer in) {
        byte[] bytes = new byte[3];
        in.get(bytes);
        int val = 0;
        if (bytes[0] < 0) {
            val += ((bytes[0] + 256) << 16);
        } else {
            val += (bytes[0] << 16);
        }
        if (bytes[1] < 0) {
            val += ((bytes[1] + 256) << 8);
        } else {
            val += (bytes[1] << 8);
        }
        if (bytes[2] < 0) {
            val += bytes[2] + 256;
        } else {
            val += bytes[2];
        }
        return val;
    }

    public static int readUnsignedMediumIntOld(ByteBuffer in) {
        byte[] bytes = new byte[3];
        in.get(bytes);
        int val = 0;
        val += (bytes[0] & 0xFF) * 256 * 256;
        val += (bytes[1] & 0xFF) * 256;
        val += (bytes[2] & 0xFF);
        return val;
    }

    public static int readMediumIntOld(ByteBuffer in) {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.put((byte) 0x00);
        buf.put(in.get());
        buf.put(in.get());
        buf.put(in.get());
        buf.flip();
        int value = buf.getInt();
        buf.release();
        return value;
    }

    public static int readMediumInt(ByteBuffer in) {
        byte[] bytes = new byte[3];
        in.get(bytes);
        int val = 0;
        if (bytes[0] < 0) {
            val += ((bytes[0] + 256) << 16);
        } else {
            val += (bytes[0] << 16);
        }
        if (bytes[1] < 0) {
            val += ((bytes[1] + 256) << 8);
        } else {
            val += (bytes[1] << 8);
        }
        if (bytes[2] < 0) {
            val += bytes[2] + 256;
        } else {
            val += bytes[2];
        }
        return val;
    }

    public static int readReverseInt(ByteBuffer in) {
        byte[] bytes = new byte[4];
        in.get(bytes);
        int val = 0;
        val += bytes[3] << 24;
        val += bytes[2] << 16;
        val += bytes[1] << 8;
        val += bytes[0];
        return val;
    }

    public static int readReverseIntOld(ByteBuffer in) {
        byte[] bytes = new byte[4];
        in.get(bytes);
        int val = 0;
        val += bytes[3] * 256 * 256 * 256;
        val += bytes[2] * 256 * 256;
        val += bytes[1] * 256;
        val += bytes[0];
        return val;
    }

    public static byte encodeHeaderByte(byte headerSize, byte channelId) {
        return (byte) ((headerSize << 6) + channelId);
    }

    public static byte decodeChannelId(byte header) {
        return (byte) (header & 0x3f);
    }

    public static byte decodeHeaderSize(byte header) {
        int headerInt = (header >= 0) ? header : header + 256;
        byte size = (byte) (headerInt >> 6);
        return size;
    }

    public static int getHeaderLength(byte headerSize) {
        switch(headerSize) {
            case HEADER_NEW:
                return 12;
            case HEADER_SAME_SOURCE:
                return 8;
            case HEADER_TIMER_CHANGE:
                return 4;
            case HEADER_CONTINUE:
                return 1;
            default:
                return -1;
        }
    }
}
