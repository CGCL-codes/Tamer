package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.variable.CspGenericDoubleConstant;
import jopt.csp.variable.CspGenericIndex;

/**
 * A generic double constant such as Xi representing 1.1, 7.3, and 12.8 for i=0, i=1, and i=2, respectively
 */
public class GenericDoubleConstant extends GenericNumConstant implements CspGenericDoubleConstant {

    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param vals		array of Double objects that this generic constant wraps
     */
    public GenericDoubleConstant(String name, CspGenericIndex indices[], Double vals[]) {
        super(name, indices, vals);
    }

    /**
     * Constructor
     * 
     * @param name      unique name of this node
     * @param indices   array of indices that generic node is based upon
     * @param vals     array of Double objects that this generic constant wraps
     */
    public GenericDoubleConstant(String name, CspGenericIndex indices[], double vals[]) {
        super(name, indices, getObjectArray(vals));
    }

    /**
     * Method to convert primative array of doubles to an Object array
     */
    private static MutableNumber[] getObjectArray(double vals[]) {
        MutableNumber[] intObAr = new MutableNumber[vals.length];
        for (int i = 0; i < vals.length; i++) {
            intObAr[i] = new MutableNumber(vals[i]);
        }
        return intObAr;
    }

    public GenericNumConstant generateNumConstant(Number num) {
        return new GenericDoubleConstant(null, new GenericIndex[] { new GenericIndex("", 1) }, new Double[] { (Double) num });
    }

    public GenericNumConstant generateNumConstant(String name, CspGenericIndex indices[], Number vals[]) {
        return new GenericDoubleConstant(name, indices, toDoubleArray(vals));
    }

    /**
     * Method to convert an array of Number objects to and array of Double objects
     */
    private static Double[] toDoubleArray(Number vals[]) {
        if (vals == null) return null;
        Double[] intObAr = new Double[vals.length];
        for (int i = 0; i < vals.length; i++) {
            intObAr[i] = (Double) vals[i];
        }
        return intObAr;
    }

    public Double getDoubleForIndex() {
        int nodeIdx = 0;
        for (int i = 0; i < indices.length; i++) nodeIdx += indices[i].currentVal() * indexOffsets[i];
        if (vals[nodeIdx] instanceof MutableNumber) {
            return (Double) ((MutableNumber) vals[nodeIdx]).toConst();
        }
        return (Double) new Double(vals[nodeIdx].doubleValue());
    }

    public int getNumberType() {
        return NumConstants.DOUBLE;
    }

    public Double[] getDoubleConstants() {
        return (Double[]) getNumConstants();
    }

    public Double getMin() {
        if (getNumMin() instanceof MutableNumber) {
            return (Double) ((MutableNumber) getNumMin()).toConst();
        } else {
            return (Double) getNumMin();
        }
    }

    public Double getMax() {
        if (getNumMax() instanceof MutableNumber) {
            return (Double) ((MutableNumber) getNumMax()).toConst();
        } else {
            return (Double) getNumMax();
        }
    }

    public String toString() {
        name = name == null ? "~Double" : name;
        StringBuffer buf = new StringBuffer(name);
        buf.append(":[");
        for (int i = 0; i < indices.length; i++) {
            if (i > 0) buf.append(",");
            buf.append(indices[i].getName());
        }
        buf.append("]");
        return buf.toString();
    }
}
