package net.sourceforge.smokestack.jms;

import static org.hamcrest.MatcherAssert.assertThat;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import org.hamcrest.core.IsNot;

/**
 * @author gliptak
 *
 */
public class MockTopicSubscriber extends MockMessageConsumer implements TopicSubscriber {

    public MockTopicSubscriber(Topic topic, String name) {
        super(topic, name);
    }

    public boolean getNoLocal() throws JMSException {
        assertThat("mockState", mockState, IsNot.not(MessageConsumerState.CLOSE));
        return NoLocal;
    }

    public Topic getTopic() throws JMSException {
        assertThat("mockState", mockState, IsNot.not(MessageConsumerState.CLOSE));
        return (Topic) destination;
    }
}
