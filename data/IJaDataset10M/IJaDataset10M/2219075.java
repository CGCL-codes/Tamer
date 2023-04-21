package net.sf.orcc.tools.merger;

import net.sf.dftools.graph.Vertex;
import net.sf.orcc.df.DfVertex;
import net.sf.orcc.df.Network;

/**
 * This class computes a single appearance schedule (SAS) with 1-level nested
 * loop from the given SDF graph.
 * 
 * @author Ghislain Roquier
 * 
 */
public class SASLoopScheduler extends AbstractScheduler {

    public SASLoopScheduler(Network network) {
        super(network);
    }

    @Override
    public void schedule() {
        schedule = new Schedule();
        schedule.setIterationCount(1);
        TopologicalSorter sort = new TopologicalSorter(network);
        for (Vertex vertex : sort.topologicalSort()) {
            DfVertex vert = (DfVertex) vertex;
            if (vert.isInstance()) {
                int rep = repetitions.get(vert);
                Iterand iterand = null;
                if (rep > 1) {
                    Schedule subSched = new Schedule();
                    subSched.setIterationCount(repetitions.get(vert));
                    subSched.add(new Iterand(vert));
                    iterand = new Iterand(subSched);
                } else {
                    iterand = new Iterand(vert);
                }
                schedule.add(iterand);
            }
        }
    }
}
