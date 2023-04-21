package playground.fabrice.primloc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.BasicLocation;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.api.experimental.facilities.ActivityFacilities;
import org.matsim.core.api.experimental.facilities.ActivityFacility;
import org.matsim.core.config.Config;
import org.matsim.core.facilities.ActivityFacilityImpl;
import org.matsim.core.facilities.ActivityOptionImpl;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.knowledges.KnowledgeImpl;
import org.matsim.knowledges.Knowledges;
import org.matsim.population.algorithms.AbstractPersonAlgorithm;
import playground.balmermi.world.Layer;
import playground.balmermi.world.World;
import playground.balmermi.world.Zone;
import playground.balmermi.world.ZoneLayer;
import Jama.Matrix;

public class PrimlocModule extends AbstractPersonAlgorithm {

    private static final String module_name = "primary location choice";

    private PrimlocCore core = new PrimlocCore();

    private String primaryActivityName;

    private Zone[] zones;

    private Layer zoneLayer;

    private HashMap<Zone, Integer> zoneids = new HashMap<Zone, Integer>();

    private HashMap<Zone, ArrayList<ActivityFacilityImpl>> primActFacilitiesPerZone = new HashMap<Zone, ArrayList<ActivityFacilityImpl>>();

    private PrimlocTravelCostAggregator travelCostAggregator;

    private PrimlocCalibrationError errorCalibrationClass;

    CumulativeDistribution externalTripDist;

    private boolean overwriteKnowledge;

    private boolean calibration;

    private boolean unspecifiedMu;

    private static final Logger log = Logger.getLogger(PrimlocModule.class);

    private Random random;

    private final Config cfg;

    private Knowledges knowledges;

    public PrimlocModule(Config config, Knowledges knowledges) {
        this.cfg = config;
        this.knowledges = knowledges;
    }

    @Override
    public void run(Person guy) {
        if (!agentHasPrimaryActivityInPlan(guy)) return;
        KnowledgeImpl knowledge = this.knowledges.getKnowledgesByPersonId().get(guy.getId());
        ActivityFacilityImpl home = knowledge.getActivities("home").get(0).getFacility();
        Zone homezone = (Zone) zoneLayer.getNearestLocations(home.getCoord(), null).get(0);
        if (homezone == null) log.warn("Homeless person (poor guy)"); else {
            int homeZoneID = zoneids.get(homezone);
            double epsilon = MatsimRandom.getRandom().nextDouble();
            double cumul = 0.0;
            int workZoneID = 0;
            while ((cumul < epsilon) && (workZoneID < core.numZ - 1)) {
                cumul += (core.trips.get(homeZoneID, workZoneID)) / core.P[homeZoneID];
                workZoneID++;
            }
            ArrayList<ActivityFacilityImpl> workplaces = primActFacilitiesPerZone.get(zones[workZoneID]);
            while (workplaces.size() == 0) {
                int zid = (int) (random.nextDouble() * core.numZ);
                workplaces = primActFacilitiesPerZone.get(zones[zid]);
            }
            int wid = (int) (random.nextDouble() * workplaces.size());
            ActivityFacilityImpl workplace = workplaces.get(wid);
            if (overwriteKnowledge) knowledge.removeActivities(primaryActivityName);
            knowledge.addActivityOption(new ActivityOptionImpl(primaryActivityName, workplace), true);
        }
    }

    public void setup(World world, Population population, ActivityFacilities facilities) {
        random = new Random(this.cfg.global().getRandomSeed());
        setupParameters();
        setupAggregationLayer(world);
        setupTravelCosts();
        setupNumberHomesPerZone(population);
        setupNumberJobsPerZone(facilities);
        normalizeJobHomeVectors();
        setupCalibrationData();
        if (calibration) core.runCalibrationProcess(); else core.runModel();
    }

    public void setAggregationLayer(Layer zoneLayer) {
        if (!(zoneLayer instanceof ZoneLayer)) Gbl.errorMsg(new Exception("PrimLocChoice_MATSIM needs a Zone Layer"));
        this.zoneLayer = zoneLayer;
    }

    public void setTravelCost(PrimlocTravelCostAggregator travelCostAggregator) {
        this.travelCostAggregator = travelCostAggregator;
    }

    public void setExternalTripDistribution(CumulativeDistribution tripDist) {
        externalTripDist = tripDist;
    }

    private void setupParameters() {
        primaryActivityName = cfg.getParam(module_name, "primary activity");
        overwriteKnowledge = Boolean.parseBoolean(cfg.getParam(module_name, "overwrite knowledge"));
        calibration = Boolean.parseBoolean(cfg.getParam(module_name, "calibration"));
        String muString = cfg.findParam(module_name, "mu");
        unspecifiedMu = (muString == null);
        if (!unspecifiedMu) core.mu = Double.parseDouble(muString);
        core.theta = Double.parseDouble(cfg.getParam(module_name, "theta"));
        core.threshold1 = Double.parseDouble(cfg.getParam(module_name, "threshold1"));
        core.threshold2 = Double.parseDouble(cfg.getParam(module_name, "threshold2"));
        core.threshold3 = Double.parseDouble(cfg.getParam(module_name, "threshold3"));
        core.maxiter = Integer.parseInt(cfg.getParam(module_name, "maxiter"));
        core.verbose = Boolean.parseBoolean(cfg.getParam(module_name, "verbose"));
    }

    private void setupAggregationLayer(World world) {
        if (zoneLayer == null) {
            String layerName = cfg.findParam(module_name, "aggregation layer");
            if (layerName == null) Gbl.errorMsg(new Exception("PrimLocChoice_MATSIM needs an aggregation layer"));
            zoneLayer = world.getLayer(layerName);
            if (!(zoneLayer instanceof ZoneLayer)) Gbl.errorMsg(new Exception("PrimLocChoice_MATSIM needs a Zone Layer"));
        }
        Collection<? extends BasicLocation> listloc = zoneLayer.getLocations().values();
        int internalID = 0;
        core.numZ = listloc.size();
        zones = new Zone[core.numZ];
        for (Object obj : listloc) {
            Zone zone = (Zone) obj;
            zoneids.put(zone, internalID);
            zones[internalID] = zone;
            primActFacilitiesPerZone.put(zone, new ArrayList<ActivityFacilityImpl>());
            internalID++;
        }
    }

    private void setupTravelCosts() {
        String distParam = cfg.findParam(module_name, "euclidean distance costs");
        if (distParam != null) {
            if (Boolean.parseBoolean(distParam)) setEuclideanDistanceImpedances(); else if (travelCostAggregator == null) Gbl.errorMsg(new Exception("PrimLocChoice_MATSIM needs a Travel costs aggregator or euclidean distance costs enabled"));
        } else if (travelCostAggregator == null) Gbl.errorMsg(new Exception("PrimLocChoice_MATSIM needs a Travel costs aggregator or euclidean distance costs enabled"));
        core.setupCostStatistics();
        if (unspecifiedMu) {
            core.mu = core.avgCost;
            if (core.verbose) System.out.println("Setting mu = <cost> = " + core.avgCost);
        }
    }

    private void setupNumberHomesPerZone(Population population) {
        core.P = new double[core.numZ];
        for (Person guy : population.getPersons().values()) if (agentHasPrimaryActivityInPlan(guy)) {
            ActivityFacilityImpl homeOfGuy = this.knowledges.getKnowledgesByPersonId().get(guy.getId()).getActivities("home").get(0).getFacility();
            ArrayList<? extends BasicLocation> list = zoneLayer.getNearestLocations(homeOfGuy.getCoord(), null);
            Zone homezone = (Zone) list.get(0);
            if (homezone == null) log.warn("Homeless employed person (poor guy)"); else core.P[zoneids.get(homezone)]++;
        }
    }

    private boolean agentHasPrimaryActivityInPlan(Person guy) {
        for (Plan plan : guy.getPlans()) {
            for (PlanElement pe : plan.getPlanElements()) {
                if ((pe instanceof Activity) && (((Activity) pe).getType().equals(primaryActivityName))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setupNumberJobsPerZone(ActivityFacilities facilities) {
        core.J = new double[core.numZ];
        for (ActivityFacility facility : facilities.getFacilities().values()) {
            ActivityOptionImpl act = (ActivityOptionImpl) facility.getActivityOptions().get(primaryActivityName);
            if (act != null) {
                ArrayList<? extends BasicLocation> list = zoneLayer.getNearestLocations(facility.getCoord(), null);
                Zone zone = (Zone) list.get(0);
                core.J[zoneids.get(zone)] += act.getCapacity();
                primActFacilitiesPerZone.get(zone).add((ActivityFacilityImpl) facility);
            }
        }
    }

    private void normalizeJobHomeVectors() {
        core.N = 0.0;
        for (int i = 0; i < core.numZ; i++) {
            core.P[i] = Math.max(1.0, core.P[i]);
            core.J[i] = Math.max(1.0, core.J[i]);
            core.N += core.P[i];
        }
        System.out.println("# employed " + core.N);
        core.normalizeJobVector();
        System.out.println("Zone attribute vector:");
        for (int i = 0; i < core.numZ; i++) {
            System.out.println("Zone #" + zones[i].getId() + "\t#residents: " + core.df.format(core.P[i]) + "\t#" + primaryActivityName + ": " + core.df.format(core.J[i]));
        }
    }

    private void setEuclideanDistanceImpedances() {
        core.cij = new Matrix(core.numZ, core.numZ);
        for (int i = 0; i < core.numZ; i++) {
            for (int j = 0; j < core.numZ; j++) core.cij.set(i, j, zones[i].calcDistance(zones[j].getCoord()));
            core.cij.set(i, i, (CoordUtils.calcDistance(zones[i].getMax(), zones[i].getMin())) / 2);
            if (core.cij.get(i, i) == 0.0) Gbl.errorMsg(new Exception("PrimLocChoice_core requires Cii>0 for intrazonal travel costs"));
        }
    }

    private void setupCalibrationData() {
        if (errorCalibrationClass == null) {
            if (externalTripDist == null) return; else {
                errorCalibrationClass = new PrimlocTripDistributionError(externalTripDist, core.cij);
            }
        }
        core.setCalibrationError(errorCalibrationClass);
    }
}
