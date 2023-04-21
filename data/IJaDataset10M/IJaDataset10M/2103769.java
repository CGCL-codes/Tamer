package chaski.proc.walign;

import gnu.trove.TIntDoubleHashMap;
import gnu.trove.TIntDoubleIterator;
import java.io.IOException;
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
public class ADTableNormReducer extends ConfigurableReducer<Text, Text, Text, NullWritable> {

    @PersistField
    public double probThreshold = 1e-7;

    TIntDoubleHashMap hm = new TIntDoubleHashMap();

    NullWritable nl = NullWritable.get();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        double sumValue = 0;
        hm.clear();
        String kt = key.toString();
        for (Text value : values) {
            String vl = value.toString();
            String[] pare = vl.trim().split("\\s+");
            int k = Integer.parseInt(pare[0]);
            double v = Double.parseDouble(pare[1]);
            sumValue += v;
            v += hm.get(k);
            hm.put(k, v);
        }
        TIntDoubleIterator it = hm.iterator();
        while (it.hasNext()) {
            it.advance();
            int k = it.key();
            double score = it.value();
            score /= sumValue;
            if (score > probThreshold) {
                String out = String.format("%d %s %g", k, kt, score);
                context.write(new Text(out), nl);
            }
        }
    }

    @Override
    protected void setup2(Context context) throws IOException, InterruptedException {
        super.setup2(context);
        probThreshold = Math.max(1e-7, probThreshold);
    }
}
