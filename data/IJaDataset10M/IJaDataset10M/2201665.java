package com.ownedthx.xmldoclet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.ownedthx.xmldoclet.xmlbindings.Package;
import com.ownedthx.xmldoclet.xmlbindings.*;
import com.ownedthx.xmldoclet.simpledata.Interface1;
import com.ownedthx.xmldoclet.simpledata.Interface2;
import com.ownedthx.xmldoclet.simpledata.Interface3;
import com.ownedthx.xmldoclet.simpledata.Interface4;
import com.ownedthx.xmldoclet.parser.Parser;

/**
 * Unit test group for Interfaces
 */
public class InterfaceTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public InterfaceTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(InterfaceTest.class);
    }

    public void setUp() {
        TestGlobals.initializeLogging();
    }

    /**
     * Rigourous Parser :-)
     */
    public void testSampledoc() {
        App app = new App();
        Root root = app.processSource(".", new String[] { "./src/test/java" }, null, null, new String[] { "com" });
        assertTrue(true);
    }

    /**
     * testing a interface with nothing defined
     */
    public void testInterface1() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Interface1.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Interface sdInterface1 = sdPackage.interfaces[0];
        assertEquals(root.packages.length, 1);
        assertEquals(sdPackage.comment, "");
        assertEquals(sdPackage.name, "com.ownedthx.xmldoclet.simpledata");
        assertEquals(sdPackage.interfaces.length, 1);
        assertNull(sdPackage.exceptions);
        assertNull(sdPackage.annotations);
        assertNull(sdPackage.enums);
        assertNull(sdPackage.classes);
        assertEquals(sdInterface1.comment, "Interface1");
        assertEquals(sdInterface1.name, Interface1.class.getSimpleName());
        assertEquals(sdInterface1.qualifiedName, Interface1.class.getName());
        assertEquals(sdInterface1.scope, "public");
        assertNull(sdInterface1.methods);
        assertNull(sdInterface1.annotationInstances);
        assertNull(sdInterface1.interfaces);
        assertTrue(sdInterface1.isIncluded);
    }

    /**
     * testing a interface with 1 method
     */
    public void testInterface2() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Interface2.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Interface sdInterface2 = sdPackage.interfaces[0];
        Method method1 = sdInterface2.methods[0];
        assertEquals(root.packages.length, 1);
        assertEquals(sdPackage.comment, "");
        assertEquals(sdPackage.name, "com.ownedthx.xmldoclet.simpledata");
        assertEquals(sdPackage.interfaces.length, 1);
        assertNull(sdPackage.exceptions);
        assertNull(sdPackage.annotations);
        assertNull(sdPackage.enums);
        assertNull(sdPackage.classes);
        assertEquals(sdInterface2.comment, "Interface2");
        assertEquals(sdInterface2.name, Interface2.class.getSimpleName());
        assertEquals(sdInterface2.qualifiedName, Interface2.class.getName());
        assertEquals(sdInterface2.scope, "public");
        assertEquals(sdInterface2.methods.length, 1);
        assertNull(sdInterface2.annotationInstances);
        assertNull(sdInterface2.interfaces);
        assertTrue(sdInterface2.isIncluded);
        assertEquals(method1.comment, "method1");
        assertEquals(method1.name, "method1");
        assertEquals(method1.signature, "()");
        assertFalse(method1.isFinal);
        assertFalse(method1.isNative);
        assertFalse(method1.isStatic);
        assertFalse(method1.isSynchronized);
        assertFalse(method1.isVarArgs);
        assertEquals(method1.qualifiedName, "com.ownedthx.xmldoclet.simpledata.Interface2.method1");
        assertEquals(method1.hash, Parser.computeHash(method1.qualifiedName, method1.signature));
        assertEquals(method1.scope, "public");
        assertNull(method1.annotationInstances);
        assertNull(method1.parameters);
        assertNull(method1.exceptions);
    }

    /**
     * testing a interface that extends another interface
     */
    public void testInterface3() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Interface3.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Interface sdInterface3 = sdPackage.interfaces[0];
        String seriInterface = sdInterface3.interfaces[0];
        assertEquals(root.packages.length, 1);
        assertEquals(sdPackage.comment, "");
        assertEquals(sdPackage.name, "com.ownedthx.xmldoclet.simpledata");
        assertEquals(sdPackage.interfaces.length, 1);
        assertNull(sdPackage.exceptions);
        assertNull(sdPackage.annotations);
        assertNull(sdPackage.enums);
        assertNull(sdPackage.classes);
        assertEquals(sdInterface3.comment, "Interface3");
        assertEquals(sdInterface3.name, Interface3.class.getSimpleName());
        assertEquals(sdInterface3.qualifiedName, Interface3.class.getName());
        assertEquals(sdInterface3.scope, "public");
        assertNull(sdInterface3.methods);
        assertNull(sdInterface3.annotationInstances);
        assertEquals(sdInterface3.interfaces.length, 1);
        assertTrue(sdInterface3.isIncluded);
        assertEquals(seriInterface, java.io.Serializable.class.getName());
    }

    /**
     * testing a interface that implements one annotation
     */
    public void testInterface4() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Interface4.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Interface sdInterface4 = sdPackage.interfaces[0];
        AnnotationInstance annotation = sdInterface4.annotationInstances[0];
        assertEquals(root.packages.length, 1);
        assertEquals(sdPackage.comment, "");
        assertEquals(sdPackage.name, "com.ownedthx.xmldoclet.simpledata");
        assertEquals(sdPackage.interfaces.length, 1);
        assertNull(sdPackage.exceptions);
        assertNull(sdPackage.annotations);
        assertNull(sdPackage.enums);
        assertNull(sdPackage.classes);
        assertEquals(sdInterface4.comment, "Interface4");
        assertEquals(sdInterface4.name, Interface4.class.getSimpleName());
        assertEquals(sdInterface4.qualifiedName, Interface4.class.getName());
        assertEquals(sdInterface4.scope, "public");
        assertNull(sdInterface4.methods);
        assertEquals(sdInterface4.annotationInstances.length, 1);
        assertNull(sdInterface4.interfaces);
        assertTrue(sdInterface4.isIncluded);
        assertEquals(annotation.qualifiedName, "java.lang.Deprecated");
        assertEquals(annotation.name, "Deprecated");
        assertNull(annotation.arguments);
    }

    /**
     * testing a interface that is abstract
     */
    public void testInterface5() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Interface5.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Interface sdInterface5 = sdPackage.interfaces[0];
        Method method1 = sdInterface5.methods[0];
        assertEquals(root.packages.length, 1);
        assertEquals(sdPackage.comment, "");
        assertEquals(sdPackage.name, "com.ownedthx.xmldoclet.simpledata");
        assertEquals(sdPackage.interfaces.length, 1);
        assertNull(sdPackage.exceptions);
        assertNull(sdPackage.annotations);
        assertNull(sdPackage.enums);
        assertNull(sdPackage.classes);
        assertEquals(sdInterface5.comment, "Interface5");
        assertEquals(sdInterface5.name, "Interface5");
        assertEquals(sdInterface5.qualifiedName, "com.ownedthx.xmldoclet.simpledata.Interface5");
        assertEquals(sdInterface5.scope, "packageprivate");
        assertEquals(sdInterface5.methods.length, 1);
        assertNull(sdInterface5.annotationInstances);
        assertNull(sdInterface5.interfaces);
        assertTrue(sdInterface5.isIncluded);
        assertEquals(method1.comment, "method1");
        assertEquals(method1.name, "method1");
        assertEquals(method1.signature, "()");
        assertFalse(method1.isFinal);
        assertFalse(method1.isNative);
        assertFalse(method1.isStatic);
        assertFalse(method1.isSynchronized);
        assertFalse(method1.isVarArgs);
        assertEquals(method1.qualifiedName, "com.ownedthx.xmldoclet.simpledata.Interface5.method1");
        assertEquals(method1.hash, Parser.computeHash(method1.qualifiedName, method1.signature));
        assertEquals(method1.scope, "public");
        assertNull(method1.annotationInstances);
        assertNull(method1.parameters);
        assertNull(method1.exceptions);
    }

    /**
     * testing a interface that has a type variable
     */
    public void testInterface6() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Interface6.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Interface sdInterface6 = sdPackage.interfaces[0];
        TypeVar typeVar = sdInterface6.typeVariables[0];
        assertEquals(root.packages.length, 1);
        assertEquals(sdPackage.comment, "");
        assertEquals(sdPackage.name, "com.ownedthx.xmldoclet.simpledata");
        assertEquals(sdPackage.interfaces.length, 1);
        assertNull(sdPackage.exceptions);
        assertNull(sdPackage.annotations);
        assertNull(sdPackage.enums);
        assertNull(sdPackage.classes);
        assertEquals(sdInterface6.comment, "Interface6");
        assertEquals(sdInterface6.name, "Interface6");
        assertEquals(sdInterface6.qualifiedName, "com.ownedthx.xmldoclet.simpledata.Interface6");
        assertEquals(sdInterface6.scope, "public");
        assertNull(sdInterface6.methods);
        assertNull(sdInterface6.annotationInstances);
        assertNull(sdInterface6.interfaces);
        assertTrue(sdInterface6.isIncluded);
        assertEquals(typeVar.name, "Fun");
        assertNull(typeVar.bounds);
    }

    /**
     * testing a interface that has a type variable with extends
     */
    public void testInterface7() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Interface7.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Interface sdInterface7 = sdPackage.interfaces[0];
        TypeVar typeVar = sdInterface7.typeVariables[0];
        assertEquals(root.packages.length, 1);
        assertEquals(sdPackage.comment, "");
        assertEquals(sdPackage.name, "com.ownedthx.xmldoclet.simpledata");
        assertEquals(sdPackage.interfaces.length, 1);
        assertNull(sdPackage.exceptions);
        assertNull(sdPackage.annotations);
        assertNull(sdPackage.enums);
        assertNull(sdPackage.classes);
        assertEquals(sdInterface7.comment, "Interface7");
        assertEquals(sdInterface7.name, "Interface7");
        assertEquals(sdInterface7.qualifiedName, "com.ownedthx.xmldoclet.simpledata.Interface7");
        assertEquals(sdInterface7.scope, "public");
        assertNull(sdInterface7.methods);
        assertNull(sdInterface7.annotationInstances);
        assertNull(sdInterface7.interfaces);
        assertTrue(sdInterface7.isIncluded);
        assertEquals(typeVar.bounds.length, 1);
        assertEquals(typeVar.bounds[0], "java.lang.String");
    }

    /**
     * testing a interface that has a type variable with extends of a class and interface
     */
    public void testInterface8() {
        App app = new App();
        String[] sourceFiles = new String[] { "./src/test/java/com/ownedthx/xmldoclet/simpledata/Interface8.java" };
        Root root = app.processSource(null, null, null, sourceFiles, null);
        Package sdPackage = root.packages[0];
        Interface sdInterface8 = sdPackage.interfaces[0];
        TypeVar typeVar = sdInterface8.typeVariables[0];
        assertEquals(root.packages.length, 1);
        assertEquals(sdPackage.comment, "");
        assertEquals(sdPackage.name, "com.ownedthx.xmldoclet.simpledata");
        assertEquals(sdPackage.interfaces.length, 1);
        assertNull(sdPackage.exceptions);
        assertNull(sdPackage.annotations);
        assertNull(sdPackage.enums);
        assertNull(sdPackage.classes);
        assertEquals(sdInterface8.comment, "Interface8");
        assertEquals(sdInterface8.name, "Interface8");
        assertEquals(sdInterface8.qualifiedName, "com.ownedthx.xmldoclet.simpledata.Interface8");
        assertEquals(sdInterface8.scope, "public");
        assertNull(sdInterface8.methods);
        assertNull(sdInterface8.annotationInstances);
        assertNull(sdInterface8.interfaces);
        assertTrue(sdInterface8.isIncluded);
        assertEquals(typeVar.bounds.length, 2);
        assertEquals(typeVar.bounds[0], "java.lang.String");
        assertEquals(typeVar.bounds[1], "java.lang.Runnable");
    }
}
