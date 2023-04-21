package fi.vtt.probeframework.javaclient.protocol;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BaseTest {

    public String textFor2Bytes = "Alice is sitting by her sister lazily and became a bit tired, " + "and she sees a White Rabbit in a waistcoat carrying a watch. " + "She follows it down a rabbit hole, and falls down a very long chamber " + "full of strange things on shelves. After landing safely on the ground, " + "she goes into a long hallway with a glass table with a gold key. " + "Alice opens up a curtain and finds a small door, which the key fits in perfectly, " + "and behind it is a beautiful garden, but she can't fit in. " + "Alice then finds a small bottle labeled 'DRINK ME,' and drinks it. " + "The drink causes her to shrink. Alice accidentally leaves the key on the table, " + "and with her diminished stature can no longer reach it and becomes very scared. " + "She then sees a cake that says 'EAT ME,' and proceeds to eat it";

    protected void assertArrayEqualsIgnoreTime(byte[] expected, byte[] msg) {
        assertArrayEqualsIgnoreLastBytes(expected, msg, 4);
    }

    protected void assertArrayEqualsIgnoreLastBytes(byte[] expected, byte[] msg, int bytesToIgnore) {
        for (int i = 1; i <= bytesToIgnore; i++) {
            msg[msg.length - i] = 0;
        }
        assertArrayEquals(expected, msg);
    }

    /**
   * Convert a byte[] array to hexdump style string format.
   */
    public static String hex(byte in[]) {
        byte b = 0x00;
        int i = 0;
        if (in == null || in.length <= 0) {
            return null;
        }
        String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
        String str = "";
        String temp = "";
        while (i < in.length) {
            temp += (char) in[i];
            b = (byte) (in[i] & 0xF0);
            b = (byte) (b >>> 4);
            b = (byte) (b & 0x0F);
            str += pseudo[(int) b];
            b = (byte) (in[i] & 0x0F);
            str += pseudo[(int) b];
            str += " ";
            i++;
            if (i % 20 == 0) {
                str += "    " + temp;
                str += System.getProperty("line.separator");
                temp = "";
            }
        }
        int add = 4 + (20 - i % 20) * 3;
        for (int x = 0; x < add; x++) {
            str += " ";
        }
        str += temp;
        return str;
    }
}
