package javazoom.spi.mpeg.sampled.convert;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class RandomAudioInputStream extends InputStream {

    private RandomAccessFile file;

    private long pos;

    public RandomAudioInputStream(RandomAccessFile file) {
        this.file = file;
        this.pos = 0L;
    }

    public int read() throws IOException {
        int a = this.file.read();
        this.pos += 1L;
        return a;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (this.file == null) {
            return -1;
        }
        int r = this.file.read(b, off, len);
        this.pos += r;
        return r;
    }

    public int available() throws IOException {
        return (int) (this.file.length() - this.pos);
    }

    public long skip(long l) throws IOException {
        this.file.seek(l);
        this.pos = l;
        return l;
    }

    public void close() throws IOException {
        this.file.close();
    }
}
