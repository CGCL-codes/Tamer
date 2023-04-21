package reducers.files.justification;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import utils.NumberUtils;

public class ExportJustificationReconstructReducer extends Reducer<LongWritable, BytesWritable, NullWritable, Text> {

    Text oValue = new Text();

    @Override
    public void reduce(LongWritable key, Iterable<BytesWritable> values, Context context) throws IOException, InterruptedException {
        String subject = null;
        String predicate = null;
        String object = null;
        String type = null;
        String rsubject = null;
        String rpredicate = null;
        String robject = null;
        long subjectId = 0;
        long predicateId = 0;
        long objectId = 0;
        Iterator<BytesWritable> itr = values.iterator();
        while (itr.hasNext()) {
            BytesWritable value = itr.next();
            switch(value.getBytes()[0]) {
                case 1:
                    subject = new String(value.getBytes(), 1 + 8, value.getLength() - 8 - 1);
                    subjectId = NumberUtils.decodeLong(value.getBytes(), 1);
                    break;
                case 2:
                    predicate = new String(value.getBytes(), 1 + 8, value.getLength() - 8 - 1);
                    predicateId = NumberUtils.decodeLong(value.getBytes(), 1);
                    break;
                case 3:
                    object = new String(value.getBytes(), 1 + 8, value.getLength() - 8 - 1);
                    objectId = NumberUtils.decodeLong(value.getBytes(), 1);
                    break;
                case 4:
                    type = new String(value.getBytes(), 1 + 8, value.getLength() - 8 - 1);
                    break;
                case 5:
                    rsubject = new String(value.getBytes(), 1 + 8, value.getLength() - 8 - 1);
                    break;
                case 6:
                    rpredicate = new String(value.getBytes(), 1 + 8, value.getLength() - 8 - 1);
                    break;
                case 7:
                    robject = new String(value.getBytes(), 1 + 8, value.getLength() - 8 - 1);
                    break;
            }
        }
        oValue.set("<" + subjectId + "," + predicateId + "," + objectId + "> == " + subject + " " + predicate + " " + object + " .");
        context.write(NullWritable.get(), oValue);
    }
}
