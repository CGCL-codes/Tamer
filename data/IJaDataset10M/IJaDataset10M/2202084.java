package jobs;

import io.files.readers.FilesTriplesReader;
import io.files.writers.FilesTriplesWriter;
import java.io.IOException;
import mappers.files.rdfs.RDFSSpecialPropsMapper;
import mappers.files.rdfs.RDFSSubPropDomRangeMapper;
import mappers.files.rdfs.RDFSSubPropInheritMapper;
import mappers.files.rdfs.RDFSSubclasMapper;
import mappers.files.rdfs.SwapTriplesMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import partitioners.MyHashPartitioner;
import reducers.files.rdfs.DelDuplicatesReducer;
import reducers.files.rdfs.RDFSSpecialPropsReducer;
import reducers.files.rdfs.RDFSSubclasReducer;
import reducers.files.rdfs.RDFSSubpropDomRangeReducer;
import reducers.files.rdfs.RDFSSubpropInheritReducer;
import data.Triple;
import data.TripleSource;

public class FilesRDFSReasoner extends Configured implements Tool {

    protected static Logger log = LoggerFactory.getLogger(FilesRDFSReasoner.class);

    private int numMapTasks = -1;

    private int numReduceTasks = -1;

    public static int step = 0;

    private int lastExecutionPropInheritance = -1;

    private int lastExecutionDomRange = -1;

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equalsIgnoreCase("--maptasks")) {
                numMapTasks = Integer.valueOf(args[++i]);
            }
            if (args[i].equalsIgnoreCase("--reducetasks")) {
                numReduceTasks = Integer.valueOf(args[++i]);
            }
            if (args[i].equalsIgnoreCase("--startingStep")) {
                FilesRDFSReasoner.step = Integer.valueOf(args[++i]);
            }
            if (args[i].equalsIgnoreCase("--lastStepPropInher")) {
                lastExecutionPropInheritance = Integer.valueOf(args[++i]);
            }
            if (args[i].equalsIgnoreCase("--lastStepDomRange")) {
                lastExecutionDomRange = Integer.valueOf(args[++i]);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("USAGE: RFDSReasoner [pool path] [options]");
            return;
        }
        try {
            ToolRunner.run(new Configuration(), new FilesRDFSReasoner(), args);
        } catch (Exception e) {
        }
    }

    private void configureOutputJob(Job job, String baseOutputDir, String outputName) {
        job.setOutputKeyClass(TripleSource.class);
        job.setOutputValueClass(Triple.class);
        job.setOutputFormatClass(FilesTriplesWriter.class);
        Path outputFolder = new Path(baseOutputDir + "/" + outputName);
        SequenceFileOutputFormat.setOutputPath(job, outputFolder);
    }

    private Job createNewJob(String jobName, String baseInputDir, String filenameFilter) throws IOException {
        Configuration conf = new Configuration();
        conf.setInt("maptasks", numMapTasks);
        conf.set("input.filter", filenameFilter);
        Job job = new Job(conf);
        job.setJobName(jobName);
        job.setJarByClass(FilesRDFSReasoner.class);
        FilesTriplesReader.addInputPath(job, new Path(baseInputDir));
        job.setInputFormatClass(FilesTriplesReader.class);
        job.setNumReduceTasks(numReduceTasks);
        System.out.println("����������-" + jobName + "������baseInputDirΪ-" + baseInputDir);
        return job;
    }

    public long launchDerivation(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        long time = System.currentTimeMillis();
        parseArgs(args);
        Job job = null;
        long derivation = 0;
        job = createNewJob("RDFS subproperty inheritance reasoning", args[0], "FILTER_ONLY_HIDDEN");
        job.setMapperClass(RDFSSubPropInheritMapper.class);
        job.setMapOutputKeyClass(BytesWritable.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setReducerClass(RDFSSubpropInheritReducer.class);
        job.getConfiguration().setInt("reasoner.step", ++step);
        job.getConfiguration().setInt("lastExecution.step", lastExecutionPropInheritance);
        lastExecutionPropInheritance = step;
        configureOutputJob(job, args[0], "dir-rdfs-derivation/dir-subprop-inherit");
        job.waitForCompletion(true);
        long propInheritanceDerivation = job.getCounters().findCounter("org.apache.hadoop.mapred.Task$Counter", "REDUCE_OUTPUT_RECORDS").getValue();
        job = createNewJob("RDFS subproperty domain and range reasoning", args[0], "FILTER_ONLY_HIDDEN");
        job.setMapperClass(RDFSSubPropDomRangeMapper.class);
        job.setMapOutputKeyClass(BytesWritable.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setPartitionerClass(MyHashPartitioner.class);
        job.setReducerClass(RDFSSubpropDomRangeReducer.class);
        job.getConfiguration().setInt("reasoner.step", ++step);
        job.getConfiguration().setInt("lastExecution.step", lastExecutionDomRange);
        lastExecutionDomRange = step;
        configureOutputJob(job, args[0], "dir-rdfs-derivation/dir-subprop-domain-range");
        job.waitForCompletion(true);
        long domRangeDerivation = job.getCounters().findCounter("org.apache.hadoop.mapred.Task$Counter", "REDUCE_OUTPUT_RECORDS").getValue();
        if (propInheritanceDerivation > 0 || domRangeDerivation > 0) {
            job = createNewJob("RDFS cleaning up subprop duplicates", args[0], "FILTER_ONLY_HIDDEN");
            job.setMapperClass(SwapTriplesMapper.class);
            job.setMapOutputKeyClass(Triple.class);
            job.setMapOutputValueClass(TripleSource.class);
            job.setReducerClass(DelDuplicatesReducer.class);
            job.getConfiguration().setInt("reasoner.filterStep", step - 2);
            configureOutputJob(job, args[0], "dir-rdfs-output/dir-subprop-filtered-" + step);
            job.waitForCompletion(true);
            derivation = job.getCounters().findCounter("org.apache.hadoop.mapred.Task$Counter", "REDUCE_OUTPUT_RECORDS").getValue();
        } else {
            derivation = 0;
        }
        FileSystem.get(job.getConfiguration()).delete(new Path(args[0], "dir-rdfs-derivation/"), true);
        job = createNewJob("RDFS subclass reasoning", args[0], "FILTER_ONLY_TYPE_SUBCLASS");
        job.setMapperClass(RDFSSubclasMapper.class);
        job.setMapOutputKeyClass(BytesWritable.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setReducerClass(RDFSSubclasReducer.class);
        job.getConfiguration().setInt("reasoner.step", ++step);
        configureOutputJob(job, args[0], "dir-rdfs-output/dir-subclass-" + step);
        job.waitForCompletion(true);
        derivation += job.getCounters().findCounter("org.apache.hadoop.mapred.Task$Counter", "REDUCE_OUTPUT_RECORDS").getValue();
        long specialTriples = 0;
        Counter counter = job.getCounters().findCounter("RDFS derived triples", "subclass of resource");
        specialTriples += counter.getValue();
        counter = job.getCounters().findCounter("RDFS derived triples", "subclass of Literal");
        specialTriples += counter.getValue();
        counter = job.getCounters().findCounter("RDFS derived triples", "subproperty of member");
        specialTriples += counter.getValue();
        if (specialTriples > 0) {
            job = createNewJob("RDFS special properties reasoning", args[0], "FILTER_ONLY_OTHER_SUBCLASS_SUBPROP");
            job.setMapperClass(RDFSSpecialPropsMapper.class);
            job.setMapOutputKeyClass(BytesWritable.class);
            job.setMapOutputValueClass(LongWritable.class);
            job.setReducerClass(RDFSSpecialPropsReducer.class);
            job.getConfiguration().setInt("reasoner.step", ++step);
            configureOutputJob(job, args[0], "dir-rdfs-output/dir-special-props-" + step);
            job.waitForCompletion(true);
            derivation += job.getCounters().findCounter("org.apache.hadoop.mapred.Task$Counter", "REDUCE_OUTPUT_RECORDS").getValue();
        }
        log.info("RDFS reasoning time: " + (System.currentTimeMillis() - time));
        log.info("RDFS derivation: " + derivation);
        return derivation;
    }

    @Override
    public int run(String[] args) throws Exception {
        launchDerivation(args);
        return 0;
    }
}
