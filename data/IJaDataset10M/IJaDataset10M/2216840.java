package playground.mmoyo.cadyts_integration.ptBseAsPlanStrategy.analysis;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.counts.Count;
import org.matsim.counts.CountSimComparison;
import org.matsim.counts.CountSimComparisonImpl;
import org.matsim.counts.Counts;
import org.matsim.counts.Volume;
import org.matsim.pt.counts.SimpleWriter;

/**
 * This is a modified copy of CountsComparisonAlgorithm, in order to realize the
 * same functionality for pt counts.
 */
public class PtBseCountsComparisonAlgorithm {

    /**
	 * The StopAttributes of the simulation
	 */
    protected final PtBseOccupancyAnalyzer oa;

    /**
	 * The counts object
	 */
    protected Counts counts;

    /**
	 * The result list
	 */
    protected final List<CountSimComparison> countSimComp;

    protected Node distanceFilterNode = null;

    protected Double distanceFilter = null;

    protected final Network network;

    protected double countsScaleFactor;

    protected static final Logger log = Logger.getLogger(PtBseCountsComparisonAlgorithm.class);

    protected StringBuffer content = new StringBuffer();

    public PtBseCountsComparisonAlgorithm(final PtBseOccupancyAnalyzer oa, final Counts counts, final Network network, final double countsScaleFactor) {
        this.oa = oa;
        this.counts = counts;
        this.countSimComp = new ArrayList<CountSimComparison>();
        this.network = network;
        this.countsScaleFactor = countsScaleFactor;
    }

    /**
	 * Creates the List with the counts vs sim values stored in the
	 * countAttribute Attribute of this class.
	 */
    final String STR_NOVOLUMES = "No volumes for stop: ";

    final String STR_STOPID = "StopId :\t";

    final String STR_HEAD = "\nhour\tsimVal\tscaledSimVal\tcountVal\n";

    final char CHR_HT = '\t';

    final char CHR_NL = '\n';

    protected void compare() {
        double countValue;
        for (Count count : this.counts.getCounts().values()) {
            Id stopId = count.getLocId();
            if (!isInRange(count.getCoord())) {
                System.out.println("InRange?\t" + isInRange(count.getCoord()));
                continue;
            }
            int[] volumes = this.getVolumesForStop(stopId);
            if (volumes == null) {
                log.warn(STR_NOVOLUMES + stopId);
                continue;
            } else if (volumes.length == 0) {
                log.warn(STR_NOVOLUMES + stopId);
                continue;
            }
            this.content.append(STR_STOPID);
            this.content.append(stopId.toString());
            this.content.append(STR_HEAD);
            for (int hour = 1; hour <= 24; hour++) {
                Volume volume = count.getVolume(hour);
                if (volume != null) {
                    this.content.append(hour);
                    this.content.append(CHR_HT);
                    countValue = volume.getValue();
                    double simValue = volumes[hour - 1];
                    this.content.append(simValue);
                    this.content.append(CHR_HT);
                    simValue *= this.countsScaleFactor;
                    this.content.append(simValue);
                    this.content.append(CHR_HT);
                    this.content.append(countValue);
                    this.content.append(CHR_NL);
                    this.countSimComp.add(new CountSimComparisonImpl(stopId, hour, countValue, simValue));
                } else {
                    countValue = 0.0;
                }
            }
        }
    }

    public int[] getVolumesForStop(Id stopId) {
        return this.oa.getOccupancyVolumesForStop(stopId);
    }

    /**
	 *
	 * @param stopCoord
	 * @return
	 *         <code>true</true> if the Link with the given Id is not farther away than the
	 * distance specified by the distance filter from the center node of the filter.
	 */
    protected boolean isInRange(final Coord stopCoord) {
        if ((this.distanceFilterNode == null) || (this.distanceFilter == null)) {
            return true;
        }
        double dist = CoordUtils.calcDistance(stopCoord, this.distanceFilterNode.getCoord());
        return dist < this.distanceFilter.doubleValue();
    }

    /**
	 *
	 * @return the result list
	 */
    public List<CountSimComparison> getComparison() {
        return this.countSimComp;
    }

    public void run() {
        this.compare();
    }

    /**
	 * Set a distance filter, dropping everything out which is not in the
	 * distance given in meters around the given Node Id.
	 *
	 * @param distance
	 * @param nodeId
	 */
    public void setDistanceFilter(final Double distance, final String nodeId) {
        this.distanceFilter = distance;
        this.distanceFilterNode = this.network.getNodes().get(new IdImpl(nodeId));
    }

    public void setCountsScaleFactor(final double countsScaleFactor) {
        this.countsScaleFactor = countsScaleFactor;
    }

    public void write(String outputFilename) {
        new SimpleWriter(outputFilename, content.toString());
    }
}
