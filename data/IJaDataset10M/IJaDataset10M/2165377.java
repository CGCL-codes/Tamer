package uk.ac.ebi.intact.application.dataConversion.psiUpload.model.util.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.ac.ebi.intact.application.dataConversion.psiUpload.model.util.ReadOnlyIterator;
import java.util.ArrayList;
import java.util.Collection;

/**
 * That class .
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: ReadOnlyIteratorTest.java 3917 2005-04-28 08:27:39Z skerrien $
 */
public class ReadOnlyIteratorTest extends TestCase {

    /**
     * Constructs a NewtServerProxyTest instance with the specified name.
     *
     * @param name the name of the test.
     */
    public ReadOnlyIteratorTest(final String name) {
        super(name);
    }

    /**
     * Returns this test suite. Reflection is used here to add all the testXXX() methods to the suite.
     */
    public static Test suite() {
        return new TestSuite(ReadOnlyIteratorTest.class);
    }

    public void testCreation() {
        Collection myCollection = new ArrayList(1);
        myCollection.add(new Integer(1));
        ReadOnlyIterator roi = new ReadOnlyIterator(myCollection.iterator());
        assertNotNull(roi);
        assertTrue(roi instanceof ReadOnlyIterator);
        try {
            roi.remove();
            fail("ReadOnlyIterator should not allow remove()");
        } catch (UnsupportedOperationException e) {
        }
    }
}
