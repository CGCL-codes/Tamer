package skewreduce.framework;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WritableInputFormat<K extends Writable, V extends Writable> extends FileInputFormat<K, V> {

    private static final Logger LOG = LoggerFactory.getLogger(WritableInputFormat.class);

    private static class EndianConvertor implements DataInput {

        private ByteBuffer recBuf;

        private byte[] buf;

        private FSDataInputStream in;

        public EndianConvertor(FSDataInputStream in) {
            this.in = in;
            buf = new byte[8];
            recBuf = ByteBuffer.wrap(buf);
            recBuf.order(ByteOrder.LITTLE_ENDIAN);
        }

        @Override
        public boolean readBoolean() throws IOException {
            return in.readBoolean();
        }

        @Override
        public byte readByte() throws IOException {
            return in.readByte();
        }

        @Override
        public char readChar() throws IOException {
            return in.readChar();
        }

        @Override
        public double readDouble() throws IOException {
            in.readFully(buf);
            recBuf.position(0).limit(8);
            return recBuf.getDouble();
        }

        @Override
        public float readFloat() throws IOException {
            in.readFully(buf, 0, 4);
            recBuf.position(0).limit(4);
            return recBuf.getFloat();
        }

        @Override
        public void readFully(byte[] b) throws IOException {
            in.readFully(b);
        }

        @Override
        public void readFully(byte[] b, int off, int len) throws IOException {
            in.readFully(b, off, len);
        }

        @Override
        public int readInt() throws IOException {
            in.readFully(buf, 0, 4);
            recBuf.position(0).limit(4);
            return recBuf.getInt();
        }

        @Override
        public String readLine() throws IOException {
            return in.readLine();
        }

        @Override
        public long readLong() throws IOException {
            in.readFully(buf);
            recBuf.position(0).limit(8);
            return recBuf.getLong();
        }

        @Override
        public short readShort() throws IOException {
            in.readFully(buf, 0, 2);
            recBuf.position(0).limit(2);
            return recBuf.getShort();
        }

        @Override
        public int readUnsignedByte() throws IOException {
            return in.readUnsignedByte();
        }

        @Override
        public int readUnsignedShort() throws IOException {
            return (int) (readShort() & 0x0ffff);
        }

        @Override
        public String readUTF() throws IOException {
            return in.readUTF();
        }

        @Override
        public int skipBytes(int n) throws IOException {
            return in.skipBytes(n);
        }
    }

    protected static class WritableRecordReader<K extends Writable, V extends Writable> extends RecordReader<K, V> {

        protected WritableInputFormat<K, V> format;

        protected long start;

        protected long pos;

        protected long end;

        protected int recordSize;

        private ByteOrder border;

        private FSDataInputStream fileIn;

        protected DataInput reader;

        protected K key;

        protected V value;

        public WritableRecordReader() {
        }

        public WritableRecordReader(WritableInputFormat<K, V> fmt) {
            format = fmt;
            recordSize = format.getRecordSize();
            border = format.getByteOrder();
        }

        @Override
        public void initialize(InputSplit genericSplit, TaskAttemptContext context) throws IOException {
            FileSplit split = (FileSplit) genericSplit;
            Configuration job = context.getConfiguration();
            start = split.getStart();
            end = start + split.getLength();
            final Path file = split.getPath();
            long skip = recordSize - (start % recordSize);
            start += ((skip == recordSize) ? 0 : skip);
            FileSystem fs = file.getFileSystem(job);
            fileIn = fs.open(split.getPath());
            fileIn.seek(start);
            this.pos = start;
            if (border == ByteOrder.BIG_ENDIAN) {
                reader = fileIn;
            } else {
                reader = new EndianConvertor(fileIn);
            }
        }

        public boolean nextKeyValue() throws IOException {
            if (pos >= end) return false;
            key = format.createKey();
            value = format.createValue();
            try {
                key.readFields(reader);
                value.readFields(reader);
                pos += recordSize;
                return true;
            } catch (EOFException ignore) {
                pos += recordSize;
                return false;
            }
        }

        @Override
        public K getCurrentKey() {
            return key;
        }

        @Override
        public V getCurrentValue() {
            return value;
        }

        public float getProgress() {
            if (start == end) {
                return 0.0f;
            } else {
                return Math.min(1.0f, (pos - start) / (float) (end - start));
            }
        }

        public synchronized void close() throws IOException {
            if (fileIn != null) fileIn.close();
            fileIn = null;
        }
    }

    public abstract K createKey();

    public abstract V createValue();

    public abstract int getKeySize();

    public abstract int getValueSize();

    public ByteOrder getByteOrder() {
        return ByteOrder.BIG_ENDIAN;
    }

    public final int getRecordSize() {
        return getKeySize() + getValueSize();
    }

    @Override
    public RecordReader<K, V> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        return new WritableRecordReader<K, V>(this);
    }
}
