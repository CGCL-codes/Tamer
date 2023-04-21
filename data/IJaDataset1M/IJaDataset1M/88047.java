package net.gombology.OscXXL;

import junit.framework.TestCase;

public class MessageTest extends TestCase {

    public void testAddBooleanNull() {
        Message msg = new Message();
        msg.addArgument(null);
        assertEquals("N", msg.getTypetag());
    }

    public void testSetAddress() {
        Message msg = new Message();
        msg.setAddress("/test");
        assertEquals("/test", msg.getAddress());
    }

    public void testArgsArray() {
        Message msg = new Message();
        Object[] args = { false, true, null, 1, (long) 2, (float) 3.0, 4.0, "boe", 'A' };
        msg.setArguments(args);
        assertEquals("FTNihfdsc", msg.getTypetag());
    }

    public void testClear() {
        Message msg = new Message();
        msg.addArgument(new Character('A'));
        msg.clear();
        assertEquals("", msg.getTypetag());
        assertTrue(msg.getArguments().length == 0);
    }

    public void testAddCharObj() {
        Message msg = new Message();
        msg.addArgument(new Character('A'));
        assertEquals("c", msg.getTypetag());
        assertEquals('A', msg.getArguments()[0]);
    }

    public void testAddChar() {
        Message msg = new Message();
        msg.addArgument('A');
        assertEquals("c", msg.getTypetag());
        assertEquals('A', msg.getArguments()[0]);
    }

    public void testAddString() {
        Message msg = new Message();
        msg.addArgument("boe");
        assertEquals("s", msg.getTypetag());
        assertEquals("boe", msg.getArguments()[0]);
    }

    public void testAddDoubleObj() {
        Message msg = new Message();
        msg.addArgument(new Double(100.9));
        assertEquals("d", msg.getTypetag());
        assertEquals(100.9, msg.getArguments()[0]);
    }

    public void testAddFloatObj() {
        Message msg = new Message();
        msg.addArgument(new Float(100.9));
        assertEquals("f", msg.getTypetag());
        assertEquals(new Float(100.9), msg.getArguments()[0]);
    }

    public void testAddFloatType() {
        Message msg = new Message();
        msg.addArgument((float) 100.9);
        assertEquals("f", msg.getTypetag());
        assertEquals((float) 100.9, msg.getArguments()[0]);
    }

    public void testAddLongObj() {
        Message msg = new Message();
        msg.addArgument(new Long(2147483646));
        assertEquals("h", msg.getTypetag());
        assertTrue(2147483646 == (Long) msg.getArguments()[0]);
    }

    public void testAddIntegerObj() {
        Message msg = new Message();
        msg.addArgument(new Integer(100));
        assertEquals("i", msg.getTypetag());
        assertEquals(100, msg.getArguments()[0]);
    }

    public void testAddIntegerType() {
        Message msg = new Message();
        msg.addArgument(100);
        assertEquals("i", msg.getTypetag());
        assertEquals(100, msg.getArguments()[0]);
    }

    public void testAddBooleanObj() {
        Message msg = new Message();
        msg.addArgument(new Boolean(false));
        assertEquals("F", msg.getTypetag());
    }

    public void testAddBooleanType() {
        Message msg = new Message();
        msg.addArgument(false);
        assertEquals("F", msg.getTypetag());
    }

    public void testAddBooleanTypeTrue() {
        Message msg = new Message();
        msg.addArgument(true);
        assertEquals("T", msg.getTypetag());
    }
}
