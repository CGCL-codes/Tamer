package org.leeing.hadoop.maxtemp;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.leeing.hadoop.util.DirectoryUtil;

/**
 *
 *  Find the highest recorded global temperature for each year in the dataset
 *  Each line of he dataset is as follows:
 *  0043012650999991949032418004+62300+010750FM-12+048599999V0202701N00461220001CN0500001N9+00781+99999999999
 * @author leeing
 */
public class MaxTemperature {

    public static void main(String[] args) throws Exception {
        String from = "hdfs://localhost:8020/user/leeing/maxtemp/sample.txt";
        String to = "hdfs://localhost:8020/user/leeing/maxtemp/output";
        DirectoryUtil.delete(to);
        Job job = new Job();
        job.setJarByClass(MaxTemperature.class);
        FileInputFormat.addInputPath(job, new Path(from));
        FileOutputFormat.setOutputPath(job, new Path(to));
        job.setMapperClass(MaxTemperatureMapper.class);
        job.setReducerClass(MaxTemperatureReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
