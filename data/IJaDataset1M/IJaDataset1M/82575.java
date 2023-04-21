package mappers.files.rdfs;

import io.files.readers.FilesTriplesReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.NumberUtils;
import utils.TriplesUtils;
import data.Triple;
import data.TripleSource;

public class RDFSSubPropInheritMapper extends Mapper<TripleSource, Triple, BytesWritable, LongWritable> {

    private static Logger log = LoggerFactory.getLogger(RDFSSubPropInheritMapper.class);

    protected Set<Long> subpropSchemaTriples = null;

    protected LongWritable oValue = new LongWritable(0);

    byte[] bKey = new byte[17];

    protected BytesWritable oKey = new BytesWritable();

    private boolean hasSchemaChanged = false;

    private int previousExecutionStep = -1;

    protected void map(TripleSource key, Triple value, Context context) throws IOException, InterruptedException {
        if (!hasSchemaChanged && key.getStep() <= previousExecutionStep) return;
        if (subpropSchemaTriples.contains(value.getPredicate())) {
            if (!value.isObjectLiteral()) bKey[0] = 2; else bKey[0] = 3;
            long time = System.nanoTime();
            NumberUtils.encodeLong(bKey, 1, value.getSubject());
            time = System.nanoTime() - time;
            time = System.nanoTime();
            NumberUtils.encodeLong(bKey, 9, value.getObject());
            time = System.nanoTime() - time;
            oKey.set(bKey, 0, 17);
            oValue.set(value.getPredicate());
            context.write(oKey, oValue);
        }
        if (value.getPredicate() == TriplesUtils.RDFS_SUBPROPERTY && subpropSchemaTriples.contains(value.getObject())) {
            bKey[0] = 5;
            NumberUtils.encodeLong(bKey, 1, value.getSubject());
            oKey.set(bKey, 0, 9);
            oValue.set(value.getObject());
            context.write(oKey, oValue);
        }
    }

    @Override
    protected void setup(Context context) throws IOException {
        hasSchemaChanged = false;
        previousExecutionStep = context.getConfiguration().getInt("lastExecution.step", -1);
        if (subpropSchemaTriples == null) {
            subpropSchemaTriples = new HashSet<Long>();
            hasSchemaChanged = FilesTriplesReader.loadSetIntoMemory(subpropSchemaTriples, context, "FILTER_ONLY_SUBPROP_SCHEMA", previousExecutionStep);
        } else {
            log.debug("Subprop schema triples already loaded in memory");
        }
    }
}
