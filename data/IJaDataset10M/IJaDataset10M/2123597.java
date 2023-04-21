package net.firstpartners.nounit.reader.bytecode.test;

import java.io.File;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.firstpartners.nounit.reader.ISnippetFactory;
import net.firstpartners.nounit.reader.bytecode.AbstractByteCodeSnippetFactory;
import net.firstpartners.nounit.reader.bytecode.ByteCodeClassSnippetFactory;
import net.firstpartners.nounit.snippet.Snippets;
import net.firstpartners.nounit.test.TestData;

/**
 * Test the Class Snippet Factory<BR>
 */
public class TestByteCodeClassSnippetFactory extends TestCase {

    /**
     * Constructor as required by Junit.
     * @param name to be displayed on testrunner
     */
    public TestByteCodeClassSnippetFactory(String name) {
        super(name);
    }

    /**
     * Enable Junit to run this Class individually.
     * @param args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Enable Junit to run this class as part of AllTests.java
     * @return TestSuite
     */
    public static Test suite() {
        return new TestSuite(TestByteCodeClassSnippetFactory.class);
    }

    public void testGetClassInfo() throws Exception {
        File sourceFile = new File(TestData.SAMPLE_CLASS_PROXY);
        ISnippetFactory myFactory = new ByteCodeClassSnippetFactory(sourceFile);
        Snippets javaInformation = myFactory.getSnippets();
        String info = javaInformation.toString();
        assertTrue(info.indexOf("public eip.ProxyBean") > -1);
        assertTrue(info.indexOf("handleGenericRequest(") > -1);
        assertTrue(info.indexOf("main(") > -1);
        assertTrue(info.indexOf("handleCommandLineRequest(") > -1);
        assertTrue(info.indexOf("<init>(") > -1);
        assertTrue(info.indexOf("clearError(") > -1);
        assertTrue(info.indexOf("extends java.lang.Object") > -1);
        assertTrue(info.indexOf("public") > -1);
        assertTrue(info.indexOf("protected") > -1);
    }

    /**
     * Test the breaking of the parameter String (tag info) into Hashtable
     */
    public void testParameterStringConversion() throws Exception {
        AbstractByteCodeSnippetFactory myFactory = new ByteCodeClassSnippetFactory(TestData.getSampleFile());
        String testString = ".somepackage/someClass";
        String results = myFactory.cleanValues(testString);
        assertTrue(results.equals("somepackage.someClass"));
    }
}
