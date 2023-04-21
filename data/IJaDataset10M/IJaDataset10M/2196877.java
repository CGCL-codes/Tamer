package jmetal.base.variable;

import jmetal.base.Configuration.*;
import jmetal.base.Variable;
import jmetal.util.PseudoRandom;

/**
 * Implements a permutation of integer decision variable
 */
public class Permutation extends Variable {

    /**
   * Stores a permutation of <code>int</code> values
   */
    public int[] vector_;

    /**
   * Stores the length of the permutation
   */
    public int size_;

    /**
   * Constructor
   */
    public Permutation() {
        setVariableType(VariableType_.Permutation);
        size_ = 0;
        vector_ = null;
    }

    /**
  * Constructor
  * @param size Length of the permutation
  * This constructor has been contributed by Madan Sathe
  */
    public Permutation(int size) {
        setVariableType(VariableType_.Permutation);
        size_ = size;
        vector_ = new int[size_];
        java.util.ArrayList<Integer> randomSequence = new java.util.ArrayList<Integer>(size_);
        for (int i = 0; i < size_; i++) randomSequence.add(i);
        java.util.Collections.shuffle(randomSequence);
        for (int j = 0; j < randomSequence.size(); j++) vector_[j] = randomSequence.get(j);
    }

    /** 
   * Copy Constructor
   * @param permutation The permutation to copy
   */
    public Permutation(Permutation permutation) {
        setVariableType(VariableType_.Permutation);
        size_ = permutation.size_;
        vector_ = new int[size_];
        for (int i = 0; i < size_; i++) {
            vector_[i] = permutation.vector_[i];
        }
    }

    /** 
   * Create an exact copy of the <code>Permutation</code> object.
   * @return An exact copy of the object.
   */
    public Variable deepCopy() {
        return new Permutation(this);
    }

    /**
   * Returns the length of the permutation.
   * @return The length
   */
    public int getLength() {
        return size_;
    }

    /**
   * Returns a string representing the object
   * @return The string
   */
    public String toString() {
        String string;
        string = "";
        for (int i = 0; i < size_; i++) string += vector_[i] + " ";
        return string;
    }
}
