package net.rptools.clientserver.hessian;

import junit.framework.TestCase;

public class HessianUtilsTest extends TestCase {

    public void testMethodToBytes() {
        byte[] bytes = HessianUtils.methodToBytes("doit", "param1", 10);
        assertEquals(25, bytes.length);
        int n = 0;
        assertEquals('c', (char) bytes[n++]);
        assertEquals(1, bytes[n++]);
        assertEquals(0, bytes[n++]);
        assertEquals('m', (char) bytes[n++]);
        assertEquals(0, bytes[n++]);
        assertEquals(4, bytes[n++]);
        assertEquals('d', (char) bytes[n++]);
        assertEquals('o', (char) bytes[n++]);
        assertEquals('i', (char) bytes[n++]);
        assertEquals('t', (char) bytes[n++]);
        assertEquals('S', (char) bytes[n++]);
        assertEquals(0, bytes[n++]);
        assertEquals(6, bytes[n++]);
        assertEquals('p', (char) bytes[n++]);
        assertEquals('a', (char) bytes[n++]);
        assertEquals('r', (char) bytes[n++]);
        assertEquals('a', (char) bytes[n++]);
        assertEquals('m', (char) bytes[n++]);
        assertEquals('1', (char) bytes[n++]);
        assertEquals('I', (char) bytes[n++]);
        assertEquals(0, bytes[n++]);
        assertEquals(0, bytes[n++]);
        assertEquals(0, bytes[n++]);
        assertEquals(10, bytes[n++]);
        assertEquals('z', (char) bytes[n++]);
    }
}
