package playground.johannes.socialnetworks.graph.spatial.analysis;

import gnu.trove.TDoubleArrayList;
import gnu.trove.TObjectDoubleHashMap;
import gnu.trove.TObjectDoubleIterator;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math.stat.StatUtils;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import playground.johannes.sna.graph.Graph;
import playground.johannes.sna.graph.analysis.ModuleAnalyzerTask;
import playground.johannes.sna.graph.spatial.SpatialEdge;
import playground.johannes.sna.graph.spatial.SpatialGraph;
import playground.johannes.sna.graph.spatial.SpatialVertex;
import playground.johannes.sna.math.Discretizer;
import playground.johannes.sna.math.FixedSampleSizeDiscretizer;
import playground.johannes.socialnetworks.gis.SpatialCostFunction;
import playground.johannes.socialnetworks.statistics.Correlations;
import com.vividsolutions.jts.geom.Point;

/**
 * @author illenberger
 *
 */
public class DistanceAccessibilityTask extends ModuleAnalyzerTask<Distance> {

    private static final Logger logger = Logger.getLogger(DistanceAccessibilityTask.class);

    private Set<Point> opportunities;

    private SpatialCostFunction costFunction;

    public DistanceAccessibilityTask(Set<Point> opportunities, SpatialCostFunction costFunction) {
        setModule(new Distance());
        this.costFunction = costFunction;
        this.opportunities = opportunities;
    }

    @Override
    public void analyze(Graph graph, Map<String, DescriptiveStatistics> statsMap) {
        if (getOutputDirectory() != null) {
            SpatialGraph spatialGraph = (SpatialGraph) graph;
            TObjectDoubleHashMap<SpatialVertex> distMap = module.vertexMean((Set<? extends SpatialVertex>) graph.getVertices());
            TObjectDoubleHashMap<SpatialVertex> accessMap = new LogAccessibility().values((Set<? extends SpatialVertex>) graph.getVertices(), costFunction, opportunities);
            double[] accessValues = new double[distMap.size()];
            double[] dValues = new double[distMap.size()];
            TObjectDoubleIterator<SpatialVertex> it = distMap.iterator();
            for (int i = 0; i < distMap.size(); i++) {
                it.advance();
                accessValues[i] = accessMap.get((SpatialVertex) it.key());
                dValues[i] = it.value();
            }
            try {
                double binsize = (StatUtils.max(accessValues) - StatUtils.min(accessValues)) / 20.0;
                Discretizer disc = FixedSampleSizeDiscretizer.create(accessValues, 20);
                Correlations.writeToFile(Correlations.mean(accessValues, dValues, disc), String.format("%1$s/d_mean_access.txt", getOutputDirectory()), "access", "d_mean");
            } catch (IOException e) {
                e.printStackTrace();
            }
            TDoubleArrayList accessValues2 = new TDoubleArrayList(graph.getEdges().size() * 2);
            TDoubleArrayList dValues2 = new TDoubleArrayList(graph.getEdges().size() * 2);
            for (SpatialEdge edge : spatialGraph.getEdges()) {
                double length = edge.length();
                if (!Double.isNaN(length)) {
                    accessValues2.add(accessMap.get(edge.getVertices().getFirst()));
                    dValues2.add(length);
                    accessValues2.add(accessMap.get(edge.getVertices().getSecond()));
                    dValues2.add(length);
                }
            }
            try {
                accessValues = accessValues2.toNativeArray();
                dValues = dValues2.toNativeArray();
                Discretizer disc = FixedSampleSizeDiscretizer.create(accessValues, 100);
                Correlations.writeToFile(Correlations.mean(accessValues, dValues, disc), String.format("%1$s/d_access.txt", getOutputDirectory()), "access", "d");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.warn("No output directory specified!");
        }
    }
}
