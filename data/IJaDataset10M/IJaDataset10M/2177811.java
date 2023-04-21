package gov.nasa.jpf.jvm;

import org.junit.Test;
import org.junit.runner.JUnitCore;

/**
 * JPF driver for java.lang.Class test
 */
public class TestJavaLangClassJPF extends TestJPF {

    static final String TEST_CLASS = "gov.nasa.jpf.jvm.TestJavaLangClass";

    public static void main(String[] args) {
        JUnitCore.main("gov.nasa.jpf.jvm.TestJavaLangClassJPF");
    }

    /**************************** tests **********************************/
    @Test
    public void testClassField() {
        String[] args = { TEST_CLASS, "testClassField" };
        runJPFnoException(args);
    }

    @Test
    public void testClassForName() {
        String[] args = { TEST_CLASS, "testClassForName" };
        runJPFnoException(args);
    }

    @Test
    public void testGetClass() {
        String[] args = { TEST_CLASS, "testGetClass" };
        runJPFnoException(args);
    }

    @Test
    public void testIdentity() {
        String[] args = { TEST_CLASS, "testIdentity" };
        runJPFnoException(args);
    }

    @Test
    public void testNewInstance() {
        String[] args = { TEST_CLASS, "testNewInstance" };
        runJPFnoException(args);
    }

    @Test
    public void testNewInstanceFailAccess() {
        String[] args = { TEST_CLASS, "testNewInstanceFailAccess" };
        runJPFappException(args, "java.lang.IllegalAccessException");
    }

    @Test
    public void testNewInstanceFailAbstract() {
        String[] args = { TEST_CLASS, "testNewInstanceFailAbstract" };
        runJPFappException(args, "java.lang.InstantiationException");
    }

    @Test
    public void testIsAssignableFrom() {
        String[] args = { TEST_CLASS, "testIsAssignableFrom" };
        runJPFnoException(args);
    }

    @Test
    public void testInstanceOf() {
        String[] args = { TEST_CLASS, "testInstanceOf" };
        runJPFnoException(args);
    }

    @Test
    public void testAsSubclass() {
        String[] args = { TEST_CLASS, "testAsSubclass" };
        runJPFnoException(args);
    }

    @Test
    public void testInterfaces() {
        String[] args = { TEST_CLASS, "testInterfaces" };
        runJPFnoException(args);
    }

    @Test
    public void testMethods() {
        String[] args = { TEST_CLASS, "testMethods" };
        runJPFnoException(args);
    }
}
