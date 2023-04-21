package org.neurox.esearch.test.parser;

import java.net.URL;
import junit.framework.TestCase;
import org.neurox.esearch.internal.SimpleURLCreator;

public class TestURLCreator extends TestCase {

    public void testSimpleURLCreator() throws Exception {
        String prefix = "http://www.google.ch/search?hl=de&q=";
        String postfix = "&btnG=Suche&meta=";
        String seperator = "+";
        String searchString = "test string";
        SimpleURLCreator creator = new SimpleURLCreator(prefix, postfix, seperator);
        URL original = new URL("http://www.google.ch/search?hl=de&q=test+string&btnG=Suche&meta=");
        assertEquals(original, creator.createSearchURL(searchString));
    }
}
