package ch.comtools.jsch.jzlib;

final class Adler32 {

    private static final int BASE = 65521;

    private static final int NMAX = 5552;

    long adler32(long adler, byte[] buf, int index, int len) {
        if (buf == null) {
            return 1L;
        }
        long s1 = adler & 0xffff;
        long s2 = (adler >> 16) & 0xffff;
        int k;
        while (len > 0) {
            k = len < NMAX ? len : NMAX;
            len -= k;
            while (k >= 16) {
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                s1 += buf[index++] & 0xff;
                s2 += s1;
                k -= 16;
            }
            if (k != 0) {
                do {
                    s1 += buf[index++] & 0xff;
                    s2 += s1;
                } while (--k != 0);
            }
            s1 %= BASE;
            s2 %= BASE;
        }
        return (s2 << 16) | s1;
    }
}
