package org.ensembl.variation.test;

import java.util.List;
import org.ensembl.driver.AdaptorException;
import org.ensembl.util.PersistentUtil;
import org.ensembl.variation.datamodel.Population;

/**
 * Tests the Population Adaptor implementation.
 * 
 * @author <a href="mailto:craig@ebi.ac.uk">Craig Melsopp</a>
 *
 */
public class PopulationTest extends VariationBase {

    public PopulationTest(String name) throws Exception {
        super(name);
    }

    public void testFetchByID() throws AdaptorException {
        final long ID = 1;
        Population p = vdriver.getPopulationAdaptor().fetch(ID);
        checkPopulation(p);
        assertEquals(ID, p.getInternalID());
    }

    public void testFetchByName() throws AdaptorException {
        final String name = "EUROPE";
        Population p = vdriver.getPopulationAdaptor().fetch(name);
        checkPopulation(p);
        assertEquals(name, p.getName());
    }

    public void testFetchSuperPopulations() throws AdaptorException {
        final long ID = 1;
        Population p = vdriver.getPopulationAdaptor().fetch(ID);
        compare(p.getSuperPopulations(), vdriver.getPopulationAdaptor().fetchSuperPopulations(p));
    }

    public void testFetchSubPopulations() throws AdaptorException {
        final long ID = 1;
        Population p = vdriver.getPopulationAdaptor().fetch(ID);
        compare(p.getSubPopulations(), vdriver.getPopulationAdaptor().fetchSubPopulations(p));
    }

    private void checkPopulation(Population p) {
        assertNotNull(p);
        assertTrue(p.getInternalID() > 0);
        assertNotNull(p.getDescription());
        assertNotNull(p.getName());
    }

    /**
   * @param list
   * @param list2
   */
    private void compare(List a, List b) {
        assertEquals(a.size(), b.size());
        long[] aIDs = PersistentUtil.listToInternalIDArray(a);
        long[] bIDs = PersistentUtil.listToInternalIDArray(b);
        for (int i = 0; i < aIDs.length; i++) {
            assertEquals("population order wrong", aIDs[i], bIDs[i]);
        }
    }
}
