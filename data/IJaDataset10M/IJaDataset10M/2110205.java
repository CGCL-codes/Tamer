package org.databene.benerator.consumer;

import org.databene.benerator.Consumer;
import org.databene.benerator.wrapper.ProductWrapper;

/**
 * Abstract implementation of the Consumer interface. 
 * Custom implementations should rather inherit from this class 
 * than implement the Consumer interface directly.
 * This increases the chance to keep custom consumers compatible 
 * with future versions.<br/><br/>
 * Created: 25.01.2008 22:37:42
 * @since 0.4.0
 * @author Volker Bergmann
 */
public abstract class AbstractConsumer implements Consumer {

    public void startConsuming(ProductWrapper<?> wrapper) {
        startProductConsumption(wrapper.unwrap());
    }

    public void finishConsuming(ProductWrapper<?> wrapper) {
        finishProductConsumption(wrapper.unwrap());
    }

    public abstract void startProductConsumption(Object object);

    public void finishProductConsumption(Object object) {
    }

    public void flush() {
    }

    public void close() {
    }
}
