package skewreduce.framework;

import java.io.IOException;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Counter;

public class MuxMapper<K1, V1> extends LoopingMapper<K1, V1, ByteWritable, MuxData> {

    private ByteWritable streamIndex = new ByteWritable();

    private MuxData streamValue = new MuxData();

    private Counter[] streamCounters;

    protected void setupCounters(Context context, int nstream) {
        streamCounters = new Counter[nstream];
        for (int i = 0; i < nstream; ++i) streamCounters[i] = context.getCounter("skewreduce", "MUX_WRITE_" + i);
    }

    public final <K extends Writable, V extends Writable> void write(Context context, Enum<?> stream, K key, V value) throws IOException, InterruptedException {
        write(context, stream.ordinal(), key, value);
    }

    public final <K extends Writable, V extends Writable> void write(Context context, int stream, K key, V value) throws IOException, InterruptedException {
        streamIndex.set((byte) stream);
        streamValue.set(key, value);
        context.write(streamIndex, streamValue);
        streamCounters[stream].increment(1L);
    }
}
