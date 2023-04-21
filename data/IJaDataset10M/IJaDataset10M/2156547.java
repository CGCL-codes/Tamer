package playground.tnicolai.matsim4opus.matsim4urbansim.archive;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.ShutdownEvent;
import org.matsim.core.controler.listener.ShutdownListener;
import org.matsim.core.network.LinkImpl;
import org.matsim.core.network.NetworkImpl;
import org.matsim.core.router.costcalculators.TravelTimeAndDistanceBasedTravelDisutility;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.utils.LeastCostPathTree;
import playground.tnicolai.matsim4opus.constants.Constants;
import playground.tnicolai.matsim4opus.gis.GridUtils;
import playground.tnicolai.matsim4opus.gis.SpatialGrid;
import playground.tnicolai.matsim4opus.gis.Zone;
import playground.tnicolai.matsim4opus.gis.ZoneLayer;
import playground.tnicolai.matsim4opus.matsim4urbansim.costcalculators.FreeSpeedTravelTimeCostCalculator;
import playground.tnicolai.matsim4opus.matsim4urbansim.costcalculators.TravelWalkTimeCostCalculator;
import playground.tnicolai.matsim4opus.utils.helperObjects.AggregateObject2NearestNode;
import playground.tnicolai.matsim4opus.utils.helperObjects.Benchmark;
import playground.tnicolai.matsim4opus.utils.helperObjects.CounterObject;
import playground.tnicolai.matsim4opus.utils.io.writer.AnalysisCellBasedAccessibilityCSVWriter;
import playground.tnicolai.matsim4opus.utils.misc.ProgressBar;
import com.vividsolutions.jts.geom.Point;

/**
 * improvements sep'11:
 * 
 * Code improvements since last version (deadline ersa paper):
 * - Aggregated Workplaces: 
 * 	Workplaces with same parcel_id are aggregated to a weighted job (see JobClusterObject)
 * 	This means much less iteration cycles
 * - Less time consuming look-ups: 
 * 	All workplaces are assigned to their nearest node in an pre-proscess step
 * 	(see addNearestNodeToJobClusterArray) instead to do nearest node look-ups in each 
 * 	iteration cycle
 * - Distance based accessibility added: 
 * 	like the travel time accessibility computation now also distances are computed
 * 	with LeastCostPathTree (tnicolai feb'12 distances are replaced by walking times 
 *  which is also linear and corresponds to distances)
 * 
 * improvements jan'12:
 * 
 * - Better readability:
 * 	Removed unused methods such as "addNearestNodeToJobClusterArray" (this is done while gathering/processing
 * 	workplaces). Also all results are now dumped directly from this class. Before, the SpatialGrid 
 * 	tables were transfered to another class to dump out the results. This also improves readability
 * - Workplace data dump:
 * 	Dumping out the used workplace data was simplified, since the simulation now already uses aggregated data.
 * 	Corresponding subroutines aggregating the data are not needed any more (see dumpWorkplaceData()).
 * 	But coordinates of the origin workplaces could not dumped out, this is now done in ReadFromUrbansimParcelModel
 *  during processing the UrbnAism job data
 *  
 *  improvements feb'12
 *  - distance between square centroid and nearest node on road network is considered in the accessibility computation
 *  as walk time of the euclidian distance between both (centroid and nearest node). This walk time is added as an offset 
 *  to each measured travel times
 *  - using walk travel times instead of travel distances. This is because of the betas that are utils/time unit. The walk time
 *  corresponds to distances since this is also linear.
 *  
 *  improvements march'12
 *  - revised distance measure from centroid to network:
 *  	using orthogonal distance from centroid to nearest network link!
 * 
 * TODO: implement configurable betas for different accessibility measures based on different costs
 * beta, betaTravelTimes, betaLnTravelTimes, betaPowerTravelTimes, betaTravelCosts, betaLnTravelCosts,
 * betaPowerTravelCosts, betaTravelDistance, betaLnTravelDistance, betaPowerTravelDistance
 * 
 * @author thomas
 * 
 */
public class CellBasedAccessibilityNetworkControlerListener implements ShutdownListener {

    private static final Logger log = Logger.getLogger(CellBasedAccessibilityNetworkControlerListener.class);

    private AggregateObject2NearestNode[] aggregatedOpportunities;

    private ZoneLayer<CounterObject> startZones;

    private SpatialGrid congestedTravelTimeAccessibilityGrid;

    private SpatialGrid freespeedTravelTimeAccessibilityGrid;

    private SpatialGrid walkTravelTimeAccessibilityGrid;

    private double walkSpeedMeterPerMin = -1;

    private Benchmark benchmark;

    private String fileExtension = "networkBoundary";

    /**
	 * constructor
	 * 
	 * @param startZones
	 * @param aggregatedOpportunities
	 * @param travelTimeAccessibilityGrid
	 * @param travelCostAccessibilityGrid
	 * @param travelDistanceAccessibilityGrid
	 * @param benchmark
	 */
    CellBasedAccessibilityNetworkControlerListener(ZoneLayer<CounterObject> startZones, AggregateObject2NearestNode[] aggregatedOpportunities, SpatialGrid congestedTravelTimeAccessibilityGrid, SpatialGrid freespeedTravelTimeAccessibilityGrid, SpatialGrid walkTravelTimeAccessibilityGrid, Benchmark benchmark) {
        assert (startZones != null);
        this.startZones = startZones;
        assert (aggregatedOpportunities != null);
        this.aggregatedOpportunities = aggregatedOpportunities;
        assert (congestedTravelTimeAccessibilityGrid != null);
        this.congestedTravelTimeAccessibilityGrid = congestedTravelTimeAccessibilityGrid;
        assert (freespeedTravelTimeAccessibilityGrid != null);
        this.freespeedTravelTimeAccessibilityGrid = freespeedTravelTimeAccessibilityGrid;
        assert (walkTravelTimeAccessibilityGrid != null);
        this.walkTravelTimeAccessibilityGrid = walkTravelTimeAccessibilityGrid;
        assert (benchmark != null);
        this.benchmark = benchmark;
    }

    /**
	 * calculating accessibility indicators
	 */
    @Override
    public void notifyShutdown(ShutdownEvent event) {
        log.info("Entering notifyShutdown ...");
        int benchmarkID = this.benchmark.addMeasure("1-Point accessibility computation");
        Controler controler = event.getControler();
        Scenario sc = controler.getScenario();
        double walkSpeedMeterPerSec = sc.getConfig().plansCalcRoute().getWalkSpeed();
        this.walkSpeedMeterPerMin = walkSpeedMeterPerSec * 60.;
        TravelTime ttc = controler.getTravelTimeCalculator();
        LeastCostPathTree lcptCongestedTravelTime = new LeastCostPathTree(ttc, new TravelTimeAndDistanceBasedTravelDisutility(ttc, controler.getConfig().planCalcScore()));
        LeastCostPathTree lcptFreespeedTravelTime = new LeastCostPathTree(ttc, new FreeSpeedTravelTimeCostCalculator());
        LeastCostPathTree lcptWalkTime = new LeastCostPathTree(ttc, new TravelWalkTimeCostCalculator(walkSpeedMeterPerSec));
        NetworkImpl network = (NetworkImpl) controler.getNetwork();
        double depatureTime = 8. * 3600;
        double betaScale = sc.getConfig().planCalcScore().getBrainExpBeta();
        double betaScalePreFactor = 1 / (betaScale);
        double betaCarHour = sc.getConfig().planCalcScore().getTraveling_utils_hr() - sc.getConfig().planCalcScore().getPerforming_utils_hr();
        double betaCarMin = betaCarHour / 60.;
        double betaWalkHour = sc.getConfig().planCalcScore().getTravelingWalk_utils_hr() - sc.getConfig().planCalcScore().getPerforming_utils_hr();
        double betaWalkMin = betaWalkHour / 60.;
        try {
            AnalysisCellBasedAccessibilityCSVWriter accCsvWriter = new AnalysisCellBasedAccessibilityCSVWriter(fileExtension);
            log.info("Computing and writing grid based accessibility measures with following settings:");
            log.info("Departure time (in seconds): " + depatureTime);
            log.info("Beta car traveling utils/h: " + sc.getConfig().planCalcScore().getTraveling_utils_hr());
            log.info("Beta walk traveling utils/h: " + sc.getConfig().planCalcScore().getTravelingWalk_utils_hr());
            log.info("Beta performing utils/h: " + sc.getConfig().planCalcScore().getPerforming_utils_hr());
            log.info("Beta scale: " + betaScale);
            log.info("Beta car traveling per h: " + betaCarHour);
            log.info("Beta car traveling per min: " + betaCarMin);
            log.info("Beta walk traveling per h: " + betaWalkHour);
            log.info("Beta walk traveling per min: " + betaWalkMin);
            log.info("Walk speed (meter/min): " + this.walkSpeedMeterPerMin);
            Iterator<Zone<CounterObject>> startZoneIterator = startZones.getZones().iterator();
            log.info(startZones.getZones().size() + " measurement points are now processing ...");
            ProgressBar bar = new ProgressBar(startZones.getZones().size());
            while (startZoneIterator.hasNext()) {
                bar.update();
                Zone<CounterObject> startZone = startZoneIterator.next();
                Point point = startZone.getGeometry().getCentroid();
                Coord coordFromZone = new CoordImpl(point.getX(), point.getY());
                assert (coordFromZone != null);
                Node fromNode = network.getNearestNode(coordFromZone);
                assert (fromNode != null);
                lcptCongestedTravelTime.calculate(network, fromNode, depatureTime);
                lcptFreespeedTravelTime.calculate(network, fromNode, depatureTime);
                lcptWalkTime.calculate(network, fromNode, depatureTime);
                LinkImpl nearestLink = network.getNearestLink(coordFromZone);
                double distCentroid2Link = nearestLink.calcDistance(coordFromZone);
                double walkTimeOffset_min = (distCentroid2Link / this.walkSpeedMeterPerMin);
                double congestedTravelTimesCarSum = 0.;
                double freespeedTravelTimesCarSum = 0.;
                double travelTimesWalkSum = 0.;
                for (int i = 0; i < this.aggregatedOpportunities.length; i++) {
                    Node destinationNode = this.aggregatedOpportunities[i].getNearestNode();
                    Id nodeID = destinationNode.getId();
                    int jobWeight = this.aggregatedOpportunities[i].getNumberOfObjects();
                    double arrivalTime = lcptCongestedTravelTime.getTree().get(nodeID).getTime();
                    double congestedTravelTime_min = (arrivalTime - depatureTime) / 60.;
                    double freespeedTravelTime_min = lcptFreespeedTravelTime.getTree().get(nodeID).getCost() / 60.;
                    double walkTravelTime_min = lcptWalkTime.getTree().get(nodeID).getCost() / 60.;
                    congestedTravelTimesCarSum += Math.exp(betaScale * ((betaCarMin * congestedTravelTime_min) + (betaWalkMin * walkTimeOffset_min))) * jobWeight;
                    freespeedTravelTimesCarSum += Math.exp(betaScale * ((betaCarMin * freespeedTravelTime_min) + (betaWalkMin * walkTimeOffset_min))) * jobWeight;
                    travelTimesWalkSum += Math.exp(betaScale * (betaWalkMin * (walkTravelTime_min + walkTimeOffset_min))) * jobWeight;
                }
                double congestedTravelTimesCarLogSum = betaScalePreFactor * Math.log(congestedTravelTimesCarSum);
                double freespeedTravelTimesCarLogSum = betaScalePreFactor * Math.log(freespeedTravelTimesCarSum);
                double travelTimesWalkLogSum = betaScalePreFactor * Math.log(travelTimesWalkSum);
                setAccessibilityValues2StartZoneAndSpatialGrid(startZone, congestedTravelTimesCarLogSum, freespeedTravelTimesCarLogSum, travelTimesWalkLogSum);
                accCsvWriter.write(startZone, coordFromZone, fromNode, congestedTravelTimesCarLogSum, freespeedTravelTimesCarLogSum, travelTimesWalkLogSum);
            }
            System.out.println("");
            if (this.benchmark != null && benchmarkID > 0) {
                this.benchmark.stoppMeasurement(benchmarkID);
                log.info("Accessibility computation with " + startZones.getZones().size() + " starting points (origins) and " + this.aggregatedOpportunities.length + " destinations (workplaces) took " + this.benchmark.getDurationInSeconds(benchmarkID) + " seconds (" + this.benchmark.getDurationInSeconds(benchmarkID) / 60. + " minutes).");
            }
            accCsvWriter.close();
            dumpResults();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dumpResults() throws IOException {
        log.info("Writing files ...");
        GridUtils.writeSpatialGridTable(congestedTravelTimeAccessibilityGrid, Constants.MATSIM_4_OPUS_TEMP + Constants.ERSA_CONGESTED_TRAVEL_TIME_ACCESSIBILITY + "_cellsize_" + congestedTravelTimeAccessibilityGrid.getResolution() + fileExtension + Constants.FILE_TYPE_TXT);
        GridUtils.writeSpatialGridTable(freespeedTravelTimeAccessibilityGrid, Constants.MATSIM_4_OPUS_TEMP + Constants.ERSA_FREESPEED_TRAVEL_TIME_ACCESSIBILITY + "_cellsize_" + freespeedTravelTimeAccessibilityGrid.getResolution() + fileExtension + Constants.FILE_TYPE_TXT);
        GridUtils.writeSpatialGridTable(walkTravelTimeAccessibilityGrid, Constants.MATSIM_4_OPUS_TEMP + Constants.ERSA_WALK_TRAVEL_TIME_ACCESSIBILITY + "_cellsize_" + walkTravelTimeAccessibilityGrid.getResolution() + fileExtension + Constants.FILE_TYPE_TXT);
        GridUtils.writeKMZFiles(startZones, congestedTravelTimeAccessibilityGrid, Constants.MATSIM_4_OPUS_TEMP + Constants.ERSA_CONGESTED_TRAVEL_TIME_ACCESSIBILITY + "_cellsize_" + congestedTravelTimeAccessibilityGrid.getResolution() + fileExtension + Constants.FILE_TYPE_KMZ);
        GridUtils.writeKMZFiles(startZones, freespeedTravelTimeAccessibilityGrid, Constants.MATSIM_4_OPUS_TEMP + Constants.ERSA_FREESPEED_TRAVEL_TIME_ACCESSIBILITY + "_cellsize_" + freespeedTravelTimeAccessibilityGrid.getResolution() + fileExtension + Constants.FILE_TYPE_KMZ);
        GridUtils.writeKMZFiles(startZones, walkTravelTimeAccessibilityGrid, Constants.MATSIM_4_OPUS_TEMP + Constants.ERSA_WALK_TRAVEL_TIME_ACCESSIBILITY + "_cellsize_" + walkTravelTimeAccessibilityGrid.getResolution() + fileExtension + Constants.FILE_TYPE_KMZ);
        log.info("Writing files done!");
    }

    /**
	 * @param startZone
	 * @param congestedTravelTimesCarLogSum
	 * @param freespeedTravelTimesCarLogSum
	 * @param accessibilityTravelDistance
	 */
    private void setAccessibilityValues2StartZoneAndSpatialGrid(Zone<CounterObject> startZone, double congestedTravelTimesCarLogSum, double freespeedTravelTimesCarLogSum, double travelTimesWalkLogSum) {
        congestedTravelTimeAccessibilityGrid.setValue(congestedTravelTimesCarLogSum, startZone.getGeometry().getCentroid());
        freespeedTravelTimeAccessibilityGrid.setValue(freespeedTravelTimesCarLogSum, startZone.getGeometry().getCentroid());
        walkTravelTimeAccessibilityGrid.setValue(travelTimesWalkLogSum, startZone.getGeometry().getCentroid());
    }
}
