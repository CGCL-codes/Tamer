package com.mebigfatguy.smax;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Assert;
import org.junit.Test;

public class SmaxParserTest {

    @Test
    public void testOneElement() throws Exception {
        SmaxParser parser = new SmaxParser();
        Reader r = new BufferedReader(new StringReader("<test1></test1>"));
        SmaxToken t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.StartTag, t.getTokenType());
        Assert.assertEquals("test1", t.getTokenValue());
        t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.EndTag, t.getTokenType());
        Assert.assertEquals("test1", t.getTokenValue());
    }

    @Test
    public void testEmptyElement() throws Exception {
        SmaxParser parser = new SmaxParser();
        Reader r = new BufferedReader(new StringReader("<test/>"));
        SmaxToken t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.EmptyTag, t.getTokenType());
        Assert.assertEquals("test", t.getTokenValue());
    }

    @Test
    public void testAttributes() throws Exception {
        SmaxParser parser = new SmaxParser();
        Reader r = new BufferedReader(new StringReader("<test one='1' two = \"2\" ></test>"));
        SmaxToken t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.StartTag, t.getTokenType());
        Assert.assertEquals("test", t.getTokenValue());
        t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.AttributeName, t.getTokenType());
        Assert.assertEquals("one", t.getTokenValue());
        t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.Equals, t.getTokenType());
        Assert.assertEquals("=", t.getTokenValue());
        t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.AttributeValue, t.getTokenType());
        Assert.assertEquals("1", t.getTokenValue());
        t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.AttributeName, t.getTokenType());
        Assert.assertEquals("two", t.getTokenValue());
        t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.Equals, t.getTokenType());
        Assert.assertEquals("=", t.getTokenValue());
        t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.AttributeValue, t.getTokenType());
        Assert.assertEquals("2", t.getTokenValue());
    }

    @Test
    public void testPCData() throws Exception {
        SmaxParser parser = new SmaxParser();
        Reader r = new BufferedReader(new StringReader("<test>Sample Data</test>"));
        SmaxToken t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.StartTag, t.getTokenType());
        Assert.assertEquals("test", t.getTokenValue());
        t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.PCData, t.getTokenType());
        Assert.assertEquals("Sample Data", t.getTokenValue());
        t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.EndTag, t.getTokenType());
        Assert.assertEquals("test", t.getTokenValue());
    }

    @Test
    public void testComment() throws Exception {
        SmaxParser parser = new SmaxParser();
        Reader r = new BufferedReader(new StringReader("<test><!--This is a test--></test>"));
        SmaxToken t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.StartTag, t.getTokenType());
        Assert.assertEquals("test", t.getTokenValue());
        t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.Comment, t.getTokenType());
        Assert.assertEquals("This is a test", t.getTokenValue());
        t = parser.getNextToken(r);
        Assert.assertEquals(SmaxTokenType.EndTag, t.getTokenType());
        Assert.assertEquals("test", t.getTokenValue());
    }
}
