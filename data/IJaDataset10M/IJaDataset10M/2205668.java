package Main;

public class ByteBuffer {

    byte[] data = null;

    private int lenght = 0;

    private int pointer = 0;

    public ByteBuffer(int n) {
        data = new byte[n];
        lenght = n;
    }

    public int getLenght() {
        return lenght;
    }

    public void write(int val) {
        data[pointer++] = (byte) (val >>> 24);
        data[pointer++] = (byte) (val >>> 16);
        data[pointer++] = (byte) (val >>> 8);
        data[pointer++] = (byte) (val);
    }

    public int readInt() {
        return (data[pointer++] << 24) + ((data[pointer++] & 0xFF) << 16) + ((data[pointer++] & 0xFF) << 8) + (data[pointer++] & 0xFF);
    }

    public void write(byte[] bytes, int len) {
        int i, n = len + pointer;
        for (i = 0; i < len; i++, pointer++) {
            data[pointer] = bytes[i];
        }
    }

    public void setPointer(int p) {
        pointer = p;
    }

    public int getPointer() {
        return pointer;
    }

    public void reset() {
        pointer = 0;
    }

    public void add(ByteBuffer bb) {
    }

    public byte[] getBytes() {
        return data;
    }

    public void destroy() {
        data = null;
        System.gc();
    }
}
