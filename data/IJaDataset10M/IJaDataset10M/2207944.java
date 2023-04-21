package edu.ucla.sspace.text;

import java.io.*;
import java.util.*;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class IteratorFactoryTests {

    @Test
    public void testNoConfig() {
        Iterator<String> it = IteratorFactory.tokenize(getReader());
        assertEquals("this", it.next());
        assertEquals("is", it.next());
        assertEquals("my", it.next());
        assertEquals("example", it.next());
        assertEquals("sentence", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testWithFilter() throws IOException {
        File validTokens = createFileWithText("this\nmy\nexample\nsentence");
        String filterProp = "include=" + validTokens.getAbsolutePath();
        Properties props = new Properties();
        props.setProperty(IteratorFactory.TOKEN_FILTER_PROPERTY, filterProp);
        IteratorFactory.setProperties(props);
        Iterator<String> it = IteratorFactory.tokenize(getReader());
        assertEquals("this", it.next());
        assertEquals("my", it.next());
        assertEquals("example", it.next());
        assertEquals("sentence", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testWithCompounds() throws IOException {
        File compounds = createFileWithText("example sentence");
        Properties props = new Properties();
        props.setProperty(IteratorFactory.COMPOUND_TOKENS_FILE_PROPERTY, compounds.getAbsolutePath());
        IteratorFactory.setProperties(props);
        Iterator<String> it = IteratorFactory.tokenize(getReader());
        assertEquals("this", it.next());
        assertEquals("is", it.next());
        assertEquals("my", it.next());
        assertEquals("example sentence", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testWithCompoundsAndFilter() throws IOException {
        File validTokens = createFileWithText("this\nmy\nexample sentence");
        String filterProp = "include=" + validTokens.getAbsolutePath();
        Properties props = new Properties();
        props.setProperty(IteratorFactory.TOKEN_FILTER_PROPERTY, filterProp);
        File compounds = createFileWithText("example sentence");
        props.setProperty(IteratorFactory.COMPOUND_TOKENS_FILE_PROPERTY, compounds.getAbsolutePath());
        IteratorFactory.setProperties(props);
        Iterator<String> it = IteratorFactory.tokenize(getReader());
        assertEquals("this", it.next());
        assertEquals("my", it.next());
        assertEquals("example sentence", it.next());
        assertFalse(it.hasNext());
    }

    public static File createFileWithText(String text) throws IOException {
        File tmp = File.createTempFile("test", ".txt");
        PrintWriter pw = new PrintWriter(tmp);
        pw.println(text);
        pw.close();
        return tmp;
    }

    public static BufferedReader getReader() {
        return new BufferedReader(new StringReader("this is my example sentence"));
    }
}
