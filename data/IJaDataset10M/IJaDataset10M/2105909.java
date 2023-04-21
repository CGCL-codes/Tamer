package org.netbeans.lib.lexer;

import java.util.List;
import junit.framework.TestCase;
import org.netbeans.lib.lexer.test.CharRangesDump;

/**
 * Test several simple lexer impls.
 *
 * @author mmetelka
 */
public class CharRangesTest extends TestCase {

    public CharRangesTest(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {
    }

    protected void tearDown() throws java.lang.Exception {
    }

    public void testCharRanges() {
        List<Integer> charRanges = new CharRangesDump(new CharRangesDump.CharacterMethodAcceptor("isWhitespace")).charRanges();
        TestCase.assertEquals(charRanges.get(0).intValue(), 0x9);
        TestCase.assertEquals(charRanges.get(1).intValue(), 0xd);
        TestCase.assertEquals(charRanges.get(2).intValue(), 0x1c);
        TestCase.assertEquals(charRanges.get(3).intValue(), 0x20);
        charRanges = new CharRangesDump(new CharRangesDump.CharacterMethodAcceptor("isJavaIdentifierStart")).charRanges();
        TestCase.assertEquals(charRanges.get(0).intValue(), 0x24);
        TestCase.assertEquals(charRanges.get(1).intValue(), 0x24);
        TestCase.assertEquals(charRanges.get(2).intValue(), 0x41);
        TestCase.assertEquals(charRanges.get(3).intValue(), 0x5a);
        TestCase.assertEquals(charRanges.get(4).intValue(), 0x5f);
        TestCase.assertEquals(charRanges.get(5).intValue(), 0x5f);
        TestCase.assertEquals(charRanges.get(6).intValue(), 0x61);
        TestCase.assertEquals(charRanges.get(7).intValue(), 0x7a);
        charRanges = new CharRangesDump(new CharRangesDump.CharacterMethodAcceptor("isJavaIdentifierPart")).charRanges();
        TestCase.assertEquals(charRanges.get(0).intValue(), 0x0);
        TestCase.assertEquals(charRanges.get(1).intValue(), 0x8);
        TestCase.assertEquals(charRanges.get(2).intValue(), 0xe);
        TestCase.assertEquals(charRanges.get(3).intValue(), 0x1b);
        TestCase.assertEquals(charRanges.get(4).intValue(), 0x24);
        TestCase.assertEquals(charRanges.get(5).intValue(), 0x24);
        TestCase.assertEquals(charRanges.get(6).intValue(), 0x30);
        TestCase.assertEquals(charRanges.get(7).intValue(), 0x39);
        TestCase.assertEquals(charRanges.get(8).intValue(), 0x41);
        TestCase.assertEquals(charRanges.get(9).intValue(), 0x5a);
        TestCase.assertEquals(charRanges.get(10).intValue(), 0x5f);
        TestCase.assertEquals(charRanges.get(11).intValue(), 0x5f);
        TestCase.assertEquals(charRanges.get(12).intValue(), 0x61);
        TestCase.assertEquals(charRanges.get(13).intValue(), 0x7a);
        TestCase.assertEquals(charRanges.get(14).intValue(), 0x7f);
        TestCase.assertEquals(charRanges.get(15).intValue(), 0x9f);
        TestCase.assertEquals((char) -1, 0xFFFF);
    }
}
