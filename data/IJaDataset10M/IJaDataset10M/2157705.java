package org.sf.xrime.algorithms.BFS.alg_2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.sf.xrime.algorithms.BFS.BFSLabel;
import org.sf.xrime.algorithms.utils.GraphAlgorithmMapReduceBase;
import org.sf.xrime.model.edge.AbstractEdge;
import org.sf.xrime.model.edge.Edge;
import org.sf.xrime.model.vertex.LabeledAdjVertex;

/**
 * Mapper of BFS. In this algorithm, we choose to "advance the known frontier" in Mapper. 
 * @author weixue@cn.ibm.com
 */
public class BFSMapper extends GraphAlgorithmMapReduceBase implements Mapper<Text, LabeledAdjVertex, Text, LabeledAdjVertex> {

    /**
   * map method of BFSMapper. Emit info to adjacent vertexes if is the init vertex or in
   * the current frontier. 
   */
    @Override
    public void map(Text key, LabeledAdjVertex value, OutputCollector<Text, LabeledAdjVertex> collector, Reporter reporter) throws IOException {
        String id = value.getId();
        String init_vertex = context.get(ConstantLabels.INIT_VERTEX, "");
        boolean is_frontier = value.getLabel(ConstantLabels.IS_FRONTIER) != null;
        BFSLabel label = (BFSLabel) value.getLabel(BFSLabel.bfsLabelPathsKey);
        if (is_frontier || (label.getStatus() == 0 && id.equals(init_vertex))) {
            label.setStatus(1);
            label.addPrep(id);
            value.removeLabel(ConstantLabels.IS_FRONTIER);
            collector.collect(key, value);
            Iterator<AbstractEdge> iter = (Iterator<AbstractEdge>) value.getIncidentElements();
            LabeledAdjVertex notifier = (LabeledAdjVertex) value.clone();
            notifier.setEdges(new ArrayList<Edge>());
            while (iter.hasNext()) {
                collector.collect(new Text(((Edge) iter.next()).getTo()), notifier);
            }
        } else {
            collector.collect(key, value);
        }
    }
}
