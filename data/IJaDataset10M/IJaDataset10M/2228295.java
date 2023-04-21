package org.matsim.evacuation.tutorial;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.apache.log4j.Logger;
import org.geotools.data.FeatureSource;
import org.geotools.feature.Feature;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.ScenarioImpl;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.api.core.v01.population.PopulationWriter;
import org.matsim.core.network.LinkImpl;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.matsim.evacuation.base.EvacuationAreaFileWriter;
import org.matsim.evacuation.base.EvacuationAreaLink;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class EvacuationDemandGenerator {

    private static final Logger log = Logger.getLogger(EvacuationDemandGenerator.class);

    private static int ID = 0;

    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.out.println("Usage:");
            System.out.println("EvacuationNetworkGenerator network-file evac-zone-shape-file output-path pop-file epsg-code");
            System.exit(0);
        }
        String network = args[0];
        String evacZone = args[1];
        String outputPath = args[2];
        String popFile = args[3];
        String epsg = args[4];
        Scenario sc = new ScenarioImpl();
        Network net = sc.getNetwork();
        new MatsimNetworkReader(sc).readFile(network);
        log.info("Generating evacuation area links file.");
        FeatureSource fts = ShapeFileReader.readDataFile(evacZone);
        if (fts.getFeatures().size() > 1) {
            log.error("Evacuation zone shape file contains more than one evacuation zone! Exiting!!");
            System.exit(-1);
        }
        Feature ft = (Feature) fts.getFeatures().iterator().next();
        Geometry geo = ft.getDefaultGeometry();
        Map<Id, EvacuationAreaLink> els = new HashMap<Id, EvacuationAreaLink>();
        for (Link link : net.getLinks().values()) {
            if (geo.contains(MGC.coord2Point(link.getCoord()))) {
                EvacuationAreaLink el = new EvacuationAreaLink(link.getId().toString(), 0);
                els.put(link.getId(), el);
            }
        }
        new EvacuationAreaFileWriter(els).writeFile(outputPath + "/evacuation_area.xml");
        log.info("done.");
        sc.getConfig().global().setCoordinateSystem(epsg);
        log.info("Creating synthetic population.");
        Random rnd = new Random();
        Iterator it = ShapeFileReader.readDataFile(popFile).getFeatures().iterator();
        while (it.hasNext()) {
            Feature pFt = (Feature) it.next();
            long pers = (Long) pFt.getAttribute("persons");
            createPersons(sc, pFt, rnd, (int) pers);
        }
        String popFilename = outputPath + "/population.xml";
        new PopulationWriter(sc.getPopulation(), sc.getNetwork()).write(popFilename);
        log.info("population written to: " + popFilename);
    }

    private static void createPersons(Scenario scenario, Feature ft, Random rnd, int number) {
        Population pop = scenario.getPopulation();
        PopulationFactory pb = pop.getFactory();
        for (; number > 0; number--) {
            Person pers = pb.createPerson(scenario.createId(Integer.toString(ID++)));
            pop.addPerson(pers);
            Plan plan = pb.createPlan();
            Point p = getRandomPointInFeature(rnd, ft);
            NetworkLayer net = (NetworkLayer) scenario.getNetwork();
            LinkImpl l = net.getNearestLink(MGC.point2Coord(p));
            Activity act = pb.createActivityFromLinkId("h", l.getId());
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
