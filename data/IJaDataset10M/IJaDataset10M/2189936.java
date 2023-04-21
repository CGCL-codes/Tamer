package playground.andreas.bvgAna.level0;

import java.util.TreeMap;
import java.util.Map.Entry;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.pt.transitSchedule.api.TransitLine;
import org.matsim.pt.transitSchedule.api.TransitSchedule;

/**
 * Helper class providing access to transit schedule related information. Convenience only.
 * 
 * @author aneumann
 *
 */
public class TransitScheduleDataProvider {

    private final Logger log = Logger.getLogger(TransitScheduleDataProvider.class);

    private final Level logLevel = Level.DEBUG;

    private TransitSchedule transitSchedule;

    private TreeMap<Id, Id> routeId2lineIdMap;

    public TransitScheduleDataProvider(TransitSchedule transitSchedule) {
        this.log.setLevel(this.logLevel);
        this.transitSchedule = transitSchedule;
    }

    /**
	 * @return Returns the name of a given stop id. Should be <code>null</code> if none is set.
	 */
    public String getStopName(Id stopId) {
        return this.transitSchedule.getFacilities().get(stopId).getName();
    }

    /**
	 * @return Returns a map containing the line id for each route id
	 */
    public TreeMap<Id, Id> getRouteId2lineIdMap() {
        if (this.routeId2lineIdMap == null) {
            createRouteId2lineIdMap();
        }
        return this.routeId2lineIdMap;
    }

    private void createRouteId2lineIdMap() {
        this.log.debug("Creating routeId2lineIdMap...");
        TreeMap<Id, Id> map = new TreeMap<Id, Id>();
        for (Entry<Id, TransitLine> lineEntry : this.transitSchedule.getTransitLines().entrySet()) {
            Id lineId = lineEntry.getKey();
            for (Id routeId : lineEntry.getValue().getRoutes().keySet()) {
                map.put(routeId, lineId);
            }
        }
        this.log.debug("... finished");
        this.routeId2lineIdMap = map;
    }
}
