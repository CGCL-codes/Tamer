package org.occ.hmr;

import java.io.IOException;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class IdentityReduce extends Reducer<Text, BytesWritable, Text, BytesWritable> {

    public void setup(Context context) {
    }

    public void reduce(Text key, Iterable<BytesWritable> values, Context context) throws IOException, InterruptedException {
        for (BytesWritable b : values) {
            context.write(key, b);
        }
    }
}
