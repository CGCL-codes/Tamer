package com.avometric.SHARD.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * This is an example Hadoop Map/Reduce application.
 * It reads the text input files, breaks each line into words
 * and counts them. The output is a locally sorted list of words and the
 * count of how often they occurred.
 *
 * To run: bin/hadoop jar build/hadoop-examples.jar wordcount
 *            [-m <i>maps</i>] [-r <i>reduces</i>] <i>in-dir</i> <i>out-dir</i>
 */
public class TripleMultiplier extends Configured implements Tool {

    private String _fileSystem;

    private String _tracker;

    private String _replication;

    private String _jar;

    private String _mappers;

    private String _reducers;

    private String _numUniv;

    public TripleMultiplier(String fileSystem, String tracker, String replication, String jar, String mappers, String reducers, String numUniv) {
        _fileSystem = fileSystem;
        _tracker = tracker;
        _replication = replication;
        _jar = jar;
        _mappers = mappers;
        _reducers = reducers;
        _numUniv = numUniv;
    }

    /**
   * Counts the words in each line.
   * For each line of input, break the line into words and emit them as
   * (<b>word</b>, <b>1</b>).
   */
    public static class MapClass extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {

        private Text outKey = new Text();

        private Text outValue = new Text();

        private int _numUniv;

        public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
            String line = value.toString();
            String str = "University0";
            String base = "University";
            outKey.set(line);
            output.collect(outKey, outValue);
            ArrayList<Integer> first = new ArrayList<Integer>();
            int cur = line.indexOf(str, 0);
            while (cur >= 0) {
                first.add(new Integer(cur));
                cur = line.indexOf(str, cur + 1);
            }
            for (int i = 1; i <= _numUniv; i++) {
                String newLine = line;
                String newUniv = base.concat(Integer.toString(i));
                for (int j = 0; j < first.size(); j++) {
                    int index = first.get(j);
                    String pre = newLine.substring(0, index);
                    String suf = newLine.substring(index + 11);
                    newLine = pre.concat(newUniv.concat(suf));
                }
                outKey.set(newLine);
                output.collect(outKey, outValue);
            }
        }

        @Override
        public void configure(JobConf job) {
            _numUniv = new Integer(job.get("numUniv"));
        }
    }

    /**
   * A reducer class that just emits the sum of the input values.
   */
    public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
            output.collect(key, values.next());
        }
    }

    static int printUsage() {
        System.out.println("wordcount [-m <maps>] [-r <reduces>] <input> <output>");
        ToolRunner.printGenericCommandUsage(System.out);
        return -1;
    }

    /**
   * The main driver for word count map/reduce program.
   * Invoke this method to submit the map/reduce job.
   * @throws IOException When there is communication problems with the
   *                     job tracker.
   */
    public int run(String[] args) throws Exception {
        JobConf conf = new JobConf(getConf(), TripleMultiplier.class);
        conf.setJobName("firstclause");
        conf.set("fs.default.name", _fileSystem);
        conf.set("mapred.job.tracker", _tracker);
        conf.set("dfs.replication", _replication);
        conf.setJar(_jar);
        conf.setNumMapTasks(Integer.parseInt(_mappers));
        conf.setNumReduceTasks(Integer.parseInt(_reducers));
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);
        conf.setMapperClass(MapClass.class);
        conf.setCombinerClass(Reduce.class);
        conf.setReducerClass(Reduce.class);
        List<String> other_args = new ArrayList<String>();
        for (int i = 0; i < args.length; ++i) {
            try {
                if ("-m".equals(args[i])) {
                    conf.setNumMapTasks(Integer.parseInt(args[++i]));
                } else if ("-r".equals(args[i])) {
                    conf.setNumReduceTasks(Integer.parseInt(args[++i]));
                } else {
                    other_args.add(args[i]);
                }
            } catch (NumberFormatException except) {
                System.out.println("ERROR: Integer expected instead of " + args[i]);
                return printUsage();
            } catch (ArrayIndexOutOfBoundsException except) {
                System.out.println("ERROR: Required parameter missing from " + args[i - 1]);
                return printUsage();
            }
        }
        if (other_args.size() != 2) {
            System.out.println("ERROR: Wrong number of parameters: " + other_args.size() + " instead of 2.");
            return printUsage();
        }
        FileInputFormat.setInputPaths(conf, other_args.get(0));
        FileOutputFormat.setOutputPath(conf, new Path(other_args.get(1)));
        conf.set("numUniv", _numUniv);
        JobClient.runJob(conf);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        String[] mr_args = new String[2];
        mr_args[0] = args[0];
        mr_args[1] = args[1];
        String fileSystem = args[2];
        String tracker = args[3];
        String replication = args[4];
        String jar = args[5];
        String mappers = args[6];
        String reducers = args[7];
        String numUniv = args[8];
        System.out.println("File System: " + fileSystem);
        System.out.println("Trakcer: " + tracker);
        System.out.println("Replication: " + replication);
        System.out.println("Jar: " + jar);
        System.out.println("Mappers: " + mappers);
        System.out.println("Reducers: " + reducers);
        System.out.println("Num Universities: " + numUniv);
        int res = ToolRunner.run(new Configuration(), new TripleMultiplier(fileSystem, tracker, replication, jar, mappers, reducers, numUniv), mr_args);
        System.exit(res);
    }
}
