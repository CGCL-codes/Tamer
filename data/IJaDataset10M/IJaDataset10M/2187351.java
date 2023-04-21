package com.ownedthx.xmldoclet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.ownedthx.xmldoclet.xmlbindings.Package;
import com.ownedthx.xmldoclet.xmlbindings.*;
import com.ownedthx.xmldoclet.xmlbindings.Class;
import com.ownedthx.xmldoclet.xmlbindings.Enum;
import java.util.ArrayList;

/**
 * Unit test group for Enums
 */
public class EnumTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public EnumTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(EnumTest.class);
    }

    public void setUp() {
        TestGlobals.initializeLogging();
    }

    /**
     * testing a simple enum
     */
    public void testEnum1() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Enum1.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Enum enum1 = sdPackage.enums[0];
        assertEquals(enum1.name, "Enum1");
        assertEquals(enum1.qualifiedName, "com.ownedthx.xmldoclet.simpledata.Enum1");
        assertEquals(enum1.fields.length, 3);
        EnumField field = enum1.fields[0];
        assertEquals(field.name, "a");
        field = enum1.fields[1];
        assertEquals(field.name, "b");
        field = enum1.fields[2];
        assertEquals(field.name, "c");
    }

    /**
     * testing an empty enum
     */
    public void testEnum2() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Enum2.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Enum enum2 = sdPackage.enums[0];
        assertEquals(enum2.name, "Enum2");
        assertEquals(enum2.qualifiedName, "com.ownedthx.xmldoclet.simpledata.Enum2");
        assertNull(enum2.fields);
    }

    /**
     * testing enum comment
     */
    public void testEnum3() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Enum3.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Enum enum3 = sdPackage.enums[0];
        assertEquals(enum3.comment, "Enum3");
    }

    /**
     * testing enum field comment
     */
    public void testEnum4() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Enum4.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Enum enum4 = sdPackage.enums[0];
        EnumField field = enum4.fields[0];
        assertEquals(field.comment, "field1");
    }

    /**
     * testing single annotation
     */
    public void testEnum5() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Enum5.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Enum enum4 = sdPackage.enums[0];
        assertEquals(enum4.annotationInstances.length, 1);
        AnnotationInstance annotation = enum4.annotationInstances[0];
        assertEquals(annotation.qualifiedName, "java.lang.Deprecated");
    }

    /**
     * testing multiple annotation
     */
    public void testEnum6() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Enum6.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Enum enum4 = sdPackage.enums[0];
        assertEquals(enum4.annotationInstances.length, 2);
        AnnotationInstance annotation = enum4.annotationInstances[0];
        assertEquals(annotation.qualifiedName, "java.lang.Deprecated");
        assertEquals(annotation.name, "Deprecated");
        assertNull(annotation.arguments);
        annotation = enum4.annotationInstances[1];
        assertEquals(annotation.qualifiedName, "java.lang.SuppressWarnings");
        assertEquals(annotation.name, "SuppressWarnings");
        assertEquals(annotation.arguments.length, 1);
        AnnotationArgument argument = annotation.arguments[0];
        assertEquals(argument.name, "value");
        assertEquals(argument.value[0], "mister");
    }
}
