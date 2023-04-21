package playground.anhorni.locationchoice.cs.depr.filters;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.matsim.api.basic.v01.population.BasicPlanElement;
import org.matsim.core.api.population.Activity;
import org.matsim.core.api.population.Leg;
import org.matsim.core.api.population.Plan;
import playground.anhorni.locationchoice.cs.helper.Trip;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class ActTypeAndAreaTripFilter extends TripFilter {

    private static final Logger log = Logger.getLogger(ActTypeAndAreaTripFilter.class);

    List<Polygon> areaPolygons = null;

    private String actType = null;

    public ActTypeAndAreaTripFilter(String shapeFile, String actType) {
        this.readShapeFile(shapeFile);
        this.actType = actType;
    }

    private void readShapeFile(String shapeFile) {
        if (shapeFile != null) {
            AreaReader reader = new AreaReader();
            reader.readShapeFile(shapeFile);
            this.areaPolygons = reader.getAreaPolygons();
            log.info("Number of polygons: " + this.areaPolygons.size());
        }
    }

    protected boolean filterPlan(final Plan plan, String mode) {
        boolean choiceSetAdded = false;
        final List<? extends BasicPlanElement> actslegs = plan.getPlanElements();
        Activity previousAct = (Activity) actslegs.get(0);
        for (int j = 0; j < actslegs.size(); j = j + 2) {
            Activity act = (Activity) actslegs.get(j);
            if (j < actslegs.size() - 1) {
                Leg leg = (Leg) actslegs.get(j + 1);
                if (act.getType().equals(actType) && this.withinArea(previousAct, act) && leg.getMode().toString().equals(mode)) {
                    Activity afterShoppingAct = (Activity) actslegs.get(j + 2);
                    Trip trip = new Trip(j / 2, previousAct, act, afterShoppingAct);
                    choiceSetAdded = true;
                }
            }
            previousAct = act;
        }
        return choiceSetAdded;
    }

    private boolean withinArea(Activity act0, Activity act1) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coord0 = new Coordinate(act0.getCoord().getX(), act0.getCoord().getY());
        Coordinate coord1 = new Coordinate(act1.getCoord().getX(), act1.getCoord().getY());
        Point point0 = geometryFactory.createPoint(coord0);
        Point point1 = geometryFactory.createPoint(coord1);
        boolean point0Inside = false;
        boolean point1Inside = false;
        Iterator<Polygon> polygon_it = areaPolygons.iterator();
        while (polygon_it.hasNext()) {
            Polygon polygon = polygon_it.next();
            if (polygon.contains(point0)) {
                point0Inside = true;
            }
            if (polygon.contains(point1)) {
                point1Inside = true;
            }
        }
        if (point0Inside && point1Inside) return true; else return false;
    }
}
