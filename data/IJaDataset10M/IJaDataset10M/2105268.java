package org.octave.graphics;

import org.octave.Matrix;

public class ArrayProperty extends Property {

    String[] allowedTypes;

    int allowedDims;

    protected ArrayProperty(ArrayProperty p) {
        super(p);
        this.allowedTypes = p.allowedTypes;
        this.allowedDims = p.allowedDims;
    }

    public ArrayProperty(PropertySet parent, String name, String[] types, int dims) {
        super(parent, name);
        allowedTypes = types;
        allowedDims = dims;
    }

    public ArrayProperty(PropertySet parent, String name, Matrix matrix) {
        this(parent, name, (String[]) null, -1);
        this.pvalue = (matrix != null ? matrix : new Matrix(new double[0], new int[] { 0, 0 }));
    }

    public ArrayProperty(PropertySet parent, String name, String[] types, int dims, Matrix matrix) {
        this(parent, name, types, dims);
        this.pvalue = (matrix != null ? matrix : new Matrix(new double[0], new int[] { 0, 0 }));
    }

    public Property cloneProperty() {
        return new ArrayProperty(this);
    }

    protected Object convertValue(Object array) throws PropertyException {
        if (array == null) return new Matrix(new double[0], new int[] { 0, 0 });
        if (array instanceof Matrix) {
            Matrix m = (Matrix) array;
            if (!isAllowedType(m.getClassName())) throw new PropertyException("invalid matrix class - " + m.getClassName());
            if (allowedDims != -1) {
                if (m.getNDims() != allowedDims) throw new PropertyException("invalid matrix number of dimensions - " + m.getNDims());
            }
            return array;
        } else if (array instanceof Number) {
            Number n = (Number) array;
            if (n instanceof Double && isAllowedType("double")) return new Matrix(new double[] { n.doubleValue() }, new int[] { 1, 1 }); else if (n instanceof Byte && isAllowedType("byte")) return new Matrix(new byte[] { n.byteValue() }, new int[] { 1, 1 }); else throw new PropertyException("invalid data type - " + n.getClass().toString());
        } else {
            try {
                double[] v = (double[]) array;
                return new Matrix(v);
            } catch (ClassCastException e) {
                throw new PropertyException("invalid property value - " + array.toString());
            }
        }
    }

    protected void setInternal(Object value) throws PropertyException {
        super.setInternal(value);
    }

    public Matrix getMatrix() {
        return (Matrix) pvalue;
    }

    public int getNDims() {
        return ((Matrix) pvalue).getNDims();
    }

    public int getDim(int index) {
        return ((Matrix) pvalue).getDim(index);
    }

    public String getClassName() {
        return ((Matrix) pvalue).getClassName();
    }

    public boolean isType(String cls) {
        return getClassName().equals(cls);
    }

    public boolean isAllowedType(String cls) {
        if (allowedTypes != null) {
            for (int i = 0; i < allowedTypes.length; i++) if (allowedTypes[i].equals(cls)) return true;
            return false;
        }
        return true;
    }

    public boolean isEmpty() {
        if (pvalue == null) return true;
        for (int i = 0; i < getNDims(); i++) if (getDim(i) > 0) return false;
        return true;
    }

    public String toString() {
        return pvalue.toString();
    }

    public double[] asDoubleVector() {
        return getMatrix().asDoubleVector();
    }

    public double[][] asDoubleMatrix() {
        return getMatrix().asDoubleMatrix();
    }

    public double[][][] asDoubleMatrix3() {
        return getMatrix().asDoubleMatrix3();
    }

    public int[][] asIntMatrix() {
        return getMatrix().asIntMatrix();
    }
}
