package org.sf.xrime.algorithms.layout.gfr;

import java.io.IOException;
import java.util.Random;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.sf.xrime.ProcessorExecutionException;
import org.sf.xrime.algorithms.GraphAlgorithm;
import org.sf.xrime.algorithms.layout.ConstantLabels;
import org.sf.xrime.algorithms.utils.GraphAlgorithmMapReduceBase;
import org.sf.xrime.model.vertex.AdjSetVertex;
import org.sf.xrime.model.vertex.LabeledAdjSetVertex;

/**
 * Generate random initial layout for the graph.
 * @author xue
 */
public class RandomInitialLayoutGenerate extends GraphAlgorithm {

    /**
   * Default constructor.
   */
    public RandomInitialLayoutGenerate() {
        super();
    }

    /**
   * Mapper is enough to generate the initial layout.
   * @author xue
   */
    public static class MapClass extends GraphAlgorithmMapReduceBase implements Mapper<Text, AdjSetVertex, Text, LabeledAdjSetVertex> {

        /**
     * Class level random number generator.
     */
        public static Random rn_generator = new Random(System.currentTimeMillis());

        @Override
        public void map(Text key, AdjSetVertex value, OutputCollector<Text, LabeledAdjSetVertex> output, Reporter reporter) throws IOException {
            int max_x = Integer.parseInt(context.getParameter(ConstantLabels.MAX_X_COORDINATE));
            int max_y = Integer.parseInt(context.getParameter(ConstantLabels.MAX_Y_COORDINATE));
            int x = rn_generator.nextInt(max_x);
            int y = rn_generator.nextInt(max_y);
            LabeledAdjSetVertex result = new LabeledAdjSetVertex(value);
            result.setLabel(ConstantLabels.X_COORDINATE, new IntWritable(x));
            result.setLabel(ConstantLabels.Y_COORDINATE, new IntWritable(y));
            output.collect(key, result);
        }
    }

    @Override
    public void execute() throws ProcessorExecutionException {
        JobConf conf = new JobConf(context, RandomInitialLayoutGenerate.class);
        conf.setJobName("RandomInitialLayoutGenerate");
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(LabeledAdjSetVertex.class);
        conf.setMapperClass(MapClass.class);
        conf.setInputFormat(SequenceFileInputFormat.class);
        conf.setOutputFormat(SequenceFileOutputFormat.class);
        try {
            FileInputFormat.setInputPaths(conf, getSource().getPath());
            FileOutputFormat.setOutputPath(conf, getDestination().getPath());
        } catch (IllegalAccessException e1) {
            throw new ProcessorExecutionException(e1);
        }
        conf.setNumMapTasks(1);
        conf.setNumReduceTasks(0);
        try {
            this.runningJob = JobClient.runJob(conf);
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        }
    }
}
