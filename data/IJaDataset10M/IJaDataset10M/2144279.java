package skewreduce.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skewreduce.lsst.Image2DPartition;

public abstract class ArrayInputFormat<V extends Writable> extends WritableInputFormat<ArrayIndex, V> {

    private static final Logger LOG = LoggerFactory.getLogger(ArrayInputFormat.class);

    static class LocalCount implements Comparable<LocalCount> {

        final String host;

        int count;

        LocalCount(String h) {
            host = h;
        }

        public void incr() {
            ++count;
        }

        public int getCount() {
            return count;
        }

        @Override
        public int hashCode() {
            return host.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return host.equals(o);
        }

        @Override
        public int compareTo(LocalCount o) {
            return o.count - count;
        }
    }

    protected String[] computeLocalHosts(BlockLocation[] locs, int begin, int end) throws IOException {
        if (end >= locs.length) return new String[0];
        HashMap<String, LocalCount> map = new HashMap<String, LocalCount>();
        for (int i = begin; i <= end; ++i) {
            BlockLocation loc = locs[i];
            String[] hosts = loc.getHosts();
            for (String host : hosts) {
                LocalCount count = map.get(host);
                if (count == null) {
                    count = new LocalCount(host);
                    map.put(host, count);
                }
                count.incr();
            }
        }
        LocalCount[] counts = map.values().toArray(new LocalCount[map.size()]);
        Arrays.sort(counts);
        ArrayList<String> hosts = new ArrayList<String>(3);
        int maxChunks = counts[0].getCount();
        for (LocalCount c : counts) {
            if (c.getCount() == maxChunks) {
                hosts.add(c.host);
            } else {
                break;
            }
        }
        return hosts.toArray(new String[0]);
    }

    @Override
    public List<InputSplit> getSplits(JobContext job) throws IOException {
        Configuration conf = job.getConfiguration();
        ArrayDimension dims = new ArrayDimension(conf);
        Image2DPartition partition = new Image2DPartition(conf.get("skewreduce.partition.spec"));
        List<InputSplit> splits = new ArrayList<InputSplit>();
        List<FileStatus> files = listStatus(job);
        if (files.size() != 1) throw new IllegalArgumentException("Only one input file is supported!: given " + files.size());
        FileStatus file = files.get(0);
        Path path = file.getPath();
        FileSystem fs = path.getFileSystem(job.getConfiguration());
        long length = file.getLen();
        BlockLocation[] blkLocations = fs.getFileBlockLocations(file, 0, length);
        if ((length != 0) && isSplitable(job, path)) {
            int width = dims.getMax(0);
            long beginIndex = (partition.getMin(1) * width + partition.getMin(0)) * getValueSize();
            long endIndex = ((partition.getMax(1) - 1) * width + partition.getMax(0)) * getValueSize();
            int blockBegin = getBlockIndex(blkLocations, beginIndex);
            int blockEnd = getBlockIndex(blkLocations, endIndex - 1);
            String[] localHosts = computeLocalHosts(blkLocations, blockBegin, blockEnd);
            splits.add(new FileSplit(path, beginIndex, (endIndex - beginIndex), localHosts));
        } else if (length != 0) {
            splits.add(new FileSplit(path, 0, length, blkLocations[0].getHosts()));
        } else {
            splits.add(new FileSplit(path, 0, length, new String[0]));
        }
        LOG.debug("Total # of splits: {}", splits.size());
        return splits;
    }

    @Override
    public final ArrayIndex createKey() {
        return null;
    }

    @Override
    public final int getKeySize() {
        return 0;
    }

    @Override
    public RecordReader<ArrayIndex, V> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        return new ArrayRecordReader(this);
    }

    class ArrayRecordReader extends WritableRecordReader<ArrayIndex, V> {

        ArrayDimension dim;

        ArrayIndex cursor;

        public ArrayRecordReader() {
        }

        public ArrayRecordReader(WritableInputFormat<ArrayIndex, V> fmt) {
            super(fmt);
        }

        @Override
        public void initialize(InputSplit genericSplit, TaskAttemptContext context) throws IOException {
            super.initialize(genericSplit, context);
            dim = new ArrayDimension(context.getConfiguration());
            Image2DPartition partition = new Image2DPartition(context.getConfiguration().get("skewreduce.partition.spec"));
            cursor = dim.createIndex(partition.getMin(0), partition.getMin(1));
        }

        public boolean nextKeyValue() throws IOException {
            if (pos >= end) return false;
            key = cursor;
            value = format.createValue();
            value.readFields(reader);
            pos += recordSize;
            cursor = cursor.incr();
            return true;
        }
    }
}
