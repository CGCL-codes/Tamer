package net.sf.gaboto.entities.test;

import net.sf.gaboto.Gaboto;
import net.sf.gaboto.GabotoSnapshot;
import net.sf.gaboto.node.GabotoEntity;
import net.sf.gaboto.node.pool.EntityPool;
import net.sf.gaboto.node.pool.EntityPoolConfiguration;
import net.sf.gaboto.time.TimeInstant;
import net.sf.gaboto.vocabulary.OxPointsVocab;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ox.oucs.oxpoints.OxpointsFactory;
import uk.ac.ox.oucs.oxpoints.gaboto.entities.Building;
import uk.ac.ox.oucs.oxpoints.gaboto.entities.College;
import uk.ac.ox.oucs.oxpoints.gaboto.entities.Website;
import static org.junit.Assert.assertTrue;

public class TestPassiveProperties {

    @BeforeClass
    public static void setUp() throws Exception {
    }

    @Test
    public void testClassicPropertyLoading() {
        Gaboto oxp = OxpointsFactory.getOxpointsFromXML();
        GabotoSnapshot snapshot = oxp.getSnapshot(TimeInstant.now());
        EntityPoolConfiguration config = new EntityPoolConfiguration(snapshot);
        config.addAcceptedType(OxPointsVocab.Building_URI);
        EntityPool pool = EntityPool.createFrom(config);
        boolean foundPassive = false;
        for (GabotoEntity e : pool.getEntities()) {
            Building building = (Building) e;
            if (building.getOccupiedBy() != null) foundPassive = true;
        }
        assertTrue(foundPassive);
    }

    @Test
    public void testClassicPropertyLoading2() {
        Gaboto oxp = OxpointsFactory.getOxpointsFromXML();
        GabotoSnapshot snapshot = oxp.getSnapshot(TimeInstant.now());
        EntityPoolConfiguration config = new EntityPoolConfiguration(snapshot);
        config.addAcceptedType(OxPointsVocab.College_URI);
        EntityPool pool = EntityPool.createFrom(config);
        boolean foundPassive = false;
        for (GabotoEntity e : pool.getEntities()) {
            College col = (College) e;
            if (col.getOccupiedPlaces() != null) foundPassive = true;
        }
        assertTrue(foundPassive);
    }
}
