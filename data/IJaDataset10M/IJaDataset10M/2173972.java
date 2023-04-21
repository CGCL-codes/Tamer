package org.boblight4j.utils;

import static junit.framework.Assert.assertEquals;
import org.boblight4j.exception.BoblightParseException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScannerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    private Scanner testable;

    @Before
    public void setUp() throws Exception {
        testable = new Scanner("10 word");
    }

    @Test
    public void testNextChar() {
        testable = new Scanner("abcd");
        assertEquals('a', testable.nextChar());
        assertEquals('b', testable.nextChar());
        assertEquals('c', testable.nextChar());
        assertEquals('d', testable.nextChar());
    }

    @Test
    public void testNextFloat() throws BoblightParseException {
        testable = new Scanner(".888 .22 .33 .44");
        assertEquals(.888f, testable.nextFloat());
        assertEquals(' ', testable.nextChar());
        assertEquals(.22f, testable.nextFloat());
        assertEquals(' ', testable.nextChar());
        assertEquals(.33f, testable.nextFloat());
        assertEquals(' ', testable.nextChar());
        assertEquals(.44f, testable.nextFloat());
    }

    @Test
    public void testNextInt() {
        testable = new Scanner("1d2d3d4");
        assertEquals(1, testable.nextInt());
        assertEquals('d', testable.nextChar());
        assertEquals(2, testable.nextInt());
        assertEquals('d', testable.nextChar());
        assertEquals(3, testable.nextInt());
        assertEquals('d', testable.nextChar());
        assertEquals(4, testable.nextInt());
    }
}
