package uk.ac.ebi.intact.model;

import uk.ac.ebi.intact.core.unit.IntactBasicTestCase;
import org.junit.Test;
import org.junit.Assert;

/**
 * AnnotatedObject tester.
 *
 * @author Samuel Kerrien
 * @version $Id$
 * @since 1.6.3
 */
public class AnnotatedObjectTest extends IntactBasicTestCase {

    @Test
    public void clone_shortlabel_length() {
        AnnotatedObject ao = getMockBuilder().createProtein("P12345", "1234567890");
        Assert.assertNotNull(ao);
        Assert.assertEquals("1234567890", ao.getShortLabel());
        try {
            AnnotatedObject aoc = (AnnotatedObject) ((AnnotatedObjectImpl) ao).clone();
            Assert.assertEquals("1234567890-x", aoc.getShortLabel());
        } catch (CloneNotSupportedException e) {
            Assert.fail();
        }
        ao = getMockBuilder().createProtein("P12345", "1234567890123456789");
        Assert.assertNotNull(ao);
        Assert.assertEquals("1234567890123456789", ao.getShortLabel());
        try {
            AnnotatedObject aoc = (AnnotatedObject) ((AnnotatedObjectImpl) ao).clone();
            Assert.assertEquals("1234567890123456789-x", aoc.getShortLabel());
        } catch (CloneNotSupportedException e) {
            Assert.fail();
        }
    }
}
