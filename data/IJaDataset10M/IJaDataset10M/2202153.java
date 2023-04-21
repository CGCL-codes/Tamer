package org.netbeans.lib.lexer.test.join;

import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.LanguagePath;
import org.netbeans.api.lexer.PartType;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenChange;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenHierarchyEvent;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.junit.NbTestCase;
import org.netbeans.lib.lexer.lang.TestJoinTextTokenId;
import org.netbeans.lib.lexer.test.LexerTestUtilities;
import org.netbeans.lib.lexer.lang.TestJoinTopTokenId;
import org.netbeans.lib.lexer.lang.TestPlainTokenId;
import org.netbeans.lib.lexer.test.ModificationTextDocument;

/**
 * Test embedded sections that should be lexed together.
 *
 * @author Miloslav Metelka
 */
public class JoinSectionsMod1Test extends NbTestCase {

    public JoinSectionsMod1Test(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    @Override
    public PrintStream getLog() {
        return System.out;
    }

    @Override
    protected Level logLevel() {
        return Level.INFO;
    }

    public void testRemoveContent() throws Exception {
        String text = "a<x>";
        ModificationTextDocument doc = new ModificationTextDocument();
        doc.insertString(0, text, null);
        doc.putProperty(Language.class, TestJoinTopTokenId.language());
        LexerTestUtilities.incCheck(doc, true);
        TokenHierarchy<?> hi = TokenHierarchy.get(doc);
        TokenSequence<?> ts = hi.tokenSequence();
        LexerTestUtilities.incCheck(doc, true);
        doc.remove(0, doc.getLength());
        LexerTestUtilities.incCheck(doc, true);
    }

    public void testCreateEmbedding() throws Exception {
        String text = "x{a(y}<z>{)}zc";
        ModificationTextDocument doc = new ModificationTextDocument();
        doc.insertString(0, text, null);
        doc.putProperty(Language.class, TestJoinTopTokenId.language());
        LexerTestUtilities.incCheck(doc, true);
        TokenHierarchy<?> hi = TokenHierarchy.get(doc);
        TokenSequence<?> ts = hi.tokenSequence();
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.TEXT, "x", -1);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.BRACES, "{a(y}", -1);
        ts.createEmbedding(TestPlainTokenId.language(), 1, 1, true);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.TAG, "<z>", -1);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.BRACES, "{)}", -1);
        ts.createEmbedding(TestPlainTokenId.language(), 1, 1, true);
    }

    public void testCreateEmptyEmbedding() throws Exception {
        String text = "{a}x{}y{}";
        ModificationTextDocument doc = new ModificationTextDocument();
        doc.insertString(0, text, null);
        doc.putProperty(Language.class, TestJoinTopTokenId.language());
        LexerTestUtilities.incCheck(doc, true);
        TokenHierarchy<?> hi = TokenHierarchy.get(doc);
        TokenSequence<?> ts = hi.tokenSequence();
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.BRACES, "{a}", -1);
        ts.createEmbedding(TestPlainTokenId.language(), 1, 1, true);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.TEXT, "x", -1);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.BRACES, "{}", -1);
        ts.createEmbedding(TestPlainTokenId.language(), 1, 1, true);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.TEXT, "y", -1);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.BRACES, "{}", -1);
        ts.createEmbedding(TestPlainTokenId.language(), 1, 1, true);
    }

    public void testRemoveFirstSection() throws Exception {
        String text = "<a[x y]b><c[z]>";
        ModificationTextDocument doc = new ModificationTextDocument();
        doc.insertString(0, text, null);
        doc.putProperty(Language.class, TestJoinTopTokenId.language());
        LexerTestUtilities.incCheck(doc, true);
        TokenHierarchy<?> hi = TokenHierarchy.get(doc);
        TokenSequence<?> ts = hi.tokenSequence();
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.TAG, "<a[x y]b>", -1);
        TokenSequence<?> ts1 = ts.embedded();
        assertTrue(ts1.moveNext());
        LexerTestUtilities.assertTokenEquals(ts1, TestJoinTextTokenId.TEXT, "a", -1);
        assertTrue(ts1.moveNext());
        LexerTestUtilities.assertTokenEquals(ts1, TestJoinTextTokenId.BRACKETS, "[x y]", -1);
        TokenSequence<?> ts2 = ts1.embedded();
        assertTrue(ts2.moveNext());
        LexerTestUtilities.assertTokenEquals(ts2, TestPlainTokenId.WORD, "x", -1);
        assertTrue(ts2.moveNext());
        LexerTestUtilities.assertTokenEquals(ts2, TestPlainTokenId.WHITESPACE, " ", -1);
        assertTrue(ts2.moveNext());
        LexerTestUtilities.assertTokenEquals(ts2, TestPlainTokenId.WORD, "y", -1);
        assertFalse(ts2.moveNext());
        assertTrue(ts1.moveNext());
        LexerTestUtilities.assertTokenEquals(ts1, TestJoinTextTokenId.TEXT, "b", -1);
        assertFalse(ts2.moveNext());
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.TAG, "<c[z]>", -1);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.TEXT, "\n", -1);
        assertFalse(ts.moveNext());
        LexerTestUtilities.incCheck(doc, true);
        doc.remove(0, 9);
        LexerTestUtilities.incCheck(doc, true);
    }

    public void testShortDocMod() throws Exception {
        String text = "xay<b>zc";
        ModificationTextDocument doc = new ModificationTextDocument();
        doc.insertString(0, text, null);
        doc.putProperty(Language.class, TestJoinTopTokenId.language());
        LexerTestUtilities.incCheck(doc, true);
        doc.remove(6, 1);
        LexerTestUtilities.incCheck(doc, true);
        doc.insertString(6, "yz<uv>hk", null);
        LexerTestUtilities.incCheck(doc, true);
        doc.remove(12, 3);
        LexerTestUtilities.incCheck(doc, true);
        doc.insertString(12, "hkc", null);
        LexerTestUtilities.incCheck(doc, true);
        doc.insertString(7, "{", null);
        LexerTestUtilities.incCheck(doc, true);
        doc.insertString(16, "}", null);
        LexerTestUtilities.incCheck(doc, true);
        doc.insertString(9, "}", null);
        LexerTestUtilities.incCheck(doc, true);
    }

    public void testJoinSections() throws Exception {
        String text = "a(b<cd>e)f<gh>i(j<kl>m)n";
        ModificationTextDocument doc = new ModificationTextDocument();
        doc.insertString(0, text, null);
        doc.putProperty(Language.class, TestJoinTopTokenId.language());
        LexerTestUtilities.initLastTokenHierarchyEventListening(doc);
        TokenHierarchy<?> hi = TokenHierarchy.get(doc);
        TokenSequence<?> ts = hi.tokenSequence();
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.TEXT, "a(b", -1);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.TAG, "<cd>", -1);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.TEXT, "e)f", -1);
        LanguagePath innerLP = LanguagePath.get(TestJoinTopTokenId.language()).embedded(TestJoinTextTokenId.language);
        List<TokenSequence<?>> tsList = hi.tokenSequenceList(innerLP, 0, Integer.MAX_VALUE);
        checkInitialTokens(tsList);
        int i = 0;
        for (TokenSequence<?> ts2 : tsList) {
            assertSame(ts2, tsList.get(i++));
        }
        LexerTestUtilities.assertConsistency(hi);
        tsList = hi.tokenSequenceList(innerLP, 0, 7);
        assertEquals(1, tsList.size());
        tsList = hi.tokenSequenceList(innerLP, 0, 8);
        assertEquals(2, tsList.size());
        doc.remove(8, 1);
        LexerTestUtilities.assertConsistency(hi);
        LexerTestUtilities.incCheck(doc, true);
        TokenHierarchyEvent evt = LexerTestUtilities.getLastTokenHierarchyEvent(doc);
        assertNotNull(evt);
        TokenChange<?> tc = evt.tokenChange();
        assertNotNull(tc);
        assertEquals(2, tc.index());
        assertEquals(7, tc.offset());
        assertEquals(1, tc.addedTokenCount());
        assertEquals(1, tc.removedTokenCount());
        assertEquals(TestJoinTopTokenId.language(), tc.language());
        assertTrue(tc.isBoundsChange());
        assertEquals(1, tc.embeddedChangeCount());
        TokenChange<?> tcInner = tc.embeddedChange(0);
        TokenSequence<?> tsAdded = tcInner.currentTokenSequence();
        assertTrue(tsAdded.moveNext());
        LexerTestUtilities.assertTokenEquals(tsAdded, TestJoinTextTokenId.PARENS, "(befi(jm)", 1);
        assertEquals(1, tcInner.index());
        assertEquals(1, tcInner.offset());
        assertEquals(1, tcInner.addedTokenCount());
        assertEquals(3, tcInner.removedTokenCount());
        assertEquals(TestJoinTextTokenId.language, tcInner.language());
        assertEquals(0, tcInner.embeddedChangeCount());
        tsList = hi.tokenSequenceList(innerLP, 0, Integer.MAX_VALUE);
        assertEquals(4, tsList.size());
        ts = tsList.get(0);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.TEXT, "a", -1);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.PARENS, "(b", -1);
        Token<?> token = ts.token();
        assertEquals(PartType.START, token.partType());
        assertFalse(ts.moveNext());
        ts = tsList.get(1);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.PARENS, "ef", -1);
        token = ts.token();
        assertEquals(PartType.MIDDLE, token.partType());
        assertFalse(ts.moveNext());
        ts = tsList.get(2);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.PARENS, "i(j", -1);
        token = ts.token();
        assertEquals(PartType.MIDDLE, token.partType());
        assertFalse(ts.moveNext());
        ts = tsList.get(3);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.PARENS, "m)", -1);
        token = ts.token();
        assertEquals(PartType.END, token.partType());
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.TEXT, "n\n", -1);
        assertFalse(ts.moveNext());
        doc.insertString(8, ")", null);
        LexerTestUtilities.assertConsistency(hi);
        tsList = hi.tokenSequenceList(innerLP, 0, Integer.MAX_VALUE);
        checkInitialTokens(tsList);
        doc.remove(0, doc.getLength());
        LexerTestUtilities.assertConsistency(hi);
        ts = hi.tokenSequence();
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTopTokenId.TEXT, "\n", -1);
        assertFalse(ts.moveNext());
        doc.insertString(0, text, null);
    }

    private void checkInitialTokens(List<TokenSequence<?>> tsList) {
        assertEquals(4, tsList.size());
        TokenSequence<?> ts = tsList.get(0);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.TEXT, "a", -1);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.PARENS, "(b", -1);
        Token<?> token = ts.token();
        assertEquals(PartType.START, token.partType());
        assertFalse(ts.moveNext());
        ts = tsList.get(1);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.PARENS, "e)", -1);
        token = ts.token();
        assertEquals(PartType.END, token.partType());
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.TEXT, "f", -1);
        assertFalse(ts.moveNext());
        ts = tsList.get(2);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.TEXT, "i", -1);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.PARENS, "(j", -1);
        token = ts.token();
        assertEquals(PartType.START, token.partType());
        assertFalse(ts.moveNext());
        ts = tsList.get(3);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.PARENS, "m)", -1);
        token = ts.token();
        assertEquals(PartType.END, token.partType());
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestJoinTextTokenId.TEXT, "n\n", -1);
        assertFalse(ts.moveNext());
    }
}
