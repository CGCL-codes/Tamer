package com.jeantessier.classreader;

import junit.framework.*;

public class TestClassNameHelper extends TestCase {

    public void testPath2ClassName() {
        assertEquals("I", ClassNameHelper.path2ClassName("I"));
        assertEquals("L", ClassNameHelper.path2ClassName("L"));
        assertEquals("V", ClassNameHelper.path2ClassName("V"));
        assertEquals("List", ClassNameHelper.path2ClassName("List"));
        assertEquals("package.Class", ClassNameHelper.path2ClassName("package/Class"));
    }

    public void testConvertClassName() {
        assertEquals("I", ClassNameHelper.convertClassName("I"));
        assertEquals("I[]", ClassNameHelper.convertClassName("[I"));
        assertEquals("package.Class", ClassNameHelper.convertClassName("package/Class"));
        assertEquals("package.Class", ClassNameHelper.convertClassName("Lpackage/Class;"));
        assertEquals("package.Class[]", ClassNameHelper.convertClassName("[Lpackage/Class;"));
        assertEquals("List", ClassNameHelper.convertClassName("List"));
    }
}
