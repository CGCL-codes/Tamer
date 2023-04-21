package org.activision.io.cache;

import org.activision.io.InStream;

public class CacheFile {

    private int position;

    private int length;

    private InStream data;

    public CacheFile(int position, int length) {
        this.setPosition(position);
        this.setLength(length);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public void setData(InStream data) {
        this.data = data;
    }

    public InStream getData() {
        if (this.data == null) {
            this.setData(RS2Cache.getData().getData(position, length));
        }
        return data;
    }
}
