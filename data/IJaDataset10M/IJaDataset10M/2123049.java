package org.fernwood.jbasic.runtime;

import org.fernwood.jbasic.value.Value;

/**
 * This class handles a list of register locations to store values. During
 * compile time, a varying number of these locations may be needed to hold
 * temporary values that are inconvenient to hold in a stack. The compiler can
 * allocate a register to use for it's purposes, and hold on to that as long as
 * needed. For example, a DO statement might allocate a register to hold a value
 * that an UNTIL might fetch. The DO code compile function allocates a register
 * and stores it such that the UNTIL code can match it (typically using a
 * LoopControlBlock). The UNTIL code free's the register so it is available for
 * use by other compilation units.
 * 
 * @author tom
 * @version version 1.0 Feb 12, 2006
 * 
 */
public class RegisterArray {

    private int maxRegister;

    private Value register[] = null;

    private boolean reserved[] = null;

    /**
	 * Allocate a RegisterArray that can handle a given number of live register
	 * calls. This register array is allocated during compile time, and is used
	 * during execution time.
	 * 
	 * @param m
	 *            The number of Values that this register array can hold.
	 */
    public RegisterArray(final int m) {
        maxRegister = m;
        register = new Value[maxRegister];
        reserved = new boolean[maxRegister];
        for (int i = 0; i < maxRegister; i++) {
            reserved[i] = false;
            register[i] = null;
        }
        register[0] = new Value(0);
    }

    /**
	 * Returns the number of Value objects this RegisterArray can hold.
	 * 
	 * @return a 1-based count of the number of registers available in this
	 *         object.
	 */
    public int size() {
        return maxRegister;
    }

    /**
	 * Allocate an unclaimed slot in the register array to use for a specific
	 * compilation unit.
	 * 
	 * @return The slot number, which can be used for get and set operations.
	 *         Returns zero if there are now available slots.
	 */
    int allocate() {
        for (int i = 1; i < maxRegister; i++) if (!reserved[i]) {
            reserved[i] = true;
            return i;
        }
        return 0;
    }

    /**
	 * Allocate an unclaimed spot in the register array, and immediately store a
	 * value in it. This is used for compile-time constant storage.
	 * 
	 * @param v
	 *            The Value object to be stored in the newly allocated slot.
	 * @return The slot number of the newly allocated location. Returns zero if
	 *         there are no available slots.
	 */
    int allocate(final Value v) {
        final int n = allocate();
        register[n] = v;
        return n;
    }

    /**
	 * Free the use of an element in the register array, and release any stored
	 * value there.
	 * 
	 * @param n
	 *            The slot number to release.
	 * @return the Value that was in the slot just released, if any.
	 */
    Value free(final int n) {
        if ((n < 0) | (n >= maxRegister)) return null;
        final Value temp = register[n];
        reserved[n] = false;
        register[n] = null;
        return temp;
    }

    /**
	 * Set a Value object into a previously allocated slot in the RegisterArray.
	 * 
	 * @param n
	 *            The slot number in which to store the value.
	 * @param v
	 *            A Value object to be stored in the given location.
	 */
    public void set(final int n, final Value v) {
        if ((n < 0) | (n >= maxRegister)) return;
        reserved[n] = true;
        register[n] = v;
    }

    /**
	 * Retrieve a Value object from a RegisterArray slot
	 * 
	 * @param n
	 *            The slot number to retrieve the value from.
	 * @return The Value previously stored in the given slot. If the slot number
	 *         is outside the allowed range for this RegisterArray, null is
	 *         returned.
	 */
    public Value get(final int n) {
        if ((n < 0) | (n >= maxRegister)) return null;
        return register[n];
    }
}
