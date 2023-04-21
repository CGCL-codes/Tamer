package la4j.vector.dense;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import la4j.factory.DenseFactory;
import la4j.vector.AbstractVector;
import la4j.vector.Vector;

public class DenseVector extends AbstractVector implements Vector {

    private double self[];

    public DenseVector() {
        this(0);
    }

    public DenseVector(int lenght) {
        super(new DenseFactory());
        this.self = new double[lenght];
        this.length = lenght;
    }

    public DenseVector(double array[]) {
        super(new DenseFactory());
        this.self = array;
        this.length = array.length;
    }

    @Override
    public double get(int i) {
        if (i >= length || i < 0) throw new IndexOutOfBoundsException();
        return self[i];
    }

    @Override
    public void set(int i, double value) {
        if (i >= length || i < 0) throw new IndexOutOfBoundsException();
        self[i] = value;
    }

    @Override
    public double[] toArray() {
        return self;
    }

    @Override
    public double[] toArrayCopy() {
        double arraycopy[] = new double[length];
        System.arraycopy(self, 0, arraycopy, 0, length);
        return arraycopy;
    }

    @Override
    public int nonzero() {
        int result = 0;
        for (int i = 0; i < length; i++) {
            if (Math.abs(self[i]) > EPS) {
                result++;
            }
        }
        return result;
    }

    @Override
    public void resize(int length) {
        if (length < 0) throw new IllegalArgumentException();
        if (length == this.length) return;
        if (length < this.length) {
            this.length = length;
        } else {
            double newSelf[] = new double[length];
            System.arraycopy(self, 0, newSelf, 0, self.length);
            this.self = newSelf;
            this.length = length;
        }
    }

    @Override
    public void swap(int i, int j) {
        if (i >= length || i < 0 || j >= length || j < 0) throw new IndexOutOfBoundsException();
        if (i == j) return;
        double d = self[i];
        self[i] = self[j];
        self[j] = d;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(length);
        out.writeByte(META_MARKER);
        for (int i = 0; i < length; i++) {
            out.writeDouble(self[i]);
            out.writeByte(ELEMENT_MARKER);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        length = in.readInt();
        in.readByte();
        self = new double[length];
        for (int i = 0; i < length; i++) {
            self[i] = in.readDouble();
            in.readByte();
        }
    }

    @Override
    public Vector clone() {
        DenseVector dolly = (DenseVector) super.clone();
        dolly.self = self.clone();
        return dolly;
    }
}
