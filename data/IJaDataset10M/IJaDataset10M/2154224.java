package jmetal.encodings.variable;

import jmetal.core.Problem;
import jmetal.core.Variable;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * Class implementing a decision variable representing an array of real values.
 * The real values of the array have their own bounds.
 */
public class ArrayReal extends Variable {

    /**
	 * Problem using the type
	 */
    Problem problem_;

    /**
	 * Stores an array of real values
	 */
    public Double[] array_;

    /**
	 * Stores the length of the array
	 */
    public int size_;

    /**
	 * Constructor
	 */
    public ArrayReal() {
        problem_ = null;
        size_ = 0;
        array_ = null;
    }

    /**
	 * Constructor
	 * @param size Size of the array
	 */
    public ArrayReal(int size, Problem problem) {
        problem_ = problem;
        size_ = size;
        array_ = new Double[size_];
        for (int i = 0; i < size_; i++) {
            array_[i] = PseudoRandom.randDouble() * (problem_.getUpperLimit(i) - problem_.getLowerLimit(i)) + problem_.getLowerLimit(i);
        }
    }

    /** 
	 * Copy Constructor
	 * @param arrayInt The arrayDouble to copy
	 */
    public ArrayReal(ArrayReal arrayReal) {
        problem_ = arrayReal.problem_;
        size_ = arrayReal.size_;
        array_ = new Double[size_];
        for (int i = 0; i < size_; i++) {
            array_[i] = arrayReal.array_[i];
        }
    }

    @Override
    public Variable deepCopy() {
        return new ArrayReal(this);
    }

    /**
	 * Returns the length of the arrayReal.
	 * @return The length
	 */
    public int getLength() {
        return size_;
    }

    /**
	 * getValue
	 * @param index Index of value to be returned
	 * @return the value in position index
	 */
    public double getValue(int index) throws JMException {
        if ((index >= 0) && (index < size_)) return array_[index]; else {
            Configuration.logger_.severe("jmetal.base.variable.ArrayReal.getValue: index value (" + index + ") invalid");
            throw new JMException("jmetal.base.variable.ArrayReal: index value (" + index + ") invalid");
        }
    }

    /**
	 * setValue
	 * @param index Index of value to be returned
	 * @param value The value to be set in position index
	 */
    public void setValue(int index, double value) throws JMException {
        if ((index >= 0) && (index < size_)) array_[index] = value; else {
            Configuration.logger_.severe("jmetal.base.variable.ArrayReal.setValue: index value (" + index + ") invalid");
            throw new JMException("jmetal.base.variable.ArrayReal: index value (" + index + ") invalid");
        }
    }

    /**
	 * Get the lower bound of a value
	 * @param index The index of the value
	 * @return the lower bound
	 */
    public double getLowerBound(int index) throws JMException {
        if ((index >= 0) && (index < size_)) return problem_.getLowerLimit(index); else {
            Configuration.logger_.severe("jmetal.base.variable.ArrayReal.getLowerBound: index value (" + index + ") invalid");
            throw new JMException("jmetal.base.variable.getLowerBound: index value (" + index + ") invalid");
        }
    }

    /**
	 * Get the upper bound of a value
	 * @param index The index of the value
	 * @return the upper bound
	 */
    public double getUpperBound(int index) throws JMException {
        if ((index >= 0) && (index < size_)) return problem_.getUpperLimit(index); else {
            Configuration.logger_.severe("jmetal.base.variable.ArrayReal.getUpperBound: index value (" + index + ") invalid");
            throw new JMException("jmetal.base.variable.getUpperBound: index value (" + index + ") invalid");
        }
    }

    /**
	 * Returns a string representing the object
	 * @return The string
	 */
    public String toString() {
        String string;
        string = "";
        for (int i = 0; i < (size_ - 1); i++) string += array_[i] + " ";
        string += array_[size_ - 1];
        return string;
    }
}
