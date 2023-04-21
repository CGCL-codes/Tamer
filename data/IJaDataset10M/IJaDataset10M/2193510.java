package net.sourceforge.etsysync.utils;

import java.util.Iterator;
import junit.framework.TestCase;

public class ResponsePatternTest extends TestCase {

    public void testHasNoChild() {
        ResponsePattern childlessPattern = new ResponsePattern("childlessPattern");
        assertFalse(childlessPattern.hasNestedPattern());
    }

    public void testGettingOutWhatYouPutIn() {
        String baseName = "testPattern";
        String[] elements = { "first", "second", "third" };
        ResponsePattern testPattern = new ResponsePattern(baseName);
        loadPattern(testPattern, elements);
        assertSimilarToArray(testPattern, elements);
        assertEquals(baseName, testPattern.getName());
    }

    private void loadPattern(ResponsePattern pattern, String[] elements) {
        for (String element : elements) {
            pattern.add(element);
        }
    }

    private void assertSimilarToArray(ResponsePattern pattern, String[] elements) {
        Iterator<ResponsePattern> iterator = pattern.iterator();
        for (String element : elements) {
            assertEquals(element, iterator.next().getName());
        }
    }

    public void testPassingNull() {
        ResponsePattern pattern = new ResponsePattern(null);
        assertNull(pattern.getName());
        assertNull(pattern.getString());
        assertNull(pattern.getLong());
        assertNull(pattern.getInteger());
    }
}
