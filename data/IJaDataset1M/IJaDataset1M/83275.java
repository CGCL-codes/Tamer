package calclipse.lib.math.mp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import calclipse.lib.math.rpn.Fragment;
import calclipse.lib.math.rpn.Operand;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.RPNToken;
import calclipse.lib.math.rpn.RPNTokenType;

public class SymbolTableTest {

    private SymbolTable scanner;

    public SymbolTableTest() {
    }

    @Before
    public void setUp() throws Exception {
        scanner = new SymbolTable();
    }

    @After
    public void tearDown() throws Exception {
        scanner = null;
    }

    @Test
    public void testAdd() {
        final RPNToken t1 = new RPNToken("t1", RPNTokenType.UNDEFINED);
        final RPNToken t2 = new RPNToken("", RPNTokenType.UNDEFINED);
        assertTrue("1", scanner.add(t1));
        assertFalse("2", scanner.add(t2));
        assertFalse("3", scanner.add(t1));
    }

    @Test
    public void testRemove() {
        final RPNToken t1 = new RPNToken("t1", RPNTokenType.UNDEFINED);
        scanner.add(t1);
        assertSame("1", t1, scanner.remove("t1"));
        assertNull("2", scanner.remove("t1"));
    }

    @Test
    public void testGet() {
        assertNull("1", scanner.get("t1"));
        final RPNToken t1 = new RPNToken("t1", RPNTokenType.UNDEFINED);
        scanner.add(t1);
        assertSame("2", t1, scanner.get("t1"));
    }

    private static boolean containsAll(final List<RPNToken> l1, final List<RPNToken> l2) {
        final List<String> l3 = new ArrayList<String>(l1.size());
        for (final RPNToken token : l1) {
            l3.add(token.getName());
        }
        final List<String> l4 = new ArrayList<String>(l2.size());
        for (final RPNToken token : l2) {
            l4.add(token.getName());
        }
        return l3.containsAll(l4);
    }

    @Test
    public void testGetTokens() {
        final RPNToken t1 = new RPNToken("a", RPNTokenType.UNDEFINED);
        final RPNToken t2 = new RPNToken("b", RPNTokenType.UNDEFINED);
        final RPNToken t3 = new RPNToken("B", RPNTokenType.UNDEFINED);
        final RPNToken t4 = new RPNToken("B C", RPNTokenType.UNDEFINED);
        scanner.add(t1);
        scanner.add(t2);
        scanner.add(t3);
        scanner.add(t4);
        final List<RPNToken> l1 = new ArrayList<RPNToken>();
        l1.add(t1);
        l1.add(t2);
        l1.add(t3);
        l1.add(t4);
        final List<RPNToken> l2 = scanner.getTokens();
        assertTrue("1", containsAll(l1, l2));
        assertTrue("2", containsAll(l2, l1));
    }

    @Test
    public void testGetTokensChar() {
        final RPNToken t1 = new RPNToken("a", RPNTokenType.UNDEFINED);
        final RPNToken t2 = new RPNToken("b", RPNTokenType.UNDEFINED);
        final RPNToken t3 = new RPNToken("B", RPNTokenType.UNDEFINED);
        final RPNToken t4 = new RPNToken("B C", RPNTokenType.UNDEFINED);
        scanner.add(t1);
        scanner.add(t2);
        scanner.add(t3);
        scanner.add(t4);
        final List<RPNToken> l1 = new ArrayList<RPNToken>();
        l1.add(t3);
        l1.add(t4);
        final List<RPNToken> l2 = scanner.getTokens('B');
        assertTrue("1", containsAll(l1, l2));
        assertTrue("2", containsAll(l2, l1));
    }

    @Test
    public void testClear() {
        final RPNToken t1 = new RPNToken("a", RPNTokenType.UNDEFINED);
        final RPNToken t2 = new RPNToken("b", RPNTokenType.UNDEFINED);
        final RPNToken t3 = new RPNToken("B", RPNTokenType.UNDEFINED);
        final RPNToken t4 = new RPNToken("B C", RPNTokenType.UNDEFINED);
        scanner.add(t1);
        scanner.add(t2);
        scanner.add(t3);
        scanner.add(t4);
        scanner.clear();
        assertTrue("1", scanner.getTokens().isEmpty());
        assertTrue("2", scanner.getTokens('a').isEmpty());
    }

    @Test
    public void testNumber() throws RPNException {
        scanner.reset(" 2.34 56 ");
        assertTrue("1", scanner.hasNext());
        final Fragment f = scanner.next();
        assertFalse("2", scanner.hasNext());
        assertEquals("3", "2.34 56", f.getToken().getName());
        final Operand o = (Operand) f.getToken();
        assertEquals("4", new Double(2.3456), o.getValue());
    }

    @Test
    public void testNumber2() throws RPNException {
        scanner.reset(".2");
        assertTrue("1", scanner.hasNext());
        final Fragment f = scanner.next();
        assertFalse("2", scanner.hasNext());
        assertEquals("3", ".2", f.getToken().getName());
        final Operand o = (Operand) f.getToken();
        assertEquals("4", new Double(.2), o.getValue());
    }

    @Test
    public void testString() throws RPNException {
        scanner.reset(" ' a\"b ' \" c'd \" ");
        assertTrue("1", scanner.hasNext());
        final Fragment f = scanner.next();
        assertTrue("2", scanner.hasNext());
        final Fragment f2 = scanner.next();
        assertFalse("3", scanner.hasNext());
        assertEquals("4", "' a\"b '", f.getToken().getName());
        final Operand o = (Operand) f.getToken();
        assertEquals("5", " a\"b ", o.getValue());
        assertEquals("6", "\" c'd \"", f2.getToken().getName());
        final Operand o2 = (Operand) f2.getToken();
        assertEquals("7", " c'd ", o2.getValue());
    }

    @Test
    public void testSymbol() throws RPNException {
        final RPNToken t1 = new RPNToken("s", RPNTokenType.NOOP);
        final RPNToken t2 = new RPNToken("int", RPNTokenType.NOOP);
        final RPNToken t3 = new RPNToken("sin", RPNTokenType.NOOP);
        final RPNToken t4 = new RPNToken("atan2", RPNTokenType.NOOP);
        final RPNToken t5 = new RPNToken("3.4", RPNTokenType.NOOP);
        scanner.add(t1);
        scanner.add(t2);
        scanner.add(t3);
        scanner.add(t4);
        scanner.add(t5);
        scanner.reset(" s int ");
        assertTrue("1", scanner.hasNext());
        final Fragment f1 = scanner.next();
        assertTrue("2", scanner.hasNext());
        final Fragment f2 = scanner.next();
        assertFalse("3", scanner.hasNext());
        assertSame("4", t1, f1.getToken());
        assertSame("5", t2, f2.getToken());
    }

    @Test
    public void testSymbol2() throws RPNException {
        final RPNToken t1 = new RPNToken("s", RPNTokenType.NOOP);
        final RPNToken t2 = new RPNToken("int", RPNTokenType.NOOP);
        final RPNToken t3 = new RPNToken("sin", RPNTokenType.NOOP);
        final RPNToken t4 = new RPNToken("atan2", RPNTokenType.NOOP);
        final RPNToken t5 = new RPNToken("3.4", RPNTokenType.NOOP);
        scanner.add(t1);
        scanner.add(t2);
        scanner.add(t3);
        scanner.add(t4);
        scanner.add(t5);
        scanner.reset(" sintatan253.4");
        assertTrue("1", scanner.hasNext());
        final Fragment f1 = scanner.next();
        assertTrue("2", scanner.hasNext());
        final Fragment f2 = scanner.next();
        assertTrue("3", scanner.hasNext());
        final Fragment f3 = scanner.next();
        assertTrue("4", scanner.hasNext());
        final Fragment f4 = scanner.next();
        assertTrue("5", scanner.hasNext());
        final Fragment f5 = scanner.next();
        assertFalse("6", scanner.hasNext());
        assertSame("7", t3, f1.getToken());
        assertEquals("8", "t", f2.getToken().getName());
        assertSame("9", RPNTokenType.UNDEFINED, f2.getToken().getType());
        assertSame("10", t4, f3.getToken());
        assertSame("11", RPNTokenType.OPERAND, f4.getToken().getType());
        assertEquals("12", "5", f4.getToken().getName());
        assertSame("13", t5, f5.getToken());
    }

    @Test
    public void testComment() throws RPNException {
        scanner.reset(" /* 1.2 */ ");
        assertFalse("1", scanner.hasNext());
    }

    @Test
    public void testComment2() throws RPNException {
        scanner.reset("/* abc */4/*'TEST'*/5");
        assertTrue("1", scanner.hasNext());
        final Fragment f1 = scanner.next();
        assertTrue("2", scanner.hasNext());
        final Fragment f2 = scanner.next();
        assertFalse("3", scanner.hasNext());
        assertSame("4", RPNTokenType.OPERAND, f1.getToken().getType());
        assertSame("5", RPNTokenType.OPERAND, f2.getToken().getType());
        assertEquals("6", "4", f1.getToken().getName());
        assertEquals("7", "5", f2.getToken().getName());
    }

    @Test
    public void testInvalidNumber() throws RPNException {
        scanner.add(new RPNToken("2", RPNTokenType.NOOP));
        scanner.reset("  .2");
        try {
            scanner.hasNext();
            scanner.next();
            fail();
        } catch (final RPNException ex) {
            final Fragment f = ex.getDetail();
            assertEquals("1", 2, f.getPosition());
        }
    }

    @Test
    public void testUndelimitedComment() throws RPNException {
        scanner.reset("\r\n\t/* abc de \" ' /* '\r\n ");
        try {
            scanner.hasNext();
            scanner.next();
            fail();
        } catch (final RPNException ex) {
            final Fragment f = ex.getDetail();
            assertEquals("1", 3, f.getPosition());
        }
    }

    @Test
    public void testUndelimitedString() throws RPNException {
        scanner.reset("''\"\"\"/* abc de  ' /* '\r\n ");
        try {
            scanner.hasNext();
            scanner.next();
            scanner.hasNext();
            scanner.next();
            scanner.hasNext();
            scanner.next();
            fail();
        } catch (final RPNException ex) {
            final Fragment f = ex.getDetail();
            assertEquals("1", 4, f.getPosition());
        }
    }
}
