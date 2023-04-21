package net.sf.gridarta.model.io;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link PathManager}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class PathManagerTest {

    /**
     * Test case for {@link PathManager#path(CharSequence)}.
     */
    @Test
    public void testPath() {
        Assert.assertEquals("Expecting trailing slash from directories being removed.", "/foo", PathManager.path("/foo/"));
        Assert.assertEquals("Expecting file: URIs being converted to URIs without scheme.", "/foo", PathManager.path("file:/foo/"));
        Assert.assertEquals("Expecting multiple // characters to be collapsed.", "/foo/bar", PathManager.path("//foo///bar"));
    }

    /**
     * Test case for {@link PathManager#absoluteToRelative(String, String)}.
     */
    @Test
    public void testAbsoluteToRelative() {
        Assert.assertEquals("../stoneglow/stoneglow_0000", PathManager.absoluteToRelative("/relic/castle_0000", "/stoneglow/stoneglow_0000"));
        Assert.assertEquals("../stoneglow/stoneglow_0000", PathManager.absoluteToRelative("/relic/", "/stoneglow/stoneglow_0000"));
    }

    /**
     * Test case for {@link PathManager#absoluteToRelative(String, String)}.
     */
    @Test
    public void testRelativeToAbsolute() {
        Assert.assertEquals("/stoneglow/stoneglow_0000", PathManager.relativeToAbsolute("/relic/castle_0000", "../stoneglow/stoneglow_0000"));
        Assert.assertEquals("/stoneglow/stoneglow_0000", PathManager.relativeToAbsolute("/relic/", "../stoneglow/stoneglow_0000"));
        Assert.assertEquals("/stoneglow/stoneglow_0000", PathManager.relativeToAbsolute("/relic/castle_0000", "/stoneglow/stoneglow_0000"));
        Assert.assertEquals("/stoneglow/stoneglow_0000", PathManager.relativeToAbsolute("/relic/", "/stoneglow/stoneglow_0000"));
    }

    /**
     * Test case for {@link PathManager#isRelative(String)}.
     */
    @Test
    public void testIsRelative() {
        Assert.assertTrue(PathManager.isRelative("../stoneglow/stoneglow_0000"));
        Assert.assertTrue(PathManager.isRelative("stoneglow/stoneglow_0000"));
        Assert.assertFalse(PathManager.isRelative("/stoneglow/stoneglow_0000"));
        Assert.assertTrue(PathManager.isRelative(""));
    }

    /**
     * Test case for {@link PathManager#isAbsolute(String)}.
     */
    @Test
    public void testIsAbsolute() {
        Assert.assertFalse(PathManager.isAbsolute("../stoneglow/stoneglow_0000"));
        Assert.assertFalse(PathManager.isAbsolute("stoneglow/stoneglow_0000"));
        Assert.assertTrue(PathManager.isAbsolute("/stoneglow/stoneglow_0000"));
        Assert.assertFalse(PathManager.isAbsolute(""));
    }
}
