package org.argouml.uml.cognitive.critics;

import org.argouml.model.Model;
import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

/**
 * Testing the class {@link CrUnconventionalPackName}.
 *
 * @author mkl
 */
public class TestCrUnconventionalPackName extends TestCase {

    /**
     * An instance of the class to test.
     */
    private CrUnconventionalPackName cr = new CrUnconventionalPackName();

    /**
     * The constructor.
     *
     * @param arg0 The test case name.
     */
    public TestCrUnconventionalPackName(String arg0) {
        super(arg0);
    }

    public void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
    }

    /**
     * Testing computeSuggestion.
     */
    public void testComputeSuggestion() {
        assertEquals("packageName", cr.computeSuggestion(null));
        assertEquals("x", cr.computeSuggestion("X"));
    }

    public void testPredicate2() {
        Object me = Model.getModelManagementFactory().createPackage();
        Model.getCoreHelper().setName(me, null);
        assertFalse(cr.predicate2(me, null));
        Model.getCoreHelper().setName(me, "Uppercase");
        assertTrue(cr.predicate2(me, null));
        Model.getCoreHelper().setName(me, "lowercase");
        assertFalse(cr.predicate2(me, null));
    }
}
