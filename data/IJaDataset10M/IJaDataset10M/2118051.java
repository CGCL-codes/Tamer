package de.avetana.javax.obex;

public class MD5 {

    private static long S11 = 7L;

    private static long S12 = 12L;

    private static long S13 = 17L;

    private static long S14 = 22L;

    private static long S21 = 5L;

    private static long S22 = 9L;

    private static long S23 = 14L;

    private static long S24 = 20L;

    private static long S31 = 4L;

    private static long S32 = 11L;

    private static long S33 = 16L;

    private static long S34 = 23L;

    private static long S41 = 6L;

    private static long S42 = 10L;

    private static long S43 = 15L;

    private static long S44 = 21L;

    private static char pad[] = { 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    private char bytBuffer[] = new char[64];

    private long lngState[] = new long[4];

    private long lngByteCount = 0;

    public MD5() {
        this.init();
    }

    private static long[] decode(char bytBlock[]) {
        long lngBlock[] = new long[16];
        int j = 0;
        for (int i = 0; i < bytBlock.length; i += 4) {
            lngBlock[j++] = bytBlock[i] + bytBlock[i + 1] * 256L + bytBlock[i + 2] * 65536L + bytBlock[i + 3] * 16777216L;
        }
        return (lngBlock);
    }

    private static void transform(long lngState[], char bytBlock[]) {
        long lngA = lngState[0];
        long lngB = lngState[1];
        long lngC = lngState[2];
        long lngD = lngState[3];
        long x[] = new long[16];
        x = decode(bytBlock);
        lngA = ff(lngA, lngB, lngC, lngD, x[0], S11, 0xd76aa478L);
        lngD = ff(lngD, lngA, lngB, lngC, x[1], S12, 0xe8c7b756L);
        lngC = ff(lngC, lngD, lngA, lngB, x[2], S13, 0x242070dbL);
        lngB = ff(lngB, lngC, lngD, lngA, x[3], S14, 0xc1bdceeeL);
        lngA = ff(lngA, lngB, lngC, lngD, x[4], S11, 0xf57c0fafL);
        lngD = ff(lngD, lngA, lngB, lngC, x[5], S12, 0x4787c62aL);
        lngC = ff(lngC, lngD, lngA, lngB, x[6], S13, 0xa8304613L);
        lngB = ff(lngB, lngC, lngD, lngA, x[7], S14, 0xfd469501L);
        lngA = ff(lngA, lngB, lngC, lngD, x[8], S11, 0x698098d8L);
        lngD = ff(lngD, lngA, lngB, lngC, x[9], S12, 0x8b44f7afL);
        lngC = ff(lngC, lngD, lngA, lngB, x[10], S13, 0xffff5bb1L);
        lngB = ff(lngB, lngC, lngD, lngA, x[11], S14, 0x895cd7beL);
        lngA = ff(lngA, lngB, lngC, lngD, x[12], S11, 0x6b901122L);
        lngD = ff(lngD, lngA, lngB, lngC, x[13], S12, 0xfd987193L);
        lngC = ff(lngC, lngD, lngA, lngB, x[14], S13, 0xa679438eL);
        lngB = ff(lngB, lngC, lngD, lngA, x[15], S14, 0x49b40821L);
        lngA = gg(lngA, lngB, lngC, lngD, x[1], S21, 0xf61e2562L);
        lngD = gg(lngD, lngA, lngB, lngC, x[6], S22, 0xc040b340L);
        lngC = gg(lngC, lngD, lngA, lngB, x[11], S23, 0x265e5a51L);
        lngB = gg(lngB, lngC, lngD, lngA, x[0], S24, 0xe9b6c7aaL);
        lngA = gg(lngA, lngB, lngC, lngD, x[5], S21, 0xd62f105dL);
        lngD = gg(lngD, lngA, lngB, lngC, x[10], S22, 0x2441453L);
        lngC = gg(lngC, lngD, lngA, lngB, x[15], S23, 0xd8a1e681L);
        lngB = gg(lngB, lngC, lngD, lngA, x[4], S24, 0xe7d3fbc8L);
        lngA = gg(lngA, lngB, lngC, lngD, x[9], S21, 0x21e1cde6L);
        lngD = gg(lngD, lngA, lngB, lngC, x[14], S22, 0xc33707d6L);
        lngC = gg(lngC, lngD, lngA, lngB, x[3], S23, 0xf4d50d87L);
        lngB = gg(lngB, lngC, lngD, lngA, x[8], S24, 0x455a14edL);
        lngA = gg(lngA, lngB, lngC, lngD, x[13], S21, 0xa9e3e905L);
        lngD = gg(lngD, lngA, lngB, lngC, x[2], S22, 0xfcefa3f8L);
        lngC = gg(lngC, lngD, lngA, lngB, x[7], S23, 0x676f02d9L);
        lngB = gg(lngB, lngC, lngD, lngA, x[12], S24, 0x8d2a4c8aL);
        lngA = hh(lngA, lngB, lngC, lngD, x[5], S31, 0xfffa3942L);
        lngD = hh(lngD, lngA, lngB, lngC, x[8], S32, 0x8771f681L);
        lngC = hh(lngC, lngD, lngA, lngB, x[11], S33, 0x6d9d6122L);
        lngB = hh(lngB, lngC, lngD, lngA, x[14], S34, 0xfde5380cL);
        lngA = hh(lngA, lngB, lngC, lngD, x[1], S31, 0xa4beea44L);
        lngD = hh(lngD, lngA, lngB, lngC, x[4], S32, 0x4bdecfa9L);
        lngC = hh(lngC, lngD, lngA, lngB, x[7], S33, 0xf6bb4b60L);
        lngB = hh(lngB, lngC, lngD, lngA, x[10], S34, 0xbebfbc70L);
        lngA = hh(lngA, lngB, lngC, lngD, x[13], S31, 0x289b7ec6L);
        lngD = hh(lngD, lngA, lngB, lngC, x[0], S32, 0xeaa127faL);
        lngC = hh(lngC, lngD, lngA, lngB, x[3], S33, 0xd4ef3085L);
        lngB = hh(lngB, lngC, lngD, lngA, x[6], S34, 0x4881d05L);
        lngA = hh(lngA, lngB, lngC, lngD, x[9], S31, 0xd9d4d039L);
        lngD = hh(lngD, lngA, lngB, lngC, x[12], S32, 0xe6db99e5L);
        lngC = hh(lngC, lngD, lngA, lngB, x[15], S33, 0x1fa27cf8L);
        lngB = hh(lngB, lngC, lngD, lngA, x[2], S34, 0xc4ac5665L);
        lngA = ii(lngA, lngB, lngC, lngD, x[0], S41, 0xf4292244L);
        lngD = ii(lngD, lngA, lngB, lngC, x[7], S42, 0x432aff97L);
        lngC = ii(lngC, lngD, lngA, lngB, x[14], S43, 0xab9423a7L);
        lngB = ii(lngB, lngC, lngD, lngA, x[5], S44, 0xfc93a039L);
        lngA = ii(lngA, lngB, lngC, lngD, x[12], S41, 0x655b59c3L);
        lngD = ii(lngD, lngA, lngB, lngC, x[3], S42, 0x8f0ccc92L);
        lngC = ii(lngC, lngD, lngA, lngB, x[10], S43, 0xffeff47dL);
        lngB = ii(lngB, lngC, lngD, lngA, x[1], S44, 0x85845dd1L);
        lngA = ii(lngA, lngB, lngC, lngD, x[8], S41, 0x6fa87e4fL);
        lngD = ii(lngD, lngA, lngB, lngC, x[15], S42, 0xfe2ce6e0L);
        lngC = ii(lngC, lngD, lngA, lngB, x[6], S43, 0xa3014314L);
        lngB = ii(lngB, lngC, lngD, lngA, x[13], S44, 0x4e0811a1L);
        lngA = ii(lngA, lngB, lngC, lngD, x[4], S41, 0xf7537e82L);
        lngD = ii(lngD, lngA, lngB, lngC, x[11], S42, 0xbd3af235L);
        lngC = ii(lngC, lngD, lngA, lngB, x[2], S43, 0x2ad7d2bbL);
        lngB = ii(lngB, lngC, lngD, lngA, x[9], S44, 0xeb86d391L);
        lngState[0] = (lngState[0] + lngA) & 0xFFFFFFFFL;
        lngState[1] = (lngState[1] + lngB) & 0xFFFFFFFFL;
        lngState[2] = (lngState[2] + lngC) & 0xFFFFFFFFL;
        lngState[3] = (lngState[3] + lngD) & 0xFFFFFFFFL;
        x = decode(pad);
    }

    private static long ff(long lngA, long lngB, long lngC, long lngD, long lngX, long lngS, long lngAC) {
        lngA = (lngA + (lngB & lngC | (~lngB) & lngD) + lngX + lngAC) & 0xFFFFFFFFL;
        lngA = ((lngA << lngS) | (lngA >>> (32L - lngS))) & 0xFFFFFFFFL;
        lngA = (lngA + lngB) & 0xFFFFFFFFL;
        return (lngA);
    }

    private static long gg(long lngA, long lngB, long lngC, long lngD, long lngX, long lngS, long lngAC) {
        lngA = (lngA + (lngB & lngD | lngC & ~lngD) + lngX + lngAC) & 0xFFFFFFFFL;
        lngA = ((lngA << lngS) | (lngA >>> (32L - lngS))) & 0xFFFFFFFFL;
        lngA = (lngA + lngB) & 0xFFFFFFFFL;
        return (lngA);
    }

    private static long hh(long lngA, long lngB, long lngC, long lngD, long lngX, long lngS, long lngAC) {
        lngA = (lngA + (lngB ^ lngC ^ lngD) + lngX + lngAC) & 0xFFFFFFFFL;
        lngA = ((lngA << lngS) | (lngA >>> (32L - lngS))) & 0xFFFFFFFFL;
        lngA = (lngA + lngB) & 0xFFFFFFFFL;
        return (lngA);
    }

    private static long ii(long lngA, long lngB, long lngC, long lngD, long lngX, long lngS, long lngAC) {
        lngA = (lngA + (lngC ^ (lngB | ~lngD)) + lngX + lngAC) & 0xFFFFFFFFL;
        lngA = ((lngA << lngS) | (lngA >>> (32L - lngS))) & 0xFFFFFFFFL;
        lngA = (lngA + lngB) & 0xFFFFFFFFL;
        return (lngA);
    }

    public void update(char bytInput[], long lngLen) {
        int index = (int) (this.lngByteCount % 64);
        int i = 0;
        this.lngByteCount += lngLen;
        int partLen = 64 - index;
        if (lngLen >= partLen) {
            for (int j = 0; j < partLen; ++j) {
                this.bytBuffer[j + index] = bytInput[j];
            }
            transform(this.lngState, this.bytBuffer);
            for (i = partLen; i + 63 < lngLen; i += 64) {
                for (int j = 0; j < 64; ++j) {
                    this.bytBuffer[j] = bytInput[j + i];
                }
                transform(this.lngState, this.bytBuffer);
            }
            index = 0;
        } else {
            i = 0;
        }
        for (int j = 0; j < lngLen - i; ++j) {
            this.bytBuffer[index + j] = bytInput[i + j];
        }
    }

    public void md5final() {
        char bytBits[] = new char[8];
        int index, padLen;
        long bits = this.lngByteCount * 8;
        bytBits[0] = (char) (bits & 0xffL);
        bytBits[1] = (char) ((bits >>> 8) & 0xffL);
        bytBits[2] = (char) ((bits >>> 16) & 0xffL);
        bytBits[3] = (char) ((bits >>> 24) & 0xffL);
        bytBits[4] = (char) ((bits >>> 32) & 0xffL);
        bytBits[5] = (char) ((bits >>> 40) & 0xffL);
        bytBits[6] = (char) ((bits >>> 48) & 0xffL);
        bytBits[7] = (char) ((bits >>> 56) & 0xffL);
        index = (int) this.lngByteCount % 64;
        if (index < 56) {
            padLen = 56 - index;
        } else {
            padLen = 120 - index;
        }
        update(pad, padLen);
        update(bytBits, 8);
    }

    public byte[] toByteArray() {
        byte arr[] = new byte[16];
        int c = 0;
        for (int j = 0; j < 4; ++j) {
            for (int i = 0; i < 32; i += 8) {
                arr[c++] = (byte) ((this.lngState[j] >>> i) & 0xFFL);
            }
        }
        return arr;
    }

    public void init() {
        this.lngByteCount = 0;
        this.lngState[0] = 0x67452301L;
        this.lngState[1] = 0xefcdab89L;
        this.lngState[2] = 0x98badcfeL;
        this.lngState[3] = 0x10325476L;
    }
}
