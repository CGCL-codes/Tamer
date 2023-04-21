package de.herberlin.wwwutil;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream reader for the chunked content encoding.
 *
 * @author Hans Joachim Herbertz
 * @created 07.12.2002
 */
public class ChunkedInputStream extends FilterInputStream {

    private ContentLengthInputStream clIn = null;

    /**
	 * We store if the chunked input stream if exhausted,
	 * for the client might ty to ignore -1 return value
	 * or might not check for array length returned. */
    private boolean alreadyExhausted = false;

    /**
	 * Flag storing if the chunk part is parsed for the first
	 * time so no leading whitespace chars are expected. */
    private boolean isNew = true;

    /**
	 * Constructor for ChunkedInputStream.
	 * @param in The chunked stream to parse
	 */
    public ChunkedInputStream(InputStream in) {
        super(in);
        clIn = new ContentLengthInputStream(in, 0);
    }

    /** 
	 * Must be called when a chunked part is expected.
	 * Parses the input stream for the coming content 
	 * length and returns it. */
    private long parseChunked() throws IOException {
        int b = -1;
        if (alreadyExhausted) return 0;
        if (!isNew) {
            b = in.read();
            b = in.read();
        }
        isNew = false;
        ByteArrayOutputStream bst = new ByteArrayOutputStream();
        do {
            b = in.read();
            if (b > 13) {
                bst.write(b);
            } else {
                break;
            }
        } while (true);
        b = in.read();
        String t = new String(bst.toByteArray()).trim();
        b = Integer.parseInt(t, 16);
        if (b == 0) alreadyExhausted = true;
        return b;
    }

    /**
	 * @see java.io.InputStream#read()
	 */
    public int read() throws IOException {
        int ret = clIn.read();
        if (ret == -1) {
            clIn = new ContentLengthInputStream(in, parseChunked());
            ret = clIn.read();
        }
        return ret;
    }

    /**
	 * @see java.io.InputStream#read(byte[])
	 */
    public int read(byte[] b) throws IOException {
        for (int i = 0; i < b.length; i++) {
            int current = read();
            if (current == -1) {
                if (i == 0) return -1; else return i;
            } else {
                b[i] = (byte) current;
            }
        }
        return b.length;
    }

    /**
	 * @see java.io.InputStream#close()
	 */
    public void close() throws IOException {
        super.close();
        clIn.close();
    }

    /**
	 * This method is not implemented. */
    public int read(byte[] b, int off, int len) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Method not allowed.");
    }

    /**
	 * This method is not implemented. */
    public long skip(long l) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Method not allowed.");
    }

    /**
	 * This method is not implemented. */
    public void reset() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Method not allowed.");
    }
}
