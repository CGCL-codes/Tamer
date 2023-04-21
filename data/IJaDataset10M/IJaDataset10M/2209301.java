package model.device.artnet;

/**
 *
 * @author Landy
 */
public class ArtNetUtils {

    public static int toInt(byte b) {
        if (b < 0) {
            return 255 - (b * -1);
        } else return (int) b;
    }

    public static byte toByte(int i) {
        byte b;
        if (i > 127) {
            int t = i - 256;
            b = (byte) t;
        } else {
            b = (byte) i;
        }
        return b;
    }

    public static byte toByte2(int i) {
        byte b = (byte) i;
        return b;
    }

    public static int bitsToBtye(int[] bits) {
        int b = 0;
        if (bits[0] == 1) b += 1;
        if (bits[1] == 1) b += 2;
        if (bits[2] == 1) b += 4;
        if (bits[3] == 1) b += 8;
        if (bits[4] == 1) b += 16;
        if (bits[5] == 1) b += 32;
        if (bits[6] == 1) b += 64;
        if (bits[7] == 1) b += 128;
        return b;
    }

    public static int[] byteToBits(int b) {
        int[] bits = new int[8];
        if (b >= 128) {
            b -= 128;
            bits[7] = 1;
        } else bits[7] = 0;
        if (b >= 64) {
            b -= 64;
            bits[6] = 1;
        } else bits[6] = 0;
        if (b >= 32) {
            b -= 32;
            bits[5] = 1;
        } else bits[5] = 0;
        if (b >= 16) {
            b -= 16;
            bits[4] = 1;
        } else bits[4] = 0;
        if (b >= 8) {
            b -= 8;
            bits[3] = 1;
        } else bits[3] = 0;
        if (b >= 4) {
            b -= 4;
            bits[2] = 1;
        } else bits[2] = 0;
        if (b >= 2) {
            b -= 2;
            bits[1] = 1;
        } else bits[1] = 0;
        if (b >= 1) {
            b -= 1;
            bits[0] = 1;
        } else bits[0] = 0;
        return bits;
    }
}
