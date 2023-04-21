package org.sf.xrime.algorithms.statistics.egoCentric2;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.sf.xrime.ProcessorExecutionException;
import org.sf.xrime.algorithms.GraphAlgorithm;
import org.sf.xrime.algorithms.utils.GraphAlgorithmMapReduceBase;
import org.sf.xrime.model.edge.AdjVertexEdge;
import org.sf.xrime.model.edge.Edge;
import org.sf.xrime.model.vertex.AdjBiSetVertex;
import org.sf.xrime.model.vertex.AdjSetVertex;

/**
 * Find arcs among neighbours of each vertex. This algorithm is designed in an arc-centered way.
 * <p>
 * This algorithm assumes the input is in the form of AdjBiSetVertex.
 * @author weixue@cn.ibm.com
 */
public class FindInterestingArcs extends GraphAlgorithm {

    public static class MapClass extends GraphAlgorithmMapReduceBase implements Mapper<Text, AdjBiSetVertex, Text, AdjSetVertex> {

        /** A reused edge object. */
        Edge reused_edge = new Edge();

        /** A reused text object. */
        Text reused_key = new Text();

        /** A reused vertex object. */
        AdjSetVertex reused_vertex = new AdjSetVertex();

        @Override
        public void map(Text key, AdjBiSetVertex value, OutputCollector<Text, AdjSetVertex> output, Reporter reporter) throws IOException {
            for (AdjVertexEdge forward_oppo : value.getForwardVertexes()) {
                reused_edge.setFrom(value.getId());
                reused_edge.setTo(forward_oppo.getOpposite());
                reused_key.set(reused_edge.toString());
                reused_vertex.setId(value.getId());
                reused_vertex.setOpposites(value.getBackwardVertexes());
                output.collect(reused_key, reused_vertex);
            }
            for (AdjVertexEdge backward_oppo : value.getBackwardVertexes()) {
                reused_edge.setFrom(backward_oppo.getOpposite());
                reused_edge.setTo(value.getId());
                reused_key.set(reused_edge.toString());
                reused_vertex.setId(value.getId());
                reused_vertex.setOpposites(value.getBackwardVertexes());
                output.collect(reused_key, reused_vertex);
            }
        }
    }

    public static class ReduceClass extends GraphAlgorithmMapReduceBase implements Reducer<Text, AdjSetVertex, Text, Edge> {

        /** Reused edge object. */
        Edge reused_edge = new Edge();

        /** Reused text object. */
        Text reused_key = new Text();

        @Override
        public void reduce(Text key, Iterator<AdjSetVertex> values, OutputCollector<Text, Edge> output, Reporter reporter) throws IOException {
            String edge_str = key.toString();
            reused_edge.fromString(edge_str);
            try {
                AdjSetVertex one_end = new AdjSetVertex(values.next());
                AdjSetVertex other_end = new AdjSetVertex(values.next());
                one_end.getOpposites().retainAll(other_end.getOpposites());
                for (AdjVertexEdge oppo : one_end.getOpposites()) {
                    reused_key.set(oppo.getOpposite());
                    output.collect(reused_key, reused_edge);
                }
            } catch (NoSuchElementException e) {
                throw new IOException(e);
            }
        }
    }

    @Override
    public void execute() throws ProcessorExecutionException {
        JobConf conf = new JobConf(context, FindInterestingArcs.class);
        conf.setJobName("FindInterestingArcs");
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Edge.class);
        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(AdjSetVertex.class);
        conf.setMapperClass(MapClass.class);
        conf.setReducerClass(ReduceClass.class);
        conf.setInputFormat(SequenceFileInputFormat.class);
        conf.setOutputFormat(SequenceFileOutputFormat.class);
        conf.setCompressMapOutput(true);
        conf.setMapOutputCompressorClass(GzipCodec.class);
        try {
            FileInputFormat.setInputPaths(conf, getSource().getPath());
            FileOutputFormat.setOutputPath(conf, getDestination().getPath());
        } catch (IllegalAccessException e1) {
            throw new ProcessorExecutionException(e1);
        }
        conf.setNumMapTasks(getMapperNum());
        conf.setNumReduceTasks(getReducerNum());
        try {
            this.runningJob = JobClient.runJob(conf);
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        }
    }
}
