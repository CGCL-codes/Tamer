package ptp.util;

public class DumpUtil {

    public static void dump(byte[] array, int off, int length) {
        for (int i = 0; i < length; i += 8) {
            System.out.println();
            for (int j = i; j < (i + 8 > length ? length : i + 8); j++) {
                System.out.format("%1$02x ", array[j]);
            }
            for (int j = (i + 8 > length ? length : i + 8); j < i + 8; j++) {
                System.out.print("   ");
            }
            System.out.print(" | ");
            for (int j = i; j < (i + 8 > length ? length : i + 8); j++) {
                int c = array[j] & 0xFF;
                if (c == '\r' || c == '\n') {
                    c = ' ';
                }
                System.out.format("%c ", c);
            }
        }
    }
}
