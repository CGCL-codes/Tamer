package skewreduce.framework;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;

public class ArrayDimension implements Writable {

    int[] dimension;

    public ArrayDimension(int... dim) {
        dimension = dim.clone();
    }

    public ArrayDimension(Configuration conf) {
        String[] vals = conf.getStrings("skewreduce.array.dimension");
        dimension = new int[vals.length];
        for (int i = 0; i < vals.length; ++i) {
            dimension[i] = Integer.parseInt(vals[i]);
        }
    }

    public int size() {
        return dimension.length;
    }

    public int getMax(int i) {
        return dimension[i];
    }

    public ArrayIndex createIndex() {
        return createIndex(dimension);
    }

    public ArrayIndex createIndex(int... idx) {
        if (idx.length != dimension.length) throw new IllegalArgumentException(String.format("Dimension mismatch: %d (expected %d)", idx.length, dimension.length));
        return new ArrayIndex(this, idx);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        int dims = in.readInt();
        dimension = new int[dims];
        for (int i = 0; i < dims; ++i) {
            dimension[i] = in.readInt();
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(dimension.length);
        for (int d : dimension) {
            out.writeInt(d);
        }
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        int i = 0;
        for (i = 0; i < dimension.length - 1; ++i) {
            b.append(dimension[i]);
            b.append(',');
        }
        b.append(dimension[i]);
        return b.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof ArrayDimension) {
            return Arrays.equals(dimension, ((ArrayDimension) o).dimension);
        }
        return false;
    }
}
