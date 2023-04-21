package com.netx.ut.generics.R1;

import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import com.netx.generics.R1.time.Timestamp;
import com.netx.generics.R1.translation.ParseException;
import com.netx.generics.R1.util.*;
import com.netx.ut.generics.R1.CreateObject.ToCreate;
import com.netx.ut.generics.R1.CreateObject.ToCreateAbstract;
import com.netx.ut.generics.R1.CreateObject.ToCreateInterface;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UTUtil extends UnitTester {

    @BeforeClass
    public void setUp() {
    }

    @Test
    public void testCreateObject() throws Throwable {
        try {
            Tools.createObject(ToCreate.class);
            fail();
        } catch (ConstructorNotFoundException cnfe) {
            println(cnfe);
        }
        try {
            Tools.createObject(ToCreate.class, new Integer(1));
            fail();
        } catch (ConstructorNotVisibleException cnve) {
            println(cnve);
        }
        try {
            Tools.createObject(ToCreateAbstract.class);
            fail();
        } catch (IllegalConstructionTargetException icte) {
            println(icte);
        }
        try {
            Tools.createObject(ToCreateInterface.class);
            fail();
        } catch (IllegalConstructionTargetException icte) {
            println(icte);
        }
        try {
            Tools.createObject(ToCreate.class, "");
            fail();
        } catch (ConstructorInvocationException cie) {
            println(cie);
        }
    }

    @Test
    public void testStrings() {
        assertEquals(Strings.replaceAll("abcd", "c", "###"), "ab###d");
        assertEquals(Strings.replaceAll("A4B4B4", "4", "44"), "A44B44B44");
        assertEquals(Strings.replaceAll("1aybcekks2aybcekks3aybcekks4", "aybcekks", "_"), "1_2_3_4");
        assertEquals(Strings.replaceAll("AaAbAcAd", "A", ""), "abcd");
        assertEquals(Strings.escape("a|b|c", "|"), "a\\|b\\|c");
        assertEquals(Strings.escape("abc^+#|\\def", "|"), "abc^+#\\|\\\\def");
        assertEquals(Strings.escape("||", "|"), "\\|\\|");
        assertTrue(Arrays.equals(Strings.splitAndUnescape(Strings.escape("a|b|c", "|"), "|"), new String[] { "a|b|c" }));
        assertTrue(Arrays.equals(Strings.splitAndUnescape(Strings.escape("a|b|", "|"), "|"), new String[] { "a|b|" }));
        assertTrue(Arrays.equals(Strings.splitAndUnescape(Strings.escape("||", "|"), "|"), new String[] { "||" }));
        assertTrue(Arrays.equals(Strings.splitAndUnescape("a|b|c", "|"), new String[] { "a", "b", "c" }));
        assertTrue(Arrays.equals(Strings.splitAndUnescape("a|b|", "|"), new String[] { "a", "b" }));
        assertTrue(Arrays.equals(Strings.splitAndUnescape("||", "|"), new String[0]));
        String part1 = Strings.escape("abc\\def|ghi", "|");
        String part2 = Strings.escape("1|2|33", "|");
        assertTrue(Arrays.equals(Strings.splitAndUnescape(part1 + "|" + part2, "|"), new String[] { "abc\\def|ghi", "1|2|33" }));
    }

    @Test
    public void t03_Wildchars() {
        String[] names = { "Carlos Silva", "Isabel Figueiredo", "Jo�o Madeira", "Darren King", "Quim Tenebroso", "Johanna Wilson", "Lu�s Soares", "Farrokh Bulsara", "Rebekah Staunton", "Zhu Malik" };
        _matchRegexp("*sa", names, new String[] { "Isabel Figueiredo", "Farrokh Bulsara" });
        _matchRegexp("*sa*", names, new String[] { "Isabel Figueiredo", "Farrokh Bulsara" });
        _matchRegexp("*", names, names);
        _matchRegexp("*So*", names, new String[] { "Quim Tenebroso", "Johanna Wilson", "Lu�s Soares" });
        _matchRegexp("Isa*", names, new String[] { "Isabel Figueiredo" });
        _matchRegexp("*ra", names, new String[] { "Jo�o Madeira", "Farrokh Bulsara" });
        String[] filenames = new String[] { "KB885836.log", "file2.tmp", "no-ext" };
        _matchRegexp("KB885836.log", filenames, new String[] { "KB885836.log" });
        _matchRegexp("*", filenames, new String[] { "KB885836.log", "file2.tmp", "no-ext" });
        _matchRegexp("*.*", filenames, new String[] { "KB885836.log", "file2.tmp" });
        _matchRegexp("*.tmp", filenames, new String[] { "file2.tmp" });
        _matchRegexp("*-*", filenames, new String[] { "no-ext" });
        _matchRegexp("*+", new String[] { "abc+", "abc" }, new String[] { "abc+" });
        _matchRegexp("*$*", new String[] { "abc$G", "abc" }, new String[] { "abc$G" });
    }

    private void _matchRegexp(String pattern, String[] names, String[] results) {
        Wildcards regexp = Wildcards.compile(pattern, true);
        int count = 0;
        for (String name : names) {
            if (regexp.matches(name)) {
                if (count == results.length) {
                    println("unexpected result: " + name);
                    fail();
                }
                if (!name.equals(results[count++])) {
                    println("unexpected result: " + name);
                    fail();
                }
            }
        }
    }

    @Test
    public void t04_CorrectGenExpr() {
        Expr.registerCall(new ExprFunctions.MinCall());
        assertEquals(Expr.evaluate("before_[100 + 500]_after"), "before_600_after");
        assertEquals(Expr.evaluate("before_[100500]_after[777]"), "before_100500_after777");
        assertEquals(Expr.evaluate("b_\\[100]_a"), "b_[100]_a");
        assertEquals(Expr.evaluate("[15 + 3 > 16 * 5 + (15 + 47)]"), false);
        assertEquals(Expr.evaluate("[16 * 5 + (15 + 47)]"), 142);
        assertEquals(Expr.evaluate("[(10 + 10.5) * 5]"), 102.5);
        assertEquals(Expr.evaluate("['a'^5]"), "aaaaa");
        assertEquals(Expr.evaluate("[2^10]"), 1024);
        assertEquals(Expr.evaluate("[2.0^10]").toString(), "1024.0");
        assertEquals(Expr.evaluate("1_[now('yyyyMMdd')]_2"), "1_" + (new Timestamp().format(new SimpleDateFormat("yyyyMMdd"))) + "_2");
    }

    @Test
    public void tFailWrongGenExpr() {
        _tryBuiltin("before_[100 + 500_after");
        _tryBuiltin("[100 + 500 / (1+2))]");
        _tryBuiltin("['a'^5.5]");
        _tryBuiltin("[abc()]");
        _tryBuiltin("[now()now()]");
        _tryBuiltin("['a' >> 'b']");
        _tryBuiltin("[ctx('abc') > 'b']");
        _tryBuiltin("[ctx(100)]");
        try {
            Expr.registerCall(new ExprFunctions.WrongCall1());
        } catch (IllegalArgumentException iae) {
            println(iae.toString());
        }
        try {
            Expr.registerCall(new ExprFunctions.WrongCall2());
        } catch (IllegalArgumentException iae) {
            println(iae.toString());
        }
    }

    private void _tryBuiltin(String expr) {
        try {
            println(Expr.evaluate(expr));
            fail();
        } catch (ParseException pe) {
            println(pe + " / " + expr);
        }
    }

    @Test
    public void testVersions() {
        try {
            new Version(null);
            fail();
        } catch (IllegalArgumentException iae) {
            println(iae);
        }
        try {
            new Version();
            fail();
        } catch (IllegalArgumentException iae) {
            println(iae);
        }
        try {
            new Version(1, -1);
            fail();
        } catch (IllegalArgumentException iae) {
            println(iae);
        }
        Version vs1 = new Version(1, 0, 11, 0);
        Version vs2 = new Version(new int[] { 1, 0, 11 });
        assertEquals(vs1, vs2);
        assertEquals(vs2, vs1);
        assertEquals(vs1.compareTo(vs2), 0);
        assertEquals(vs2.compareTo(vs1), 0);
        assertEquals(vs1, vs1);
        assertEquals(vs2, vs2);
        assertEquals(vs1.compareTo(vs1), 0);
        assertEquals(vs2.compareTo(vs2), 0);
        Version vs3 = new Version(1, 1, 0, 0, 9);
        assertNotEquals(vs1, vs3);
        assertEquals(vs1.compareTo(vs3), -1);
        assertEquals(vs3.compareTo(vs1), 1);
        assertTrue(vs3.after(vs1));
        assertTrue(vs1.before(vs3));
        assertEquals(vs2.compareTo(vs3), -1);
        Version vs4 = new Version(1, 0, 11, 1);
        assertEquals(vs1.compareTo(vs4), -1);
        println(vs1);
        println(vs2);
        println(vs3);
        println(vs4);
    }

    public void t06_ByteValue() {
        ByteValue bv = new ByteValue(34220277760L, ByteValue.MEASURE.BYTES);
        println(bv.getAs(ByteValue.MEASURE.GIGABYTES));
    }

    @Test
    public void testParseIPAddress() {
        try {
            Tools.parseInetAddress("some-host-name");
            fail();
        } catch (IllegalArgumentException iae) {
            println(iae);
        }
        try {
            Tools.parseInetAddress("10.a.100.6");
            fail();
        } catch (IllegalArgumentException iae) {
            println(iae);
        }
        try {
            Tools.parseInetAddress("10.600.100.6");
            fail();
        } catch (IllegalArgumentException iae) {
            println(iae);
        }
        Tools.parseInetAddress("10.10.150.6");
    }

    @Test
    public void testStackTraceReader() throws IOException {
        Exception e;
        try {
            throw new RuntimeException("a random exception");
        } catch (RuntimeException re) {
            e = re;
        }
        BufferedReader reader = new BufferedReader(new StackTraceReader(e));
        StringBuilder sb1 = new StringBuilder();
        String line = reader.readLine();
        sb1.append(line);
        println(line);
        StringBuilder sb2 = new StringBuilder();
        line = e.toString();
        sb2.append(line);
        StackTraceElement[] ste = e.getStackTrace();
        for (int i = 0; i < ste.length; i++) {
            line = reader.readLine();
            sb1.append(line);
            println(line);
            line = "  at " + ste[i].toString();
            sb2.append(line);
        }
        println("[Reader chars: " + sb1.length() + "]");
        println("[Printed chars: " + sb2.length() + "]");
        assertEquals(sb1.length(), sb2.length());
        assertNull(reader.readLine());
        assertEquals(reader.read(), -1);
    }

    @Test
    public void testIdentifiers() {
        assertTrue(Identifiers.isCodeIdentifier("abc"));
        assertFalse(Identifiers.isCodeIdentifier("9abc"));
        assertFalse(Identifiers.isCodeIdentifier("abc-def"));
        assertFalse(Identifiers.isCodeIdentifier("abc.def"));
        assertTrue(Identifiers.isCodeIdentifier("abc.def", true));
        assertTrue(Identifiers.isCodeIdentifier("abc_def"));
        assertFalse(Identifiers.isCodeIdentifier("abc#"));
        assertFalse(Identifiers.isCodeIdentifier("abc$"));
        assertTrue(Identifiers.isTextIdentifier("abc"));
        assertFalse(Identifiers.isTextIdentifier("9abc"));
        assertTrue(Identifiers.isTextIdentifier("abc-def"));
        assertFalse(Identifiers.isTextIdentifier("abc.def"));
        assertTrue(Identifiers.isTextIdentifier("abc.def", true));
        assertTrue(Identifiers.isTextIdentifier("abc_def"));
        assertFalse(Identifiers.isTextIdentifier("abc#"));
        assertFalse(Identifiers.isTextIdentifier("abc$"));
        assertEquals(Identifiers.toTextIdentifier("abc"), "abc");
        assertEquals(Identifiers.toTextIdentifier("ALongCodeIdentifier"), "a-long-code-identifier");
        assertEquals(Identifiers.toTextIdentifier("ALong_CodeIdentifier"), "a-long-code-identifier");
        try {
            Identifiers.toTextIdentifier("9abc");
            fail();
        } catch (ParseException pe) {
            println(pe);
        }
        try {
            Identifiers.toTextIdentifier("a-long-text-identifier");
            fail();
        } catch (ParseException pe) {
            println(pe);
        }
        try {
            Identifiers.toTextIdentifier("abc#");
            fail();
        } catch (ParseException pe) {
            println(pe);
        }
        assertEquals(Identifiers.toCodeIdentifier("abc"), "abc");
        assertEquals(Identifiers.toCodeIdentifier("ALongCodeIdentifier"), "ALongCodeIdentifier");
        assertEquals(Identifiers.toCodeIdentifier("ALong_CodeIdentifier"), "ALong_CodeIdentifier");
        assertEquals(Identifiers.toCodeIdentifier("a-long-code-identifier"), "aLongCodeIdentifier");
        assertEquals(Identifiers.toCodeIdentifier("A-long-code-identifier"), "ALongCodeIdentifier");
        assertEquals(Identifiers.toCodeIdentifier("a-long-code-_identifier"), "aLongCode_identifier");
        assertEquals(Identifiers.toCodeIdentifier("a-long-code-1dentifier"), "aLongCode1dentifier");
        try {
            Identifiers.toCodeIdentifier("9abc");
            fail();
        } catch (ParseException pe) {
            println(pe);
        }
        try {
            Identifiers.toCodeIdentifier("abc#");
            fail();
        } catch (ParseException pe) {
            println(pe);
        }
    }
}
