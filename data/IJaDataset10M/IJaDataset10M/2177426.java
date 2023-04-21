package org.sf.xrime.algorithms.transform.vertex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.sf.xrime.model.edge.Edge;
import org.sf.xrime.model.edge.EdgeUtils;
import org.sf.xrime.model.vertex.AdjSetVertex;

public class AdjVertex2AdjSetVertexReducer extends MapReduceBase implements Reducer<Text, ObjectWritable, Text, AdjSetVertex> {

    Text outputKey = new Text();

    @Override
    public void reduce(Text key, Iterator<ObjectWritable> values, OutputCollector<Text, AdjSetVertex> output, Reporter reporter) throws IOException {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        AdjSetVertex vertex = null;
        while (values.hasNext()) {
            ObjectWritable obj = values.next();
            if (obj.get() instanceof Edge) {
                edges.add((Edge) obj.get());
            }
            if (obj.get() instanceof AdjSetVertex) {
                vertex = (AdjSetVertex) obj.get();
            }
        }
        if (vertex != null) {
            for (Edge edge : edges) {
                vertex.addOpposite(EdgeUtils.getAdjVertexEdgeByEdge(vertex, edge));
            }
            outputKey.set(vertex.getId());
            output.collect(key, vertex);
        }
    }
}
