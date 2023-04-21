package org.apache.solr.spelling;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.solr.common.util.NamedList;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Assert;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Test for SpellingQueryConverter
 *
 * @version $Id: SpellingQueryConverterTest.java 816091 2009-09-17 08:26:41Z shalin $
 * @since solr 1.3
 */
public class SpellingQueryConverterTest {

    @Test
    public void test() throws Exception {
        SpellingQueryConverter converter = new SpellingQueryConverter();
        converter.init(new NamedList());
        converter.setAnalyzer(new WhitespaceAnalyzer());
        Collection<Token> tokens = converter.convert("field:foo");
        assertTrue("tokens is null and it shouldn't be", tokens != null);
        assertTrue("tokens Size: " + tokens.size() + " is not: " + 1, tokens.size() == 1);
    }

    @Test
    public void testSpecialChars() {
        SpellingQueryConverter converter = new SpellingQueryConverter();
        converter.init(new NamedList());
        converter.setAnalyzer(new WhitespaceAnalyzer());
        String original = "field_with_underscore:value_with_underscore";
        Collection<Token> tokens = converter.convert(original);
        assertTrue("tokens is null and it shouldn't be", tokens != null);
        assertEquals("tokens Size: " + tokens.size() + " is not 1", 1, tokens.size());
        assertTrue("Token offsets do not match", isOffsetCorrect(original, tokens));
        original = "field_with_digits123:value_with_digits123";
        tokens = converter.convert(original);
        assertTrue("tokens is null and it shouldn't be", tokens != null);
        assertEquals("tokens Size: " + tokens.size() + " is not 1", 1, tokens.size());
        assertTrue("Token offsets do not match", isOffsetCorrect(original, tokens));
        original = "field-with-hyphens:value-with-hyphens";
        tokens = converter.convert(original);
        assertTrue("tokens is null and it shouldn't be", tokens != null);
        assertEquals("tokens Size: " + tokens.size() + " is not 1", 1, tokens.size());
        assertTrue("Token offsets do not match", isOffsetCorrect(original, tokens));
        original = "foo:bar^5.0";
        tokens = converter.convert(original);
        assertTrue("tokens is null and it shouldn't be", tokens != null);
        assertEquals("tokens Size: " + tokens.size() + " is not 1", 1, tokens.size());
        assertTrue("Token offsets do not match", isOffsetCorrect(original, tokens));
    }

    private boolean isOffsetCorrect(String s, Collection<Token> tokens) {
        for (Token token : tokens) {
            int start = token.startOffset();
            int end = token.endOffset();
            if (!s.substring(start, end).equals(token.term())) return false;
        }
        return true;
    }

    @Test
    public void testUnicode() {
        SpellingQueryConverter converter = new SpellingQueryConverter();
        converter.init(new NamedList());
        converter.setAnalyzer(new WhitespaceAnalyzer());
        Collection<Token> tokens = converter.convert("text_field:我购买了道具和服装。");
        assertTrue("tokens is null and it shouldn't be", tokens != null);
        assertEquals("tokens Size: " + tokens.size() + " is not 1", 1, tokens.size());
        tokens = converter.convert("text_购field:我购买了道具和服装。");
        assertTrue("tokens is null and it shouldn't be", tokens != null);
        assertEquals("tokens Size: " + tokens.size() + " is not 1", 1, tokens.size());
        tokens = converter.convert("text_field:我购xyz买了道具和服装。");
        assertTrue("tokens is null and it shouldn't be", tokens != null);
        assertEquals("tokens Size: " + tokens.size() + " is not 1", 1, tokens.size());
    }

    @Test
    public void testMultipleClauses() {
        SpellingQueryConverter converter = new SpellingQueryConverter();
        converter.init(new NamedList());
        converter.setAnalyzer(new WhitespaceAnalyzer());
        Collection<Token> tokens = converter.convert("买text_field:我购买了道具和服装。 field2:bar");
        assertTrue("tokens is null and it shouldn't be", tokens != null);
        assertEquals("tokens Size: " + tokens.size() + " is not 2", 2, tokens.size());
        tokens = converter.convert("text_field:我购买了道具和服装。 bar");
        assertTrue("tokens is null and it shouldn't be", tokens != null);
        assertEquals("tokens Size: " + tokens.size() + " is not 2", 2, tokens.size());
    }
}
