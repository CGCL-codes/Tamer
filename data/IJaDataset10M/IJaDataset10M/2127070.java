package playground.mrieser.pt.fares;

import java.util.HashMap;
import java.util.Map;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.facilities.ActivityFacilitiesImpl;
import org.matsim.core.facilities.ActivityFacilityImpl;
import org.matsim.core.utils.collections.Tuple;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.testcases.MatsimTestCase;

public class MultipleFareSystemsTest extends MatsimTestCase {

    public void testGetSingleTripCost() {
        final ActivityFacilitiesImpl facilities = new ActivityFacilitiesImpl();
        final ActivityFacilityImpl stop1 = facilities.createFacility(new IdImpl(1), new CoordImpl(100, 200));
        final ActivityFacilityImpl stop2 = facilities.createFacility(new IdImpl(2), new CoordImpl(2100, 200));
        final ActivityFacilityImpl stop3 = facilities.createFacility(new IdImpl(3), new CoordImpl(1100, 1200));
        final ActivityFacilityImpl stop4 = facilities.createFacility(new IdImpl(4), new CoordImpl(2100, 1200));
        final ActivityFacilityImpl stop5 = facilities.createFacility(new IdImpl(5), new CoordImpl(100, 1200));
        final ActivityFacilityImpl stop6 = facilities.createFacility(new IdImpl(6), new CoordImpl(2100, 2200));
        final Map<Tuple<ActivityFacilityImpl, ActivityFacilityImpl>, Double> fares1 = new HashMap<Tuple<ActivityFacilityImpl, ActivityFacilityImpl>, Double>();
        fares1.put(new Tuple<ActivityFacilityImpl, ActivityFacilityImpl>(stop1, stop2), 2.0);
        fares1.put(new Tuple<ActivityFacilityImpl, ActivityFacilityImpl>(stop2, stop3), 3.0);
        final Map<Tuple<ActivityFacilityImpl, ActivityFacilityImpl>, Double> fares2 = new HashMap<Tuple<ActivityFacilityImpl, ActivityFacilityImpl>, Double>();
        fares2.put(new Tuple<ActivityFacilityImpl, ActivityFacilityImpl>(stop5, stop6), 1.5);
        MultipleFareSystems combiFares = new MultipleFareSystems();
        combiFares.addFares(new TableLookupFares(fares1));
        combiFares.addFares(new TableLookupFares(fares2));
        combiFares.addFares(new BeelineDistanceBasedFares(5.0));
        assertEquals(0.0, combiFares.getSingleTripCost(stop1, stop1), EPSILON);
        assertEquals(0.0, combiFares.getSingleTripCost(stop5, stop5), EPSILON);
        assertEquals(0.0, combiFares.getSingleTripCost(stop6, stop6), EPSILON);
        assertEquals(2.0, combiFares.getSingleTripCost(stop1, stop2), EPSILON);
        assertEquals(1.5, combiFares.getSingleTripCost(stop5, stop6), EPSILON);
        assertEquals(5.0, combiFares.getSingleTripCost(stop2, stop4), EPSILON);
        assertEquals(10.0, combiFares.getSingleTripCost(stop2, stop6), EPSILON);
    }
}
