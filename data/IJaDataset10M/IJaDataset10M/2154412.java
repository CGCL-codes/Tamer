package com.amd.aparapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Used to parse ClassFile structure. <br/>
 * 
 * Provides low level access to sequential bytes in a stream given a specific offset.
 * 
 * Does not keep track of accesses.  For this you will need a <code>ByteReader</code>
 * 
 * @see com.amd.aparapi.ByteReader
 * 
 * @author gfrost
 *
 */
class ByteBuffer {

    private byte[] bytes;

    /**
    * Construct from an <code>InputStream</code>
    * 
    * @param _inputStream
    */
    ByteBuffer(InputStream _inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bytes = new byte[4096];
        int bytesRead = 0;
        try {
            while ((bytesRead = _inputStream.read(bytes)) > 0) {
                baos.write(bytes, 0, bytesRead);
            }
            bytes = baos.toByteArray();
        } catch (IOException e) {
            bytes = new byte[0];
            e.printStackTrace();
        }
    }

    int u2(int _offset) {
        return (u1(_offset) << 8 | u1(_offset + 1));
    }

    int s2(int _offset) {
        int s2 = u2(_offset);
        if (s2 > 0x7fff) {
            s2 = -(0x10000 - s2);
        }
        return (s2);
    }

    int u4(int _offset) {
        return (u2(_offset) << 16 | u2(_offset + 2));
    }

    int s4(int _offset) {
        int s4 = u4(_offset);
        return (s4);
    }

    ByteBuffer(byte[] _bytes) {
        bytes = _bytes;
    }

    int u1(int _offset) {
        return ((bytes[_offset] & 0xff));
    }

    int size() {
        return (bytes.length);
    }

    double d8(int _offset) {
        return (Double.longBitsToDouble(u8(_offset)));
    }

    float f4(int _offset) {
        return (Float.intBitsToFloat(u4(_offset)));
    }

    long u8(int _offset) {
        return (((long) u4(_offset)) << 32 | u4(_offset + 4));
    }

    int utf8bytes(int _offset) {
        return (2 + u2(_offset));
    }

    byte[] bytes(int _offset, int _length) {
        byte[] returnBytes = new byte[_length];
        System.arraycopy(bytes, _offset, returnBytes, 0, _length);
        return (returnBytes);
    }

    String utf8(int _offset) {
        int utflen = u2(_offset);
        _offset += 2;
        byte[] bytearr = new byte[utflen];
        char[] chararr = new char[utflen];
        int c, char2, char3;
        int count = 0;
        int chararr_count = 0;
        for (int i = 0; i < utflen; i++) {
            bytearr[i] = b(_offset + i);
        }
        _offset += utflen;
        while (count < utflen) {
            c = bytearr[count] & 0xff;
            if (c > 127) break;
            count++;
            chararr[chararr_count++] = (char) c;
        }
        while (count < utflen) {
            c = bytearr[count] & 0xff;
            switch(c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    count++;
                    chararr[chararr_count++] = (char) c;
                    break;
                case 12:
                case 13:
                    count += 2;
                    if (count > utflen) {
                        System.out.println("malformed input: partial character at end");
                        return (null);
                    }
                    char2 = bytearr[count - 1];
                    if ((char2 & 0xC0) != 0x80) {
                        System.out.println("malformed input around byte " + count);
                        return (null);
                    }
                    chararr[chararr_count++] = (char) (((c & 0x1F) << 6) | (char2 & 0x3F));
                    break;
                case 14:
                    count += 3;
                    if (count > utflen) {
                        System.out.println("malformed input: partial character at end");
                        return (null);
                    }
                    char2 = bytearr[count - 2];
                    char3 = bytearr[count - 1];
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80)) {
                        System.out.println("malformed input around byte " + (count - 1));
                        return (null);
                    }
                    chararr[chararr_count++] = (char) (((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
                    break;
                default:
                    System.out.println("malformed input around byte " + count);
                    return (null);
            }
        }
        String returnString = new String(chararr, 0, chararr_count);
        return (returnString);
    }

    byte b(int _offset) {
        return bytes[_offset];
    }
}
