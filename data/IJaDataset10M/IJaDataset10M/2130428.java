package org.sf.xrime.algorithms.partitions.connected.weakly.alg_1;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
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
import org.apache.hadoop.util.ToolRunner;
import org.sf.xrime.ProcessorExecutionException;
import org.sf.xrime.algorithms.GraphAlgorithm;
import org.sf.xrime.algorithms.utils.GraphAlgorithmMapReduceBase;
import org.sf.xrime.model.Graph;
import org.sf.xrime.model.vertex.Vertex;
import org.sf.xrime.model.vertex.VertexComparator;
import org.sf.xrime.model.vertex.VertexSet;

/**
 * This class is used to summarize the final result of vertex set expansion, i.e., vertex
 * labeling.
 * @author xue
 *
 */
public class VertexSetSummarize extends GraphAlgorithm {

    /**
   * Default constructor.
   */
    public VertexSetSummarize() {
        super();
    }

    /**
   * Emit label as key, vertex as value.
   * @author xue
   */
    public static class MapClass extends GraphAlgorithmMapReduceBase implements Mapper<Text, VertexSet, Text, VertexSet> {

        @Override
        public void map(Text key, VertexSet value, OutputCollector<Text, VertexSet> output, Reporter reporter) throws IOException {
            String label = ((Vertex) value.getVertexes().toArray()[0]).getId();
            Vertex vertex = new Vertex(key.toString());
            VertexSet vertex_set = new VertexSet();
            vertex_set.addVertex(vertex);
            output.collect(new Text(label), vertex_set);
        }
    }

    /**
   * Merge vertexes within the same maximized weakly connected component into a vertex set,
   * which is identified with the smallest/lowest vertex id in the set.
   * @author xue
   *
   */
    public static class ReduceClass extends GraphAlgorithmMapReduceBase implements Reducer<Text, VertexSet, Text, VertexSet> {

        @Override
        public void reduce(Text key, Iterator<VertexSet> values, OutputCollector<Text, VertexSet> output, Reporter reporter) throws IOException {
            VertexSet result_set = new VertexSet();
            result_set.setVertexes(new TreeSet<Vertex>(new VertexComparator()));
            while (values.hasNext()) {
                VertexSet curr_set = values.next();
                for (Vertex curr_vertex : curr_set.getVertexes()) {
                    result_set.addVertex(curr_vertex);
                }
            }
            output.collect(key, result_set);
        }
    }

    @Override
    public void setArguments(String[] params) throws ProcessorExecutionException {
        if (params.length != 2) {
            throw new ProcessorExecutionException("Wrong number of parameters: " + params.length + " instead of 2.");
        }
        Graph src = new Graph(Graph.defaultGraph());
        src.setPath(new Path(params[0]));
        Graph dest = new Graph(Graph.defaultGraph());
        dest.setPath(new Path(params[1]));
        setSource(src);
        setDestination(dest);
    }

    @Override
    public void execute() throws ProcessorExecutionException {
        JobConf conf = new JobConf(context, VertexSetSummarize.class);
        conf.setJobName("VertexSetSummarize");
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(VertexSet.class);
        conf.setMapperClass(MapClass.class);
        conf.setCombinerClass(ReduceClass.class);
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
        try {
            this.runningJob = JobClient.runJob(conf);
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        }
    }

    /**
   * @param args
   */
    public static void main(String[] args) {
        try {
            int res = ToolRunner.run(new VertexSetSummarize(), args);
            System.exit(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
