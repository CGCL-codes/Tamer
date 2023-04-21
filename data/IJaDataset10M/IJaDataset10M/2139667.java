package be.lassi.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import be.lassi.base.Dirty;
import be.lassi.base.DirtyIndicator;

/**
 * Helper class that helps testing the 'dirty' flag when testing
 * classes that can be marked dirty.
 */
public class DirtyTest {

    private DirtyIndicator dirty = new DirtyIndicator();

    /**
     * Gets the dirty indicator.
     * 
     * @return the dirty indicator
     */
    public Dirty getDirty() {
        return dirty;
    }

    /**
     * Asserts that the object is marked dirty, and resets the
     * 'dirty' flag.
     */
    public void dirty() {
        assertTrue(dirty.isDirty());
        dirty.clear();
    }

    /**
     * Asserts that the object is not marked dirty.
     */
    public void notDirty() {
        assertFalse(dirty.isDirty());
    }
}
