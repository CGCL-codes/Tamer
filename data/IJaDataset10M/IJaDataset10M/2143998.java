package io.hbase.writers;

import io.hbase.writers.SimpleOutputCommitter;
import java.io.IOException;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import data.Triple;
import data.TripleSource;

public class SesameHBaseTriplesWriter extends OutputFormat<TripleSource, Triple> {

    @Override
    public void checkOutputSpecs(JobContext context) throws IOException, InterruptedException {
    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {
        return new SimpleOutputCommitter();
    }

    @Override
    public RecordWriter<TripleSource, Triple> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {
        return new SesameHBaseTriplesRecordWriter();
    }
}
