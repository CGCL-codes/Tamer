package playground.johannes.studies.plans;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.geotools.feature.Feature;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.MatsimPopulationReader;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.core.scenario.ScenarioUtils;
import playground.johannes.socialnetworks.gis.io.FeatureSHP;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author illenberger
 *
 */
public class Population2Coordinates {

    /**
	 * @param args
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        Scenario scenario = (ScenarioImpl) ScenarioUtils.createScenario(ConfigUtils.createConfig());
        MatsimPopulationReader reader = new MatsimPopulationReader(scenario);
        reader.readFile("/Users/jillenberger/Work/socialnets/data/schweiz/complete/plans/plans.0.10.xml");
        Feature feature = FeatureSHP.readFeatures("/Users/jillenberger/Work/socialnets/data/schweiz/complete/zones/Kanton.shp").iterator().next();
        Set<Coord> coords = getCoords(scenario.getPopulation());
        Set<Point> points = filterCoors(coords, feature.getDefaultGeometry());
        writePoints(points, "/Users/jillenberger/Work/socialnets/data/schweiz/complete/plans/points.zrh.txt");
    }

    public static Set<Coord> getCoords(Population population) {
        Set<Coord> coords = new HashSet<Coord>();
        for (Person person : population.getPersons().values()) {
            Plan plan = person.getPlans().get(0);
            Activity act = (Activity) plan.getPlanElements().get(0);
            Coord c = act.getCoord();
            coords.add(c);
        }
        return coords;
    }

    private static Set<Point> filterCoors(Set<Coord> source, Geometry zone) {
        GeometryFactory factory = new GeometryFactory();
        Set<Point> points = new HashSet<Point>();
        for (Coord c : source) {
            Point p = factory.createPoint(new Coordinate(c.getX(), c.getY()));
            if (zone.contains(p)) points.add(p);
        }
        return points;
    }

    private static void writePoints(Set<Point> points, String file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("x\ty");
        writer.newLine();
        for (Point p : points) {
            writer.write(String.valueOf(p.getX()));
            writer.write("\t");
            writer.write(String.valueOf(p.getY()));
            writer.newLine();
        }
        writer.close();
    }
}
