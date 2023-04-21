package io.hbase.readers;

import java.io.IOException;
import java.util.List;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import data.Triple;
import data.TripleSource;

public class HBaseTriplesReader extends InputFormat<TripleSource, Triple> {

    protected static final byte[] COLUMN_FAMILY = "V".getBytes();

    protected static final byte[] T_COLUMN_QUALIFIER = "T".getBytes();

    TableInputFormat table = new TableInputFormat();

    @Override
    public RecordReader<TripleSource, Triple> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        table.setConf(context.getConfiguration());
        return new HBaseTriplesRecordReader(table.createRecordReader(split, context));
    }

    @Override
    public List<InputSplit> getSplits(JobContext context) throws IOException, InterruptedException {
        table.setConf(context.getConfiguration());
        return table.getSplits(context);
    }
}
