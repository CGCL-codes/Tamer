package com.dodalizer.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import junit.framework.TestCase;
import com.dodalizer.parser.classes.JavaClass;
import com.dodalizer.parser.method.JavaMethod;
import com.dodalizer.parser.source.JavaSource;

public class JavaParserTest extends TestCase {

    public void test_compilationUnit_invalid_file() throws Exception {
        JavaParser parser = new JavaParser();
        try {
            parser.parseFile("testdata/NoTestJavaFile.java");
            fail("No Exception Thrown.");
        } catch (Exception e) {
            if (!(e instanceof FileNotFoundException)) {
                fail("Wrong Exception Thrown");
            }
        }
    }

    public void test_compilationUnit() throws Exception {
        JavaParser parser = new JavaParser();
        JavaSource source = parser.parseFile("testdata/TestJavaFile.java");
        assertEquals("Incorrect Package Name", "com.dodalizer.parser.source", source.getPackage());
        JavaClass[] classes = source.getClasses();
        assertNotNull("No classes found.", classes);
        assertEquals(1, classes.length);
        assertEquals("TestJavaFile", classes[0].getName());
        assertFalse(classes[0].isInterface());
        assertEquals("SuperTestJavaFile", classes[0].getExtends()[0]);
        JavaMethod[] methods = classes[0].getMethods();
        assertNotNull(methods);
        assertEquals(2, methods.length);
        assertTrue(methods[0].getModifierSet().isPublic());
        assertEquals("getName", methods[0].getName());
        assertEquals("String", methods[0].getReturnType());
        assertFalse(methods[1].getModifierSet().isPublic());
        assertTrue(methods[1].getModifierSet().isPrivate());
        assertEquals("setName", methods[1].getName());
        assertEquals("void", methods[1].getReturnType());
    }
}
