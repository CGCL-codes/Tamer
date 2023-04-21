package org.ikasan.framework.initiator.messagedriven;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.TextMessage;
import junit.framework.Assert;
import org.ikasan.framework.component.Event;
import org.ikasan.framework.component.IkasanExceptionHandler;
import org.ikasan.framework.event.serialisation.EventDeserialisationException;
import org.ikasan.framework.event.serialisation.JmsMessageEventSerialiser;
import org.ikasan.framework.flow.Flow;
import org.ikasan.framework.flow.invoker.FlowInvocationContext;
import org.ikasan.framework.initiator.AbortTransactionException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * Test class for EventMessageDrivenInitiator
 * 
 * @author Ikasan Development Team
 *
 */
@SuppressWarnings("unchecked")
public class EventMessageDrivenInitiatorTest {

    String moduleName = "moduleName";

    String name = "name";

    private static Mockery mockery = new Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    JmsMessageEventSerialiser<MapMessage> jmsMessageEventSerialiser = mockery.mock(JmsMessageEventSerialiser.class);

    Flow flow = mockery.mock(Flow.class);

    IkasanExceptionHandler exceptionHandler = mockery.mock(IkasanExceptionHandler.class);

    MessageListenerContainer messageListenerContainer = mockery.mock(MessageListenerContainer.class);

    TextMessage textMessage = mockery.mock(TextMessage.class);

    Message message = mockery.mock(Message.class);

    /**
	 * Tests that MapMessages are supported
	 * 
	 * @throws JMSException
	 * @throws EventDeserialisationException 
	 */
    @Test
    public void testOnMessageHandlesMapMessage() throws JMSException, EventDeserialisationException {
        final Event event = mockery.mock(Event.class);
        final MapMessage mapMessage = mockery.mock(MapMessage.class);
        mockery.checking(new Expectations() {

            {
                allowing(mapMessage).getJMSMessageID();
                will(returnValue("messageId"));
                one(jmsMessageEventSerialiser).fromMessage(mapMessage, moduleName, name);
                will(returnValue(event));
                one(event).getId();
                will(returnValue("eventId"));
                one(flow).invoke((FlowInvocationContext) (with(a(FlowInvocationContext.class))), (Event) with(equal(event)));
                will(returnValue(null));
            }
        });
        EventMessageDrivenInitiator eventMessageDrivenInitiator = new EventMessageDrivenInitiator(moduleName, name, flow, exceptionHandler, jmsMessageEventSerialiser);
        eventMessageDrivenInitiator.onMessage(mapMessage);
    }

    /**
	 * Tests that TextMessages are not supported
	 * 
	 * @throws JMSException
	 */
    @Test
    public void testOnMessageDoesNotHandleTextMessage() throws JMSException {
        AbortTransactionException exception = null;
        mockery.checking(new Expectations() {

            {
                one(messageListenerContainer).setListenerSetupExceptionListener((ListenerSetupFailureListener) with(anything()));
                allowing(textMessage).getJMSMessageID();
                will(returnValue("messageId"));
                one(messageListenerContainer).stop();
                one(flow).stop();
            }
        });
        EventMessageDrivenInitiator eventMessageDrivenInitiator = new EventMessageDrivenInitiator(moduleName, name, flow, exceptionHandler, jmsMessageEventSerialiser);
        eventMessageDrivenInitiator.setMessageListenerContainer(messageListenerContainer);
        try {
            eventMessageDrivenInitiator.onMessage(textMessage);
            Assert.fail("should have thrown AbortTransactionException");
        } catch (AbortTransactionException abortTransactionException) {
            exception = abortTransactionException;
        }
        Assert.assertNotNull("should have thrown AbortTransactionException", exception);
    }
}
