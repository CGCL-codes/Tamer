package com.kapil.framework.concurrent;

/**
 * Interface for the <code>producer</code> which is responsible for placing the data into a {@link IBroker}
 * which will then be consumed by the {@link IConsumer}
 * 
 * @author Kapil Viren Ahuja
 *
 * @param <V>
 */
public interface IProducer<V> extends Runnable {

    /**
     * Primary method that is executed when the Producer is executed.
     */
    void run();
}
