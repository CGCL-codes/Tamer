package org.databene.benerator.consumer;

import java.util.ArrayList;
import java.util.List;
import org.databene.benerator.Consumer;

/**
 * {@link Consumer} implementation that stores all consumed objects in a {@link List}.<br/><br/>
 * Created: 23.01.2011 08:17:14
 * @since 0.6.4
 * @author Volker Bergmann
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ListConsumer extends AbstractConsumer {

    private static int DEFAULT_CAPACITY = 10;

    private List consumedData;

    public ListConsumer() {
        this(DEFAULT_CAPACITY);
    }

    public ListConsumer(int capacity) {
        this.consumedData = new ArrayList(capacity);
    }

    @Override
    public void startProductConsumption(Object data) {
        this.consumedData.add(data);
    }

    public List getConsumedData() {
        return consumedData;
    }

    public void clear() {
        this.consumedData.clear();
    }
}
