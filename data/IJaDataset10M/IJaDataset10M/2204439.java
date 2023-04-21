package trb.fps.util;

public class FloatList {

    private static final int DEFAULT_INITIAL_CAPACITY = 32;

    private float[] data;

    private int size = 0;

    public FloatList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public FloatList(int initialCapacity) {
        data = new float[Math.max(initialCapacity, DEFAULT_INITIAL_CAPACITY)];
    }

    public FloatList clear() {
        data = new float[DEFAULT_INITIAL_CAPACITY];
        size = 0;
        return this;
    }

    public FloatList append(float... floats) {
        ensureCapacity(size + floats.length);
        System.arraycopy(floats, 0, data, size, floats.length);
        size += floats.length;
        return this;
    }

    public void set(int i, float... fs) {
        ensureCapacity(i + fs.length);
        System.arraycopy(fs, 0, data, i, fs.length);
        size = Math.max(size, i + fs.length);
    }

    private void ensureCapacity(int newCapacity) {
        if (data.length >= newCapacity) {
            return;
        }
        int newDataLength = Math.max(data.length * 2, newCapacity);
        float[] newData = new float[newDataLength];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    public int size() {
        return size;
    }

    public float[] toArray() {
        if (data.length == size()) {
            return data.clone();
        }
        float[] floats = new float[size()];
        System.arraycopy(data, 0, floats, 0, size());
        return floats;
    }
}
