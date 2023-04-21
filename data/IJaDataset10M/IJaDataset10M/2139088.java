package org.sf.xrime.algorithms.BCApproximation;

import java.io.IOException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.sf.xrime.ProcessorExecutionException;
import org.sf.xrime.algorithms.GraphAlgorithm;
import org.sf.xrime.model.vertex.LabeledAdjBiSetVertex;
import org.sf.xrime.utils.SequenceTempDirMgr;

public class BCBackwardStep extends GraphAlgorithm {

    private boolean end = false;

    private JobConf jobConf;

    private FileSystem client = null;

    private SequenceTempDirMgr tempDirs = null;

    private int dist;

    public void setDistance(int distance) {
        dist = distance;
    }

    public int getDistance() {
        return dist;
    }

    public boolean isEnd() {
        return end;
    }

    public FileSystem getClient() {
        return client;
    }

    public void setClient(FileSystem client) {
        this.client = client;
    }

    public SequenceTempDirMgr getTempDirs() {
        return tempDirs;
    }

    public void setTempDirs(SequenceTempDirMgr tempDirs) {
        this.tempDirs = tempDirs;
    }

    @Override
    public void execute() throws ProcessorExecutionException {
        try {
            context.setParameter("distance", Integer.toString(dist));
            jobConf = new JobConf(context, BCBackwardStep.class);
            jobConf.setJobName("BC");
            jobConf.setMapperClass(BCBackwardMapper.class);
            jobConf.setReducerClass(BCBackwardReducer.class);
            jobConf.setNumMapTasks(1);
            jobConf.setNumReduceTasks(1);
            jobConf.setMapOutputValueClass(LabeledAdjBiSetVertex.class);
            jobConf.setOutputKeyClass(Text.class);
            jobConf.setOutputValueClass(LabeledAdjBiSetVertex.class);
            jobConf.setInputFormat(SequenceFileInputFormat.class);
            jobConf.setOutputFormat(SequenceFileOutputFormat.class);
            FileInputFormat.setInputPaths(jobConf, context.getSource().getPath());
            FileOutputFormat.setOutputPath(jobConf, context.getDestination().getPath());
            this.runningJob = JobClient.runJob(jobConf);
            if (dist > 0) {
                end = false;
            } else end = true;
        } catch (IOException e) {
            throw new ProcessorExecutionException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
