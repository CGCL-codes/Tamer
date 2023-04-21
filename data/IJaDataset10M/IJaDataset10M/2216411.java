package org.amino.pattern.internal;

/**
 * Interface for worker in pattern framework. The worker might be executed by
 * several threads without any synchronization. It's the duty of implementer of
 * this interface to ensure run() method is thread-safe.
 * 
 * @author Zhi Gan (ganzhi@gmail.com)
 * 
 * @param <I>
 *            input type.
 * @param <O>
 *            result type.
 * 
 */
public interface Doable<I, O> {

    /**
     * Run the work item.
     * 
     * @param input
     *            input action
     * @return result.
     */
    O run(I input);
}
