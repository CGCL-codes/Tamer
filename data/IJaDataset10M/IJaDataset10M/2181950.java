package playground.johannes.socialnetworks.survey.ivt2009.analysis.deprecated;

import gnu.trove.TDoubleObjectHashMap;
import gnu.trove.TDoubleObjectIterator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import playground.johannes.sna.graph.Graph;
import playground.johannes.sna.graph.analysis.AnalyzerTask;
import playground.johannes.sna.math.Discretizer;
import playground.johannes.sna.math.LinearDiscretizer;
import playground.johannes.sna.snowball.SampledVertex;
import playground.johannes.socialnetworks.graph.social.SocialGraph;
import playground.johannes.socialnetworks.graph.social.SocialVertex;
import playground.johannes.socialnetworks.graph.spatial.analysis.Distance;

/**
 * @author illenberger
 * 
 */
public class DistanceAge extends AnalyzerTask {

    @Override
    public void analyze(Graph graph, Map<String, DescriptiveStatistics> statsMap) {
        TDoubleObjectHashMap<Set<SocialVertex>> partitions = new TDoubleObjectHashMap<Set<SocialVertex>>();
        Discretizer discr = new LinearDiscretizer(5.0);
        SocialGraph g = (SocialGraph) graph;
        for (SocialVertex vertex : g.getVertices()) {
            if (((SampledVertex) vertex).isSampled()) {
                double age = discr.discretize(vertex.getPerson().getAge());
                Set<SocialVertex> partition = partitions.get(age);
                if (partition == null) {
                    partition = new HashSet<SocialVertex>();
                    partitions.put(age, partition);
                }
                partition.add(vertex);
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getOutputDirectory() + "/d_age.txt"));
            writer.write("age\tdist");
            writer.newLine();
            Distance dist = new Distance();
            TDoubleObjectIterator<Set<SocialVertex>> it = partitions.iterator();
            for (int i = 0; i < partitions.size(); i++) {
                it.advance();
                double mean = dist.vertexMeanDistribution(it.value()).mean();
                writer.write(String.valueOf(it.key()));
                writer.write("\t");
                writer.write(String.valueOf(mean));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
