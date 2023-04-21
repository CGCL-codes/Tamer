package org.systemsbiology.jrap;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class Bytes {

    public static int SHORT_little_endian_TO_big_endian(int i) {
        return ((i >> 8) & 0xff) + ((i << 8) & 0xff00);
    }

    public static int INT_little_endian_TO_big_endian(int i) {
        return ((i & 0xff) << 24) + ((i & 0xff00) << 8) + ((i & 0xff0000) >> 8) + ((i >> 24) & 0xff);
    }

    public static float quadletToFloat(byte... bytes) {
        int intBits = 0;
        for (int i = 0; i < 4; i++) {
            intBits |= (((int) bytes[i]) & 0xff);
            if (i < 3) {
                intBits <<= 8;
            }
        }
        return Float.intBitsToFloat(intBits);
    }

    public static double octletToDouble(byte... bytes) {
        long longBits = 0L;
        for (int i = 0; i < 8; i++) {
            longBits |= (((long) bytes[i]) & 0xff);
            if (i < 7) {
                longBits <<= 8;
            }
        }
        return Double.longBitsToDouble(longBits);
    }

    public static byte[] floatToQuadlet(float real) {
        int intBits = Float.floatToIntBits(real);
        byte[] ba = new byte[4];
        for (int i = ba.length - 1; i >= 0; i--) {
            ba[i] = (byte) (intBits & 0xff);
            intBits >>= 8;
        }
        return ba;
    }

    public static byte[] doubleToOctlet(double real) {
        long longBits = Double.doubleToLongBits(real);
        byte[] ba = new byte[8];
        for (int i = ba.length - 1; i >= 0; i--) {
            ba[i] = (byte) (longBits & 0xff);
            longBits >>= 8;
        }
        return ba;
    }

    /**
	 * This method convert chunks of bytes into couple of floats.
	 * 
	 * @param bytes byte-codes spectrum
	 * @param precision mz and intensity precision in bytes
	 * @return
	 */
    public static float[][] byteQuadsToMzInts(byte[] bytes, int peakNumber, int precision) {
        int mzIntNumber = bytes.length / (precision * 2);
        if (mzIntNumber != peakNumber) {
            throw new IllegalArgumentException("bad number of peaks: expected " + peakNumber + ", found " + mzIntNumber);
        }
        float[][] tmpMassIntensityList = new float[2][mzIntNumber];
        int peakIndex = 0;
        int fieldIndex = 0;
        if (precision <= 0) System.err.println("FLOATBYTES <= 0!!!");
        for (int i = 0; i < bytes.length; i += precision) {
            float result = 0;
            if (precision == 4) {
                result = quadletToFloat(bytes[i], bytes[i + 1], bytes[i + 2], bytes[i + 3]);
            } else {
                result = (float) octletToDouble(bytes[i], bytes[i + 1], bytes[i + 2], bytes[i + 3], bytes[i + 4], bytes[i + 5], bytes[i + 6], bytes[i + 7]);
            }
            tmpMassIntensityList[fieldIndex++][peakIndex] = result;
            if (fieldIndex == 2) {
                fieldIndex = 0;
                peakIndex++;
            }
        }
        bytes = null;
        return tmpMassIntensityList;
    }

    public static byte[] uncompress(byte[] bytes, int compressedLen, int uncompressedLen) throws DataFormatException {
        Inflater decompresser = new Inflater();
        decompresser.setInput(bytes, 0, compressedLen);
        byte[] result = new byte[uncompressedLen];
        decompresser.inflate(result);
        decompresser.end();
        return result;
    }
}
