package org.jeuron.jlightning.processor;

import org.jeuron.jlightning.message.Message;

/**
 * <p>Implemented by {@link DefaultSynchronousProcessor} this interface defines the
 * basic synchronous processor operations.
 *
 * @author Mike Karrys
 * @since 1.0
 * @see Processor
 * @see AbstractProcessor
 * @see DefaultSynchronousProcessor
 */
public interface SynchronousProcessor extends Processor {

    /**
     * Method used to send and receive Messages synchronously.
     * @param message
     * @return message
     */
    Message sendReceiveMessage(Message message) throws ProcessorException;
}
