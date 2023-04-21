package playground.fabrice.primloc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.matsim.basic.v01.Id;
import org.matsim.config.Config;
import org.matsim.facilities.Activity;
import org.matsim.facilities.Facilities;
import org.matsim.facilities.Facility;
import org.matsim.gbl.Gbl;
import org.matsim.plans.Person;
import org.matsim.plans.Plans;
import org.matsim.plans.algorithms.PersonAlgorithm;
import org.matsim.world.Layer;
import org.matsim.world.Location;
import org.matsim.world.Zone;
import org.matsim.world.ZoneLayer;
import Jama.Matrix;

public class PrimlocDriver extends PersonAlgorithm {

    static final String module_name = "primary location choice";

    private static final Logger log = Logger.getLogger(PrimlocDriver.class);

    PrimlocEngine core = new PrimlocEngine();

    Zone[] zones;

    Layer zonelayer;

    HashMap<Zone, Integer> zoneids = new HashMap<Zone, Integer>();

    HashMap<Zone, ArrayList<Facility>> primActFacilitiesPerZone = new HashMap<Zone, ArrayList<Facility>>();

    String primaryActivityName;

    public void run(Person guy) {
        Facility facility = guy.getKnowledge().getActivities("home").get(0).getFacility();
        Zone homezone = (Zone) this.zonelayer.getNearestLocations(facility.getCenter(), null).get(0);
        if (homezone == null) log.warn("Homeless employed person (poor guy)"); else {
            int homeZoneID = this.zoneids.get(homezone);
            double epsilon = Math.random();
            double cumul = 0.0;
            int workZoneID = 0;
            while ((cumul < epsilon) && (workZoneID < this.core.numZ - 1)) {
                cumul += (this.core.trips.get(homeZoneID, workZoneID)) / this.core.P[homeZoneID];
                workZoneID++;
            }
            ArrayList<Facility> list2 = this.primActFacilitiesPerZone.get(this.zones[workZoneID]);
            while (list2.size() == 0) {
                list2 = this.primActFacilitiesPerZone.get(this.zones[Gbl.random.nextInt(this.core.numZ)]);
            }
            facility = list2.get(Gbl.random.nextInt(list2.size()));
            ArrayList<Activity> primActs = guy.getKnowledge().getActivities(primaryActivityName);
            if (primActs.size() > 0) {
                guy.getKnowledge().removeActivity(primActs.get(0));
            }
            guy.getKnowledge().addActivity(new Activity(primaryActivityName, facility));
        }
    }

    public void setup(Plans plans) {
        primaryActivityName = Gbl.getConfig().getParam(module_name, "primary activity");
        getZonesAndParams();
        getRealTravelImpedances();
        getNumberHomesPerZone(plans);
        getNumberJobsPerZone();
        hack();
        if (this.core.calibration) loadCalibrationMatrix();
        if (this.core.calibration) this.core.calibrationProcess(); else this.core.runModel();
    }

    void getZonesAndParams() {
        Config cfg = Gbl.getConfig();
        this.primaryActivityName = Gbl.getConfig().getParam(module_name, "primary activity");
        String layerName = cfg.getParam(module_name, "aggregation layer");
        if (layerName == null) Gbl.errorMsg(new Exception("PrimLocChoice_MATSIM needs an aggregation layer"));
        this.zonelayer = Gbl.getWorld().getLayer(layerName);
        if (!(this.zonelayer instanceof ZoneLayer)) Gbl.errorMsg(new Exception("PrimLocChoice_MATSIM needs a Zone Layer"));
        this.core.mu = Double.parseDouble(cfg.getParam(module_name, "mu"));
        if (cfg.getParam(module_name, "calibration matrix") != null) {
            log.warn("Matrix importation unsupported - calibration matrix ignored");
        }
        this.core.theta = Double.parseDouble(cfg.getParam(module_name, "theta"));
        this.core.threshold1 = Double.parseDouble(cfg.getParam(module_name, "threshold1"));
        this.core.threshold2 = Double.parseDouble(cfg.getParam(module_name, "threshold2"));
        this.core.threshold3 = Double.parseDouble(cfg.getParam(module_name, "threshold3"));
        this.core.maxiter = Integer.parseInt(cfg.getParam(module_name, "maxiter"));
        this.core.verbose = Boolean.parseBoolean(cfg.getParam(module_name, "verbose"));
        Collection<? extends Location> listloc = this.zonelayer.getLocations().values();
        int internalID = 0;
        this.core.numZ = listloc.size();
        this.zones = new Zone[this.core.numZ];
        for (Object obj : listloc) {
            Zone zone = (Zone) obj;
            this.zoneids.put(zone, internalID);
            this.zones[internalID] = zone;
            this.primActFacilitiesPerZone.put(zone, new ArrayList<Facility>());
            internalID++;
        }
    }

    void loadCalibrationMatrix() {
    }

    void getTravelImpedances() {
        this.core.cij = new Matrix(this.core.numZ, this.core.numZ);
        for (int i = 0; i < this.core.numZ; i++) {
            for (int j = 0; j < this.core.numZ; j++) {
                this.core.cij.set(i, j, this.zones[i].calcDistance(this.zones[j].getCenter()));
            }
            this.core.cij.set(i, i, (this.zones[i].getMax().calcDistance(this.zones[i].getMin())) / 2);
            if (this.core.cij.get(i, i) == 0.0) {
                Gbl.errorMsg(new Exception("PrimLocChoice_core requires Cii>0 for intrazonal travel costs"));
            }
        }
        this.core.setupECij();
    }

    void getNumberHomesPerZone(Plans plans) {
        this.core.P = new double[this.core.numZ];
        Map<Id, Person> agents = plans.getPersons();
        for (Person guy : agents.values()) {
            Facility facility = guy.getKnowledge().getActivities("home").get(0).getFacility();
            ArrayList<Location> list = this.zonelayer.getNearestLocations(facility.getCenter(), null);
            Zone homezone = (Zone) list.get(0);
            if (homezone == null) log.warn("Homeless employed person (poor guy)"); else this.core.P[this.zoneids.get(homezone)]++;
        }
    }

    void getNumberJobsPerZone() {
        this.core.J = new double[this.core.numZ];
        Collection<? extends Facility> facilities = ((Facilities) Gbl.getWorld().getLayer(Facilities.LAYER_TYPE)).getFacilities().values();
        for (Facility facility : facilities) {
            Activity act = facility.getActivity(this.primaryActivityName);
            if (act != null) {
                ArrayList<Location> list = this.zonelayer.getNearestLocations(facility.getCenter(), null);
                Zone zone = (Zone) list.get(0);
                this.core.J[this.zoneids.get(zone)] += act.getCapacity();
                this.primActFacilitiesPerZone.get(zone).add(facility);
            }
        }
    }

    void hack() {
        this.core.N = 0.0;
        for (int i = 0; i < this.core.numZ; i++) {
            this.core.P[i] = Math.max(1.0, this.core.P[i]);
            this.core.J[i] = Math.max(1.0, this.core.J[i]);
            this.core.N += this.core.P[i];
        }
        System.out.println("# employed " + this.core.N);
        this.core.normalizeJobVector();
        System.out.println("Zone attribute vector:");
        for (int i = 0; i < this.core.numZ; i++) {
            System.out.println("Zone #" + this.zones[i].getId() + "\t#residents: " + this.core.df.format(this.core.P[i]) + "\t#" + this.primaryActivityName + ": " + this.core.df.format(this.core.J[i]));
        }
    }

    void getRealTravelImpedances() {
        String costFileName = null;
        this.core.cij = new Matrix(this.core.numZ, this.core.numZ);
        costFileName = "/playground/fabrice/scenarios/lyon/tt_dist_ OD.txt";
        this.core.cij = SomeIO.readMatrix1(costFileName, this.core.numZ, this.core.numZ);
        for (int i = 0; i < this.core.numZ; i++) {
            double mincij = Double.POSITIVE_INFINITY;
            for (int j = 0; j < this.core.numZ; j++) {
                double v = this.core.cij.get(i, j);
                if ((v < mincij) && (v > 0.0)) mincij = v;
            }
            if (this.core.cij.get(i, i) == 0.0) this.core.cij.set(i, i, mincij);
        }
        this.core.setupECij();
    }
}
