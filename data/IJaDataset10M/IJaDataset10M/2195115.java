package org.nakedobjects.metamodel.encoding;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;

public class DataOutputExtendedDecorator implements DataOutputExtended {

    private final DataOutputExtended underlying;

    public DataOutputExtendedDecorator(DataOutputExtended underlying) {
        this.underlying = underlying;
    }

    public DataOutputStream getDataOutputStream() {
        return underlying.getDataOutputStream();
    }

    public void writeBoolean(boolean v) throws IOException {
        underlying.writeBoolean(v);
    }

    public void writeBooleans(boolean[] booleans) throws IOException {
        underlying.writeBooleans(booleans);
    }

    public void writeChar(int v) throws IOException {
        underlying.writeChar(v);
    }

    public void writeChars(char[] chars) throws IOException {
        underlying.writeChars(chars);
    }

    public void write(int b) throws IOException {
        underlying.write(b);
    }

    public void writeByte(int v) throws IOException {
        underlying.writeByte(v);
    }

    public void write(byte[] b) throws IOException {
        underlying.write(b);
    }

    public void writeBytes(byte[] bytes) throws IOException {
        underlying.writeBytes(bytes);
    }

    public void writeShort(int v) throws IOException {
        underlying.writeShort(v);
    }

    public void writeShorts(short[] shorts) throws IOException {
        underlying.writeShorts(shorts);
    }

    public void writeInt(int v) throws IOException {
        underlying.writeInt(v);
    }

    public void writeInts(int[] ints) throws IOException {
        underlying.writeInts(ints);
    }

    public void writeLong(long v) throws IOException {
        underlying.writeLong(v);
    }

    public void writeLongs(long[] longs) throws IOException {
        underlying.writeLongs(longs);
    }

    public void writeFloat(float v) throws IOException {
        underlying.writeFloat(v);
    }

    public void writeFloats(float[] floats) throws IOException {
        underlying.writeFloats(floats);
    }

    public void writeDouble(double v) throws IOException {
        underlying.writeDouble(v);
    }

    public void writeDoubles(double[] doubles) throws IOException {
        underlying.writeDoubles(doubles);
    }

    public void writeUTF(String str) throws IOException {
        underlying.writeUTF(str);
    }

    public void writeUTFs(String[] strings) throws IOException {
        underlying.writeUTFs(strings);
    }

    public void writeEncodable(Object encodable) throws IOException {
        underlying.writeEncodable(encodable);
    }

    public void writeEncodables(Object[] encodables) throws IOException {
        underlying.writeEncodables(encodables);
    }

    public void writeSerializable(Object serializable) throws IOException {
        underlying.writeSerializable(serializable);
    }

    public void writeSerializables(Object[] serializables) throws IOException {
        underlying.writeSerializables(serializables);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        underlying.write(b, off, len);
    }

    public void writeBytes(String s) throws IOException {
        underlying.writeBytes(s);
    }

    public void writeChars(String s) throws IOException {
        underlying.writeChars(s);
    }

    public void flush() throws IOException {
        underlying.flush();
    }
}
