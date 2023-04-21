package uk.ac.rdg.resc.edal.cdm;

import org.geotoolkit.metadata.iso.extent.DefaultGeographicBoundingBox;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.rdg.resc.edal.coverage.domain.Domain;
import uk.ac.rdg.resc.edal.coverage.domain.impl.HorizontalDomain;
import uk.ac.rdg.resc.edal.coverage.grid.HorizontalGrid;
import uk.ac.rdg.resc.edal.coverage.grid.impl.RegularGridImpl;
import uk.ac.rdg.resc.edal.geometry.HorizontalPosition;
import uk.ac.rdg.resc.edal.geometry.impl.LonLatPositionImpl;

/**
 * Test for the {@link PixelMap} class
 * @author Jon
 */
public class PixelMapTest {

    /**
     * Tests the generation of a PixelMap that maps to a single-point domain
     */
    @Test
    public void testSinglePointPixelMap() {
        HorizontalGrid sourceGrid = new RegularGridImpl(DefaultGeographicBoundingBox.WORLD, 1000, 1000);
        Domain<HorizontalPosition> targetDomain = new HorizontalDomain(new LonLatPositionImpl(0, 0));
        PixelMap pm = new PixelMap(sourceGrid, targetDomain);
        assertEquals(1, pm.getNumUniqueIJPairs());
        assertEquals(false, pm.isEmpty());
    }
}
