package org.apache.openjpa.sdo;

import org.apache.openjpa.sdo.SourceCode;
import junit.framework.TestCase;

/**
 * Tests sanity of Source Code generation.
 * 
 * @author Pinaki Poddar
 * 
 */
public class TestSourceCode extends TestCase {

    public void testImport() {
        assertTrue(SourceCode.isValidImportName("java.util.*"));
        assertTrue(SourceCode.isValidImportName("java.util.List"));
        assertFalse(SourceCode.isValidImportName("java.util."));
        assertFalse(SourceCode.isValidImportName("com.catch.*."));
        assertFalse(SourceCode.isValidImportName("com.catch.List"));
    }

    public void testPackageName() {
        assertTrue(SourceCode.isValidPackageName("java.util"));
        assertFalse(SourceCode.isValidPackageName("java.util."));
        assertFalse(SourceCode.isValidPackageName("java.util.*"));
        assertFalse(SourceCode.isValidPackageName("com.catch"));
        assertFalse(SourceCode.isValidPackageName("com.catch.*"));
    }

    public void testTypeName() {
        assertTrue(SourceCode.isValidTypeName("java.util.List"));
        assertFalse(SourceCode.isValidTypeName("java.util.List.*"));
        assertFalse(SourceCode.isValidTypeName(" java.util.List"));
        assertTrue(SourceCode.isValidTypeName("java.util.List<Item>"));
    }

    public void testWriteCode() {
        SourceCode code = new SourceCode("com.bea.sdo", "TestClass");
        code.addImport("java.util.*");
        code.addImport("java.io.*");
        code.getTopLevelClass().addInterface("Serializable").addInterface("PC").setSuper("BaseEntity").addAnnotation("@Entity");
        code.addField("x", "int").makePublic().addGetter().addSetter().addAnnotation("@Basic").addProperty("fetch", "FetchType.LAZY");
        code.addField("y", "String");
        code.addMethod("add", "int").makePublic().makeStatic().addArgument("int", "a").addArgument("int", "b").addCodeLine("int x=a+b").addCodeLine("return x");
    }

    public void testBeanCode() {
        SourceCode code = new SourceCode("com.bea.sdo", "TestClass");
        code.addImport("java.util.*");
        code.addImport("java.io.*");
        code.getTopLevelClass().addInterface("Serializable").addInterface("PC").setSuper("BaseEntity").addAnnotation("@Entity");
        code.addField("x", "int").makePrivate().addAnnotation("@Basic").addProperty("fetch", "FetchType.LAZY");
        code.addField("y", "String");
        code.markAsBean();
    }

    public void testDuplicateField() {
        SourceCode code = new SourceCode("some.pkg", "SomeClass");
        try {
            code.addField("a", "int");
            code.addField("a", "float");
            fail("Expected DuplicateField exception");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testDuplicateMethod() {
        SourceCode code = new SourceCode("some.pkg", "SomeClass");
        try {
            SourceCode.Method m1 = new SourceCode.Method("add", "int");
            SourceCode.Method m2 = new SourceCode.Method("add", "float");
            m1.makePublic().makeStatic().addArgument("int", "a").addArgument("int", "b");
            m2.addArgument("int", "a").addArgument("int", "b");
            code.addMethod(m1);
            code.addMethod(m2);
            fail("Expected DuplicateField exception");
        } catch (IllegalArgumentException e) {
        }
    }
}
