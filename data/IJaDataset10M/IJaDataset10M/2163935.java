package net.sf.antcontrib.cpptasks.trolltech;

import net.sf.antcontrib.cpptasks.compiler.AbstractProcessor;
import net.sf.antcontrib.cpptasks.compiler.TestAbstractCompiler;

/**
 * Tests for Trolltech Meta Object Compiler.
 *
 */
public class TestMetaObjectCompiler extends TestAbstractCompiler {

    /**
   * Constructor.
   * @param name test name
   */
    public TestMetaObjectCompiler(final String name) {
        super(name);
    }

    /**
   * Creates compiler for inherited tests.
   * @return AbstractProcessor compiler
   */
    protected AbstractProcessor create() {
        return MetaObjectCompiler.getInstance();
    }

    /**
   * Gets default output file extension.
   * @return String output file extension
   */
    protected String getObjectExtension() {
        return ".moc";
    }

    /**
   * Skip testGetIdentifier.
   */
    public void testGetIdentfier() {
    }

    /**
   * Override inherited test.
   */
    public void testGetOutputFileName1() {
        AbstractProcessor compiler = MetaObjectCompiler.getInstance();
        String[] output = compiler.getOutputFileNames("c:/foo\\bar\\hello.cpp", null);
        assertEquals("hello" + getObjectExtension(), output[0]);
        output = compiler.getOutputFileNames("c:/foo\\bar/hello.cpp", null);
        assertEquals("hello" + getObjectExtension(), output[0]);
        output = compiler.getOutputFileNames("hello.cpp", null);
        assertEquals("hello" + getObjectExtension(), output[0]);
        output = compiler.getOutputFileNames("c:/foo\\bar\\hello.h", null);
        assertEquals("moc_hello.cpp", output[0]);
        output = compiler.getOutputFileNames("c:/foo\\bar/hello.h", null);
        assertNull("moc_hello.cpp", output[0]);
    }
}
