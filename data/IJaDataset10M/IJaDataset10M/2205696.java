package playground.johannes.socialnetworks.survey.ivt2009.analysis;

import java.util.Map;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import playground.johannes.sna.graph.Graph;
import playground.johannes.sna.graph.analysis.AnalyzerTask;
import playground.johannes.sna.snowball.SampledVertex;
import playground.johannes.socialnetworks.graph.social.SocialGraph;
import playground.johannes.socialnetworks.graph.social.SocialVertex;

/**
 * @author illenberger
 * 
 */
public class GenderResponseRateTask extends AnalyzerTask {

    private static final Logger logger = Logger.getLogger(GenderResponseRateTask.class);

    @Override
    public void analyze(Graph g, Map<String, DescriptiveStatistics> results) {
        SocialGraph graph = (SocialGraph) g;
        int males = 0;
        int males_egos = 0;
        int females = 0;
        int females_egos = 0;
        for (SocialVertex v : graph.getVertices()) {
            String sex = v.getPerson().getPerson().getSex();
            if (sex != null) {
                if (sex.equals("m")) {
                    males++;
                    if (((SampledVertex) v).isSampled()) {
                        males_egos++;
                    }
                } else {
                    females++;
                    if (((SampledVertex) v).isSampled()) {
                        females_egos++;
                    }
                }
            }
        }
        logger.info(String.format("Males: %1$s (%2$.4f); females: %3$s (%4$.4f).", males_egos, males_egos / (double) males, females_egos, females_egos / (double) females));
    }
}
