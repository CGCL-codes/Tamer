package ar.uba.dc.rfm.dynalloy.parser.splitter;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

public class DocumentTokenizerTest {

    public Document documentSingleLineWithoutBlank() throws IOException {
        Document d = new Document();
        d.Load("qwerty{daear}");
        return d;
    }

    public Document documentSingleLine() throws IOException {
        Document d = new Document();
        d.Load("qwerty { daear   }");
        return d;
    }

    public Document documentWithManySpaces() throws IOException {
        Document d = new Document();
        d.Load("   qwerty asd\tsdfs\n\n  \nanother\n");
        return d;
    }

    public Document documentTwoLines() throws IOException {
        Document d = new Document();
        d.Load("qwerty daear\nasd poiuyt");
        return d;
    }

    @Test
    public void firstToken() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(documentSingleLine());
        DocumentSection token = new DocumentSection();
        boolean n;
        n = tokenizer.next(token);
        assertTrue(n);
        assertEquals(new Position(1, 1), token.getFrom());
        assertEquals("qwerty", token.getText());
    }

    @Test
    public void secondToken() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(documentSingleLine());
        boolean n;
        DocumentSection token = new DocumentSection();
        n = tokenizer.next(token);
        n = tokenizer.next(token);
        assertTrue(n);
        assertEquals("{", token.getText());
        assertEquals(new Position(1, 8), token.getFrom());
    }

    @Test
    public void getTokenAfterManySpaces() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(documentSingleLine());
        boolean n;
        DocumentSection token = new DocumentSection();
        n = tokenizer.next(token);
        n = tokenizer.next(token);
        n = tokenizer.next(token);
        n = tokenizer.next(token);
        assertTrue(n);
        assertEquals("}", token.getText());
        assertEquals(new Position(1, 18), token.getFrom());
    }

    @Test
    public void returnFalseAfterLastToken() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(documentSingleLine());
        boolean n;
        DocumentSection token = new DocumentSection();
        n = tokenizer.next(token);
        n = tokenizer.next(token);
        n = tokenizer.next(token);
        n = tokenizer.next(token);
        n = tokenizer.next(token);
        assertFalse(n);
    }

    @Test
    public void getFirstTokenOfSecondLine() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(documentTwoLines());
        boolean n;
        DocumentSection token = new DocumentSection();
        n = tokenizer.next(token);
        n = tokenizer.next(token);
        n = tokenizer.next(token);
        assertTrue(n);
        assertEquals(new Position(2, 1), token.getFrom());
        assertEquals("asd", token.getText());
    }

    @Test
    public void getFirstTokenWithManySpaces() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(documentWithManySpaces());
        boolean n;
        DocumentSection token = new DocumentSection();
        n = tokenizer.next(token);
        assertTrue(n);
        assertEquals(new Position(1, 4), token.getFrom());
        assertEquals("qwerty", token.getText());
    }

    @Test
    public void getTokenAfterTabWithManySpaces() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(documentWithManySpaces());
        boolean n;
        DocumentSection token = new DocumentSection();
        n = tokenizer.next(token);
        n = tokenizer.next(token);
        n = tokenizer.next(token);
        assertTrue(n);
        assertEquals(new Position(1, 15), token.getFrom());
        assertEquals("sdfs", token.getText());
    }

    @Test
    public void testTokensOnDocumentWithManySpaces() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(documentWithManySpaces());
        boolean n;
        DocumentSection token = new DocumentSection();
        n = tokenizer.next(token);
        assertTrue(n);
        assertEquals("qwerty", token.getText());
        n = tokenizer.next(token);
        assertTrue(n);
        assertEquals("asd", token.getText());
        n = tokenizer.next(token);
        assertTrue(n);
        assertEquals("sdfs", token.getText());
        n = tokenizer.next(token);
        assertTrue(n);
        assertEquals("another", token.getText());
        n = tokenizer.next(token);
        assertFalse(n);
    }

    @Test
    public void testTokenWithoutSpaces() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(documentSingleLineWithoutBlank());
        assertTokens(Arrays.<String>asList("qwerty", "{", "daear", "}"), tokenizer);
    }

    @Test
    public void testAlloyPred() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(doc("module a/d pred somePred[a:T]{a == b}"));
        assertTokens(Arrays.<String>asList("module", "a", "/", "d", "pred", "somePred", "[", "a", ":", "T", "]", "{", "a", "=", "=", "b", "}"), tokenizer);
    }

    @Test
    public void testSingleLineCommentFromStartIsOneToken() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(doc("// nice comment\n//another\nend"));
        assertTokens(Arrays.<String>asList("// nice comment", "//another", "end"), tokenizer);
    }

    @Test
    public void testSingleLineCommentWithCommentInside() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(doc("text// nice comment //another"));
        assertTokens(Arrays.<String>asList("text", "// nice comment //another"), tokenizer);
    }

    @Test
    public void testSingleLineDashDashCommentFromStartIsOneToken() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(doc("-- nice comment\n--another\nend"));
        assertTokens(Arrays.<String>asList("-- nice comment", "--another", "end"), tokenizer);
    }

    @Test
    public void testSingleLineDashDashCommentWithCommentInside() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(doc("text-- nice comment --another"));
        assertTokens(Arrays.<String>asList("text", "-- nice comment --another"), tokenizer);
    }

    @Test
    public void testMultiLineCommentInASingleLine() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(doc("text1/* comment */text2"));
        assertTokens(Arrays.<String>asList("text1", "/* comment */", "text2"), tokenizer);
    }

    @Test
    public void testMultiLineCommentInManyLines() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(doc("text1/* comment\ncomment */text2"));
        assertTokens(Arrays.<String>asList("text1", "/* comment\ncomment */", "text2"), tokenizer);
    }

    @Test
    public void testMultiLineCommentWithTrickyChars() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(doc("text1/* comment*\n/comment /* */text2"));
        assertTokens(Arrays.<String>asList("text1", "/* comment*\n/comment /* */", "text2"), tokenizer);
    }

    @Test
    public void testNonClosedMultiLineComment() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(doc("text1/* comment*\n/comment "));
        assertTokens(Arrays.<String>asList("text1", "/* comment*\n/comment "), tokenizer);
    }

    @Test
    public void testNonClosedMultiLineComment2() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(doc("text1/* comment*\n/comment *"));
        assertTokens(Arrays.<String>asList("text1", "/* comment*\n/comment *"), tokenizer);
    }

    @Test
    public void testAlloyPredAndCommentedPred() throws Exception {
        DocumentTokenizer tokenizer = new DocumentTokenizer(doc("module a/d pred somePred[a:T]{a == b}"));
        assertTokens(Arrays.<String>asList("module", "a", "/", "d", "pred", "somePred", "[", "a", ":", "T", "]", "{", "a", "=", "=", "b", "}"), tokenizer);
    }

    private void assertTokens(List<String> list, DocumentTokenizer tokenizer) {
        DocumentSection token = new DocumentSection();
        int i = 0;
        while (tokenizer.next(token)) {
            assertEquals(list.get(i++), token.getText());
        }
        assertEquals(i, list.size());
    }

    private Document doc(String str) throws IOException {
        Document doc = new Document();
        doc.Load(str);
        return doc;
    }
}
