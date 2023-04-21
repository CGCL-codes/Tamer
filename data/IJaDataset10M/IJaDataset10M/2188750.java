package com.lepidllama.packageeditor.fileio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import com.lepidllama.packageeditor.utility.StringUtils;

public class ByteArrayDataReader implements DataReader {

    byte[] data;

    int pos = 0;

    public ByteArrayDataReader(byte[] in) {
        data = in;
    }

    public ByteArrayDataReader(File inputFile) throws FileNotFoundException, IOException {
        InputStream in = new FileInputStream(inputFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int nextByte;
        while ((nextByte = in.read()) != -1) {
            baos.write(nextByte);
        }
        data = baos.toByteArray();
    }

    public int getLength() {
        return data.length;
    }

    /**
	 * Retrieve the entire data as a byte array. This is not part of the standard DataReader interface.
	 * @return the entire file as a byte array.
	 */
    public byte[] getData() {
        return data;
    }

    public int readDwordInt() {
        byte[] b = Arrays.copyOfRange(data, pos, pos + 4);
        pos += 4;
        int dword = (int) ((long) (b[3] & 0xFF) << 24 | (b[2] & 0xFF) << 16 | (b[1] & 0xFF) << 8 | (b[0] & 0xFF));
        return dword;
    }

    public long readDwordLong() {
        byte[] b = Arrays.copyOfRange(data, pos, pos + 4);
        pos += 4;
        long dword = (long) (b[3] & 0xFF) << 24 | (b[2] & 0xFF) << 16 | (b[1] & 0xFF) << 8 | (b[0] & 0xFF);
        return dword;
    }

    public int readWordInt() {
        byte[] b = Arrays.copyOfRange(data, pos, pos + 2);
        pos += 2;
        int word = (int) (b[1] & 0xFF) << 8 | (b[0] & 0xFF);
        return word;
    }

    public float readFloat() {
        byte[] b = Arrays.copyOfRange(data, pos, pos + 4);
        pos += 4;
        int dword = (b[3] & 0xFF) << 24 | (b[2] & 0xFF) << 16 | (b[1] & 0xFF) << 8 | (b[0] & 0xFF);
        return Float.intBitsToFloat(dword);
    }

    public String readDwordString() {
        byte[] b = Arrays.copyOfRange(data, pos, pos + 4);
        pos += 4;
        return StringUtils.getRawByteString(b);
    }

    public String read7BitString() {
        int length = StringUtils.read7BitStringLength(this);
        byte[] b = Arrays.copyOfRange(data, pos, pos + length);
        pos += length;
        return StringUtils.getRawByteString(b);
    }

    public String readFixedString(int bytes) {
        byte[] b = Arrays.copyOfRange(data, pos, pos + bytes);
        pos += bytes;
        return StringUtils.getRawByteString(b);
    }

    public byte[] readDwordBytes() {
        byte[] b = Arrays.copyOfRange(data, pos, pos + 4);
        pos += 4;
        return b;
    }

    public byte[] readChunk(int bytes) {
        byte[] b = Arrays.copyOfRange(data, pos, pos + bytes);
        pos += bytes;
        return b;
    }

    public String readLengthString() {
        int stringLength = readDwordInt();
        byte[] b = Arrays.copyOfRange(data, pos, pos + stringLength);
        pos += stringLength;
        return StringUtils.getRawByteString(b);
    }

    public byte readByte() {
        byte r = data[pos];
        pos += 1;
        return r;
    }

    public void seek(long pos) {
        this.pos = (int) pos;
    }

    public int readUnsignedByte() {
        int r = 0xFF & data[pos];
        pos += 1;
        return r;
    }

    public long getFilePointer() {
        return pos;
    }

    public String readNullTermString() {
        int c;
        StringBuffer sb = new StringBuffer();
        while ((c = data[pos]) != 0) {
            sb.append((char) c);
            pos++;
        }
        pos++;
        return sb.toString();
    }
}
