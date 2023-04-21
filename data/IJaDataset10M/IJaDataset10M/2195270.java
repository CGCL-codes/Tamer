package au.edu.diasb.chico.test;

import static au.edu.diasb.chico.mvc.HierarchicalExceptionClassifier.DEFAULT_DESCRIPTION;
import static au.edu.diasb.chico.mvc.HierarchicalExceptionClassifier.DEFAULT_REMEDY;
import static au.edu.diasb.chico.mvc.HierarchicalExceptionClassifier.SYSTEM_CLASS;
import java.util.Collections;
import junit.framework.TestCase;
import au.edu.diasb.chico.mvc.ExceptionClassifier;
import au.edu.diasb.chico.mvc.HierarchicalExceptionClassifier;

public class HierarchicalExceptionClassifierTest extends TestCase {

    public void testConstructor() {
        new HierarchicalExceptionClassifier();
    }

    /** 
     * This tests both the initialization of the map, and the basic 
     * classifier functionality.
     */
    public void testForThrowable() {
        ExceptionClassifier ec = new HierarchicalExceptionClassifier();
        Throwable ex = new Throwable();
        ExceptionClassifier.Classification c = ec.classify(ex);
        assertSame(ex, c.getException());
        assertEquals(SYSTEM_CLASS, c.getClassification());
        assertEquals("Something impossible has occurred.", c.getDescription());
        assertEquals(DEFAULT_REMEDY, c.getRemedy());
    }

    public void testForException() {
        ExceptionClassifier ec = new HierarchicalExceptionClassifier();
        Throwable ex = new Exception();
        ExceptionClassifier.Classification c = ec.classify(ex);
        assertSame(ex, c.getException());
        assertEquals(SYSTEM_CLASS, c.getClassification());
        assertEquals(DEFAULT_DESCRIPTION, c.getDescription());
        assertEquals(DEFAULT_REMEDY, c.getRemedy());
    }

    public void testForRuntimeException() {
        ExceptionClassifier ec = new HierarchicalExceptionClassifier();
        Throwable ex = new RuntimeException();
        ExceptionClassifier.Classification c = ec.classify(ex);
        assertSame(ex, c.getException());
        assertEquals(SYSTEM_CLASS, c.getClassification());
        assertEquals(DEFAULT_DESCRIPTION, c.getDescription());
        assertEquals(DEFAULT_REMEDY, c.getRemedy());
    }

    public void testForError() {
        ExceptionClassifier ec = new HierarchicalExceptionClassifier();
        Throwable ex = new Error();
        ExceptionClassifier.Classification c = ec.classify(ex);
        assertSame(ex, c.getException());
        assertEquals(SYSTEM_CLASS, c.getClassification());
        assertEquals("A serious system error has occurred.", c.getDescription());
        assertEquals(DEFAULT_REMEDY, c.getRemedy());
    }

    public void testSetMapping() {
        HierarchicalExceptionClassifier ec = new HierarchicalExceptionClassifier();
        ec.setMappings(Collections.singletonList("java.lang.RuntimeException=secret|a secret|don't tell"));
        Throwable ex = new RuntimeException();
        ExceptionClassifier.Classification c = ec.classify(ex);
        assertSame(ex, c.getException());
        assertEquals("secret", c.getClassification());
        assertEquals("a secret", c.getDescription());
        assertEquals("don't tell", c.getRemedy());
    }

    public void testSetMapping2() {
        HierarchicalExceptionClassifier ec = new HierarchicalExceptionClassifier();
        ec.setMappings(Collections.singletonList("java.lang.RuntimeException=secret|a secret"));
        Throwable ex = new RuntimeException();
        ExceptionClassifier.Classification c = ec.classify(ex);
        assertSame(ex, c.getException());
        assertEquals("secret", c.getClassification());
        assertEquals("a secret", c.getDescription());
        assertEquals(DEFAULT_REMEDY, c.getRemedy());
    }

    public void testSetMapping3() {
        HierarchicalExceptionClassifier ec = new HierarchicalExceptionClassifier();
        ec.setMappings(Collections.singletonList("java.lang.RuntimeException=secret"));
        Throwable ex = new RuntimeException();
        ExceptionClassifier.Classification c = ec.classify(ex);
        assertSame(ex, c.getException());
        assertEquals("secret", c.getClassification());
        assertEquals(DEFAULT_DESCRIPTION, c.getDescription());
        assertEquals(DEFAULT_REMEDY, c.getRemedy());
    }

    public void testSetMapping4() {
        HierarchicalExceptionClassifier ec = new HierarchicalExceptionClassifier();
        ec.setMappings(Collections.singletonList(" java.lang.RuntimeException = secret | a secret | don't tell "));
        Throwable ex = new RuntimeException();
        ExceptionClassifier.Classification c = ec.classify(ex);
        assertSame(ex, c.getException());
        assertEquals("secret", c.getClassification());
        assertEquals("a secret", c.getDescription());
        assertEquals("don't tell", c.getRemedy());
    }

    public void testSetMapping5() {
        HierarchicalExceptionClassifier ec = new HierarchicalExceptionClassifier();
        ec.setMappings(Collections.singletonList("java.lang.RuntimeException=secret||"));
        Throwable ex = new RuntimeException();
        ExceptionClassifier.Classification c = ec.classify(ex);
        assertSame(ex, c.getException());
        assertEquals("secret", c.getClassification());
        assertEquals(DEFAULT_DESCRIPTION, c.getDescription());
        assertEquals(DEFAULT_REMEDY, c.getRemedy());
    }

    public void testSetDefaults() {
        HierarchicalExceptionClassifier ec = new HierarchicalExceptionClassifier();
        ec.setDefaultDescription("nasty");
        ec.setDefaultRemedy("run away");
        ec.setMappings(Collections.singletonList("java.lang.RuntimeException=secret"));
        Throwable ex = new RuntimeException();
        ExceptionClassifier.Classification c = ec.classify(ex);
        assertSame(ex, c.getException());
        assertEquals("secret", c.getClassification());
        assertEquals("nasty", c.getDescription());
        assertEquals("run away", c.getRemedy());
    }

    public void testBadMappings() {
        HierarchicalExceptionClassifier ec = new HierarchicalExceptionClassifier();
        try {
            ec.setMappings(Collections.singletonList(""));
            fail("no exception");
        } catch (IllegalArgumentException ex) {
            assertEquals("Can't parse mapping ''", ex.getMessage());
        }
        try {
            ec.setMappings(Collections.singletonList("hoi"));
            fail("no exception");
        } catch (IllegalArgumentException ex) {
            assertEquals("Can't parse mapping 'hoi'", ex.getMessage());
        }
        try {
            ec.setMappings(Collections.singletonList("hoi="));
            fail("no exception");
        } catch (IllegalArgumentException ex) {
            assertEquals("Can't parse mapping 'hoi='", ex.getMessage());
        }
        try {
            ec.setMappings(Collections.singletonList("hoi=|"));
            fail("no exception");
        } catch (IllegalArgumentException ex) {
            assertEquals("Can't parse mapping 'hoi=|'", ex.getMessage());
        }
    }
}
