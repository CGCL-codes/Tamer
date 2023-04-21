package org.databene.benerator.consumer;

import static org.junit.Assert.*;
import org.databene.benerator.Consumer;
import org.databene.benerator.wrapper.ProductWrapper;
import org.junit.Test;

/**
 * Tests the {@link BadDataConsumer}.<br/><br/>
 * Created: 23.01.2011 08:15:24
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class BadDataConsumerTest {

    @Test
    public void test() {
        Consumer realTarget = new AbstractConsumer() {

            @Override
            public void startProductConsumption(Object object) {
                if (((Integer) object) % 2 == 1) throw new RuntimeException();
            }
        };
        ListConsumer badTarget = new ListConsumer();
        BadDataConsumer consumer = new BadDataConsumer(badTarget, realTarget);
        for (int i = 1; i <= 5; i++) {
            consumer.startConsuming(new ProductWrapper<Integer>().wrap(i));
            consumer.finishConsuming(new ProductWrapper<Integer>().wrap(i));
        }
        consumer.close();
        assertEquals(3, badTarget.getConsumedData().size());
        assertEquals(1, badTarget.getConsumedData().get(0));
        assertEquals(3, badTarget.getConsumedData().get(1));
    }
}
