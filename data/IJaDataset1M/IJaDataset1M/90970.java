package de.huxhorn.sulky.codec.filebuffer;

import java.io.IOException;
import java.io.RandomAccessFile;

public interface IndexStrategy {

    void setOffset(RandomAccessFile indexFile, long index, long offset) throws IOException;

    long getOffset(RandomAccessFile indexFile, long index) throws IOException;

    long getSize(RandomAccessFile indexFile) throws IOException;
}
