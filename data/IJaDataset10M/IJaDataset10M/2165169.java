package com.meschbach.psi.example.dprimecore.prime;

import java.math.BigDecimal;

/**
 * A <code>WorkFactory</code> is an interface to abstract a how a series of
 * operations are to be performed and allow for those operations to be performed
 * in a single-threaded, multi-threaded, and distributed environments.
 * 
 * @author "Mark Eschbach" &lt;meschbach@gmail.com&gt;
 * @since 1.0.0
 * @version 1.0.0
 *
 * @param T is the abstract type of all the Tasks
 * @param P is the type of the PrimeState
 */
public interface WorkFactory<T, P> {

    /**
     * Constructs a new task for finding the next prime given the
     * current <code>state</code> of used prime numbers.  This should return
     * the next prime number in the sequence.
     * 
     * @param state is the state of prime numbers
     * @param closure is to be called when complete, with the result
     * @return a unit of work representing the retrieval of the next prime number
     */
    public T nextPrime(P state, CompletionClosure closure);

    /**
     * Constructs a new task for dividing the <code>dividend</code> by
     * the divisor and providing the remainder to the <code>closure</code>
     * when complete.
     *
     * @param divisor is the divisor to divide by
     * @param dividend is the dividend to be divided
     * @param closure is the closure to notify of the remainder
     * @return a unit of work representing this division
     */
    public T divide(BigDecimal divisor, BigDecimal dividend, CompletionClosure closure);

    /**
     * Given the <code>value</code>, find the square and return the results
     * to the <code>closure</code>.
     * 
     * @param value is the value we would like to square
     * @param closure to be notified when the square is found
     * @return  a unit of work representing squaring a number
     */
    public T square(BigDecimal value, CompletionClosure closure);

    /**
     * Constructs a task notifying the <code>PrimalityCheck</code> we have
     * reached the conclusion <code>isPrime</code>.
     *
     * @param isPrime if the number was indeed prime
     * @return a unit of work representing representing our completed state
     */
    public T result(boolean isPrime);

    /**
     * @return a task that does nothing; not used currently, but you know, be prepared.
     */
    public T doNothing();
}
