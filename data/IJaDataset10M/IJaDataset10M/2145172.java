package com.googlecode.jsendnsca.core.builders;

import com.googlecode.jsendnsca.core.Level;
import com.googlecode.jsendnsca.core.MessagePayload;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

/**
 * @version $Revision$
 */
public class MessagePayloadBuilderTest {

    @Test
    public void shouldConstructNewMessagePayload() throws Exception {
        final MessagePayload messagePayload = new MessagePayloadBuilder().withHostname("localhost").withLevel(Level.CRITICAL).withServiceName("test service").withMessage("test message").create();
        assertEquals("localhost", messagePayload.getHostname());
        assertEquals(MessagePayload.LEVEL_CRITICAL, messagePayload.getLevel());
        assertEquals("test service", messagePayload.getServiceName());
        assertEquals("test message", messagePayload.getMessage());
    }

    @Test
    public void shouldConstructTwoNewMessagePayload() throws Exception {
        final MessagePayload messagePayload = new MessagePayloadBuilder().withHostname("localhost").withLevel(Level.OK).withServiceName("test service").withMessage("test message").create();
        final MessagePayload messagePayload2 = new MessagePayloadBuilder().withHostname("somehost").withLevel(Level.WARNING).withServiceName("foo service").withMessage("foo message").create();
        assertEquals("localhost", messagePayload.getHostname());
        assertEquals(MessagePayload.LEVEL_OK, messagePayload.getLevel());
        assertEquals("test service", messagePayload.getServiceName());
        assertEquals("test message", messagePayload.getMessage());
        assertEquals("somehost", messagePayload2.getHostname());
        assertEquals(MessagePayload.LEVEL_WARNING, messagePayload2.getLevel());
        assertEquals("foo service", messagePayload2.getServiceName());
        assertEquals("foo message", messagePayload2.getMessage());
    }
}
