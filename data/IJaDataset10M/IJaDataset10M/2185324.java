package org.tranche.hash;

import java.nio.ByteBuffer;
import org.bouncycastle.crypto.digests.*;

/**
 * Class that actually creates the BigHash used for identifying files/projects. 
 * @author Jayson Falkner - jfalkner@umich.edu
 */
public final class BigHashMaker {

    private MD5Digest md5Digest = new MD5Digest();

    private SHA1Digest sha1Digest = new SHA1Digest();

    private SHA256Digest sha256Digest = new SHA256Digest();

    private long totalLength = 0;

    /**
     * Update BigHash with a given array of bytes at a particular offset and 
     * length.
     * @param bytes
     * @param offset
     * @param length
     */
    public final void update(byte[] bytes, int offset, int length) {
        md5Digest.update(bytes, offset, length);
        sha1Digest.update(bytes, offset, length);
        sha256Digest.update(bytes, offset, length);
        totalLength += length;
    }

    /**
     * Create the BigHash return bytes of the BigHash.
     * @return
     */
    public final byte[] finish() {
        byte[] bytes = new byte[BigHash.HASH_LENGTH];
        md5Digest.doFinal(bytes, BigHash.MD5_OFFSET);
        sha1Digest.doFinal(bytes, BigHash.SHA1_OFFSET);
        sha256Digest.doFinal(bytes, BigHash.SHA256_OFFSET);
        ByteBuffer bb = ByteBuffer.wrap(bytes, BigHash.LENGTH_OFFSET, BigHash.LENGTH_LENGTH);
        bb.putLong(totalLength);
        return bytes;
    }
}
