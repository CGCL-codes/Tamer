package com.google.gwt.dev.javac;

import com.google.gwt.core.ext.TreeLogger;
import junit.framework.TestCase;

/**
 * Unit test for {@link MemoryUnitCache}.
 */
public class MemoryUnitCacheTest extends TestCase {

    public void testMemoryCache() {
        TreeLogger logger = TreeLogger.NULL;
        MemoryUnitCache cache = new MemoryUnitCache();
        MockCompilationUnit foo1 = new MockCompilationUnit("com.example.Foo", "source1");
        cache.add(foo1);
        MockCompilationUnit bar1 = new MockCompilationUnit("com.example.Bar", "source2");
        cache.add(bar1);
        CompilationUnit result;
        result = cache.find(foo1.getContentId());
        assertNotNull(result);
        assertEquals("com.example.Foo", result.getTypeName());
        result = cache.find(bar1.getContentId());
        assertNotNull(result);
        assertEquals("com.example.Bar", result.getTypeName());
        result = cache.find("com/example/Foo.java");
        assertNotNull(result);
        assertEquals("com.example.Foo", result.getTypeName());
        result = cache.find("com/example/Bar.java");
        assertNotNull(result);
        assertEquals("com.example.Bar", result.getTypeName());
        cache.cleanup(logger);
        MockCompilationUnit foo2 = new MockCompilationUnit("com.example.Foo", "source3");
        cache.add(foo2);
        result = cache.find(foo1.getContentId());
        assertNull(result);
        result = cache.find(foo2.getContentId());
        assertNotNull(result);
        assertEquals("com.example.Foo", result.getTypeName());
        result = cache.find("com/example/Foo.java");
        assertNotNull(result);
        assertEquals("com.example.Foo", result.getTypeName());
        assertEquals(foo2.getContentId(), result.getContentId());
        cache.remove(bar1);
        result = cache.find(bar1.getContentId());
        assertNull(result);
        result = cache.find("com/example.Bar");
        assertNull(result);
    }
}
