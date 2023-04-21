package playground.yu.counts.scaleFactor.fromEvents;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.matsim.analysis.VolumesAnalyzer;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.CountsConfigGroup;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.core.config.ConfigUtils;
import org.matsim.counts.Count;
import org.matsim.counts.CountSimComparison;
import org.matsim.counts.Counts;
import org.matsim.counts.MatsimCountsReader;
import org.matsim.counts.Volume;

/**
 * calculates countsScaleFactor meeting different criteria (e.g. min MRE).
 *
 * @author yu
 *
 */
public class CountsScaleFactorCalculator {

    protected class CountsComparisonAlgorithm {

        /** The VolumesAnalyszer of the simulation */
        private final VolumesAnalyzer volumes;

        /** The counts object */
        private final Counts counts;

        /** The result list */
        private final List<CountSimComparison> countSimComp;

        private Node distanceFilterNode = null;

        private Double distanceFilter = null;

        private final Network network;

        private double countsScaleFactor;

        private final Logger log = Logger.getLogger(CountsComparisonAlgorithm.class);

        public CountsComparisonAlgorithm(final VolumesAnalyzer volumes, final Counts counts, final Network network, final double countsScaleFactor) {
            this.volumes = volumes;
            this.counts = counts;
            countSimComp = new ArrayList<CountSimComparison>();
            this.network = network;
            this.countsScaleFactor = countsScaleFactor;
        }

        /**
		 *
		 * @return the result list
		 */
        public List<CountSimComparison> getComparison() {
            return countSimComp;
        }

        /**
		 *
		 * @param linkid
		 * @return <code>true</true> if the Link with the given Id is not farther away than the
		 * distance specified by the distance filter from the center node of the filter.
		 */
        private boolean isInRange(final Id linkid) {
            if (distanceFilterNode == null || distanceFilter == null) {
                return true;
            }
            Link l = network.getLinks().get(linkid);
            if (l == null) {
                log.warn("Cannot find requested link: " + linkid.toString());
                return false;
            }
            double dist = CoordUtils.calcDistance(l.getCoord(), distanceFilterNode.getCoord());
            return dist < distanceFilter.doubleValue();
        }

        public void setCountsScaleFactor(final double countsScaleFactor) {
            this.countsScaleFactor = countsScaleFactor;
        }

        /**
		 * Set a distance filter, dropping everything out which is not in the
		 * distance given in meters around the given Node Id.
		 *
		 * @param distance
		 * @param nodeId
		 */
        public void setDistanceFilter(final Double distance, final String nodeId) {
            distanceFilter = distance;
            distanceFilterNode = network.getNodes().get(new IdImpl(nodeId));
        }

        public void getBestScaleFactors(int startHour, int endHour) {
            double simValSquareSum = 0d, countSimProductSum = 0d;
            double simValSum = 0d, countValSum = 0d;
            double simCntQuotientSum = 0d, simCntQuotientSquareSum = 0d;
            if (startHour < 1 || startHour > endHour || endHour > 24) {
                throw new RuntimeException("Illegal start- and endHour for count comparison! [1-24]");
            }
            double countValue;
            for (Count count : counts.getCounts().values()) {
                if (!isInRange(count.getLocId())) {
                    continue;
                }
                double[] volumes = this.volumes.getVolumesPerHourForLink(count.getLocId());
                if (volumes.length == 0) {
                    log.warn("No volumes for link: " + count.getLocId().toString());
                    volumes = new double[24];
                }
                for (int hour = startHour; hour <= endHour; hour++) {
                    Volume volume = count.getVolume(hour);
                    if (volume != null) {
                        countValue = volume.getValue();
                        double simValue = volumes[hour - 1];
                        simValue *= countsScaleFactor;
                        simValSquareSum += simValue * simValue;
                        countSimProductSum += countValue * simValue;
                        simValSum += simValue;
                        countValSum += countValue;
                        double simCntQuotient = countValue > 0d ? simValue / countValue : 11d;
                        simCntQuotientSum += simCntQuotient;
                        simCntQuotientSquareSum += simCntQuotient * simCntQuotient;
                    } else {
                        countValue = 0.0;
                    }
                }
            }
            if (simValSquareSum <= 0) {
                throw new RuntimeException("sigma(sim^2)==0, no agents were simulated?");
            }
            double a = countsScaleFactor * countSimProductSum / simValSquareSum, b = countsScaleFactor * simCntQuotientSum / simCntQuotientSquareSum, c = countsScaleFactor * countValSum / simValSum;
            System.out.println("best scaleFactor for minimal MSB (mean squred bias) (  sigma[(sim-cnt)^2]  ) could be\t" + a + "\t!");
            System.out.println("best scaleFactor for minimal MSRE (mean squred relative errors) ( sigma[(sim/cnt-1)^2] ) could be\t" + b + "\t!");
            System.out.println("best scaleFactor for minimal MB (mean bias) (  [sigma(sim-cnt)]^2  ) could be\t" + c + "\t!");
            System.out.println("simSum =\t" + simValSum + "\tcntSum=\t" + countValSum);
            for (double csf = countsScaleFactor / 2d; csf < countsScaleFactor * 2d; csf += 0.05) {
                System.out.println("countScaleFactor =\t" + csf + "\tdifference=\t" + (simValSum * csf / countsScaleFactor - countValSum));
            }
        }
    }

    private final String eventsFilename;

    private final Logger log = Logger.getLogger(CountsScaleFactorCalculator.class);

    private final Scenario scenario;

    private Config config;

    private Counts counts;

    private final int startHour, endHour;

    /**
	 * @param endHour
	 * @param startHour
	 * @param config
	 */
    public CountsScaleFactorCalculator(String configFilename, String eventsFilename, int startHour, int endHour) {
        scenario = ScenarioUtils.loadScenario(ConfigUtils.loadConfig(configFilename));
        this.eventsFilename = eventsFilename;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public void run() {
        config = scenario.getConfig();
        counts = new Counts();
        CountsConfigGroup countsCG = config.counts();
        new MatsimCountsReader(counts).readFile(countsCG.getCountsFileName());
        double scaleFactor = countsCG.getCountsScaleFactor();
        log.info("compare with counts, scaleFactor =\t" + scaleFactor);
        Network network = scenario.getNetwork();
        VolumesAnalyzer volumes = new VolumesAnalyzer(3600, 24 * 3600 - 1, network);
        EventsManager events = EventsUtils.createEventsManager();
        events.addHandler(volumes);
        new MatsimEventsReader(events).readFile(eventsFilename);
        CountsComparisonAlgorithm cca = new CountsComparisonAlgorithm(volumes, counts, network, scaleFactor);
        if (countsCG.getDistanceFilter() != null && countsCG.getDistanceFilterCenterNode() != null) {
            cca.setDistanceFilter(countsCG.getDistanceFilter(), countsCG.getDistanceFilterCenterNode());
        }
        cca.setCountsScaleFactor(scaleFactor);
        cca.getBestScaleFactors(startHour, endHour);
    }

    /**
	 * @param args
	 *            - args[0] configFilename; args[1] eventsFilename, args[2]
	 *            and args[3] time range of calculating countScaleFactor
	 */
    public static void run(String[] args) {
        CountsScaleFactorCalculator rccfls = new CountsScaleFactorCalculator(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        rccfls.run();
    }

    /**
	 * @param args
	 *            - args[0] configFilename; args[1] eventsFilename, args[2] and
	 *            args[3] time range of calculating countScaleFactor
	 */
    public static void main(String[] args) {
        run(args);
    }
}
