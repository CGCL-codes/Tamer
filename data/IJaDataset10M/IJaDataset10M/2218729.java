package playground.johannes.socialnetworks.survey.ivt2009.analysis;

import gnu.trove.TDoubleArrayList;
import gnu.trove.TObjectDoubleHashMap;
import gnu.trove.TObjectDoubleIterator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import playground.johannes.sna.graph.Graph;
import playground.johannes.sna.graph.analysis.Degree;
import playground.johannes.sna.graph.analysis.ModuleAnalyzerTask;
import playground.johannes.sna.math.Distribution;
import playground.johannes.socialnetworks.graph.social.SocialGraph;
import playground.johannes.socialnetworks.graph.social.SocialVertex;
import playground.johannes.socialnetworks.statistics.Correlations;

/**
 * @author illenberger
 *
 */
public class FrequencyDegree extends ModuleAnalyzerTask<Degree> {

    @Override
    public void analyze(Graph graph, Map<String, DescriptiveStatistics> statsMap) {
        try {
            SocialGraph g = (SocialGraph) graph;
            TDoubleArrayList values1 = new TDoubleArrayList();
            TDoubleArrayList values2 = new TDoubleArrayList();
            Frequency freq = new Frequency();
            TObjectDoubleHashMap<SocialVertex> meanFreq = freq.meanEdgeFrequency(g.getVertices());
            TObjectDoubleIterator<SocialVertex> it = meanFreq.iterator();
            for (int i = 0; i < meanFreq.size(); i++) {
                it.advance();
                values1.add(it.key().getNeighbours().size());
                values2.add(it.value());
            }
            Correlations.writeToFile(Correlations.mean(values1.toNativeArray(), values2.toNativeArray(), 5), getOutputDirectory() + "/freq_mean_k.txt", "k", "freq");
            values1 = new TDoubleArrayList();
            values2 = new TDoubleArrayList();
            freq = new Frequency();
            TObjectDoubleHashMap<SocialVertex> sumFreq = freq.sumEdgeFrequency(g.getVertices());
            it = sumFreq.iterator();
            for (int i = 0; i < meanFreq.size(); i++) {
                it.advance();
                values1.add(it.key().getNeighbours().size());
                values2.add(it.value());
            }
            Correlations.writeToFile(Correlations.mean(values1.toNativeArray(), values2.toNativeArray(), 2), getOutputDirectory() + "/freq_sum_k.txt", "k", "freq");
            Distribution distr = freq.sumEdgeFrequencyDistribtion(g.getVertices());
            Distribution.writeHistogram(distr.absoluteDistributionLog2(1), getOutputDirectory() + "/freq_i.txt");
            distr = freq.freqLengthDistribution(g.getVertices());
            Distribution.writeHistogram(distr.absoluteDistribution(1000), getOutputDirectory() + "freqLength.txt");
            distr = freq.sumFreqLengthDistribution(g.getVertices());
            Distribution.writeHistogram(distr.absoluteDistribution(1000), getOutputDirectory() + "freqCost_i.txt");
            values1 = new TDoubleArrayList();
            values2 = new TDoubleArrayList();
            freq = new Frequency();
            TObjectDoubleHashMap<SocialVertex> sumFreqCost = freq.sumFreqCost(g.getVertices());
            it = sumFreqCost.iterator();
            for (int i = 0; i < meanFreq.size(); i++) {
                it.advance();
                values1.add(it.key().getNeighbours().size());
                values2.add(it.value());
            }
            Correlations.writeToFile(Correlations.mean(values1.toNativeArray(), values2.toNativeArray(), 2), getOutputDirectory() + "/sum_freqCost_k.txt", "k", "c_freq");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
