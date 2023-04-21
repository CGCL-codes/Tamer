package net.sf.grotag.common;

import static org.junit.Assert.*;
import java.io.File;
import net.sf.grotag.common.Tools;
import org.junit.Before;
import org.junit.Test;

public class ToolsTest {

    private Tools tools;

    private TestTools testTools;

    @Before
    public void setUp() throws Exception {
        testTools = TestTools.getInstance();
        tools = Tools.getInstance();
    }

    @Test
    public void testWithoutTrailingWhiteSpace() {
        assertEquals("hugo", tools.withoutTrailingWhiteSpace("hugo"));
        assertEquals("hugo", tools.withoutTrailingWhiteSpace("hugo "));
        assertEquals("hugo", tools.withoutTrailingWhiteSpace("hugo \t \t"));
        assertEquals(" hugo", tools.withoutTrailingWhiteSpace(" hugo"));
        assertEquals("", tools.withoutTrailingWhiteSpace(" "));
        assertEquals("", tools.withoutTrailingWhiteSpace(""));
    }

    @Test
    public void testGetToken() {
        assertArrayEquals(null, tools.getToken("", 0));
        assertArrayEquals(null, tools.getToken(" ", 0));
        assertArrayEquals(new String[] { "", "x" }, tools.getToken("x", 0));
        assertArrayEquals(new String[] { "", "xyz" }, tools.getToken("xyz", 0));
        assertArrayEquals(new String[] { " ", "x" }, tools.getToken(" x", 0));
        assertArrayEquals(new String[] { "", "x" }, tools.getToken("x=y", 0));
        assertArrayEquals(new String[] { "", "=" }, tools.getToken("x=y", 1));
        assertArrayEquals(new String[] { "", "x" }, tools.getToken("x y", 0));
        assertArrayEquals(new String[] { "", "y" }, tools.getToken("x y", 2));
        assertArrayEquals(new String[] { " ", "y" }, tools.getToken("x y", 1));
    }

    @Test
    public void testSeparated() {
        assertNull(tools.separated(null));
        assertNull(tools.separated(""));
        assertNull(tools.separated("\t "));
        assertArrayEquals(new String[] { "hugo" }, tools.separated("hugo"));
        assertArrayEquals(new String[] { "hugo", "sepp" }, tools.separated("hugo, sepp"));
        assertArrayEquals(new String[] { "hugo", "sepp" }, tools.separated("hugo; sepp"));
        assertArrayEquals(new String[] { "hugo,x", "sepp" }, tools.separated("hugo,x; sepp"));
    }

    @Test
    public void testCutOffAt() {
        assertEquals("hugo", tools.cutOffAt("hugo", ','));
        assertEquals("hugo", tools.cutOffAt("hugo,x", ','));
        assertEquals("", tools.cutOffAt(",hugo,x", ','));
    }

    @Test
    public void testSplitFile() {
        String[] fileParts = tools.splitFile(testTools.getTestActualFile("hugo"));
        assertNotNull(fileParts);
        assertTrue(fileParts.length > 0);
    }

    @Test
    public void testGetRelativeUrlTo() {
        File linkingFile;
        File targetFile;
        linkingFile = testTools.getTestInputFile("linking.html");
        targetFile = testTools.getTestActualFile("target.html");
        assertEquals("../actual/target.html", tools.getRelativeUrl(linkingFile, targetFile));
    }
}
