package net.tinyos.util;

import java.io.*;

/**
 * Dump class (print tinyos messages).<p>
 *
 * Print packets in hex
 *
 * @version	1, 15 Jul 2002
 * @author	David Gay
 */
public class Dump {

    public static void printByte(PrintStream p, int b) {
        String bs = Integer.toHexString(b & 0xff).toUpperCase();
        if (b >= 0 && b < 16) p.print("0");
        p.print(bs + " ");
    }

    public static void printPacket(PrintStream p, byte[] packet, int from, int count) {
        for (int i = from; i < count; i++) printByte(p, packet[i]);
    }

    public static void printPacket(PrintStream p, byte[] packet) {
        printPacket(p, packet, 0, packet.length);
    }

    public static void dump(PrintStream to, String prefix, byte[] data) {
        to.print(prefix);
        to.print(":");
        printPacket(to, data);
        to.println();
    }

    public static void dump(String prefix, byte[] packet) {
        dump(System.err, prefix, packet);
    }
}
