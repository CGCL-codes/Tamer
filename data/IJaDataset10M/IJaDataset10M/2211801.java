package uk.ac.ebi.intact.application.editor.struts.view.feature.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.log4j.Logger;
import uk.ac.ebi.intact.application.editor.struts.view.feature.FeatureActionForm;
import uk.ac.ebi.intact.application.editor.struts.framework.util.EditorConstants;
import java.util.ResourceBundle;

/**
 * The test class for FeatureActionForm class.
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: FeatureActionFormTest.java 4502 2006-01-11 14:21:50Z catherineleroy $
 */
public class FeatureActionFormTest extends TestCase {

    private static ResourceBundle ourResourceBundle;

    /**
     * Constructs an instance with the specified name.
     * @param name the name of the test.
     */
    public FeatureActionFormTest(String name) {
        super(name);
        ourResourceBundle = ResourceBundle.getBundle("uk.ac.ebi.intact.application.editor.EditorResources");
    }

    /**
     * Returns this test suite. Reflection is used here to add all
     * the testXXX() methods to the suite.
     */
    public static Test suite() {
        return new TestSuite(FeatureActionFormTest.class);
    }

    /**
     * Tests the mutation validation
     */
    public void testMutationValidation() {
        try {
            doMutationValidation();
        } catch (Exception ex) {
            Logger.getLogger(EditorConstants.LOGGER).error("", ex);
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }

    private void doMutationValidation() {
        String featureSep = ourResourceBundle.getString("mutation.feature.sep");
        String rangeSep = ourResourceBundle.getString("mutation.range.sep");
        ActionErrors errors;
        ActionError error;
        String key;
        FeatureActionForm form = new FeatureActionForm();
        errors = form.testValidateMutations("", featureSep, rangeSep);
        assertNotNull(errors);
        key = (String) errors.properties().next();
        assertEquals("feature.mutation.empty", key);
        error = (ActionError) errors.get(key).next();
        assertEquals("error.feature.mutation.empty", error.getKey());
        errors = form.testValidateMutations("K235t", featureSep, rangeSep);
        assertNotNull(errors);
        key = (String) errors.properties().next();
        assertEquals("feature.mutation.invalid", key);
        error = (ActionError) errors.get(key).next();
        assertEquals("error.feature.mutation.format", error.getKey());
        assertEquals(error.getValues()[0], "K235t");
        errors = form.testValidateMutations("kfgt", featureSep, rangeSep);
        assertNotNull(errors);
        key = (String) errors.properties().next();
        assertEquals("feature.mutation.invalid", key);
        error = (ActionError) errors.get(key).next();
        assertEquals("error.feature.mutation.format", error.getKey());
        assertEquals(error.getValues()[0], "kfgt");
        errors = form.testValidateMutations("lys235thr & ser", featureSep, rangeSep);
        assertNotNull(errors);
        key = (String) errors.properties().next();
        assertEquals("feature.mutation.invalid", key);
        error = (ActionError) errors.get(key).next();
        assertEquals("error.feature.mutation.format", error.getKey());
        assertEquals(error.getValues()[0], "ser");
        errors = form.testValidateMutations("lys235thr & ser283thr | lys", featureSep, rangeSep);
        assertNotNull(errors);
        key = (String) errors.properties().next();
        assertEquals("feature.mutation.invalid", key);
        error = (ActionError) errors.get(key).next();
        assertEquals("error.feature.mutation.format", error.getKey());
        assertEquals(error.getValues()[0], "lys");
        errors = form.testValidateMutations("trp123ala & trp123asp | leu23ile", featureSep, rangeSep);
        assertNotNull(errors);
        key = (String) errors.properties().next();
        assertEquals("feature.mutation.invalid", key);
        error = (ActionError) errors.get(key).next();
        assertEquals("error.feature.mutation.range", error.getKey());
        assertEquals(error.getValues()[0], "trp123asp");
        assertEquals(error.getValues()[1], "123");
        errors = form.testValidateMutations("trp123trp", featureSep, rangeSep);
        assertNotNull(errors);
        key = (String) errors.properties().next();
        assertEquals("feature.mutation.invalid", key);
        error = (ActionError) errors.get(key).next();
        assertEquals("error.feature.mutation.same", error.getKey());
        assertEquals(error.getValues()[0], "trp");
        assertEquals(error.getValues()[1], "trp");
        assertNull(form.testValidateMutations("lys235thr", featureSep, rangeSep));
        assertNull(form.testValidateMutations("lys235thr|lys5632thr", featureSep, rangeSep));
        assertNull(form.testValidateMutations("lys235thr & ser283thr|lys5632thr", featureSep, rangeSep));
        assertNull(form.testValidateMutations("lys235thr & ser283thr|lys5632thr & lys234xyz", featureSep, rangeSep));
        assertNull(form.testValidateMutations("trp123ala | trp123ala", featureSep, rangeSep));
        assertNull(form.testValidateMutations("trp123ala & trp124ala | trp123ala", featureSep, rangeSep));
    }
}
