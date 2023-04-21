package com.tencent.tendon.convert.json;

/**
 * JsonBuilder 读取key或者字符串value时用到的类， 主要替换StringBuilder
 *
 * @author nbzhang
 */
public final class JsonCharArray implements JsonPoolable {

    private transient char[] data;

    private int size;

    public JsonCharArray() {
        this(64);
    }

    public JsonCharArray(int len) {
        this.data = new char[len];
    }

    public JsonCharArray(char... chs) {
        this.data = chs;
        this.size = chs.length;
    }

    private void expand(int offset) {
        char[] newdata = new char[data.length + offset];
        System.arraycopy(data, 0, newdata, 0, size);
        this.data = newdata;
    }

    public final char get(int index) {
        return this.data[index];
    }

    public final void append(char e) {
        if (size + 1 >= data.length) expand(16);
        data[size++] = e;
    }

    public final void append(char[] values, int start, int len) {
        if (size + len >= data.length) expand(len + 16);
        System.arraycopy(values, start, data, size, len);
        this.size += len;
    }

    public final char[] toArray() {
        if (size == data.length) return data;
        char[] newdata = new char[size];
        System.arraycopy(data, 0, newdata, 0, size);
        return newdata;
    }

    public final int size() {
        return this.size;
    }

    @Override
    public final String toString() {
        return new String(data, 0, size);
    }

    @Override
    public final void prepare() {
        this.size = 0;
    }

    @Override
    public final void release() {
        this.size = 0;
        if (this.data.length > 64) {
            this.data = new char[64];
        }
    }

    public final void clear() {
        this.size = 0;
    }
}
