package edu.yale.cs.hadoopdb.benchmark;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import edu.yale.cs.hadoopdb.exec.HDFSJobBase;
import edu.yale.cs.hadoopdb.util.HDFSUtil;

/**
 * Adapted from Andy Pavlo's code 
 * http://database.cs.brown.edu/projects/mapreduce-vs-dbms/
 */
public class GrepTaskHDFS extends HDFSJobBase {

    public static final String GREP_PATTERN_PARAM = "grep.pattern.param";

    public static final int KEY_LENGTH = 10;

    public static final int VALUE_LENGTH = 90;

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new GrepTaskHDFS(), args);
        System.exit(res);
    }

    @Override
    protected JobConf configureJob(String... args) throws IOException {
        JobConf conf = new JobConf(getConf(), this.getClass());
        conf.setJobName("grep_hdfs");
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);
        conf.setMapperClass(Map.class);
        conf.setNumReduceTasks(0);
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
        if (args.length < 3) {
            throw new RuntimeException("Incorrect arguments provided for " + this.getClass());
        }
        conf.set(GREP_PATTERN_PARAM, args[0]);
        FileInputFormat.setInputPaths(conf, new Path(args[1]));
        Path outputPath = new Path(args[2]);
        HDFSUtil.deletePath(outputPath);
        FileOutputFormat.setOutputPath(conf, outputPath);
        return conf;
    }

    static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {

        private static String pattern;

        @Override
        public void configure(JobConf job) {
            super.configure(job);
            pattern = job.get(GREP_PATTERN_PARAM);
        }

        private Text oKey = new Text();

        private Text oValue = new Text();

        @Override
        public void map(LongWritable key, Text field, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
            if (field.find(pattern, KEY_LENGTH) > KEY_LENGTH) {
                String sField = field.toString();
                String sKey = sField.substring(0, KEY_LENGTH);
                oKey.set(sKey);
                String sValue = sField.substring(KEY_LENGTH);
                oValue.set(sValue);
                output.collect(oKey, oValue);
            }
        }
    }

    @Override
    protected int printUsage() {
        System.out.println("<pattern> <input> <output>");
        return -1;
    }
}
