package com.limegroup.bittorrent.bencoding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import org.limewire.util.BEncoder;
import org.limewire.util.StringUtils;
import junit.framework.Test;
import com.limegroup.gnutella.util.LimeTestCase;

@SuppressWarnings("unchecked")
public class BEncodeTest extends LimeTestCase {

    public BEncodeTest(String name) {
        super(name);
    }

    public static Test suite() {
        return buildTestSuite(BEncodeTest.class);
    }

    private static class TestReadChannel implements ReadableByteChannel {

        public ByteBuffer src;

        public boolean closed;

        public void setString(String src) {
            setBytes(src.getBytes());
        }

        public void setBytes(byte[] bytes) {
            this.src = ByteBuffer.wrap(bytes);
            closed = false;
        }

        public int read(ByteBuffer dst) throws IOException {
            if (!src.hasRemaining()) return closed ? -1 : 0;
            int position = src.position();
            src.limit(Math.min(src.capacity(), src.position() + dst.remaining()));
            dst.put(src);
            src.limit(src.capacity());
            return src.position() - position;
        }

        public void close() throws IOException {
            closed = true;
        }

        public boolean isOpen() {
            return !closed;
        }
    }

    static TestReadChannel chan = new TestReadChannel();

    public void testTokenRecognition() throws Exception {
        chan.setString("");
        Token t = Token.getNextToken(chan);
        assertNull(t);
        chan.setString("iSomething");
        t = Token.getNextToken(chan);
        assertEquals(Token.LONG, t.getType());
        assertEquals(1, chan.src.position());
        chan.setString("1adf");
        t = Token.getNextToken(chan);
        assertEquals(Token.STRING, t.getType());
        assertEquals(1, chan.src.position());
        chan.setString("ladf");
        t = Token.getNextToken(chan);
        assertEquals(Token.LIST, t.getType());
        assertEquals(1, chan.src.position());
        chan.setString("dadf");
        t = Token.getNextToken(chan);
        assertEquals(Token.DICTIONARY, t.getType());
        assertEquals(1, chan.src.position());
        chan.setString("t");
        t = Token.getNextToken(chan);
        assertEquals(Token.BOOLEAN, t.getType());
        assertSame(BEBoolean.TRUE, t);
        assertEquals(Boolean.TRUE, t.getResult());
    }

    /** 
     * Tests some scenarios of creating a string.
     */
    public void testString() throws Exception {
        chan.setString("4:asdf");
        Token t = Token.getNextToken(chan);
        t.handleRead();
        assertNotNull(t.getResult());
        assertEquals(Token.STRING, t.getType());
        assertEquals("asdf", StringUtils.getASCIIString((byte[]) t.getResult()));
        assertFalse(chan.src.hasRemaining());
        chan.setString("4:asdfasdf");
        t = Token.getNextToken(chan);
        t.handleRead();
        assertNotNull(t.getResult());
        assertEquals(Token.STRING, t.getType());
        assertEquals("asdf", StringUtils.getASCIIString((byte[]) t.getResult()));
        assertEquals(4, chan.src.remaining());
        chan.setString("2");
        t = Token.getNextToken(chan);
        t.handleRead();
        assertNull(t.getResult());
        chan.setString(":");
        t.handleRead();
        assertNull(t.getResult());
        chan.setString("a");
        t.handleRead();
        assertNull(t.getResult());
        chan.setString("s");
        t.handleRead();
        assertNotNull(t.getResult());
        assertEquals("as", StringUtils.getASCIIString((byte[]) t.getResult()));
        chan.setString("2:a");
        t = Token.getNextToken(chan);
        t.handleRead();
        assertNull(t.getResult());
        t.handleRead();
        chan.setString("s");
        t.handleRead();
        assertNotNull(t.getResult());
        assertEquals("as", StringUtils.getASCIIString((byte[]) t.getResult()));
        chan.setString("2:a");
        chan.close();
        t = Token.getNextToken(chan);
        try {
            t.handleRead();
            fail("trying to read from closed channel should throw");
        } catch (IOException expected) {
        }
        chan.setString("2;as");
        t = Token.getNextToken(chan);
        assertEquals(Token.STRING, t.getType());
        try {
            t.handleRead();
            fail("invalid string should throw");
        } catch (IOException expected) {
        }
        assertEquals(2, chan.src.position());
        chan.setString("0:");
        t = Token.getNextToken(chan);
        assertEquals(Token.STRING, t.getType());
        t.handleRead();
        assertEquals("", StringUtils.getASCIIString((byte[]) t.getResult()));
    }

    /**
     * tests various scenarios of parsing a Long value.
     */
    public void testLong() throws Exception {
        chan.setString("i");
        Token t = Token.getNextToken(chan);
        assertEquals(Token.LONG, t.getType());
        t.handleRead();
        assertNull(t.getResult());
        t.handleRead();
        assertNull(t.getResult());
        chan.setString("1");
        t.handleRead();
        assertNull(t.getResult());
        chan.setString("2");
        t.handleRead();
        assertNull(t.getResult());
        chan.setString("3");
        t.handleRead();
        assertNull(t.getResult());
        chan.setString("e");
        t.handleRead();
        assertNotNull(t.getResult());
        assertEquals(new Long(123), t.getResult());
        chan.setString("i123easdfadfs");
        t = Token.getNextToken(chan);
        t.handleRead();
        assertEquals(new Long(123), t.getResult());
        assertEquals(5, chan.src.position());
        assertTrue(chan.src.hasRemaining());
        chan.setString("i0e");
        t = Token.getNextToken(chan);
        t.handleRead();
        assertEquals(new Long(0), t.getResult());
        chan.setString("i-1e");
        t = Token.getNextToken(chan);
        t.handleRead();
        assertEquals(new Long(-1), t.getResult());
        chan.setString("i001e");
        t = Token.getNextToken(chan);
        try {
            t.handleRead();
            fail(" leading 0s didn't throw");
        } catch (IOException expected) {
        }
        assertNull(t.getResult());
        assertEquals(3, chan.src.position());
        chan.setString("i-01e");
        t = Token.getNextToken(chan);
        try {
            t.handleRead();
            fail(" negative 0 didn't throw");
        } catch (IOException expected) {
        }
        assertNull(t.getResult());
        assertEquals(3, chan.src.position());
        chan.setString("i13i1e");
        t = Token.getNextToken(chan);
        try {
            t.handleRead();
            fail(" invalid chars didn't throw");
        } catch (IOException expected) {
        }
        assertNull(t.getResult());
        assertEquals(4, chan.src.position());
        chan.setString("i123487129587198257981374598173498751579813458345983297e");
        t = Token.getNextToken(chan);
        try {
            t.handleRead();
            fail(" too big didn't throw");
        } catch (IOException expected) {
        }
        chan.setString("i-123487129587198257981374598173498751579813458345983297e");
        t = Token.getNextToken(chan);
        try {
            t.handleRead();
            fail(" too small didn't throw");
        } catch (IOException expected) {
        }
    }

    /**
     * test various scenarios of parsing a List
     */
    public void testList() throws Exception {
        chan.setString("l");
        Token t = Token.getNextToken(chan);
        assertEquals(Token.LIST, t.getType());
        t.handleRead();
        assertNull(t.getResult());
        t.handleRead();
        assertNull(t.getResult());
        chan.setString("i5e");
        t.handleRead();
        assertNull(t.getResult());
        chan.setString("e");
        t.handleRead();
        assertNotNull(t.getResult());
        List l = (List) t.getResult();
        assertEquals(1, l.size());
        assertTrue(l.contains(new Long(5)));
        chan.setString("le");
        t = Token.getNextToken(chan);
        t.handleRead();
        l = (List) t.getResult();
        assertTrue(l.isEmpty());
        chan.setString("l2:asi44eei56e");
        t = Token.getNextToken(chan);
        t.handleRead();
        l = (List) t.getResult();
        assertEquals(2, l.size());
        assertTrue(l.contains(new StringByte("as")));
        assertTrue(l.contains(new Long(44)));
        assertEquals(4, chan.src.remaining());
    }

    /**
     * Tests various scenarios of parsing a dictionary
     */
    public void testDictionary() throws Exception {
        chan.setString("d");
        Token t = Token.getNextToken(chan);
        assertEquals(Token.DICTIONARY, t.getType());
        t.handleRead();
        assertNull(t.getResult());
        t.handleRead();
        assertNull(t.getResult());
        chan.setString("2:as2:df");
        t.handleRead();
        assertNull(t.getResult());
        chan.setString("e");
        t.handleRead();
        assertNotNull(t.getResult());
        Map m = (Map) t.getResult();
        assertEquals(1, m.size());
        assertTrue(m.containsKey("as"));
        assertTrue(m.containsValue(new StringByte("df")));
        chan.setString("de");
        t = Token.getNextToken(chan);
        t.handleRead();
        m = (Map) t.getResult();
        assertTrue(m.isEmpty());
        chan.setString("d1:ai1eei45e");
        t = Token.getNextToken(chan);
        t.handleRead();
        m = (Map) t.getResult();
        assertEquals(1, m.size());
        assertTrue(chan.src.hasRemaining());
        assertEquals(4, chan.src.remaining());
        chan.setString("di4ei5ee");
        t = Token.getNextToken(chan);
        try {
            t.handleRead();
            fail("non-string key didn't throw");
        } catch (IOException expected) {
        }
        chan.setString("d1:ae");
        t = Token.getNextToken(chan);
        try {
            t.handleRead();
            fail("missing value didn't throw");
        } catch (IOException expected) {
        }
    }

    /**
     * tests encoding and decoding a fancy nested collection structure
     */
    public void testNested() throws Exception {
        List l = new ArrayList();
        l.add("snake");
        List l2 = new ArrayList();
        l2.add(l);
        l2.add("snake");
        l = new ArrayList();
        l.add(l2);
        l2 = new ArrayList();
        l2.add(l);
        List l3 = new ArrayList();
        List l4 = new ArrayList();
        l3.add(true);
        l4.add(false);
        l4.add(true);
        l4.add(false);
        l3.add(l4);
        Map m = new HashMap();
        m.put("key2", l2);
        Map m2 = new HashMap();
        m.put("key1", m2);
        m.put("key3", l3);
        m.put("key4", false);
        m2.put("key12", new ArrayList());
        l = new ArrayList();
        l.add("badger");
        l.add("badger");
        l.add(new Long(3));
        l2 = new ArrayList();
        l.add(l2);
        l.add(new HashMap());
        l2.add("mushroom");
        l2.add("mushroom");
        m2.put("key11", l);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BEncoder.getEncoder(baos, true, true, Token.ASCII).encodeDict(m);
        String s = new String(baos.toByteArray(), Token.ASCII);
        String expected = "d4:key1d5:key11l6:badger6:badgeri3el8:mushroom8:mushroomedee5:key12lee4:key2llll5:snakee5:snakeeee4:key3ltlftfee4:key4fe";
        assertEquals(expected, s);
        chan.setString(s);
        Token t = Token.getNextToken(chan);
        t.handleRead();
        Map outtest = (Map) t.getResult();
        assertEquals(4, outtest.size());
        Map inner = (Map) outtest.get("key1");
        List empty = (List) inner.get("key12");
        assertTrue(empty.isEmpty());
        List badgers = (List) inner.get("key11");
        assertEquals(5, badgers.size());
        assertEquals(new StringByte("badger"), badgers.get(0));
        assertEquals(new StringByte("badger"), badgers.get(1));
        assertEquals(new Long(3), badgers.get(2));
        List mushrooms = (List) badgers.get(3);
        assertEquals(2, mushrooms.size());
        assertEquals(new StringByte("mushroom"), mushrooms.get(0));
        assertEquals(new StringByte("mushroom"), mushrooms.get(1));
        Map emptyMap = (Map) badgers.get(4);
        assertTrue(emptyMap.isEmpty());
        List nestedList0 = (List) outtest.get("key2");
        assertEquals(1, nestedList0.size());
        List nestedList1 = (List) nestedList0.get(0);
        assertEquals(1, nestedList1.size());
        List nestedList2 = (List) nestedList1.get(0);
        assertEquals(2, nestedList2.size());
        assertTrue(nestedList2.contains(new StringByte("snake")));
        List nestedList3 = (List) nestedList2.get(0);
        assertEquals(1, nestedList3.size());
        assertTrue(nestedList3.contains(new StringByte("snake")));
        List boolList = (List) outtest.get("key3");
        assertEquals(2, boolList.size());
        assertEquals(Boolean.TRUE, boolList.get(0));
        List boolListInner = (List) boolList.get(1);
        assertEquals(3, boolListInner.size());
        assertEquals(Boolean.FALSE, boolListInner.get(0));
        assertEquals(Boolean.TRUE, boolListInner.get(1));
        assertEquals(Boolean.FALSE, boolListInner.get(2));
        assertEquals(Boolean.FALSE, outtest.get("key4"));
    }

    public void testUTF16() throws Exception {
        String original = new String("A" + "ê" + "ñ" + "ü" + "က" + "C");
        List<Object> l = new ArrayList<Object>(2);
        l.add(original);
        l.add(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BEncoder.getEncoder(baos).encodeList(l);
        chan.setBytes(baos.toByteArray());
        Token t = Token.getNextToken(chan);
        t.handleRead();
        assertNotNull(t);
        assertEquals(Token.LIST, t.getType());
        List parsed = (List) t.getResult();
        byte[] parsedByte = (byte[]) parsed.get(0);
        assertNotEquals(original, StringUtils.getASCIIString(parsedByte));
        assertNotEquals(original, new String(parsedByte, "UTF-8"));
        baos = new ByteArrayOutputStream();
        BEncoder.getEncoder(baos, false, false, "UTF-8").encodeList(l);
        chan.setBytes(baos.toByteArray());
        t = Token.getNextToken(chan);
        t.handleRead();
        assertNotNull(t);
        assertEquals(Token.LIST, t.getType());
        parsed = (List) t.getResult();
        parsedByte = (byte[]) parsed.get(0);
        assertNotEquals(original, StringUtils.getASCIIString(parsedByte));
        assertEquals(original, new String(parsedByte, "UTF-8"));
    }

    public void testEncodeIterables() throws Exception {
        Set<Long> s = new HashSet<Long>();
        s.add(1L);
        s.add(2L);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BEncoder.getEncoder(baos).encodeList(s);
        chan.setBytes(baos.toByteArray());
        Token t = Token.getNextToken(chan);
        t.handleRead();
        assertEquals(Token.LIST, t.getType());
        List parsed = (List) t.getResult();
        assertEquals(2, parsed.size());
        System.out.println(parsed);
        assertTrue(s.containsAll(parsed));
    }

    private static class StringByte {

        final String s;

        StringByte(String s) {
            this.s = s;
        }

        public boolean equals(Object o) {
            if (!(o instanceof byte[])) return false;
            return s.equals(StringUtils.getASCIIString((byte[]) o));
        }
    }
}
