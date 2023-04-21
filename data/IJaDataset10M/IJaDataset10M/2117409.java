package com.googlecode.gaal.data.api;

/**
 * Allows for basic vector operations
 * 
 * @author akislev
 * 
 */
public interface Vector {

    /**
     * set the value in the position x to value
     * 
     * @param x
     *            - position
     * @param value
     *            - value
     */
    public void set(int x, int value);

    /**
     * add the value to the existing value at the position x
     * 
     * @param x
     *            - position
     * @param value
     *            - the added value
     */
    public void add(int x, int value);

    /**
     * get the value at the position x
     * 
     * @param x
     *            - position
     * @return value
     */
    public int get(int x);

    /**
     * get the norm of the vector
     * 
     * @return the norm
     */
    public double norm();

    /**
     * calculate the cosine similarity between the current and the given vector
     * 
     * @param other
     *            - the vector to calculate the similarity with
     * @return cosine similarity
     */
    public double similarity(Vector other);

    /**
     * calculate the scalar(dot) product of the current and the given vector
     * 
     * @param other
     *            - the vector to calculate the scalar product with
     * @return the scalar product
     */
    public double product(Vector other);

    /**
     * get the size (the number of non-zero elements) of the vector
     * 
     * @return the number of non-zero elements in the vector
     */
    public int size();
}
