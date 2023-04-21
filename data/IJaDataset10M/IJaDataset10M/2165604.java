package partitioners;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.NumberUtils;

public class NaivePartitioner extends Partitioner<BytesWritable, NullWritable> implements Configurable {

    protected static Logger log = LoggerFactory.getLogger(NaivePartitioner.class);

    private Configuration c = null;

    float partitionSize = 0;

    @Override
    public int getPartition(BytesWritable key, NullWritable value, int numPartitions) {
        int number = NumberUtils.decodeInt(key.getBytes(), 0);
        int partition = Math.round((number / partitionSize));
        return partition;
    }

    @Override
    public Configuration getConf() {
        return c;
    }

    @Override
    public void setConf(Configuration conf) {
        this.c = conf;
        long numReduceTasks = conf.getLong("numReduceTasks", 0) + 1;
        int numPartitions = conf.getInt("mapred.reduce.tasks", 0);
        partitionSize = (float) numReduceTasks / (float) numPartitions;
    }
}
