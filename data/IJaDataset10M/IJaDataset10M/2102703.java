package skewreduce.lsst;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;
import skewreduce.lib.ITuple;

public class Coord implements Writable, Comparable<Coord>, ITuple {

    protected int x;

    protected int y;

    public Coord() {
    }

    public Coord(int a, int b) {
        x = a;
        y = b;
    }

    public Coord(String spec) {
        String[] coords = spec.split(",");
        if (coords == null || coords.length != 2) throw new IllegalArgumentException(spec);
        x = Integer.parseInt(coords[0]);
        y = Integer.parseInt(coords[1]);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int hashCode() {
        return x << 16 | y;
    }

    public String toSpec() {
        return String.format("%d,%d", x, y);
    }

    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    @Override
    public void readFields(DataInput arg0) throws IOException {
        x = arg0.readInt();
        y = arg0.readInt();
    }

    @Override
    public void write(DataOutput arg0) throws IOException {
        arg0.writeInt(x);
        arg0.writeInt(y);
    }

    @Override
    public int compareTo(Coord o) {
        if (x < o.x) return -1; else if (o.x < x) return 1;
        return y - o.y;
    }

    public Coord add(Coord o) {
        return new Coord(x + o.x, y + o.y);
    }

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public double getDouble(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getFloat(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getInt(int i) {
        return (i == 0) ? x : y;
    }

    @Override
    public long getLong(int i) {
        return getInt(i);
    }

    @Override
    public String getString(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Type getType(int i) {
        return Type.INTEGER;
    }

    @Override
    public Object getValue(int i) {
        return getInt(i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Coord) {
            Coord a = (Coord) o;
            return x == a.x && y == a.y;
        }
        return false;
    }

    @Override
    public boolean equals(ITuple o) {
        if (o instanceof Coord) {
            return equals((Object) o);
        }
        return (o.arity() == 2) && x == o.getInt(0) && y == o.getInt(1);
    }
}
