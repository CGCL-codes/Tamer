package chaski.proc.postproc;

import static chaski.utils.CommonFileOperations.deleteIfExists;
import static chaski.utils.ProgramLogic.testFileExistOnHDFSOrDie;
import static chaski.utils.ProgramLogic.testFileNotExistOnHDFSOrDie;
import java.io.FileNotFoundException;
import java.io.IOException;
import jpfm.configurable.ConfigClass;
import jpfm.configurable.ConfigField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import chaski.utils.MapReduceJob;

@ConfigClass(value = "postproc", help = "Post process the phrase table")
public class PostProcessDriver {

    private static final Log LOG = LogFactory.getLog(PostProcessDriver.class);

    @ConfigField(name = "ptable", alias = { "post.ptable" }, required = true, help = "The input directory of the combined phrase table and reorder table")
    public String inputDir;

    @ConfigField(name = "moses-p", alias = { "post.mosesp" }, required = false, defaultValue = "", help = "The output dir of moses phrase table")
    public String mosesPDir;

    @ConfigField(name = "moses-r", alias = { "post.mosesr" }, required = false, defaultValue = "", help = "The output dir of moses lexiconized reorder table")
    public String mosesRDir;

    @ConfigField(name = "queues", alias = "qu", required = false, defaultValue = "m45", help = "Specify the queue that the MR-Job will be submitted, if not specified, a warning will be displayed but the execution will continue.")
    public String queues;

    public int numberReducer = 0;

    @ConfigField(name = "verbose", alias = { "vbs" }, required = false, defaultValue = "true", help = "The detail information?")
    public boolean verbose;

    @ConfigField(name = "overwrite", alias = { "post.overwrite" }, required = false, defaultValue = "false", help = "Whether the output director will be overwritten if exists, by default it is false, and program will die if it exists")
    public boolean overwrite;

    public void execute() throws Exception {
        testFileExistOnHDFSOrDie(inputDir, "File not found " + inputDir + " on HDFS ", FileNotFoundException.class, LOG);
        if (mosesPDir.length() > 0) {
            if (!overwrite) {
                testFileNotExistOnHDFSOrDie(mosesPDir, "File already exists : " + mosesPDir + " on HDFS ", IOException.class, LOG);
            } else {
                deleteIfExists(mosesPDir);
            }
            String jobName = "GenerateMosesPhraseTable[" + inputDir + "==>" + mosesPDir;
            MapReduceJob job = null;
            if (queues.trim().length() == 0) {
                job = new MapReduceJob(jobName);
            } else {
                job = new MapReduceJob(jobName, queues);
            }
            job.setBasicInfo(TextInputFormat.class, Text.class, NullWritable.class, TextOutputFormat.class, MosesPhraseTableMapper.class, inputDir, mosesPDir);
            job.submit();
            job.waitForCompletion(verbose);
        }
        if (mosesRDir.length() > 0) {
            if (!overwrite) {
                testFileNotExistOnHDFSOrDie(mosesRDir, "File already exists : " + mosesRDir + " on HDFS ", IOException.class, LOG);
            } else {
                deleteIfExists(mosesRDir);
            }
            String jobName = "GenerateMosesReorderTable[" + inputDir + "==>" + mosesRDir;
            MapReduceJob job = null;
            if (queues.trim().length() == 0) {
                job = new MapReduceJob(jobName);
            } else {
                job = new MapReduceJob(jobName, queues);
            }
            job.setBasicInfo(TextInputFormat.class, Text.class, NullWritable.class, TextOutputFormat.class, MosesReorderTableMapper.class, inputDir, mosesRDir);
            job.submit();
            job.waitForCompletion(verbose);
        }
    }
}
