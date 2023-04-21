package subsearch.index.util;

import java.util.Random;

public class Hashing {

    /**
	 * Multiplicative hashing, inefficient implementation
	 * due to lack of unsigned ints!
	 */
    public static int multHash(String str, int m) {
        final double A = (Math.sqrt(5) - 1) / 2;
        int k = str.hashCode();
        double hash = ((A * k) % 1) * m;
        return (int) Math.floor(Math.abs(hash));
    }

    /**
	 * Java HashMap hashing function.
	 * 
	 * Note: Works well only if m is power of 2!
	 */
    public static int hash(Object o, int m) {
        int k = o.hashCode();
        k ^= (k >>> 20) ^ (k >>> 12);
        k ^= (k >>> 7) ^ (k >>> 4);
        return k & (m - 1);
    }

    /**
	 * Hashing function used by CDK.
	 */
    public static int cdkHash(Object o, int m) {
        return new java.util.Random(o.hashCode()).nextInt(m);
    }

    /**
	 * CRC32 based hashing function.
	 */
    public static int crc32Hash(String s, int m) {
        long crc32 = crc32(s);
        return Math.abs((int) (crc32 % m));
    }

    /**
	 * CRC32+RNG based hashing function.
	 */
    public static int crc32RandomHash(String s, int m) {
        long crc32 = crc32(s);
        return new Random(crc32).nextInt(m);
    }

    public static int djb2Hash(String str, int m) {
        int hash = 5381;
        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) + hash) + str.charAt(i);
        }
        return Math.abs((int) (hash % m));
    }

    public static long crc32(String s) {
        java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
        crc32.update(s.getBytes());
        return crc32.getValue();
    }

    public static int DEKHash(String str, int m) {
        int hash = str.length();
        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
        }
        return Math.abs((int) (hash % m));
    }

    public static int ELFHash(String str, int m) {
        long hash = 0;
        long x = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash << 4) + str.charAt(i);
            if ((x = hash & 0xF0000000L) != 0) {
                hash ^= (x >> 24);
            }
            hash &= ~x;
        }
        return Math.abs((int) (hash % m));
    }

    public static int SDBMHash(String str, int m) {
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
        }
        return Math.abs((int) (hash % m));
    }
}
