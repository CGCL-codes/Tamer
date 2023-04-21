package org.webiyo.examples.viewjava;

import junit.framework.TestCase;
import java.io.File;
import java.util.Iterator;

public class SourceIndexTest extends TestCase {

    public void testFindAllJavaFiles() throws Exception {
        SourceIndex index = new SourceIndex(new SourceDir("test/data/JavaProjectTest/src"), new SourceDir("test/data/JavaProjectTest/src2"));
        Iterator<SourceFile> files = index.iterator();
        SourceFile hello = files.next();
        assertEquals("Hello", hello.getClassName());
        assertEquals(new File("test/data/JavaProjectTest/src/Hello.java"), hello.getFile());
        assertEquals("public class Hello {\n" + "    // comment\n" + "    public static void main(String[] args) {\n" + "        System.out.println(\"hello\");\n" + "    }\n" + "}\n", hello.loadSource());
        assertEquals("package1.File1", files.next().getClassName());
        assertEquals("package2.File1", files.next().getClassName());
        assertFalse(files.hasNext());
    }
}
