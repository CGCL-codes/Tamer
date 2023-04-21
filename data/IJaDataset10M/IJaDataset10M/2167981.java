package net.sourceforge.transmogrify.symtab.test;

import junit.framework.*;
import junit.extensions.*;
import net.sourceforge.transmogrify.symtab.*;
import java.io.*;
import net.sourceforge.transmogrify.test.TestStarter;

public class InnerClassTest extends DefinitionLookupTest {

    private File file = new File("test/InnerClass.java");

    private File[] files = new File[] { file };

    IDefinition outerInt = null;

    IDefinition outerMethod = null;

    IDefinition outerMethod2 = null;

    IDefinition innerClassConstructor = null;

    IDefinition innerInt = null;

    IDefinition innerMethod = null;

    public InnerClassTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        createQueryEngine(files);
        outerInt = getDefinition(file, "anInt", 5, 15);
        outerMethod = getDefinition(file, "method", 7, 15);
        outerMethod2 = getDefinition(file, "method", 10, 15);
        innerClassConstructor = getDefinition(file, "AnInnerClass", 40, 12);
        innerInt = getDefinition(file, "anInnerInt", 38, 16);
        innerMethod = getDefinition(file, "innerMethod", 43, 17);
    }

    public void testInnerVariableFromEnclosingClass() {
        IDefinition def = getDefinition(file, "anInnerInt", 15, 24);
        assertNotNull(innerInt);
        assertEquals(innerInt, def);
    }

    public void testInnerMethodFromEnclosingClass() {
        IDefinition def = getDefinition(file, "innerMethod", 16, 16);
        assertNotNull(innerMethod);
        assertEquals(innerMethod, def);
    }

    public void testOuterVariableFromInnerClass() {
        IDefinition def = getDefinition(file, "anInt", 44, 15);
        assertNotNull(outerInt);
        assertEquals(outerInt, def);
    }

    public void testOuterMethodFromInnerClass() {
        IDefinition def = getDefinition(file, "method", 45, 7);
        assertNotNull(outerMethod);
        assertEquals(outerMethod, def);
    }

    public void testAnonymousInnerClass() {
        IDefinition def = getDefinition(file, "AnInnerClass", 18, 16);
        assertNotNull(innerClassConstructor);
        assertEquals(innerClassConstructor, def);
    }

    public void testMethodCallWithAnonymousInnerClass() {
        IDefinition def = getDefinition(file, "method", 18, 5);
        assertNotNull(outerMethod2);
        assertEquals(outerMethod2, def);
    }

    public void testInnerClassInMethod() {
        IVariable def = (IVariable) getDefinition(file, "mic", 28, 22);
        assertNotNull(def);
        assertEquals(getDefinition(file, "MethodInnerClass", 26, 11), def.getType());
    }

    public void testInnerClassInBlock() {
        IVariable def = (IVariable) getDefinition(file, "fic", 32, 21);
        assertNotNull(def);
        assertEquals(getDefinition(file, "ForInnerClass", 31, 13), def.getType());
    }

    public static void main(String[] args) {
        TestStarter.startRun(InnerClassTest.class);
    }
}
