package jobs;

import java.io.IOException;
import io.files.readers.NTriplesReader;
import io.hbase.writers.HBaseCombinedWriter;
import io.hbase.writers.HBaseDictWriter;
import io.hbase.writers.HBaseTriplesWriter;
import mappers.files.io.ImportTriplesDeconstructMapper;
import mappers.files.io.ImportTriplesReconstructMapper;
import mappers.files.io.ImportTriplesSampleMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.mapred.OutputLogFilter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import data.Triple;
import data.TripleSource;
import reducers.files.io.ImportTriplesDeconstructReducer;
import reducers.files.io.ImportTriplesReconstructReducer;
import reducers.files.io.ImportTriplesSampleReducer;

public class HBaseImportTriples extends Configured implements Tool {

    private static Logger log = LoggerFactory.getLogger(HBaseImportTriples.class);

    private int numReduceTasks = 1;

    private int numMapTasks = 1;

    private int sampling = 0;

    private int resourceThreshold = 0;

    public int run(String[] args) throws Exception {
        parseArgs(args);
        FileSystem fs = FileSystem.get(this.getConf());
        fs.delete(new Path(args[1]), true);
        sampleCommonResources(args);
        assignIdsToNodes(args);
        rewriteTriples(args);
        fs.delete(new Path(args[1]), true);
        return 0;
    }

    private Job createNewJob(String name) throws IOException {
        Configuration conf = new Configuration();
        conf.setInt("reasoner.samplingPercentage", sampling);
        conf.setInt("reasoner.threshold", resourceThreshold);
        conf.setInt("maptasks", numMapTasks);
        Job job = new Job(conf);
        job.setJarByClass(HBaseImportTriples.class);
        job.setJobName(name);
        FileInputFormat.setInputPathFilter(job, OutputLogFilter.class);
        job.setNumReduceTasks(numReduceTasks);
        SequenceFileOutputFormat.setCompressOutput(job, true);
        SequenceFileOutputFormat.setOutputCompressionType(job, CompressionType.BLOCK);
        return job;
    }

    public void parseArgs(String[] args) {
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equalsIgnoreCase("--maptasks")) {
                numMapTasks = Integer.valueOf(args[++i]);
            }
            if (args[i].equalsIgnoreCase("--reducetasks")) {
                numReduceTasks = Integer.valueOf(args[++i]);
            }
            if (args[i].equalsIgnoreCase("--samplingPercentage")) {
                sampling = Integer.valueOf(args[++i]);
            }
            if (args[i].equalsIgnoreCase("--samplingThreshold")) {
                resourceThreshold = Integer.valueOf(args[++i]);
            }
        }
    }

    public void sampleCommonResources(String[] args) throws Exception {
        Job job = createNewJob("Sample common resources");
        FileInputFormat.addInputPath(job, new Path(args[0]));
        job.setInputFormatClass(NTriplesReader.class);
        job.setMapperClass(ImportTriplesSampleMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setReducerClass(ImportTriplesSampleReducer.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(BytesWritable.class);
        job.setOutputFormatClass(HBaseDictWriter.class);
        long time = System.currentTimeMillis();
        job.waitForCompletion(true);
        log.debug("Job finished in " + (System.currentTimeMillis() - time));
    }

    public void assignIdsToNodes(String[] args) throws Exception {
        Job job = createNewJob("Deconstruct statements");
        job.getConfiguration().setInt("mapred.job.reuse.jvm.num.tasks", -1);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        job.setInputFormatClass(NTriplesReader.class);
        job.setMapperClass(ImportTriplesDeconstructMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setReducerClass(ImportTriplesDeconstructReducer.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(BytesWritable.class);
        job.setOutputFormatClass(HBaseCombinedWriter.class);
        SequenceFileOutputFormat.setOutputPath(job, new Path(args[1]));
        long time = System.currentTimeMillis();
        job.waitForCompletion(true);
        log.info("Job finished in " + (System.currentTimeMillis() - time));
    }

    private void rewriteTriples(String[] args) throws Exception {
        Job job = createNewJob("Reconstruct statements");
        SequenceFileInputFormat.addInputPath(job, new Path(args[1]));
        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setMapperClass(ImportTriplesReconstructMapper.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setReducerClass(ImportTriplesReconstructReducer.class);
        job.setOutputKeyClass(TripleSource.class);
        job.setOutputValueClass(Triple.class);
        job.setOutputFormatClass(HBaseTriplesWriter.class);
        long time = System.currentTimeMillis();
        job.waitForCompletion(true);
        log.debug("Job finished in " + (System.currentTimeMillis() - time));
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: HBaseImportTriples [input dir] [tmp dir]");
            System.exit(0);
        }
        long time = System.currentTimeMillis();
        int res = ToolRunner.run(new Configuration(), new HBaseImportTriples(), args);
        log.info("Import time: " + (System.currentTimeMillis() - time));
        System.exit(res);
    }
}
