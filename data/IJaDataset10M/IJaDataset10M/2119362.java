package ru.cos.sim.agents.origin;

import java.util.List;
import java.util.Vector;
import ru.cos.sim.engine.RoadNetworkUniverse;
import ru.cos.sim.road.node.OriginNode;
import ru.cos.sim.utils.ProbabilityArray;
import ru.cos.sim.vehicle.init.data.VehicleData;

/**
 * Origin factory responsible for creating origin agents.
 * @author zroslaw
 */
public class OriginFactory {

    /**
	 * Create origin instance from data about it.
	 * @param originData instance of data structure that defines origin
	 * @param universe road network universe where to create origin
	 * @return instance of origin agent
	 */
    public static Origin createOrigin(OriginData originData, RoadNetworkUniverse universe) {
        Origin origin = new Origin();
        OriginNode originNode = (OriginNode) universe.getNode(originData.getOriginNodeId());
        origin.setOriginNode(originNode);
        origin.setUniverse(universe);
        List<OriginPeriod> periods = new Vector<OriginPeriod>();
        for (OriginPeriodData periodData : originData.getTimePeriods()) {
            OriginPeriod period = createPeriod(periodData, universe);
            periods.add(period);
        }
        origin.setPeriods(periods);
        return origin;
    }

    /**
	 * Create OriginPeriod instance from its data definition
	 * @param periodData instance of origin period data structure
	 * @param universe universe
	 * @return origin period instance
	 */
    private static OriginPeriod createPeriod(OriginPeriodData periodData, RoadNetworkUniverse universe) {
        OriginPeriod period = new OriginPeriod();
        period.setUniverse(universe);
        ProbabilityArray<Integer> destinations = new ProbabilityArray<Integer>(periodData.getListOfDestinations());
        period.setDestinations(destinations);
        ProbabilityArray<VehicleData> vehicleProfiles = new ProbabilityArray<VehicleData>(periodData.getVehicleProfiles());
        period.setVehicleProfiles(vehicleProfiles);
        period.setDuration(periodData.getPeriodDuration());
        period.setNumberOfVehicles(periodData.getNumberOfVehicles());
        return period;
    }
}
