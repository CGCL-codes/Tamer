package uk.ac.ebi.intact.application.dataConversion.psiUpload.model.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.AnnotationTag;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.ExpressedInTag;

/**
 * That class .
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: ExpressedInTest.java 3917 2005-04-28 08:27:39Z skerrien $
 */
public class ExpressedInTest extends TestCase {

    /**
     * Constructs a NewtServerProxyTest instance with the specified name.
     *
     * @param name the name of the test.
     */
    public ExpressedInTest(final String name) {
        super(name);
    }

    /**
     * Returns this test suite. Reflection is used here to add all the testXXX() methods to the suite.
     */
    public static Test suite() {
        return new TestSuite(ExpressedInTest.class);
    }

    public void testCreate_ok() {
        AnnotationTag annotation = new AnnotationTag("expressedIn", "12345:biosource");
        ExpressedInTag expressedIn = new ExpressedInTag(annotation);
        assertNotNull(expressedIn);
        assertEquals("12345", expressedIn.getProteinInteractorID());
        assertEquals("biosource", expressedIn.getBioSourceShortlabel());
    }

    public void testCreate_wrong_annotation_type() {
        AnnotationTag annotation = new AnnotationTag("comment", "blablabla");
        try {
            new ExpressedInTag(annotation);
            fail("Should have generated an Exception: annotation is not of the right type (ExpressedIn)");
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testCreate_missing_id() {
        AnnotationTag annotation = new AnnotationTag("expressedIn", ":biosource");
        try {
            new ExpressedInTag(annotation);
            fail("Should have generated an Exception: protein id is missing");
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testCreate_missing_biosource() {
        AnnotationTag annotation = new AnnotationTag("expressedIn", "12345:");
        try {
            new ExpressedInTag(annotation);
            fail("Should have generated an Exception: biosource shortlabel is missing");
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testCreate_missing_biosource_and_protein_id() {
        AnnotationTag annotation = new AnnotationTag("expressedIn", ":");
        try {
            new ExpressedInTag(annotation);
            fail("Should have generated an Exception: protein id and biosource shortlabel is missing");
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testCreate_missing_biosource_and_protein_id_2() {
        AnnotationTag annotation = new AnnotationTag("expressedIn", "");
        try {
            new ExpressedInTag(annotation);
            fail("Should have generated an Exception: protein id and biosource shortlabel is missing");
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testCreate_missing_biosource_and_protein_id_3() {
        AnnotationTag annotation = new AnnotationTag("expressedIn", "blabla");
        try {
            new ExpressedInTag(annotation);
            fail("Should have generated an Exception: protein id and biosource shortlabel is missing");
        } catch (IllegalArgumentException iae) {
        }
    }
}
