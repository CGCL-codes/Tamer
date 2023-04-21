package org.vfsutils.content;

import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.provider.AbstractRandomAccessStreamContent;
import org.apache.commons.vfs.util.RandomAccessMode;

/**
 * Class that offers an RandomAccessContent for file systems that do not support
 * it natively.
 * @author kleij - at - users.sourceforge.net
 *
 */
public class PseudoRandomAccessContent extends AbstractRandomAccessStreamContent {

    private long filePointer = 0;

    private FileObject file;

    private InputStream mis;

    private DataInputStream dis;

    public PseudoRandomAccessContent(FileObject file, RandomAccessMode mode) {
        super(mode);
        this.file = file;
    }

    protected DataInputStream getDataInputStream() throws IOException {
        if (dis != null) {
            return dis;
        }
        this.mis = file.getContent().getInputStream();
        this.mis.skip(filePointer);
        dis = new DataInputStream(new FilterInputStream(mis) {

            public int read() throws IOException {
                int ret = in.read();
                if (ret > -1) {
                    filePointer++;
                }
                return ret;
            }

            public int read(byte[] b) throws IOException {
                int ret = in.read(b);
                if (ret > -1) {
                    filePointer += ret;
                }
                return ret;
            }

            public int read(byte[] b, int off, int len) throws IOException {
                int ret = in.read(b, off, len);
                if (ret > -1) {
                    filePointer += ret;
                }
                return ret;
            }

            public void close() throws IOException {
                if (mis != null) {
                    mis.close();
                    mis = null;
                }
            }
        });
        return dis;
    }

    public void close() throws IOException {
        if (dis != null) {
            try {
                dis.close();
            } catch (IOException e) {
            }
        }
    }

    public long getFilePointer() throws IOException {
        return filePointer;
    }

    public long length() throws IOException {
        return file.getContent().getSize();
    }

    public void seek(long pos) throws IOException {
        if (pos == filePointer) {
            return;
        } else if (pos > filePointer) {
            long available = dis.available();
            long readAhead = pos - filePointer;
            if (dis != null && available >= readAhead) {
                dis.skip(readAhead);
            } else if (dis != null && available > 0) {
                dis.skip(dis.available());
            }
            return;
        }
        if (pos < 0) {
            throw new FileSystemException("vfs.provider/random-access-invalid-position.error", new Object[] { new Long(pos) });
        }
        if (dis != null) {
            close();
        }
        filePointer = pos;
    }
}
