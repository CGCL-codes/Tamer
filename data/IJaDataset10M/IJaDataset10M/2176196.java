package skewreduce.framework.physical;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import skewreduce.framework.EaggPlan;
import skewreduce.framework.SchedulerEvent;

public class PPigOp extends PhysicalOp {

    @Override
    public void setup(EaggPlan plan) throws IOException {
    }

    public Job createJob(Configuration conf) throws IOException {
        return null;
    }

    @Override
    public SchedulerEvent call() throws Exception {
        return new SchedulerEvent.Completion(this);
    }
}
