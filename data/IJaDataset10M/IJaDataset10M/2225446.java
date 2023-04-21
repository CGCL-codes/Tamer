package org.netbeans.lib.lexer.inc;

import org.netbeans.lib.lexer.lang.TestTokenId;
import javax.swing.text.Document;
import org.netbeans.api.lexer.Language;
import org.netbeans.lib.lexer.test.ModificationTextDocument;
import org.netbeans.lib.lexer.test.simple.*;
import org.netbeans.api.lexer.TokenChange;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenHierarchyEvent;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.junit.NbTestCase;
import org.netbeans.lib.lexer.test.LexerTestUtilities;
import org.netbeans.lib.lexer.lang.TestPlainTokenId;
import org.netbeans.spi.lexer.MutableTextInput;
import org.netbeans.spi.lexer.TokenHierarchyControl;

/**
 * Test several simple lexer impls.
 *
 * @author mmetelka
 */
public class EmbeddingUpdateTest extends NbTestCase {

    public EmbeddingUpdateTest(String testName) {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception {
    }

    protected void tearDown() throws java.lang.Exception {
    }

    public void testEmbeddingUpdate() throws Exception {
        Document doc = new ModificationTextDocument();
        doc.putProperty(Language.class, TestTokenId.language());
        doc.insertString(0, "a/*abc def*/", null);
        LexerTestUtilities.initLastTokenHierarchyEventListening(doc);
        TokenHierarchy<?> hi = TokenHierarchy.get(doc);
        TokenSequence<?> ts = hi.tokenSequence();
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestTokenId.IDENTIFIER, "a", 0);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestTokenId.BLOCK_COMMENT, "/*abc def*/", 1);
        TokenSequence<?> ets = ts.embedded();
        assertNotNull(ets);
        assertTrue(ts.moveNext());
        assertTrue(ets.moveNext());
        LexerTestUtilities.assertTokenEquals(ets, TestPlainTokenId.WORD, "abc", 3);
        assertTrue(ets.moveNext());
        LexerTestUtilities.assertTokenEquals(ets, TestPlainTokenId.WHITESPACE, " ", 6);
        assertTrue(ets.moveNext());
        LexerTestUtilities.assertTokenEquals(ets, TestPlainTokenId.WORD, "def", 7);
        assertFalse(ets.moveNext());
        doc.insertString(4, "x", null);
        TokenHierarchyEvent evt = LexerTestUtilities.getLastTokenHierarchyEvent(doc);
        assertNotNull(evt);
        TokenChange<?> tc = evt.tokenChange();
        assertNotNull(tc);
        assertEquals(1, tc.index());
        assertEquals(1, tc.offset());
        assertEquals(1, tc.addedTokenCount());
        assertEquals(1, tc.removedTokenCount());
        assertEquals(TestTokenId.language(), tc.language());
        assertEquals(1, tc.embeddedChangeCount());
        TokenChange<?> etc = tc.embeddedChange(0);
        assertEquals(0, etc.index());
        assertEquals(3, etc.offset());
        assertEquals(1, etc.addedTokenCount());
        assertEquals(1, etc.removedTokenCount());
        assertEquals(TestPlainTokenId.language(), etc.language());
        assertEquals(0, etc.embeddedChangeCount());
        doc.remove(3, 8);
        doc.insertString(3, "x", null);
    }

    public void testEmbeddingActivityChange() throws Exception {
        Document doc = new ModificationTextDocument();
        doc.putProperty(Language.class, TestTokenId.language());
        doc.insertString(0, "a/*abc def*/", null);
        LexerTestUtilities.initLastTokenHierarchyEventListening(doc);
        TokenHierarchy<?> hi = TokenHierarchy.get(doc);
        TokenSequence<?> ts = hi.tokenSequence();
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestTokenId.IDENTIFIER, "a", 0);
        assertTrue(ts.moveNext());
        LexerTestUtilities.assertTokenEquals(ts, TestTokenId.BLOCK_COMMENT, "/*abc def*/", 1);
        TokenSequence<?> ets = ts.embedded();
        assertNotNull(ets);
        assertTrue(ts.moveNext());
        assertTrue(ets.moveNext());
        LexerTestUtilities.assertTokenEquals(ets, TestPlainTokenId.WORD, "abc", 3);
        assertTrue(ets.moveNext());
        LexerTestUtilities.assertTokenEquals(ets, TestPlainTokenId.WHITESPACE, " ", 6);
        assertTrue(ets.moveNext());
        LexerTestUtilities.assertTokenEquals(ets, TestPlainTokenId.WORD, "def", 7);
        assertFalse(ets.moveNext());
        MutableTextInput input = (MutableTextInput) doc.getProperty(MutableTextInput.class);
        TokenHierarchyControl control = input.tokenHierarchyControl();
        control.setActive(false);
        control.setActive(true);
    }
}
