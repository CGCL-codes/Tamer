package chaski.utils;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;
import org.apache.hadoop.util.LineReader;

/**
 * This class treats a line in the input as a key/value pair separated by a
 * separator character. The separator can be specified in config file under the
 * attribute name key.value.separator.in.input.line. The default separator is
 * the tab character ('\t').
 */
public class LineNumberRecordReader extends RecordReader<IntWritable, Text> {

    private static final Log LOG = LogFactory.getLog(LineNumberRecordReader.class);

    private CompressionCodecFactory compressionCodecs = null;

    private long start;

    private long pos;

    private long end;

    private LineReader in;

    private int maxLineLength;

    private IntWritable key = null;

    private Text value = null;

    private Text innerValue = null;

    private byte separator = (byte) '\t';

    public void initialize(InputSplit genericSplit, TaskAttemptContext context) throws IOException {
        FileSplit split = (FileSplit) genericSplit;
        Configuration job = context.getConfiguration();
        this.maxLineLength = job.getInt("mapred.linerecordreader.maxlength", Integer.MAX_VALUE);
        String sepStr = job.get("key.value.separator.in.input.line", "\t");
        this.separator = (byte) sepStr.charAt(0);
        start = split.getStart();
        end = start + split.getLength();
        final Path file = split.getPath();
        compressionCodecs = new CompressionCodecFactory(job);
        final CompressionCodec codec = compressionCodecs.getCodec(file);
        FileSystem fs = file.getFileSystem(job);
        FSDataInputStream fileIn = fs.open(split.getPath());
        boolean skipFirstLine = false;
        if (codec != null) {
            in = new LineReader(codec.createInputStream(fileIn), job);
            end = Long.MAX_VALUE;
        } else {
            if (start != 0) {
                skipFirstLine = true;
                --start;
                fileIn.seek(start);
            }
            in = new LineReader(fileIn, job);
        }
        if (skipFirstLine) {
            start += in.readLine(new Text(), 0, (int) Math.min((long) Integer.MAX_VALUE, end - start));
        }
        this.pos = start;
    }

    public static int findSeparator(byte[] utf, int start, int length, byte sep) {
        for (int i = start; i < (start + length); i++) {
            if (utf[i] == sep) {
                return i;
            }
        }
        return -1;
    }

    public boolean nextKeyValue() throws IOException {
        if (key == null) {
            key = new IntWritable();
        }
        if (value == null) {
            value = new Text();
        }
        if (innerValue == null) {
            innerValue = new Text();
        }
        int newSize = 0;
        while (pos < end) {
            newSize = in.readLine(innerValue, maxLineLength, Math.max((int) Math.min(Integer.MAX_VALUE, end - pos), maxLineLength));
            if (newSize == 0) {
                break;
            }
            pos += newSize;
            if (newSize < maxLineLength) {
                break;
            }
            LOG.info("Skipped line of size " + newSize + " at pos " + (pos - newSize));
        }
        if (newSize == 0) {
            key = null;
            value = null;
            return false;
        } else {
            byte[] line = null;
            int lineLen = -1;
            line = innerValue.getBytes();
            lineLen = innerValue.getLength();
            int ps = findSeparator(line, 0, lineLen, this.separator);
            if (ps == -1) {
                key.set(-1);
                value.set(line, 0, lineLen);
            } else {
                int keyLen = ps;
                byte[] keyBytes = new byte[keyLen];
                System.arraycopy(line, 0, keyBytes, 0, keyLen);
                String keyStr = Text.decode(keyBytes);
                int valLen = lineLen - keyLen - 1;
                byte[] valBytes = new byte[valLen];
                System.arraycopy(line, ps + 1, valBytes, 0, valLen);
                key.set(Integer.parseInt(keyStr));
                value.set(valBytes);
            }
            return true;
        }
    }

    @Override
    public IntWritable getCurrentKey() {
        return key;
    }

    @Override
    public Text getCurrentValue() {
        return value;
    }

    /**
	 * Get the progress within the split
	 */
    public float getProgress() {
        if (start == end) {
            return 0.0f;
        } else {
            return Math.min(1.0f, (pos - start) / (float) (end - start));
        }
    }

    public synchronized void close() throws IOException {
        if (in != null) {
            in.close();
        }
    }
}
