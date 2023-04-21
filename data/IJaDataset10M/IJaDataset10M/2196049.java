package ca.uwaterloo.crysp.otr.crypt.rim;

import ca.uwaterloo.crysp.otr.crypt.OTRCryptException;
import net.rim.device.api.crypto.*;

/**
 * The SHA-1 hash algorithm, as implemented by RIM
 * 
 * @author Can Tang <c24tang@gmail.com>
 */
public class RIMSHA1 extends ca.uwaterloo.crysp.otr.crypt.SHA1 {

    SHA1Digest sha;

    public RIMSHA1() {
        super();
        sha = new SHA1Digest();
    }

    public byte[] hash() throws OTRCryptException {
        byte[] ret = new byte[sha.getDigestLength()];
        sha.getDigest(ret, 0, true);
        return ret;
    }

    public void update(byte[] data) {
        sha.update(data);
    }

    public void update(byte[] data, int offset, int length) throws OTRCryptException {
        sha.update(data, offset, length);
    }

    public byte[] hash(byte[] data) throws OTRCryptException {
        sha.update(data);
        byte[] ret = new byte[sha.getDigestLength()];
        sha.getDigest(ret, 0, true);
        return ret;
    }

    public byte[] hash(byte[] data, int offset, int length) throws OTRCryptException {
        sha.update(data, offset, length);
        byte[] ret = new byte[sha.getDigestLength()];
        sha.getDigest(ret, 0, true);
        return ret;
    }

    public boolean verify(byte[] digest, byte[] data) throws OTRCryptException {
        sha.update(data);
        byte[] ret = new byte[sha.getDigestLength()];
        sha.getDigest(ret, 0, true);
        return ca.uwaterloo.crysp.otr.Util.arrayEquals(ret, digest);
    }

    public boolean verify(byte[] digest, byte[] data, int offset, int length) throws OTRCryptException {
        sha.update(data, offset, length);
        byte[] ret = new byte[sha.getDigestLength()];
        sha.getDigest(ret, 0, true);
        return ca.uwaterloo.crysp.otr.Util.arrayEquals(ret, digest);
    }

    public String toString() {
        return sha.toString();
    }

    public static byte[] fromHex(byte[] msg) {
        byte[] ret = new byte[msg.length / 2];
        for (int i = 0; i < msg.length; i++) {
            if (msg[i] <= 57) msg[i] -= 48; else msg[i] -= 87;
            if (i % 2 == 0) ret[i / 2] += (msg[i] << 4); else ret[i / 2] += msg[i];
        }
        return ret;
    }
}
