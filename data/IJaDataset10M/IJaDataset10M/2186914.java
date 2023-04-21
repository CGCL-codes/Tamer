package org.enerj.apache.commons.collections.set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import junit.framework.Test;
import org.enerj.apache.commons.collections.BulkTest;

/**
 * Extension of {@link AbstractTestSortedSet} for exercising the 
 * {@link UnmodifiableSortedSet} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 155406 $ $Date: 2005-02-26 12:55:26 +0000 (Sat, 26 Feb 2005) $
 * 
 * @author Phil Steitz
 */
public class TestUnmodifiableSortedSet extends AbstractTestSortedSet {

    public TestUnmodifiableSortedSet(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestUnmodifiableSortedSet.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestUnmodifiableSortedSet.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public Set makeEmptySet() {
        return UnmodifiableSortedSet.decorate(new TreeSet());
    }

    public Set makeFullSet() {
        TreeSet set = new TreeSet();
        set.addAll(Arrays.asList(getFullElements()));
        return UnmodifiableSortedSet.decorate(set);
    }

    public boolean isAddSupported() {
        return false;
    }

    public boolean isRemoveSupported() {
        return false;
    }

    protected UnmodifiableSortedSet set = null;

    protected ArrayList array = null;

    protected void setupSet() {
        set = (UnmodifiableSortedSet) makeFullSet();
        array = new ArrayList();
        array.add(new Integer(1));
    }

    /** 
     * Verify that base set and subsets are not modifiable
     */
    public void testUnmodifiable() {
        setupSet();
        verifyUnmodifiable(set);
        verifyUnmodifiable(set.headSet(new Integer(1)));
        verifyUnmodifiable(set.tailSet(new Integer(1)));
        verifyUnmodifiable(set.subSet(new Integer(1), new Integer(3)));
    }

    /**
     * Verifies that a set is not modifiable
     */
    public void verifyUnmodifiable(Set set) {
        try {
            set.add("value");
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
        }
        try {
            set.addAll(new TreeSet());
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
        }
        try {
            set.clear();
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
        }
        try {
            set.remove("x");
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
        }
        try {
            set.removeAll(array);
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
        }
        try {
            set.retainAll(array);
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException e) {
        }
    }

    public void testComparator() {
        setupSet();
        Comparator c = set.comparator();
        assertTrue("natural order, so comparator should be null", c == null);
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }
}
