package net.sf.mustang.http;

public class MultipartProgress {

    long length = 0;

    long readed = 0;

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getReaded() {
        return readed;
    }

    public void setReaded(long readed) {
        this.readed = readed;
    }
}
