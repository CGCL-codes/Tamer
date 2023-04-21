package net.openchrom.chromatogram.msd.model.core.support;

import java.util.List;
import net.openchrom.chromatogram.msd.model.core.support.IMarkedIons;
import net.openchrom.chromatogram.msd.model.core.support.SelectedIons;
import junit.framework.TestCase;

/**
 * @author eselmeister
 */
public class SelectedIons_2_Test extends TestCase {

    private IMarkedIons selectedIons;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        selectedIons = new SelectedIons();
    }

    @Override
    protected void tearDown() throws Exception {
        selectedIons = null;
        super.tearDown();
    }

    public void testContains_1() {
        assertFalse("contains", selectedIons.containsNominal(4.9f));
    }

    public void testContains_2() {
        selectedIons.add(5);
        assertTrue("contains", selectedIons.containsNominal(4.9f));
    }

    public void testContains_3() {
        selectedIons.add(5);
        selectedIons.remove(5);
        assertFalse("contains", selectedIons.containsNominal(5.4f));
    }

    public void testContains_4() {
        selectedIons.add(10);
        selectedIons.add(5);
        selectedIons.add(20);
        assertTrue("contains", selectedIons.containsNominal(20.2f));
    }

    public void testContains_5() {
        selectedIons.add(50, 60);
        List<Integer> list = selectedIons.getIonsNominal();
        assertEquals("size", 11, list.size());
        for (int i = 50; i <= 60; i++) {
            assertTrue("ion " + i, list.contains(i));
        }
    }
}
