package joptsimple.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:pholser@alumni.rice.edu">Paul Holser</a>
 */
public class KeyValuePairTest {

    @Test(expected = NullPointerException.class)
    public void nullArg() {
        KeyValuePair.valueOf(null);
    }

    @Test
    public void empty() {
        KeyValuePair pair = KeyValuePair.valueOf("");
        assertEquals("", pair.key);
        assertEquals("", pair.value);
    }

    @Test
    public void noEqualsSign() {
        KeyValuePair pair = KeyValuePair.valueOf("aString");
        assertEquals("aString", pair.key);
        assertEquals("", pair.value);
    }

    @Test
    public void signAtEnd() {
        KeyValuePair pair = KeyValuePair.valueOf("aKey=");
        assertEquals("aKey", pair.key);
        assertEquals("", pair.value);
    }

    @Test
    public void signAtStart() {
        KeyValuePair pair = KeyValuePair.valueOf("=aValue");
        assertEquals("", pair.key);
        assertEquals("aValue", pair.value);
    }

    @Test
    public void typical() {
        KeyValuePair pair = KeyValuePair.valueOf("aKey=aValue");
        assertEquals("aKey", pair.key);
        assertEquals("aValue", pair.value);
    }

    @Test
    public void multipleEqualsSignsDoNotMatter() {
        KeyValuePair pair = KeyValuePair.valueOf("aKey=1=2=3=4");
        assertEquals("aKey", pair.key);
        assertEquals("1=2=3=4", pair.value);
    }
}
