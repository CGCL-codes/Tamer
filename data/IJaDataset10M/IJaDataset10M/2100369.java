package playground.johannes.socialnetworks.sim.analysis;

import gnu.trove.TDoubleDoubleHashMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.contrib.sna.math.FixedSampleSizeDiscretizer;
import org.matsim.contrib.sna.math.Histogram;
import org.matsim.contrib.sna.math.LinearDiscretizer;
import org.matsim.contrib.sna.util.TXTWriter;
import playground.johannes.socialnetworks.graph.social.SocialGraph;
import playground.johannes.socialnetworks.graph.social.SocialVertex;

/**
 * @author illenberger
 *
 */
public class NearestLocation implements PlansAnalyzerTask {

    private final String output;

    private final Map<Person, SocialVertex> vertexMapping;

    public NearestLocation(SocialGraph graph, String output) {
        vertexMapping = new HashMap<Person, SocialVertex>(graph.getVertices().size());
        for (SocialVertex vertex : graph.getVertices()) {
            vertexMapping.put(vertex.getPerson().getPerson(), vertex);
        }
        this.output = output;
    }

    @Override
    public void analyze(Set<Plan> plans, Map<String, Double> map) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Plan plan : plans) {
            for (int idx = 2; idx < plan.getPlanElements().size(); idx += 2) {
                Activity act = (Activity) plan.getPlanElements().get(idx);
                if (act.getType().startsWith("l")) {
                    Activity prev = (Activity) plan.getPlanElements().get(idx - 2);
                    Person person = plan.getPerson();
                    SocialVertex vertex = vertexMapping.get(person);
                    List<Person> opportunities = new ArrayList<Person>(vertex.getNeighbours().size());
                    for (SocialVertex neighbor : vertex.getNeighbours()) {
                        opportunities.add(neighbor.getPerson().getPerson());
                    }
                    stats.addValue(nearestLocation(prev.getCoord(), opportunities));
                }
            }
        }
        try {
            TDoubleDoubleHashMap hist = Histogram.createHistogram(stats, new LinearDiscretizer(1000.0), false);
            TXTWriter.writeMap(hist, "d", "n", output + "/nearestLocs.txt");
            hist = Histogram.createHistogram(stats, FixedSampleSizeDiscretizer.create(stats.getValues(), 1, 50), true);
            TXTWriter.writeMap(hist, "d", "n", output + "/nearestLocs.fixed.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double nearestLocation(Coord origin, List<Person> opportunities) {
        double d_min = Double.MAX_VALUE;
        for (Person p : opportunities) {
            Coord dest = ((Activity) p.getSelectedPlan().getPlanElements().get(0)).getCoord();
            double dx = origin.getX() - dest.getX();
            double dy = origin.getY() - dest.getY();
            double d = Math.sqrt(dx * dx + dy * dy);
            if (d < d_min) {
                d_min = d;
            }
        }
        return d_min;
    }
}
