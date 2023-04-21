package net.sourceforge.jfilecrypt.algorithms;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author me
 */
public class BlockCipherInputStream extends InputStream {

    private Algorithm alg;

    private InputStream is;

    public BlockCipherInputStream(InputStream is, Algorithm alg) {
        this.is = is;
        this.alg = alg;
    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("BlockCipherInputStream does not support single byte reading.");
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return is.read(alg.decryptBytes(b), off, len);
    }
}
