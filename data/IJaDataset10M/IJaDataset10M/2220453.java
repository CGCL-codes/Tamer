package tutorial.programming.example08DemandGeneration;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import org.apache.log4j.Logger;
import org.geotools.data.FeatureSource;
import org.geotools.feature.Feature;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.ScenarioImpl;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.api.core.v01.population.PopulationWriter;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.ShapeFileReader;
import com.vividsolutions.jts.geom.Point;

/**
 * This class generates a simple artificial MATSim demand for
 * the german city Löbau. This is similar to the tutorial held
 * at the MATSim user meeting 09 by glaemmel, however based on
 * the matsim api.
 *
 * The files needed to run this tutorial are placed in the matsim examples
 * repository that can be found in the root directory of the matsim
 * sourceforge svn under the path matsimExamples/tutorial/example8DemandGeneration.
 *
 * @author glaemmel
 * @author dgrether
 *
 */
public class DemandGenerator {

    private static final Logger log = Logger.getLogger(DemandGenerator.class);

    private static int ID = 0;

    private static final String exampleDirectory = "../matsimExamples/tutorial/example8DemandGeneration/";

    public static void main(String[] args) throws IOException {
        String zonesFile = exampleDirectory + "zones.shp";
        Scenario scenario = new ScenarioImpl();
        FeatureSource fts = ShapeFileReader.readDataFile(zonesFile);
        Random rnd = new Random();
        Feature commercial = null;
        Feature recreation = null;
        Iterator<Feature> it = fts.getFeatures().iterator();
        while (it.hasNext()) {
            Feature ft = it.next();
            if (((String) ft.getAttribute("type")).equals("commercial")) {
                commercial = ft;
            } else if (((String) ft.getAttribute("type")).equals("recreation")) {
                recreation = ft;
            } else if (((String) ft.getAttribute("type")).equals("housing")) {
                long l = ((Long) ft.getAttribute("inhabitant"));
                createPersons(scenario, ft, rnd, (int) l);
            } else {
                throw new RuntimeException("Unknown zone type:" + ft.getAttribute("type"));
            }
        }
        createActivities(scenario, rnd, recreation, commercial);
        String popFilename = exampleDirectory + "population.xml";
        new PopulationWriter(scenario.getPopulation(), scenario.getNetwork()).write(popFilename);
        log.info("population written to: " + popFilename);
    }

    private static void createActivities(Scenario scenario, Random rnd, Feature recreation, Feature commercial) {
        Population pop = scenario.getPopulation();
        PopulationFactory pb = pop.getFactory();
        for (Person pers : pop.getPersons().values()) {
            Plan plan = pers.getPlans().get(0);
            Activity homeAct = (Activity) plan.getPlanElements().get(0);
            homeAct.setEndTime(7 * 3600);
            Leg leg = pb.createLeg(TransportMode.car);
            plan.addLeg(leg);
            Point p = getRandomPointInFeature(rnd, commercial);
            Activity work = pb.createActivityFromCoord("w", scenario.createCoord(p.getX(), p.getY()));
            double startTime = 8 * 3600;
            work.setStartTime(startTime);
            work.setEndTime(startTime + 6 * 3600);
            plan.addActivity(work);
            plan.addLeg(pb.createLeg(TransportMode.car));
            p = getRandomPointInFeature(rnd, recreation);
            Activity leisure = pb.createActivityFromCoord("l", scenario.createCoord(p.getX(), p.getY()));
            leisure.setEndTime(3600 * 19);
            plan.addActivity(leisure);
            plan.addLeg(pb.createLeg(TransportMode.car));
            Activity homeActII = pb.createActivityFromCoord("h", homeAct.getCoord());
            plan.addActivity(homeActII);
        }
    }

    private static void createPersons(Scenario scenario, Feature ft, Random rnd, int number) {
        Population pop = scenario.getPopulation();
        PopulationFactory pb = pop.getFactory();
        for (; number > 0; number--) {
            Person pers = pb.createPerson(scenario.createId(Integer.toString(ID++)));
            pop.addPerson(pers);
            Plan plan = pb.createPlan();
            Point p = getRandomPointInFeature(rnd, ft);
            Activity act = pb.createActivityFromCoord("h", scenario.createCoord(p.getX(), p.getY()));
            plan.addActivity(act);
            pers.addPlan(plan);
        }
    }

    private static Point getRandomPointInFeature(Random rnd, Feature ft) {
        Point p = null;
        double x, y;
        do {
            x = ft.getBounds().getMinX() + rnd.nextDouble() * (ft.getBounds().getMaxX() - ft.getBounds().getMinX());
            y = ft.getBounds().getMinY() + rnd.nextDouble() * (ft.getBounds().getMaxY() - ft.getBounds().getMinY());
            p = MGC.xy2Point(x, y);
        } while (ft.getDefaultGeometry().contains(p));
        return p;
    }
}
