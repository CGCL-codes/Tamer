package jaxlib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jaxlib.junit.XTestCase;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de>Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: StringsTest.java 2880 2011-02-16 06:35:18Z joerg_wassmer $
 */
public final class StringsTest extends XTestCase {

    public StringsTest(String name) {
        super(name);
    }

    public void testAlignCenter() {
        assertEquals("", Strings.alignCenter("", 0));
        assertEquals("a", Strings.alignCenter("a", 0));
        assertEquals(" a ", Strings.alignCenter("a", 3));
        assertEquals(" a  ", Strings.alignCenter("a", 4));
        assertEquals("abcd", Strings.alignCenter("abcd", 4));
    }

    public void testAlignLeft() {
        assertEquals("", Strings.alignLeft("", 0));
        assertEquals("a", Strings.alignLeft("a", 0));
        assertEquals("a  ", Strings.alignLeft("a", 3));
        assertEquals("a   ", Strings.alignLeft("a", 4));
        assertEquals("abcd", Strings.alignLeft("abcd", 4));
    }

    public void testAlignRight() {
        assertEquals("", Strings.alignRight("", 0));
        assertEquals("a", Strings.alignRight("a", 0));
        assertEquals("  a", Strings.alignRight("a", 3));
        assertEquals("   a", Strings.alignRight("a", 4));
        assertEquals("abcd", Strings.alignRight("abcd", 4));
    }

    public void testChain() {
        assertEquals("a", Strings.chain('-', "a"));
        assertEquals("a", Strings.chain("-", "a"));
        assertEquals("aa", Strings.chain("--", "aa"));
        assertEquals("null", Strings.chain('-', new String[] { null }));
        assertEquals("null", Strings.chain("-", new String[] { null }));
        assertEquals("null", Strings.chain("--", new String[] { null }));
        assertEquals("", Strings.chain("", ""));
        assertEquals("", Strings.chain('-', ""));
        assertEquals("", Strings.chain("-", ""));
        assertEquals("", Strings.chain("--", ""));
        assertEquals("a-b", Strings.chain('-', "a", "b"));
        assertEquals("a-b", Strings.chain("-", "a", "b"));
        assertEquals("a--b", Strings.chain("--", "a", "b"));
        assertEquals("ab", Strings.chain("", "a", "b"));
        assertEquals("ab-cd", Strings.chain('-', "ab", "cd"));
        assertEquals("ab-cd", Strings.chain("-", "ab", "cd"));
        assertEquals("ab--cd", Strings.chain("--", "ab", "cd"));
        assertEquals("null-null", Strings.chain('-', null, null));
        assertEquals("null-null", Strings.chain("-", null, null));
        assertEquals("null--null", Strings.chain("--", null, null));
        assertEquals("abcd", Strings.chain("", "ab", "cd"));
        assertEquals("a-b-c", Strings.chain('-', "a", "b", "c"));
        assertEquals("a-b-c", Strings.chain("-", "a", "b", "c"));
        assertEquals("a--b--c", Strings.chain("--", "a", "b", "c"));
        assertEquals("abc", Strings.chain("", "a", "b", "c"));
        assertEquals("ab-cd-ef", Strings.chain('-', "ab", "cd", "ef"));
        assertEquals("ab-cd-ef", Strings.chain("-", "ab", "cd", "ef"));
        assertEquals("ab--cd--ef", Strings.chain("--", "ab", "cd", "ef"));
        assertEquals("null-null-null", Strings.chain('-', null, null, null));
        assertEquals("null-null-null", Strings.chain("-", null, null, null));
        assertEquals("null--null--null", Strings.chain("--", null, null, null));
        assertEquals("abcdef", Strings.chain("", "ab", "cd", "ef"));
        assertEquals("a-b-c-d", Strings.chain('-', "a", "b", "c", "d"));
        assertEquals("a-b-c-d", Strings.chain("-", "a", "b", "c", "d"));
        assertEquals("a--b--c--d", Strings.chain("--", "a", "b", "c", "d"));
        assertEquals("abcd", Strings.chain("", "a", "b", "c", "d"));
        assertEquals("ab-cd-ef-gh", Strings.chain('-', "ab", "cd", "ef", "gh"));
        assertEquals("ab-cd-ef-gh", Strings.chain("-", "ab", "cd", "ef", "gh"));
        assertEquals("ab--cd--ef--gh", Strings.chain("--", "ab", "cd", "ef", "gh"));
        assertEquals("null-null-null-null", Strings.chain('-', null, null, null, null));
        assertEquals("null-null-null-null", Strings.chain("-", null, null, null, null));
        assertEquals("null--null--null--null", Strings.chain("--", null, null, null, null));
        assertEquals("abcdefgh", Strings.chain("", "ab", "cd", "ef", "gh"));
    }

    public void testCollapse() {
        assertNull(Strings.collapse(null));
        assertEquals("", Strings.collapse(""));
        assertEquals("", Strings.collapse(" "));
        assertEquals("", Strings.collapse("  "));
        assertEquals("", Strings.collapse(" \t \t\t  "));
        assertEquals("a", Strings.collapse(" \t \t\t  a"));
        assertEquals("a", Strings.collapse("a \t \t\t  "));
        assertEquals("a a", Strings.collapse("a \t \t\t  a"));
        assertEquals("a a a", Strings.collapse(" a \t a \t\t  a \t"));
    }

    public void testConcatObjects() {
        assertEquals("1", Strings.concat(1));
        assertEquals("123", Strings.concat(1, 23));
        assertEquals("123456", Strings.concat(1, 23, 456));
        assertEquals("12345678910", Strings.concat(1, 23, 456, 78910));
        assertEquals("1234567891011121314", Strings.concat(1, 23, 456, 78910, 11121314));
        assertEquals("12345678910111213141516171819", Strings.concat(1, 23, 456, 78910, 11121314, 1516171819));
        assertEquals("null", Strings.concat("", (Object) null));
        assertEquals("1null", Strings.concat(1, null));
        assertEquals("test = 1\ntest2 = 3\ntest3 = 4", Strings.concat("test = ", 1, '\n', "test2 = ", 3, '\n', "test3 = ", 4));
    }

    public void testConcatStrings() {
        assertEquals("a", Strings.concat("a"));
        assertEquals("abc", Strings.concat("a", "bc"));
        assertEquals("abcdef", Strings.concat("a", "bc", "def"));
        assertEquals("abcdefghij", Strings.concat("a", "bc", "def", "ghij"));
        assertEquals("abcdefghijklmno", Strings.concat("a", "bc", "def", "ghij", "klmno"));
        assertEquals("abcdefghijklmnopqrstu", Strings.concat("a", "bc", "def", "ghij", "klmno", "pqrstu"));
        assertEquals("null", Strings.concat("", (String) null));
        assertEquals("1null", Strings.concat("1", (String) null));
    }

    public void testMatchesWildcardExpression() {
        String w = new String("*");
        assertTrue(Strings.matchesWildcardExpression("aaa", new String("aaa")));
        assertTrue(Strings.matchesWildcardExpression("aaa", w));
        assertTrue(Strings.matchesWildcardExpression("aaacbbb", "a*b*"));
        assertTrue(Strings.matchesWildcardExpression("a*b*", new String("a*b*")));
        assertFalse(Strings.matchesWildcardExpression("taacbbb", "a*b*"));
    }

    public void testNormalize() {
        assertSame("", Strings.normalize(""));
        assertSame("", Strings.normalize(" "));
        assertSame("", Strings.normalize(" \r\n\n\t\r"));
        assertSame("a", Strings.normalize("a"));
        assertSame("a bb ccc dddd eeeee", Strings.normalize("a bb ccc dddd eeeee"));
        assertEquals("a bb ccc dddd  eeeee", Strings.normalize("a\nbb\rccc\tdddd\r\neeeee\n"));
    }

    public void testNormalizeLines() {
        assertSame("", Strings.normalizeLines(""));
        assertSame("", Strings.normalizeLines(" "));
        assertSame("", Strings.normalizeLines(" \r\n\n\t\r"));
        assertSame("a", Strings.normalizeLines("a"));
        assertSame("a bb ccc dddd eeeee", Strings.normalizeLines("a bb ccc dddd eeeee"));
        assertEquals("a\nbb\nccc dddd\neeeee", Strings.normalizeLines("a\nbb\rccc\tdddd\r\neeeee\n"));
    }

    public void testNCopies() {
        assertEquals("", Strings.nCopies('a', 0));
        assertEquals("a", Strings.nCopies('a', 1));
        assertEquals("aa", Strings.nCopies('a', 2));
        assertEquals("aaa", Strings.nCopies('a', 3));
        assertEquals("", Strings.nCopies("a", 0));
        assertEquals("", Strings.nCopies("", 9));
        assertEquals("a", Strings.nCopies("a", 1));
        assertEquals("aa", Strings.nCopies("a", 2));
        assertEquals("abab", Strings.nCopies("ab", 2));
        assertEquals("ababab", Strings.nCopies("ab", 3));
        assertEquals("", Strings.nCopies("a", 0, "-"));
        assertEquals("", Strings.nCopies("", 9, "-"));
        assertEquals("a", Strings.nCopies("a", 1));
        assertEquals("a-a", Strings.nCopies("a", 2, "-"));
        assertEquals("ab-ab", Strings.nCopies("ab", 2, "-"));
        assertEquals("ab-ab-ab", Strings.nCopies("ab", 3, "-"));
    }

    public void testSplitAroundChar() {
        assertEquals(Arrays.asList("mdsCatDic", "mdsCatUnit", "unit"), Arrays.asList(Strings.split("mdsCatDic.mdsCatUnit.unit", '.')));
        List<String> list = new ArrayList<String>();
        Strings.split("mdsCatDic.mdsCatUnit.unit", '.', list);
        assertEquals(Arrays.asList("mdsCatDic", "mdsCatUnit", "unit"), list);
        assertEquals(Arrays.asList("0", "0", "0"), Arrays.asList(Strings.split("0:0:0", ':')));
    }

    public void testSplitAroundString() {
        assertEquals(Arrays.asList("mdsCatDic", "mdsCatUnit", "unit"), Arrays.asList(Strings.split("mdsCatDic<>mdsCatUnit<>unit", "<>")));
        List<String> list = new ArrayList<String>();
        Strings.split("mdsCatDic<>mdsCatUnit<>unit", "<>", list);
        assertEquals(Arrays.asList("mdsCatDic", "mdsCatUnit", "unit"), list);
        assertEquals(Arrays.asList("0", "0", "0"), Arrays.asList(Strings.split("0<>0<>0", "<>")));
    }

    public void testSplitLines() {
        assertEquals(Arrays.asList(""), Strings.splitLines(""));
        assertEquals(Arrays.asList("", ""), Strings.splitLines("\n"));
        assertEquals(Arrays.asList("", "", ""), Strings.splitLines("\n\n"));
        assertEquals(Arrays.asList("a"), Strings.splitLines("a"));
        assertEquals(Arrays.asList("a", ""), Strings.splitLines("a\n"));
        assertEquals(Arrays.asList("a", "b"), Strings.splitLines("a\nb"));
        assertEquals(Arrays.asList("a", "b", ""), Strings.splitLines("a\nb\n"));
        assertEquals(Arrays.asList("", "a"), Strings.splitLines("\na"));
        assertEquals(Arrays.asList("", "a", "b"), Strings.splitLines("\na\nb"));
        assertEquals(Arrays.asList("", "a", "b", ""), Strings.splitLines("\na\nb\n"));
        assertEquals(Arrays.asList("", ""), Strings.splitLines("\r\n"));
        assertEquals(Arrays.asList("", "", ""), Strings.splitLines("\r\n\r\n"));
        assertEquals(Arrays.asList("a", ""), Strings.splitLines("a\r\n"));
        assertEquals(Arrays.asList("a", "b"), Strings.splitLines("a\r\nb"));
        assertEquals(Arrays.asList("a", "b", ""), Strings.splitLines("a\r\nb\r\n"));
        assertEquals(Arrays.asList("", "a"), Strings.splitLines("\r\na"));
        assertEquals(Arrays.asList("", "a", "b"), Strings.splitLines("\r\na\r\nb"));
        assertEquals(Arrays.asList("", "a", "b", ""), Strings.splitLines("\r\na\r\nb\r\n"));
    }
}
