package com.adobe.ac.ncss.utils;

import junit.framework.TestCase;
import org.junit.Test;

public class TestFileUtils extends TestCase {

    @Test
    public void testIsLineACorrectStatement() {
        assertFalse(FileUtils.isLineACorrectStatement("    { "));
        assertFalse(FileUtils.isLineACorrectStatement("    } "));
        assertFalse(FileUtils.isLineACorrectStatement("{"));
        assertFalse(FileUtils.isLineACorrectStatement("}"));
        assertFalse(FileUtils.isLineACorrectStatement("    class MyModel "));
        assertFalse(FileUtils.isLineACorrectStatement("class MyModel"));
        assertFalse(FileUtils.isLineACorrectStatement("function lala() : void"));
        assertFalse(FileUtils.isLineACorrectStatement("var i : int"));
        assertTrue(FileUtils.isLineACorrectStatement("var i : int;"));
        assertTrue(FileUtils.isLineACorrectStatement("  foo( bar );"));
    }
}
