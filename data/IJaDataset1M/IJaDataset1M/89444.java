package net.oesterholt.jndbm.datastruct;

import java.io.RandomAccessFile;
import net.oesterholt.jndbm.streams.NDbmDataInputStream;
import net.oesterholt.jndbm.streams.NDbmRandomAccessFile;

public class Bucket {

    private int _hash;

    private int _seek;

    private int _sizeOfBucket = NDbmDataInputStream.sizeOfInt() * 2;

    public int hash() {
        return _hash;
    }

    public int seek() {
        return _seek;
    }

    public void seek(int s) {
        _seek = s;
    }

    public void reset(NDbmRandomAccessFile fIdx) throws Exception {
        int s = _hash * _sizeOfBucket;
        if (fIdx.getFilePointer() != (s)) {
            fIdx.seek(s);
        }
        fIdx.writeInt(-1);
        fIdx.writeInt(-1);
    }

    public void writeNext(NDbmRandomAccessFile fIdx) throws Exception {
        fIdx.writeInt(_hash);
        fIdx.writeInt(_seek);
    }

    public void write(NDbmRandomAccessFile fIdx) throws Exception {
        int s = _hash * _sizeOfBucket;
        if (fIdx.getFilePointer() != (s)) {
            fIdx.seek(s);
        }
        fIdx.writeInt(_hash);
        fIdx.writeInt(_seek);
    }

    public void read(RandomAccessFile p) throws Exception {
    }

    public void set(int h, int s) {
        _hash = h;
        _seek = s;
    }

    public void set(NDbmRandomAccessFile f, int hash) throws Exception {
        int s = hash * _sizeOfBucket;
        if (f.getFilePointer() != s) {
            f.seek(s);
        }
        _hash = f.readInt();
        _seek = f.readInt();
    }

    public Bucket(int h, int s) {
        _hash = h;
        _seek = s;
    }

    public Bucket() {
        _hash = -1;
        _seek = -1;
    }

    public Bucket(RandomAccessFile f, int hash) throws Exception {
        int s = hash * _sizeOfBucket;
        if (f.getFilePointer() != s) {
            f.seek(s);
        }
        _hash = f.readInt();
        _seek = f.readInt();
    }
}
