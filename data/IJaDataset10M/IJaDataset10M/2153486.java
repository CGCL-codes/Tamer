package net.sf.katta.index.indexer;

import java.io.DataOutput;
import java.io.IOException;
import junit.framework.TestCase;
import net.sf.katta.testutil.StoreAction;
import net.sf.katta.util.IndexConfiguration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class ShardSelectionMapperTest extends TestCase {

    final Mockery _mockery = new Mockery();

    final Reporter _reporter = _mockery.mock(Reporter.class);

    final OutputCollector _outputCollector = _mockery.mock(OutputCollector.class);

    final Mapper _mapper = new ShardSelectionMapper();

    final JobConf _jobConf = new JobConf();

    public void testMapper() throws IOException {
        final Writable writable = _mockery.mock(Writable.class);
        final WritableComparable writableComparable = _mockery.mock(WritableComparable.class);
        _mockery.checking(new Expectations() {

            {
                one(writableComparable).write(with(any(DataOutput.class)));
                one(writable).write(with(any(DataOutput.class)));
                one(_outputCollector).collect(with(equal(new Text(DummyKeyGenerator.SHARD_KEY))), with(any(BytesWritable.class)));
            }
        });
        _jobConf.set(IndexConfiguration.INDEX_SHARD_KEY_GENERATOR_CLASS, DummyKeyGenerator.class.getName());
        _mapper.configure(_jobConf);
        _mapper.map(writableComparable, writable, _outputCollector, _reporter);
        _mockery.assertIsSatisfied();
    }

    public void testBug_IncreasingValueSize() throws IOException {
        final StoreAction storeAction = new StoreAction();
        _mockery.checking(new Expectations() {

            {
                allowing(_outputCollector).collect(with(any(WritableComparable.class)), with(any(Writable.class)));
                will(storeAction);
            }
        });
        _jobConf.set(IndexConfiguration.INDEX_SHARD_KEY_GENERATOR_CLASS, DummyKeyGenerator.class.getName());
        _mapper.configure(_jobConf);
        Text key1 = new Text("key1-aaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Text key2 = new Text("key2-aaaaaaa");
        _mapper.map(key1, new Text("value"), _outputCollector, _reporter);
        int sizeOfKeyValueWritable1 = readBytesWriteableSize(storeAction);
        storeAction.reset();
        _mapper.map(key2, new Text("value"), _outputCollector, _reporter);
        int sizeOfKeyValueWritable2 = readBytesWriteableSize(storeAction);
        assertNotSame(sizeOfKeyValueWritable1, sizeOfKeyValueWritable2);
        assertTrue(sizeOfKeyValueWritable1 > sizeOfKeyValueWritable2);
        _mockery.assertIsSatisfied();
    }

    private int readBytesWriteableSize(StoreAction storeAction) {
        BytesWritable bytesWritable = (BytesWritable) storeAction.getParameters().get(0)[1];
        DataInputBuffer dataInputBuffer = new DataInputBuffer();
        dataInputBuffer.reset(bytesWritable.get(), bytesWritable.getSize());
        return bytesWritable.getSize();
    }

    public static class DummyKeyGenerator implements IShardKeyGenerator {

        private static final String SHARD_KEY = "foo";

        public String getShardKey(final WritableComparable key, final Writable value, final Reporter reporter, final int ofShards) {
            return SHARD_KEY;
        }
    }
}
