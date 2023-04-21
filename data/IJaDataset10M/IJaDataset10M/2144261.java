package ru.adv.test.util;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import ru.adv.test.AbstractTest;
import ru.adv.util.URLMapper;

public class URLMapperTest extends AbstractTest {

    @Test
    public void test() throws Exception {
        assertTrue(!URLMapper.match("/private/*", "/b.xml"));
        assertTrue(URLMapper.match("/*", "/"));
        assertTrue(URLMapper.match("/*", "/index.bop"));
        assertTrue(!URLMapper.match("/*", "/foo/baz/index.bop"));
        assertTrue(!URLMapper.match("/foo/bar/*", "/foo/baz/index.bop"));
        assertTrue(URLMapper.match("/foo/bar/*", "/foo/bar/index.html"));
        assertTrue(URLMapper.match("/foo/bar/*", "/foo/bar/index.bop"));
        assertTrue(!URLMapper.match("/foo/bar/*", "/foo/bar/baz/index.bop"));
        assertTrue(URLMapper.match("/catalog", "/catalog"));
        assertTrue(!URLMapper.match("/catalog", "/catalog/index.html"));
        assertTrue(!URLMapper.match("/catalog", "/catalog/racecar.bop"));
        assertTrue(URLMapper.match("/baz/*", "/baz/"));
        assertTrue(URLMapper.match("/baz/*", "/baz"));
        assertTrue(URLMapper.match("/baz/*", "/baz/index.html"));
        assertTrue(URLMapper.match("*.bop", "/catalog/racecar.bop"));
        assertTrue(URLMapper.match("*.bop", "/index.bop"));
        assertTrue(!URLMapper.match("*.bop", "/index.xml"));
        assertTrue(URLMapper.match("/index.xml", "/index.xml"));
        assertTrue(!URLMapper.match("/index.xml", "/foo/bar/index.xml"));
        assertTrue(URLMapper.match("/foo/bar/index.html", "/foo/bar/index.html"));
        assertTrue(!URLMapper.match("/foo/bar/index.html", "/index.html"));
        assertTrue(URLMapper.match("/", "/"));
        assertTrue(!URLMapper.match("/", "/index.html"));
    }
}
