package org.datanucleus.store;

/**
 * Sequence of values.
 */
public interface NucleusSequence {

    /**
     * Method to allocate an amount of values.
     * @param amount The amount
     */
    public void allocate(int amount);

    /**
     * Accessor for the current value object.
     * @return Current value object
     */
    public Object current();

    /**
     * Accessor for the current value.
     * @return Current value
     */
    public long currentValue();

    /**
     * Accessor for the name of the sequence.
     * @return Name of the sequence
     */
    public String getName();

    /**
     * Accessor for the next value object.
     * @return Next value object
     */
    public Object next();

    /**
     * Accessor for the next value.
     * @return next value
     */
    public long nextValue();
}
