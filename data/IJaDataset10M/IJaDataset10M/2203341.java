package org.databene.benerator.consumer;

import static org.junit.Assert.*;
import org.databene.benerator.consumer.MappingEntityConsumer;
import org.databene.benerator.factory.ConsumerMock;
import org.databene.benerator.test.ModelTest;
import org.databene.benerator.wrapper.ProductWrapper;
import org.databene.model.data.Entity;
import org.junit.Test;

/**
 * Tests the {@link MappingEntityConsumer}.<br/><br/>
 * Created: 22.02.2010 20:09:02
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class MappingEntityConsumerTest extends ModelTest {

    @Test
    public void test() {
        ConsumerMock target = new ConsumerMock();
        MappingEntityConsumer consumer = new MappingEntityConsumer();
        consumer.setTarget(target);
        consumer.setMappings("'name'->'givenName', 'none'->'some'");
        Entity input = createEntity("Person", "name", "Alice", "age", 23);
        consumer.startConsuming(new ProductWrapper<Entity>().wrap(input));
        consumer.finishConsuming(new ProductWrapper<Entity>().wrap(input));
        assertEquals(createEntity("Person", "givenName", "Alice", "age", 23), target.lastProduct);
    }
}
