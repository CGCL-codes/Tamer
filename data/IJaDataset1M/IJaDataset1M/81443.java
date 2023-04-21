package org.apache.hadoop.fs.sftp;

import java.io.IOException;
import java.io.InputStream;
import org.apache.hadoop.fs.FSInputStream;
import org.apache.hadoop.fs.FileSystem;
import com.jcraft.jsch.ChannelSftp;

class SFTPInputStream extends FSInputStream {

    private InputStream wrappedStream;

    private ChannelSftp client;

    private FileSystem.Statistics stats;

    private boolean closed;

    private long pos;

    public SFTPInputStream(InputStream stream, ChannelSftp client, FileSystem.Statistics stats) {
        if (stream == null) {
            throw new IllegalArgumentException("Null InputStream");
        }
        if (client == null || !client.isConnected()) {
            throw new IllegalArgumentException("FTP client null or not connected");
        }
        this.wrappedStream = stream;
        this.client = client;
        this.stats = stats;
        this.pos = 0;
        this.closed = false;
    }

    public long getPos() throws IOException {
        return pos;
    }

    public void seek(long targetPos) throws IOException {
        throw new IOException("Seek not supported");
    }

    public boolean seekToNewSource(long targetPos) throws IOException {
        throw new IOException("Seek not supported");
    }

    public synchronized int read() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
        int byteRead = wrappedStream.read();
        if (byteRead >= 0) {
            pos++;
        }
        if (stats != null & byteRead >= 0) {
            stats.incrementBytesRead(1);
        }
        return byteRead;
    }

    public synchronized int read(byte buf[], int off, int len) throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
        int result = wrappedStream.read(buf, off, len);
        if (result > 0) {
            pos += result;
        }
        if (stats != null & result > 0) {
            stats.incrementBytesRead(result);
        }
        return result;
    }

    public synchronized void close() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
        super.close();
        closed = true;
        if (!client.isConnected()) {
            throw new IOException("Client not connected");
        }
        client.disconnect();
    }

    public boolean markSupported() {
        return false;
    }

    public void mark(int readLimit) {
    }

    public void reset() throws IOException {
        throw new IOException("Mark not supported");
    }

    public int read(long arg0, byte[] arg1, int arg2, int arg3) throws IOException {
        if (true) {
            throw new IOException("Unsopported");
        }
        return 0;
    }
}
