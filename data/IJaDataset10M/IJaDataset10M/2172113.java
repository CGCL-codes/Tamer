package agitar.uk.ac.ebi.intact.modelt;

import uk.ac.ebi.intact.model.*;
import com.agitar.lib.junit.AgitarTestCase;
import uk.ac.ebi.intact.model.util.AnnotatedObjectUtils;

public class CvCellTypeAgitarTest extends AgitarTestCase {

    static Class TARGET_CLASS = CvCellType.class;

    public void testConstructor() throws Throwable {
        Institution owner = new Institution("testCvCellTypeShortLabel");
        CvCellType cvCellType = new CvCellType(owner, "testCvCellTypeShortLabel");
        assertEquals("cvCellType.xrefs.size()", 0, cvCellType.xrefs.size());
        assertEquals("cvCellType.getAliases().size()", 0, cvCellType.getAliases().size());
        assertEquals("cvCellType.getEvidences().size()", 0, cvCellType.getEvidences().size());
        assertEquals("cvCellType.shortLabel", "testCvCellTypeShortL", cvCellType.getShortLabel());
        assertEquals("cvCellType.annotations.size()", 0, cvCellType.annotations.size());
        assertSame("cvCellType.getOwner()", owner, cvCellType.getOwner());
        assertEquals("cvCellType.references.size()", 0, cvCellType.references.size());
    }

    public void testConstructorThrowsIllegalArgumentException() throws Throwable {
        try {
            new CvCellType(new Institution("testCvCellTypeShortLabel"), "");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Must define a non empty short label", ex.getMessage());
            assertThrownBy(AnnotatedObjectUtils.class, ex);
        }
    }

    public void testConstructorThrowsNullPointerException() throws Throwable {
        try {
            new CvCellType(new Institution("testCvCellTypeShortLabel"), null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("ex.getMessage()", "Must define a non null short label", ex.getMessage());
            assertThrownBy(AnnotatedObjectUtils.class, ex);
        }
    }
}
