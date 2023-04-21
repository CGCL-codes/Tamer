package org.scribble.parser;

import org.scribble.model.ModelReference;
import junit.framework.TestCase;

public class DefaultParserContextTest extends TestCase {

    protected void setUp() throws Exception {
        org.scribble.extensions.TestRegistry reg = new org.scribble.extensions.TestRegistry();
        reg.addExtension(GenericKeyWordProvider.class);
        reg.addExtension(GenericParser.class);
        reg.addExtension(TestModelRepository.class);
        org.scribble.extensions.RegistryFactory.setRegistry(reg);
    }

    protected void tearDown() throws Exception {
        org.scribble.extensions.RegistryFactory.setRegistry(null);
    }

    public void testTokenLineComment() {
        String id1 = "ID1";
        String id2 = "ID2";
        String text = id1 + "\r\n // Line comment \r\n" + id2;
        ModelReference ref = new ModelReference("");
        DefaultParserContext context = new DefaultParserContext(ref, new java.io.ByteArrayInputStream(text.getBytes()), new DefaultTokenizer());
        if (context.getTokenCount() != 2) {
            fail("Expecting 2 tokens, but got: " + context.getTokenCount());
        }
    }

    public void testTokenBlockComment() {
        String id1 = "ID1";
        String id2 = "ID2";
        String text = id1 + "\r\n /* Block comment starts \r\n and continues */ \r\n" + id2;
        ModelReference ref = new ModelReference("");
        DefaultParserContext context = new DefaultParserContext(ref, new java.io.ByteArrayInputStream(text.getBytes()), new DefaultTokenizer());
        if (context.getTokenCount() != 2) {
            fail("Expecting 2 tokens, but got: " + context.getTokenCount());
        }
    }

    public void testTokenKeyword() {
        String id1 = "ID1";
        String id2 = "ID2";
        String keyword = "role";
        String text = id1 + " " + keyword + " " + id2;
        ModelReference ref = new ModelReference("");
        DefaultParserContext context = new DefaultParserContext(ref, new java.io.ByteArrayInputStream(text.getBytes()), new DefaultTokenizer());
        if (context.getTokenCount() != 3) {
            fail("Expecting 3 tokens, but got: " + context.getTokenCount());
        }
        if (context.lookahead(1).getType() != TokenType.Keyword) {
            fail("Expecting keyword type, but got: " + context.lookahead(1).getType());
        }
    }

    public void testTokenStringLiteral() {
        String str = "hello world";
        String text = "first \"" + str + "\" second";
        ModelReference ref = new ModelReference("");
        DefaultParserContext context = new DefaultParserContext(ref, new java.io.ByteArrayInputStream(text.getBytes()), new DefaultTokenizer());
        if (context.getTokenCount() != 3) {
            fail("Expecting 3 tokens, but got: " + context.getTokenCount());
        }
        Token token = context.lookahead(1);
        if (token.getType() != TokenType.StringLiteral) {
            fail("Not string literal");
        }
        if (token.getText().equals(str) == false) {
            fail("String '" + token.getText() + "' incorrect, expecting: " + str);
        }
    }

    public void testTokenStringLiteralLineTerminated() {
        String str = "hello world";
        String text = "first \"" + str + "\r\n";
        ModelReference ref = new ModelReference("");
        DefaultParserContext context = new DefaultParserContext(ref, new java.io.ByteArrayInputStream(text.getBytes()), new DefaultTokenizer());
        if (context.getTokenCount() != 2) {
            fail("Expecting 2 tokens, but got: " + context.getTokenCount());
        }
        Token token = context.lookahead(1);
        if (token.getType() != TokenType.StringLiteral) {
            fail("Not string literal");
        }
        if (token.getText().equals(str) == false) {
            fail("String '" + token.getText() + "' incorrect, expecting: " + str);
        }
    }
}
