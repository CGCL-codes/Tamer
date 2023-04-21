package nl.headspring.photoz.common;

import java.io.Serializable;
import java.util.zip.Checksum;

/**
 * Class StoredChecksum.
 *
 * @author Eelco Sommer
 * @since Nov 1, 2010
 */
public class StoredChecksum implements Checksum, Serializable {

    private long value;

    public StoredChecksum(long value) {
        this.value = value;
    }

    public void update(int i) {
        throw new UnsupportedOperationException();
    }

    public void update(byte[] bytes, int i, int i1) {
        throw new UnsupportedOperationException();
    }

    public long getValue() {
        return value;
    }

    public void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "StoredChecksum{" + "value=" + value + '}';
    }
}
