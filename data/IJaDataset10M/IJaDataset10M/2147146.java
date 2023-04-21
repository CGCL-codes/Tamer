package org.sf.xrime.algorithms.partitions.connected.strongly;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
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
import org.sf.xrime.model.vertex.LabeledAdjBiSetVertex;

/**
 * This class is used to extract Desc(G,v)\SCC(G,v).
 * @author xue
 *
 */
public class ExtractDescMinusSCC extends GraphAlgorithm {

    /**
   * Default constructor.
   */
    public ExtractDescMinusSCC() {
        super();
    }

    /**
   * Mapper.
   * @author xue
   */
    public static class MapClass extends GraphAlgorithmMapReduceBase implements Mapper<Text, LabeledAdjBiSetVertex, Text, LabeledAdjBiSetVertex> {

        @Override
        public void map(Text key, LabeledAdjBiSetVertex value, OutputCollector<Text, LabeledAdjBiSetVertex> output, Reporter reporter) throws IOException {
            if (value.getStringLabel(ConstantLabels.FORWARD_LABEL) != null && value.getStringLabel(ConstantLabels.BACKWARD_LABEL) == null) {
                for (AdjVertexEdge edge : value.getBackwardVertexes()) {
                    LabeledAdjBiSetVertex notifier = new LabeledAdjBiSetVertex();
                    notifier.setId(edge.getOpposite());
                    notifier.setStringLabel(ConstantLabels.DESCENDANT_TO_BE_RETAINED, key.toString());
                    output.collect(new Text(edge.getOpposite()), notifier);
                }
                for (AdjVertexEdge edge : value.getForwardVertexes()) {
                    LabeledAdjBiSetVertex notifier = new LabeledAdjBiSetVertex();
                    notifier.setId(edge.getOpposite());
                    notifier.setStringLabel(ConstantLabels.ANCESTOR_TO_BE_RETAINED, key.toString());
                    output.collect(new Text(edge.getOpposite()), notifier);
                }
                value.clearLabels();
                value.clearBackwardVertex();
                value.clearForwardVertex();
                output.collect(key, value);
            }
        }
    }

    /**
   * Reducer.
   * @author xue
   */
    public static class ReduceClass extends GraphAlgorithmMapReduceBase implements Reducer<Text, LabeledAdjBiSetVertex, Text, LabeledAdjBiSetVertex> {

        @Override
        public void reduce(Text key, Iterator<LabeledAdjBiSetVertex> values, OutputCollector<Text, LabeledAdjBiSetVertex> output, Reporter reporter) throws IOException {
            LabeledAdjBiSetVertex the_vertex = null;
            HashSet<String> ancestor_to_be_retained = new HashSet<String>();
            HashSet<String> descendant_to_be_retained = new HashSet<String>();
            while (values.hasNext()) {
                LabeledAdjBiSetVertex curr = values.next();
                String ancestor = curr.getStringLabel(ConstantLabels.ANCESTOR_TO_BE_RETAINED);
                String descendant = curr.getStringLabel(ConstantLabels.DESCENDANT_TO_BE_RETAINED);
                if (ancestor != null) {
                    ancestor_to_be_retained.add(ancestor);
                } else if (descendant != null) {
                    descendant_to_be_retained.add(descendant);
                } else {
                    the_vertex = new LabeledAdjBiSetVertex();
                    the_vertex.setId(key.toString());
                }
            }
            if (the_vertex != null) {
                for (String id : ancestor_to_be_retained) {
                    the_vertex.addBackwardVertex(new AdjVertexEdge(id));
                }
                for (String id : descendant_to_be_retained) {
                    the_vertex.addForwardVertex(new AdjVertexEdge(id));
                }
                output.collect(key, the_vertex);
            }
        }
    }

    @Override
    public void execute() throws ProcessorExecutionException {
        JobConf conf = new JobConf(context, ExtractDescMinusSCC.class);
        conf.setJobName("ExtractDescMinusSCC");
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(LabeledAdjBiSetVertex.class);
        conf.setMapperClass(MapClass.class);
        conf.setReducerClass(ReduceClass.class);
        conf.setInputFormat(SequenceFileInputFormat.class);
        conf.setOutputFormat(SequenceFileOutputFormat.class);
        try {
            FileInputFormat.setInputPaths(conf, getSource().getPath());
            FileOutputFormat.setOutputPath(conf, getDestination().getPath());
        } catch (IllegalAccessException e1) {
            throw new ProcessorExecutionException(e1);
        }
        conf.setNumMapTasks(getMapperNum());
        conf.setNumReduceTasks(getReducerNum());
        conf.setMapOutputCompressorClass(GzipCodec.class);
        conf.setCompressMapOutput(true);
        try {
            this.runningJob = JobClient.runJob(conf);
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        }
    }
}
