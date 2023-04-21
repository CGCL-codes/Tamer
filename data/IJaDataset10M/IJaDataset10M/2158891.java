package enigma.crypto.cipher.rc5;

import enigma.crypto.cipher.Decipheror;
import enigma.util.Bytes;

public class RC564Decipheror extends RC564Cipher implements Decipheror {

    private static long rotr64(long a, int n) {
        return (a >>> n) | (a << (64 - n));
    }

    public RC564Decipheror(byte[] key) {
        super(key);
    }

    public RC564Decipheror(int nRound, byte[] key) {
        super(nRound, key);
    }

    public void decrypt(byte[] src, int srcPos, byte[] dest, int destPos) {
        long a = Bytes.toLong(src, srcPos, Bytes.LITTLE_ENDIAN);
        long b = Bytes.toLong(src, srcPos + 8, Bytes.LITTLE_ENDIAN);
        for (int i = s.length - 2; i >= 2; i -= 2) {
            b -= s[i + 1];
            b = rotr64(b, (int) a & 0x3f);
            b ^= a;
            a -= s[i];
            a = rotr64(a, (int) b & 0x3f);
            a ^= b;
        }
        b -= s[1];
        a -= s[0];
        Bytes.setLong(a, dest, destPos, Bytes.LITTLE_ENDIAN);
        Bytes.setLong(b, dest, destPos + 8, Bytes.LITTLE_ENDIAN);
    }
}
