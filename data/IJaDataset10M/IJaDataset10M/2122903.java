package chaski.proc.walign;

import gnu.trove.TIntDoubleHashMap;
import gnu.trove.TIntDoubleIterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer.Context;
import chaski.proc.ConfigurableReducer;
import chaski.proc.PersistField;

/**
 * @author qing
 *
 */
public class NTableNormReducer extends ConfigurableReducer<Text, Text, Text, NullWritable> {

    @PersistField
    public double probThreshold = 1e-7;

    NullWritable nl = NullWritable.get();

    @SuppressWarnings("unused")
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        double[] entries = null;
        String kt = key.toString();
        double sumValue = 0;
        for (Text value : values) {
            String vl = value.toString();
            String[] pare = vl.trim().split("\\s+");
            if (entries == null) {
                entries = new double[pare.length];
                for (int i = 0; i < entries.length; i++) {
                    entries[i] = Double.parseDouble(pare[i]);
                    sumValue += entries[i];
                }
            } else {
                if (pare.length > entries.length) {
                    entries = Arrays.copyOf(entries, pare.length);
                }
                for (int i = 0; i < entries.length; i++) {
                    entries[i] += Double.parseDouble(pare[i]);
                    sumValue += entries[i];
                }
            }
        }
        for (int i = 0; i < entries.length; i++) {
            entries[i] /= sumValue;
            if (entries[i] < probThreshold) {
                entries[i] = probThreshold;
            }
        }
        StringBuffer bf = new StringBuffer();
        bf.append(kt).append(" ");
        for (int i = 0; i < entries.length; i++) {
            bf.append(String.format("%g", entries[i]));
            if (i < entries.length - 1) bf.append(" ");
        }
        context.write(new Text(bf.toString()), nl);
    }

    @Override
    protected void setup2(Context context) throws IOException, InterruptedException {
        super.setup2(context);
        probThreshold = Math.max(1e-7, probThreshold);
    }
}
