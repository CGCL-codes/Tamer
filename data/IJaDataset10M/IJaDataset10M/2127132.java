package home.projects.misc;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Date;

public class BitTest {

    public static void main(final String[] args) {
        testDate();
    }

    public static void testRegex() {
        String[] arr = "########|# #. #|# $# #|# # @##|# # $##|# .##|########".split("");
        for (String str : arr) {
            System.out.println(str);
        }
    }

    public static void testByteBuffer() {
        byte[] d = new byte[10];
        for (byte i = 0; i < 10; i++) d[i] = i;
        ByteBuffer buffer = ByteBuffer.allocate(11);
        buffer.put(d);
        for (byte b : buffer.array()) System.out.println(b);
        System.out.println("\n");
        buffer.position(2);
        buffer.put((byte) 100);
        for (byte b : buffer.array()) System.out.println(b);
    }

    public static void testDate() {
        Date d = new Date();
        int tmp = 1265207400 * 100;
        System.out.println(tmp);
        d.setTime(1265207400000L);
        System.out.println(d);
    }
}
