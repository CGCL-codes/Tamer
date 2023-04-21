package org.magnesia.misc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class StreamWrapper {

    private InputStream is;

    private OutputStream os;

    public StreamWrapper(Socket s) {
        try {
            this.is = s.getInputStream();
            this.os = s.getOutputStream();
        } catch (IOException e) {
            throw new WrapperException("Error while initializing streams!", e);
        }
        if (is == null || os == null) throw new WrapperException("How shall we work without a stream?");
    }

    public void close() {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeOpCode(short code) throws WrapperException {
        if (code < 0) throw new WrapperException("No negative OPCodes allowed!");
        byte b[] = new byte[2];
        b[1] = (byte) (code);
        b[0] = (byte) (code >> 8);
        writeSingleUnchecked(b);
    }

    public short readOpCode() throws WrapperException {
        byte[] b = new byte[2];
        read(b);
        short code = (short) convertToValue(b);
        System.out.println("Read opcode " + code);
        if (code < 0) throw new WrapperException("No negative OPCodes allowed!");
        return code;
    }

    public void writeDataFully(byte[] data) throws WrapperException {
        writeUnchecked(data);
    }

    public void writeData(byte[] data) throws WrapperException {
        writeData(data, 0, data.length);
    }

    public void writeData(byte[] data, int count) throws WrapperException {
        writeData(data, 0, count);
    }

    public void writeData(byte[] data, int start, int count) throws WrapperException {
        if (data.length == 0 || count == 0) throw new WrapperException("No data!");
        if (data.length < count) throw new WrapperException("data.length < count!");
        if (start < 0 || start >= data.length || start + count > data.length) throw new WrapperException("Invalid value for start");
        writeSingleUnchecked(data, start, count);
    }

    public byte[] readDataFully() throws WrapperException {
        int toRead = readInt();
        System.out.println("Should read " + toRead + " bytes");
        byte buf[] = new byte[org.magnesia.Constants.CHUNK_SIZE];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int read = 0;
        while (read >= 0 && toRead > 0) {
            read = readData(buf, ((toRead >= buf.length) ? buf.length : toRead));
            toRead -= read;
            bos.write(buf, 0, read);
        }
        return bos.toByteArray();
    }

    public int readData(byte[] data) throws WrapperException {
        return readData(data, data.length);
    }

    public int readData(byte[] data, int count) throws WrapperException {
        byte[] chunk = null;
        if (count < data.length) {
            chunk = new byte[count];
        } else {
            chunk = data;
        }
        int r = 0;
        try {
            r = is.read(chunk);
        } catch (IOException e) {
            throw new WrapperException("Error while reading from stream!", e);
        }
        if (data.length != chunk.length) {
            System.arraycopy(chunk, 0, data, 0, r);
        }
        return r;
    }

    public void writeString(String str) throws WrapperException {
        try {
            System.out.println("Writing string " + str + " with length " + str.length() + "/" + str.getBytes("UTF-8").length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeUnchecked(str.getBytes());
    }

    public String readString() throws WrapperException {
        byte[] str = readDataFully();
        System.out.println("Read string " + new String(str) + " from stream");
        return new String(str);
    }

    public void writeInt(int val) throws WrapperException {
        byte[] b = new byte[4];
        b[3] = (byte) (val);
        b[2] = (byte) (val >> 8);
        b[1] = (byte) (val >> 16);
        b[0] = (byte) (val >> 24);
        writeSingleUnchecked(b);
    }

    public int readInt() throws WrapperException {
        byte[] length = new byte[4];
        read(length);
        return (int) convertToValue(length);
    }

    public void writeLong(long val) throws WrapperException {
        byte[] b = new byte[8];
        b[7] = (byte) (val);
        b[6] = (byte) (val >> 8);
        b[5] = (byte) (val >> 16);
        b[4] = (byte) (val >> 24);
        b[3] = (byte) (val >> 32);
        b[2] = (byte) (val >> 40);
        b[1] = (byte) (val >> 48);
        b[0] = (byte) (val >> 56);
        writeSingleUnchecked(b);
    }

    public long readLong() throws WrapperException {
        byte[] length = new byte[8];
        read(length);
        return convertToValue(length);
    }

    public void writeBoolean(boolean b) throws WrapperException {
        writeSingleUnchecked(new byte[] { (byte) (b ? 1 : 0) });
    }

    public boolean readBoolean() throws WrapperException {
        byte[] length = new byte[1];
        read(length);
        return convertToValue(length) == 1 ? true : false;
    }

    private void read(byte[] buffer) {
        try {
            is.read(buffer);
        } catch (IOException e) {
            throw new WrapperException("Error while reading from stream!", e);
        }
    }

    private void writeSingleUnchecked(byte[] data) {
        writeSingleUnchecked(data, 0, data.length);
    }

    private void writeSingleUnchecked(byte[] data, int start, int count) {
        System.out.println("Writing " + count + " bytes to stream");
        try {
            os.write(data, start, count);
        } catch (IOException io) {
            throw new WrapperException("Error while writing to stream!", io);
        }
    }

    private void writeUnchecked(byte[] data) {
        writeInt(data.length);
        writeSingleUnchecked(data);
    }

    private long convertToValue(byte[] data) {
        if (data.length > 8) return -1;
        int value = 0;
        for (int i = 0; i < data.length; i++) {
            int t = 0xFF & data[i];
            value |= (t << ((data.length - 1 - i) * 8));
        }
        return value;
    }
}
