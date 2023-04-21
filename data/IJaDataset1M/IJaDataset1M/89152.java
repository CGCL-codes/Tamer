package com.purplefrog.flea2flea.test;

import java.io.*;
import java.util.*;
import com.purplefrog.flea2flea.*;
import junit.framework.*;

public class MultiPartTest extends TestCase {

    public static final String boundary = "boundary";

    public static final byte[] testMultipart1 = ("--" + boundary + "\r\nheader\r\nContent-Disposition: form-data; name=\"file\"; filename=\"issue\"\r\nContent-Type: application/octet-stream\r\n\r\nhate\nyou\n\r\n--" + boundary + "\r\nafter\r\n").getBytes();

    public static final byte[] testMultipart2 = ("--" + boundary + "\r\nheader\r\n\r\nhate\nbound\r\nbound\nx\ryou\n\r\n--" + boundary + "\r\nafter\r\n").getBytes();

    public static final byte[] testMultipart3 = ("--" + boundary + "\r\nheader\r\n\r\n\rx\r\n--" + boundary + "--\r\n").getBytes();

    public static final byte[] testMultipart4 = ("--" + boundary + "\r\nheader\r\n\r\n\nx\r\nbound\r\n--" + boundary + "--\r\n").getBytes();

    public static final byte[] testMultipart5 = ("--" + boundary + "\r\nheader\r\n\r\n\r\n--" + boundary + "--\r\n").getBytes();

    public static final byte[] testMultipart6 = ("--" + boundary + "\r\nheader\r\n\r\n\nx\r\n--" + boundary + "--\r\n").getBytes();

    public static final byte[] testMultipart7 = ("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"tag\"\r\n" + "\r\n" + "russ\r\n" + "--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"file\"; filename=\"issue\"\r\n" + "Content-Type: application/octet-stream\r\n" + "\r\n" + "Red Hat blah\n" + "\r\n" + "--" + boundary + "--\r\n").getBytes();

    public void testBoundaryHunter() throws IOException {
        MultiPartIterator mpi = probe(testMultipart1, boundary, "hate\nyou\n");
        mpi.getNextPart().drain();
        assertNull(mpi.getNextPart());
        mpi = probe(testMultipart2, boundary, "hate\nbound\r\nbound\nx\ryou\n");
        assertNotNull(mpi.getNextPart());
        mpi = probe(testMultipart3, boundary, "\rx");
        assertNull(mpi.getNextPart());
        mpi = probe(testMultipart4, boundary, "\nx\r\nbound");
        assertNull(mpi.getNextPart());
        mpi = probe(testMultipart5, boundary, "");
        assertNull(mpi.getNextPart());
        mpi = probe(testMultipart6, boundary, "\nx");
        assertNull(mpi.getNextPart());
    }

    public void testBiggie() throws IOException {
        MultiPartIterator mpi = new MultiPartIterator(new ByteArrayInputStream(testMultipart7), boundary);
        MultiPartIterator.Part2 part = mpi.getNextPart();
        assertEquals("tag", part.getContentName());
        assertEquals(null, part.getFilename());
        assertEquals("russ", new String(part.slurp()));
        part = mpi.getNextPart();
        assertEquals("file", part.getContentName());
        assertEquals("issue", part.getFilename());
        expectRead("Red Hat blah\n", part.getInputStream());
        assertEquals(-1, part.getInputStream().read());
        assertNull(mpi.getNextPart());
    }

    public void testHeaderParser() throws IOException {
        MultiPartIterator mpi = new MultiPartIterator(new ByteArrayInputStream(testMultipart1), boundary);
        MultiPartIterator.Part2 part = mpi.getNextPart();
        Iterator iter = part.headerKeyIterator();
        assertEquals("content-disposition", iter.next());
        assertEquals("content-type", iter.next());
        assertEquals("header", iter.next());
        assertFalse(iter.hasNext());
        assertEquals("file", part.getContentName());
        assertEquals("issue", part.getFilename());
        byte[] payload = part.slurp();
        assertEquals("hate\nyou\n", new String(payload));
    }

    private static MultiPartIterator probe(byte[] requestBytes, String boundary, String expected) throws IOException {
        MultiPartIterator mpi;
        MultiPartIterator.Part2 part;
        mpi = new MultiPartIterator(new ByteArrayInputStream(requestBytes), boundary);
        part = mpi.getNextPart();
        expectRead(expected, part.getInputStream());
        assertEquals(-1, part.getInputStream().read());
        mpi = new MultiPartIterator(new ByteArrayInputStream(requestBytes), boundary);
        part = mpi.getNextPart();
        expectRead1(expected, part.getInputStream());
        assertEquals(-1, part.getInputStream().read());
        return mpi;
    }

    private static void expectRead(String expected, InputStream istr) throws IOException {
        expectRead(expected.getBytes(), istr);
    }

    private static void expectRead(byte[] expected, InputStream istr) throws IOException {
        int off = 0;
        byte[] buf = new byte[expected.length];
        while (off < expected.length) {
            int n = istr.read(buf, 0, expected.length - off);
            for (int j = 0; j < n; j++) assertEquals("bad @ " + (j + off), expected[j + off], buf[j]);
            off += n;
        }
    }

    private static void expectRead1(String expected, InputStream istr) throws IOException {
        expectRead1(expected.getBytes(), istr);
    }

    private static void expectRead1(byte[] expected, InputStream istr) throws IOException {
        for (int off = 0; off < expected.length; off++) {
            assertEquals("bad @ " + off, expected[off], istr.read());
        }
    }

    public static TestSuite suite() {
        return new TestSuite(MultiPartTest.class);
    }
}
