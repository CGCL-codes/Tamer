package il.ac.biu.cs.grossmm.junit_api_tests.keys;

import java.io.Serializable;
import org.junit.Test;
import static junit.framework.Assert.*;
import il.ac.biu.cs.grossmm.api.keys.KeyPattern;
import il.ac.biu.cs.grossmm.api.keys.PatternEntry;
import static il.ac.biu.cs.grossmm.api.presence.BaseVocabulary.*;
import static il.ac.biu.cs.grossmm.api.keys.KeyPattern.*;
import static il.ac.biu.cs.grossmm.api.keys.PatternFactory.*;

public class PatternFactoryTest {

    @Test
    public void empty() {
        KeyPattern p = pattern((Serializable) null);
        assertNull(p.value());
        assertNull(p.valueClass());
        assertEquals(0, p.size());
        assertFalse(p.isMask());
    }

    @Test
    public void simpleExact() {
        KeyPattern p = pattern("Hello");
        assertEquals("Hello", p.value());
        assertNull(p.valueClass());
        assertEquals(0, p.size());
        assertFalse(p.isMask());
    }

    @Test
    public void simpleClass() {
        KeyPattern p = pattern(String.class);
        assertNull(p.value());
        assertEquals(String.class, p.valueClass());
        assertEquals(0, p.size());
        assertFalse(p.isMask());
    }

    @Test
    public void simpleExactMask() {
        KeyPattern p = maskPattern("Hello");
        assertEquals("Hello", p.value());
        assertNull(p.valueClass());
        assertEquals(0, p.size());
        assertTrue(p.isMask());
    }

    @Test
    public void simpleClassMask() {
        KeyPattern p = maskPattern(String.class);
        assertNull(p.value());
        assertEquals(String.class, p.valueClass());
        assertEquals(0, p.size());
        assertTrue(p.isMask());
    }

    @Test
    public void complex() {
        KeyPattern p = pattern("Presence", mandatory(WATCHER), optional(FILTER, ANY_SIMPLE_KEY), mandatory(CONTENT_TYPE, maskPattern(String.class, optional(RESOURCE_TYPE))));
        assertEquals("Presence", p.value());
        assertNotNull(p.subpattern(WATCHER));
        assertNull(p.subpattern(RESOURCE_TYPE));
        assertEquals(3, p.size());
        KeyPattern pp = p.subpattern(CONTENT_TYPE);
        assertNotNull(pp);
        assertTrue(pp.isMask());
        assertEquals(1, pp.size());
        PatternEntry pe = pp.entry(0);
        assertSame(RESOURCE_TYPE, pe.getAttribute());
    }
}
