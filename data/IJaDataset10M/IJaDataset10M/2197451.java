package org.apache.solr.analysis;

import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Map;

/**
 * @version $Id: TestCapitalizationFilter.java 799593 2009-07-31 12:54:02Z ehatcher $
 */
public class TestCapitalizationFilter extends BaseTokenTestCase {

    public void testCapitalization() throws Exception {
        Map<String, String> args = new HashMap<String, String>();
        args.put(CapitalizationFilterFactory.KEEP, "and the it BIG");
        args.put(CapitalizationFilterFactory.ONLY_FIRST_WORD, "true");
        CapitalizationFilterFactory factory = new CapitalizationFilterFactory();
        factory.init(args);
        char[] termBuffer;
        termBuffer = "kiTTEN".toCharArray();
        factory.processWord(termBuffer, 0, termBuffer.length, 0);
        assertEquals("Kitten", new String(termBuffer, 0, termBuffer.length));
        factory.forceFirstLetter = true;
        termBuffer = "and".toCharArray();
        factory.processWord(termBuffer, 0, termBuffer.length, 0);
        assertEquals("And", new String(termBuffer, 0, termBuffer.length));
        termBuffer = "AnD".toCharArray();
        factory.processWord(termBuffer, 0, termBuffer.length, 0);
        assertEquals("And", new String(termBuffer, 0, termBuffer.length));
        factory.forceFirstLetter = false;
        termBuffer = "AnD".toCharArray();
        factory.processWord(termBuffer, 0, termBuffer.length, 0);
        assertEquals("And", new String(termBuffer, 0, termBuffer.length));
        factory.forceFirstLetter = true;
        termBuffer = "big".toCharArray();
        factory.processWord(termBuffer, 0, termBuffer.length, 0);
        assertEquals("Big", new String(termBuffer, 0, termBuffer.length));
        termBuffer = "BIG".toCharArray();
        factory.processWord(termBuffer, 0, termBuffer.length, 0);
        assertEquals("BIG", new String(termBuffer, 0, termBuffer.length));
        String out = tsToString(factory.create(new IterTokenStream("Hello thEre my Name is Ryan")));
        assertEquals("Hello there my name is ryan", out);
        factory.onlyFirstWord = false;
        out = tsToString(factory.create(new IterTokenStream("Hello thEre my Name is Ryan")));
        assertEquals("Hello There My Name Is Ryan", out);
        factory.minWordLength = 3;
        out = tsToString(factory.create(new IterTokenStream("Hello thEre my Name is Ryan")));
        assertEquals("Hello There my Name is Ryan", out);
        out = tsToString(factory.create(new IterTokenStream("McKinley")));
        assertEquals("Mckinley", out);
        factory = new CapitalizationFilterFactory();
        args.put("okPrefix", "McK");
        factory.init(args);
        out = tsToString(factory.create(new IterTokenStream("McKinley")));
        assertEquals("McKinley", out);
        factory.forceFirstLetter = false;
        factory.onlyFirstWord = false;
        out = tsToString(factory.create(new IterTokenStream("1st 2nd third")));
        assertEquals("1st 2nd Third", out);
        factory.forceFirstLetter = true;
        out = tsToString(factory.create(new IterTokenStream("the The the")));
        assertEquals("The The the", out);
    }

    public void testKeepIgnoreCase() throws Exception {
        Map<String, String> args = new HashMap<String, String>();
        args.put(CapitalizationFilterFactory.KEEP, "kitten");
        args.put(CapitalizationFilterFactory.KEEP_IGNORE_CASE, "true");
        args.put(CapitalizationFilterFactory.ONLY_FIRST_WORD, "true");
        CapitalizationFilterFactory factory = new CapitalizationFilterFactory();
        factory.init(args);
        char[] termBuffer;
        termBuffer = "kiTTEN".toCharArray();
        factory.forceFirstLetter = true;
        factory.processWord(termBuffer, 0, termBuffer.length, 0);
        assertEquals("KiTTEN", new String(termBuffer, 0, termBuffer.length));
        factory.forceFirstLetter = false;
        termBuffer = "kiTTEN".toCharArray();
        factory.processWord(termBuffer, 0, termBuffer.length, 0);
        assertEquals("kiTTEN", new String(termBuffer, 0, termBuffer.length));
        factory.keep = null;
        termBuffer = "kiTTEN".toCharArray();
        factory.processWord(termBuffer, 0, termBuffer.length, 0);
        assertEquals("Kitten", new String(termBuffer, 0, termBuffer.length));
    }
}
