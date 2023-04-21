package ru.mipt.victator.pagerank.rank;

import java.util.Properties;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;

public class RankJob {

    public static JobConf config(int n, Properties props) {
        JobConf conf = new JobConf(RankJob.class);
        conf.setJobName("PageRank-RankJob");
        conf.set("RankJob.CalculationsParameter.Float", props.getProperty("RankJob.CalculationsParameter.Float"));
        conf.set("InputData.StartPageRank.Float", props.getProperty("InputData.StartPageRank.Float"));
        Path pIndex = new Path(props.getProperty("OutputData.Index.Dir"));
        conf.setInputPath(pIndex);
        conf.setInputFormat(SequenceFileInputFormat.class);
        Path pPRs = new Path(props.getProperty("OutputData.PageRank.Dir"));
        conf.setWorkingDirectory(new Path(pPRs, Integer.toString(n)));
        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(FloatWritable.class);
        conf.setOutputFormat(SequenceFileOutputFormat.class);
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(FloatWritable.class);
        conf.setOutputPath(new Path(pPRs, Integer.toString(n + 1)));
        int MapTasks = Integer.parseInt(props.getProperty("RankJob.MapTasks.Int"));
        conf.setNumMapTasks(MapTasks);
        conf.setMaxMapAttempts(3);
        int ReduceTasks = Integer.parseInt(props.getProperty("RankJob.ReduceTasks.Int"));
        conf.setNumReduceTasks(ReduceTasks);
        conf.setMaxReduceAttempts(3);
        conf.setMapperClass(RankMap.class);
        conf.setCombinerClass(RankReduce.class);
        conf.setReducerClass(RankReduce.class);
        return conf;
    }
}
