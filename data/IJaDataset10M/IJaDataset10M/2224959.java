package gnu.javax.net.ssl.provider;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.IOException;
import gnu.java.security.hash.IMessageDigest;

final class DigestInputStream extends FilterInputStream {

    private IMessageDigest md5, sha;

    private boolean digesting;

    DigestInputStream(InputStream in, IMessageDigest md5, IMessageDigest sha) {
        super(in);
        if (md5 == null || sha == null) throw new NullPointerException();
        this.md5 = md5;
        this.sha = sha;
        digesting = true;
    }

    void setDigesting(boolean digesting) {
        this.digesting = digesting;
    }

    public int read() throws IOException {
        int i = in.read();
        if (digesting && i != -1) {
            md5.update((byte) i);
            sha.update((byte) i);
        }
        return i;
    }

    public int read(byte[] buf) throws IOException {
        return read(buf, 0, buf.length);
    }

    public int read(byte[] buf, int off, int len) throws IOException {
        int ret = in.read(buf, off, len);
        if (digesting && ret != -1) {
            md5.update(buf, off, ret);
            sha.update(buf, off, ret);
        }
        return ret;
    }
}
