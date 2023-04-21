package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import junit.framework.JUnit4TestAdapter;
import org.junit.Before;
import org.junit.Test;
import framework.TestAbstract;

/**
 * Smoke tests for parseing Ptolemy programs.
 * @author sean
 * @deprecated
 */
public class PtolemyParsingTest extends TestAbstract {

    public PtolemyParsingTest() {
        super();
        appendToClassPath("../pyc_tests");
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testBindsNoEvents() {
        setCurrentTestFilesFolder("bindings-but-no-events/");
        String[] files = { "test.java", "testEvent1.java", "testEvent2.java", "testhandler.java" };
        String res = runCompiler(files);
        assertEquals("", res);
    }

    @Test
    public void testEventsNoBindings() {
        setCurrentTestFilesFolder("events-but-no-bindings");
        assertTrue(getFullTestPath().getAbsolutePath() + " does not exist", getFullTestPath().exists());
        String[] files = filesToCompile(getFullTestPath());
        if (files.length == 0) fail("No files found in " + getFullTestPath().getAbsolutePath());
        String res = runCompiler(files);
        assertEquals("", res);
    }

    @Test
    public void testEventTypeNoContext() {
        setCurrentTestFilesFolder("evtype-no-context");
        assertTrue(getFullTestPath().getAbsolutePath() + " does not exist", getFullTestPath().exists());
        String[] files = filesToCompile(getFullTestPath());
        if (files.length == 0) fail("No files found in " + getFullTestPath().getAbsolutePath());
        String res = runCompiler(files);
        assertEquals("", res);
    }

    @Test
    public void testEventTypeWithContext() {
        setCurrentTestFilesFolder("evtype-with-context");
        assertTrue(getFullTestPath().getAbsolutePath() + " does not exist", getFullTestPath().exists());
        String[] files = filesToCompile(getFullTestPath());
        if (files.length == 0) fail("No files found in " + getFullTestPath().getAbsolutePath());
        String res = runCompiler(files);
        assertEquals("", res);
    }

    @Test
    public void testEventTypeWithReturn() {
        setCurrentTestFilesFolder("evtype-with-return-in-event-st");
        assertTrue(getFullTestPath().getAbsolutePath() + " does not exist", getFullTestPath().exists());
        String[] files = filesToCompile(getFullTestPath());
        if (files.length == 0) fail("No files found in " + getFullTestPath().getAbsolutePath());
        String res = runCompiler(files);
        assertEquals("", res);
    }

    @Test
    public void testHandlerMultipleBindings() {
        setCurrentTestFilesFolder("handler-multiple-bindings");
        assertTrue(getFullTestPath().getAbsolutePath() + " does not exist", getFullTestPath().exists());
        String[] files = filesToCompile(getFullTestPath());
        if (files.length == 0) fail("No files found in " + getFullTestPath().getAbsolutePath());
        String res = runCompiler(files);
        assertEquals("", res);
    }

    @Test
    public void testHandlerWithEventSt() {
        setCurrentTestFilesFolder("handler-with-event-st");
        assertTrue(getFullTestPath().getAbsolutePath() + " does not exist", getFullTestPath().exists());
        String[] files = filesToCompile(getFullTestPath());
        if (files.length == 0) fail("No files found in " + getFullTestPath().getAbsolutePath());
        String res = runCompiler(files);
        assertEquals("", res);
    }

    @Test
    public void testNestEventStNoContext() {
        setCurrentTestFilesFolder("nested-event-st-no-context");
        assertTrue(getFullTestPath().getAbsolutePath() + " does not exist", getFullTestPath().exists());
        String[] files = filesToCompile(getFullTestPath());
        if (files.length == 0) fail("No files found in " + getFullTestPath().getAbsolutePath());
        String res = runCompiler(files);
        assertEquals("", res);
    }

    @Test
    public void testNestedEventStWithContext() {
        setCurrentTestFilesFolder("nested-event-st-with-context");
        assertTrue(getFullTestPath().getAbsolutePath() + " does not exist", getFullTestPath().exists());
        String[] files = filesToCompile(getFullTestPath());
        if (files.length == 0) fail("No files found in " + getFullTestPath().getAbsolutePath());
        String res = runCompiler(files);
        assertEquals("", res);
    }

    @Test
    public void testPrivFieldInEventSt() {
        setCurrentTestFilesFolder("priv-field-in-event-st");
        assertTrue(getFullTestPath().getAbsolutePath() + " does not exist", getFullTestPath().exists());
        String[] files = filesToCompile(getFullTestPath());
        if (files.length == 0) fail("No files found in " + getFullTestPath().getAbsolutePath());
        String res = runCompiler(files);
        assertEquals("", res);
    }

    @Test
    public void testRefInEventSt() {
        setCurrentTestFilesFolder("this-ref-in-event-st");
        assertTrue(getFullTestPath().getAbsolutePath() + " does not exist", getFullTestPath().exists());
        String[] files = filesToCompile(getFullTestPath());
        if (files.length == 0) fail("No files found in " + getFullTestPath().getAbsolutePath());
        String res = runCompiler(files);
        assertEquals("", res);
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PtolemyParsingTest.class);
    }
}
