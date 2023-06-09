package it.crs4.seal.prq;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import it.crs4.seal.common.FormatException;
import it.crs4.seal.common.SealToolRunner;
import it.crs4.seal.common.IMRContext;
import it.crs4.seal.common.ContextAdapter;
import it.crs4.seal.common.GroupByLocationComparator;
import it.crs4.seal.common.SequenceId;
import it.crs4.seal.common.SequencedFragment;
import it.crs4.seal.common.FastqInputFormat;
import it.crs4.seal.common.QseqInputFormat;

/**
 * Trasform data from qseq or fastq format to prq format.  In detail, at the moment it matches
 * separate read from the same location in the flowcell (head and tail sections of a
 * single DNA fragment) and puts them in a single output record so that they may be
 * more easily aligned.
 *
 * The algorithm is analogous to the one used in the SecondarySort Hadoop example.
 * We use Hadoop to sort all read sections by their location on the flow cell and
 * read number.  We group fragments by only their location on the flow cell, so that
 * all reads are presented to the reducer together, and they may be output as a
 * single record.
 *
 */
public class PairReadsQSeq extends Configured implements Tool {

    /**
	 * Partition based only on the sequence location.
	 */
    public static class FirstPartitioner extends Partitioner<SequenceId, Text> {

        @Override
        public int getPartition(SequenceId key, Text value, int numPartitions) {
            return (key.getLocation().hashCode() & Integer.MAX_VALUE) % numPartitions;
        }
    }

    public static class PrqMapper extends Mapper<Text, SequencedFragment, SequenceId, Text> {

        private PairReadsQSeqMapper impl;

        private IMRContext<SequenceId, Text> contextAdapter;

        @Override
        public void setup(Context context) {
            impl = new PairReadsQSeqMapper();
            impl.setup();
            contextAdapter = new ContextAdapter<SequenceId, Text>(context);
        }

        @Override
        public void map(Text key, SequencedFragment fragment, Context context) throws IOException, InterruptedException {
            impl.map(key, fragment, contextAdapter);
        }
    }

    public static class PrqReducer extends Reducer<SequenceId, Text, Text, Text> {

        private IMRContext<Text, Text> contextAdapter;

        private PairReadsQSeqReducer impl;

        @Override
        public void setup(Context context) {
            contextAdapter = new ContextAdapter<Text, Text>(context);
            impl = new PairReadsQSeqReducer();
            impl.setup(contextAdapter);
            impl.setMinBasesThreshold(context.getConfiguration().getInt(PrqOptionParser.MinBasesThresholdConfigName, PrqOptionParser.DefaultMinBasesThreshold));
            impl.setDropFailedFilter(context.getConfiguration().getBoolean(PrqOptionParser.DropFailedFilterConfigName, PrqOptionParser.DropFailedFilterDefault));
            impl.setWarnOnlyIfUnpaired(context.getConfiguration().getBoolean(PrqOptionParser.WarningOnlyIfUnpairedConfigName, PrqOptionParser.WarningOnlyIfUnpairedDefault));
        }

        @Override
        public void reduce(SequenceId key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            impl.reduce(key, values, contextAdapter);
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        PrqOptionParser parser = new PrqOptionParser();
        parser.parse(conf, args);
        Job job = new Job(conf, "PairReadsQSeq " + parser.getInputPaths().get(0));
        job.setJarByClass(PairReadsQSeq.class);
        if (parser.getSelectedInputFormat() == PrqOptionParser.InputFormat.qseq) job.setInputFormatClass(QseqInputFormat.class); else if (parser.getSelectedInputFormat() == PrqOptionParser.InputFormat.fastq) job.setInputFormatClass(FastqInputFormat.class);
        job.setMapperClass(PrqMapper.class);
        job.setMapOutputKeyClass(SequenceId.class);
        job.setMapOutputValueClass(Text.class);
        job.setPartitionerClass(FirstPartitioner.class);
        job.setGroupingComparatorClass(GroupByLocationComparator.class);
        job.setReducerClass(PrqReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        for (Path p : parser.getInputPaths()) FileInputFormat.addInputPath(job, p);
        FileOutputFormat.setOutputPath(job, parser.getOutputPath());
        return (job.waitForCompletion(true) ? 0 : 1);
    }

    public static void main(String[] args) throws Exception {
        int res = new SealToolRunner().run(new PairReadsQSeq(), args);
        System.exit(res);
    }
}
