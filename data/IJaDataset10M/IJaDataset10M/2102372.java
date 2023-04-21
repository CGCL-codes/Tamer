package uk.ac.lkl.migen.mockup.polydials.model;

import uk.ac.lkl.common.util.expression.Expression;
import uk.ac.lkl.migen.mockup.polydials.expression.CounterDecrement;
import uk.ac.lkl.migen.mockup.polydials.expression.CounterIncreaseSet;
import uk.ac.lkl.migen.mockup.polydials.expression.CounterIncrement;

/**
 * A counter that counts modulo.
 * 
 * @author $Author: toontalk@gmail.com $
 * @version $Revision: 2830 $
 * @version $Date: 2009-06-16 02:36:24 -0400 (Tue, 16 Jun 2009) $
 * 
 */
public class ModuloCounter extends StepwiseAdjustable implements Comparable<ModuloCounter> {

    /**
     * The next id to allocate to a counter.
     * 
     * This is used to allocate and id to an instance automatically.
     * 
     */
    private static int NEXT_ID = 0;

    /**
     * The id of this counter.
     * 
     * This is allocated automatically.
     * 
     */
    private int id;

    private int value;

    /**
     * The modulus of this counter.
     * 
     */
    private int modulus;

    private Expression<CounterIncreaseSet> incrementExpression;

    private Expression<CounterIncreaseSet> decrementExpression;

    /**
     * Create a new instance with the given modulus.
     * 
     * @param modulus
     *            the modulus of this counter
     * 
     */
    public ModuloCounter(int modulus) {
        super();
        this.id = NEXT_ID++;
        this.value = 0;
        setModulus(modulus);
        incrementExpression = new CounterIncrement(this);
        decrementExpression = new CounterDecrement(this);
    }

    public Expression<CounterIncreaseSet> getIncrementExpression() {
        return incrementExpression;
    }

    public Expression<CounterIncreaseSet> getDecrementExpression() {
        return decrementExpression;
    }

    public int getValue() {
        return value % modulus;
    }

    public void increaseValue(int increase) {
        setValue(value + increase);
    }

    public void setValue(int value) {
        if (value == this.value) return;
        this.value = value;
        fireObjectUpdated();
    }

    /**
     * Get the modulus of this instance.
     * 
     * @return the modulus
     * 
     */
    public int getModulus() {
        return modulus;
    }

    /**
     * Set the modulus of this instance.
     * 
     * This method uses a modulus of 1 if the passed argument is zero or
     * negative.
     * 
     * @param modulus
     *            the new modulus
     * 
     */
    public void setModulus(int modulus) {
        if (modulus <= 0) modulus = 1;
        if (modulus == this.modulus) return;
        this.modulus = modulus;
        fireObjectUpdated();
    }

    public int getId() {
        return id;
    }

    public int compareTo(ModuloCounter other) {
        return this.id - other.id;
    }
}
