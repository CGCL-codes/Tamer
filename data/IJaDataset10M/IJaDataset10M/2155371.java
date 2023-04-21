package com.unicont.cardio.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import junit.framework.TestCase;

public class CorelatorTest extends TestCase {

    Corelator corelator;

    public static final int COLS = 30;

    public static final long ALL = Integer.MAX_VALUE;

    @Override
    protected void setUp() throws Exception {
        corelator = new Corelator();
    }

    private byte[] reverse(byte[] b) {
        byte[] bn = new byte[b.length];
        for (int j = 0; j < bn.length; j++) {
            bn[bn.length - j - 1] = b[j];
        }
        return bn;
    }

    public void testMatch061207230202() {
        try {
            byte[] b = loadData("061207230202_0.bin");
            testMatchOfBytes(b);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            fail();
        }
    }

    public void testMatch061207230246() {
        try {
            byte[] b = loadData("061207230246_0.bin");
            testMatchOfBytes(b);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            fail();
        }
    }

    public void testMatchOnFakes() {
        byte[] b = { (byte) 0xAA, 0x55, (byte) 0xC4, (byte) 0xD7, 0x00, 0x00, 0x20, 0x20, 0x20, 0, 0, 0, 0, 0, 0, 0, 0 };
        BigInteger bi = new BigInteger(b);
        bi = bi.shiftLeft(1);
        testMatchOfBytes(bi.toByteArray());
    }

    public void testSetMarker() {
        corelator.setMarker(new byte[] { 6, 8 });
        byte[] b = { 1, 2, 3, 4, 5 };
        testMatchOfBytes(b);
    }

    private byte[] loadData(String name) throws IOException {
        System.out.println("reading:" + CorelatorTest.class.getResource(name).getFile());
        FileInputStream fis = new FileInputStream(CorelatorTest.class.getResource(name).getFile());
        FileChannel fch = fis.getChannel();
        ByteBuffer buff = ByteBuffer.allocate(5000);
        int nbytes = fch.read(buff);
        System.out.println("read " + nbytes + " bytes");
        byte[] b = new byte[nbytes];
        buff.clear();
        buff.get(b);
        fch.close();
        fis.close();
        buff = null;
        return b;
    }

    private void testMatchOfBytes(byte[] b) {
        int max_shift = corelator.match(b);
        if (max_shift > 0) {
            BigInteger bi = new BigInteger(b);
            byte ar[] = bi.shiftRight(max_shift).toByteArray();
            printArray(ar, "\nShifted data:", ALL);
        }
        printArray(b, "\nOriginal:", ALL);
        printArray(corelator.getMarker(), "Marker:", ALL);
        System.out.println("\nMax shift=" + max_shift);
    }

    private void printArray(byte[] b, String title, long sample) {
        System.out.println(title);
        if (sample != ALL && sample < b.length) {
            System.out.println("** Shown " + sample + " of " + b.length + " bytes");
        }
        for (int j = 0; j < Math.min(b.length, sample); j++) {
            if (j % COLS == 0) {
                if (j > 0) System.out.println();
                System.out.printf("%04d|", (COLS * j / COLS));
            }
            System.out.printf("%02X ", b[j]);
        }
        System.out.println();
    }
}
