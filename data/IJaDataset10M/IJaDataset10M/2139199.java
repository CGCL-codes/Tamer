package org.dcm4che2.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Revision: 5542 $ $Date: 2007-11-26 08:16:23 -0500 (Mon, 26 Nov 2007) $
 * @since Jun 25, 2005
 *
 */
class RAFInputStreamAdapter extends InputStream {

    private final RandomAccessFile raf;

    private long markedPos;

    private IOException markException;

    public RAFInputStreamAdapter(RandomAccessFile raf) {
        this.raf = raf;
    }

    @Override
    public int read() throws IOException {
        return raf.read();
    }

    @Override
    public synchronized void mark(int readlimit) {
        try {
            this.markedPos = raf.getFilePointer();
            this.markException = null;
        } catch (IOException e) {
            this.markException = e;
        }
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return raf.read(b, off, len);
    }

    @Override
    public synchronized void reset() throws IOException {
        if (markException != null) throw markException;
        raf.seek(markedPos);
    }

    @Override
    public long skip(long n) throws IOException {
        return raf.skipBytes((int) n);
    }
}
