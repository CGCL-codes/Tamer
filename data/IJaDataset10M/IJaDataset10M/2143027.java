package org.zxframework.util;

/**
 * Like using ByRef in in vb
 */
public class ByRefInt {

    private int value;

    /**
	 * Default constructor.
	 */
    public ByRefInt() {
        this.value = 0;
    }

    /**
	 * Constructor
	 * 
	 * @param intStart The starting value
	 */
    public ByRefInt(int intStart) {
        this.value = intStart;
    }

    /**
	 * Increment the internal value
	 */
    public void incr() {
        this.value++;
    }

    /**
	 * Decrement the internal value
	 *
	 */
    public void decr() {
        this.value--;
    }

    /**
	 * Add x to the internal value
	 * @param intAdd What to add
	 */
    public void add(int intAdd) {
        this.value = this.value + intAdd;
    }

    /**
	 * Substract x from the internal value.
	 * @param intSub What the substract
	 */
    public void substract(int intSub) {
        this.value = this.value - intSub;
    }

    /**
	 * Reset the internal value
	 */
    public void reset() {
        this.value = 0;
    }

    /**
	 * @return Return the internal value
	 */
    public int value() {
        return value;
    }
}
