package playground.johannes.socialnetworks.graph.analysis;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import playground.johannes.sna.graph.Graph;
import playground.johannes.sna.graph.analysis.AnalyzerTask;
import playground.johannes.sna.graph.analysis.GraphAnalyzer;

/**
 * @author illenberger
 *
 */
public class AnalyzerTaskArray extends AnalyzerTask {

    private Map<String, AnalyzerTask> analyzers;

    public AnalyzerTaskArray() {
        analyzers = new LinkedHashMap<String, AnalyzerTask>();
    }

    public void addAnalyzerTask(AnalyzerTask task, String key) {
        analyzers.put(key, task);
    }

    @Override
    public void analyze(Graph graph, Map<String, DescriptiveStatistics> statsMap) {
        for (Entry<String, AnalyzerTask> entry : analyzers.entrySet()) {
            try {
                String output = String.format("%1$s/%2$s/", getOutputDirectory(), entry.getKey());
                new File(output).mkdirs();
                GraphAnalyzer.analyze(graph, entry.getValue(), output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
