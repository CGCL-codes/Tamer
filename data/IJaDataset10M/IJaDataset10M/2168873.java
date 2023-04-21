package flattree.flat.read;

import junit.framework.TestCase;

/**
 * Test for {@link LazyReadLine}.
 */
public class LazyReadLineTest extends TestCase {

    public void test() throws Exception {
        LazyReadLine line = new LazyReadLine(new StringReadLine("01234"));
        assertEquals(0, line.getRowIndex());
        assertEquals(0, line.getIndex());
        assertEquals(0, line.getColumnIndex());
        assertEquals('0', line.read());
        assertEquals('1', line.read());
        assertEquals('2', line.read());
        assertEquals(3, line.getIndex());
        line.drain();
        assertEquals(3, line.getIndex());
    }
}
