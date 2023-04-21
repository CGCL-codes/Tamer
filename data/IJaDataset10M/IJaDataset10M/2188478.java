package org.apache.sanselan.common.byteSources;

import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.common.BinaryFileFunctions;

public abstract class ByteSource extends BinaryFileFunctions {

    protected final String filename;

    public ByteSource(final String filename) {
        this.filename = filename;
    }

    public final InputStream getInputStream(int start) throws IOException {
        InputStream is = getInputStream();
        skipBytes(is, start);
        return is;
    }

    public abstract InputStream getInputStream() throws IOException;

    public abstract byte[] getBlock(int start, int length) throws IOException;

    public abstract byte[] getAll() throws IOException;

    public abstract long getLength() throws IOException;

    public abstract String getDescription();

    public final String getFilename() {
        return filename;
    }
}
