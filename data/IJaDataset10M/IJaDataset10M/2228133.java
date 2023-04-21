package edu.uchicago.agentcell.math;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Functions;
import corejava.Format;

/**
 * @author emonet
 *
 * Implements Vect using the colt library.
 * The Vect3 are 3D vectors with methods for the typical vector analysis.
 */
public class Vect3 implements Vect {

    private static final int DIMENSION = 3;

    private static final Algebra coltAlgebra = new Algebra(Calculus.TOLERANCE);

    protected static final Format FORMAT = new Format("%1.2f");

    private DoubleMatrix1D data;

    /**
     * Default constructor for Vect3. Creates a Vect3 zero vector = (0,0,0).
     */
    public Vect3() {
        data = new DenseDoubleMatrix1D(DIMENSION);
    }

    /**
     * @param x
     * @param y
     * @param z
     * Constructor for Vect3. Creates a Vect3 vector = (x,y,z).
     */
    public Vect3(double x, double y, double z) {
        data = new DenseDoubleMatrix1D(DIMENSION);
        data.setQuick(0, x);
        data.setQuick(1, y);
        data.setQuick(2, z);
    }

    /**
     * @param newData
     * Vect3 constructor used only within the package.
     * The argument is a DoubleMatrix1D from the colt library.
     * This allows to create Vect3 with data pointing somewhere else, e.g. a row of a Matrix3x3 object.
     */
    Vect3(DoubleMatrix1D newData) {
        data = newData;
    }

    public Vect cross(Vect otherV) {
        Vect3 v = (Vect3) otherV;
        double a = (data.getQuick(1) * v.data.getQuick(2)) - (data.getQuick(2) * v.data.getQuick(1));
        double b = (data.getQuick(2) * v.data.getQuick(0)) - (data.getQuick(0) * v.data.getQuick(2));
        data.setQuick(2, (data.getQuick(0) * v.data.getQuick(1)) - (data.getQuick(1) * v.data.getQuick(0)));
        data.setQuick(1, b);
        data.setQuick(0, a);
        return this;
    }

    public Vect div(double scalar) {
        data.assign(Functions.div(scalar));
        return this;
    }

    public double length() {
        return Math.sqrt(coltAlgebra.norm2(data));
    }

    public Vect minus(Vect v) {
        data.assign(((Vect3) v).data, Functions.minus);
        return this;
    }

    public Vect mult(double scalar) {
        data.assign(Functions.mult(scalar));
        return this;
    }

    public double mult(Vect v) {
        return data.zDotProduct(((Vect3) v).data);
    }

    public Vect normalize() {
        data.assign(Functions.div(Math.sqrt(data.zDotProduct(data))));
        return this;
    }

    public Vect plus(Vect v) {
        data.assign(((Vect3) v).data, Functions.plus);
        return this;
    }

    public Vect reverse() {
        data.assign(Functions.mult(-1));
        return this;
    }

    public Vect copy() {
        return new Vect3(data.getQuick(0), data.getQuick(1), data.getQuick(2));
    }

    public Vect copy(Vect v) {
        data.setQuick(0, ((Vect3) v).data.getQuick(0));
        data.setQuick(1, ((Vect3) v).data.getQuick(1));
        data.setQuick(2, ((Vect3) v).data.getQuick(2));
        return this;
    }

    public double getElement(int index) {
        return data.getQuick(index);
    }

    public int getSize() {
        return DIMENSION;
    }

    public void setElement(int index, double value) {
        data.setQuick(index, value);
    }

    public Vect mult(Matrix m) {
        ((Matrix3x3) m).getData().zMult(data.copy(), data, 1, 0, true);
        return this;
    }

    public boolean hasUnitLength() {
        return Calculus.equals(this.length(), 1);
    }

    public boolean isZero() {
        return Calculus.equals(data.getQuick(0), 0) && Calculus.equals(data.getQuick(1), 0) && Calculus.equals(data.getQuick(2), 0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj != null) && getClass().equals(obj.getClass()) && coltAlgebra.property().equals(data, ((Vect3) obj).data)) {
            return true;
        }
        return false;
    }

    /**
     * @return data
     * get the the implemented data vector. Should be used ONLY by the classes implementing Matrix, Vect, ...
     */
    DoubleMatrix1D getData() {
        return data;
    }

    public String toLog() {
        return "" + ((float) data.getQuick(0)) + "," + ((float) data.getQuick(1)) + "," + ((float) data.getQuick(2));
    }

    public String toString() {
        return "(" + FORMAT.form(data.getQuick(0)) + ", " + FORMAT.form(data.getQuick(1)) + ", " + FORMAT.form(data.getQuick(2)) + ")";
    }

    public Vect crossOtherThis(Vect otherV) {
        Vect3 v = (Vect3) otherV;
        double a = (data.getQuick(2) * v.data.getQuick(1)) - (data.getQuick(1) * v.data.getQuick(2));
        double b = (data.getQuick(0) * v.data.getQuick(2)) - (data.getQuick(2) * v.data.getQuick(0));
        data.setQuick(2, (data.getQuick(1) * v.data.getQuick(0)) - (data.getQuick(0) * v.data.getQuick(1)));
        data.setQuick(1, b);
        data.setQuick(0, a);
        return this;
    }

    public Vect minusMult(double scalar, Vect v) {
        data.assign(((Vect3) v).data, Functions.minusMult(scalar));
        return this;
    }

    public Vect plusMult(double scalar, Vect v) {
        data.assign(((Vect3) v).data, Functions.plusMult(scalar));
        return this;
    }
}
