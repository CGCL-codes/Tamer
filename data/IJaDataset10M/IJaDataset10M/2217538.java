package org.modyna.modyna.simulation.integration;

import java.util.Vector;

/**
 * Auxiliary class to hold a vector of Double values
 * 
 * @author Dr. Rupert Rebentisch
 */
public class DoubleVector implements Cloneable {

    /** Creates a new instance of DoubleVector */
    Vector _vector;

    /**
	 * Default constructor
	 */
    public DoubleVector() {
        _vector = new Vector();
    }

    /**
	 * Add a value to the end of the vector
	 * 
	 * @param value
	 */
    public void add(double value) {
        add(new Double(value));
    }

    /**
	 * Add a value to the end of the vector
	 * 
	 * @param value
	 */
    public void add(Double value) {
        _vector.add(value);
    }

    /**
	 * Vector addition of two vectors
	 * 
	 * @param summand
	 *            vector to be added
	 * @return result sum vector
	 */
    public DoubleVector add(DoubleVector summand) {
        DoubleVector sum = new DoubleVector();
        if (summand.size() != _vector.size()) return null;
        for (int i = 0; i < _vector.size(); i++) {
            sum.add(summand.get(i) + ((Double) _vector.get(i)).doubleValue());
        }
        return sum;
    }

    /**
	 * Clear content of vector
	 */
    public void clear() {
        _vector.clear();
    }

    /**
	 * Clone the vector
	 * 
	 * @see java.lang.Object#clone()
	 */
    public Object clone() {
        return copy();
    }

    /**
	 * Copy method
	 * 
	 * @return copy of this
	 */
    public DoubleVector copy() {
        DoubleVector cloneVector = new DoubleVector();
        for (int i = 0; i < _vector.size(); i++) {
            cloneVector.add(new Double(((Double) _vector.get(i)).doubleValue()));
        }
        return cloneVector;
    }

    /**
	 * Retrieve value within vector referenced by integer
	 * 
	 * @param i
	 * @return value of i-th vector element
	 */
    public double get(int i) {
        return ((Double) _vector.get(i)).doubleValue();
    }

    /**
	 * Vector product
	 * 
	 * @param value
	 *            factor
	 * @return product
	 */
    public DoubleVector multiply(double value) {
        DoubleVector product = new DoubleVector();
        for (int i = 0; i < _vector.size(); i++) {
            product.add(value * ((Double) _vector.get(i)).doubleValue());
        }
        return product;
    }

    /**
	 * Set value at given positon
	 * 
	 * @param i
	 *            position
	 * @param value
	 *            value to be set
	 */
    public void set(int i, double value) {
        set(i, new Double(value));
    }

    /**
	 * Set value at given positon
	 * 
	 * @param i
	 *            position
	 * @param value
	 *            value to be set
	 */
    public void set(int i, Double value) {
        _vector.set(i, value);
    }

    /**
	 * How long is the vector
	 * 
	 * @return number of elements of the vector
	 */
    public int size() {
        return _vector.size();
    }
}
