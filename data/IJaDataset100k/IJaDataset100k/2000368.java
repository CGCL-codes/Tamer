package uk.azdev.openfire.friendlist.messageprocessors;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.azdev.openfire.net.ConnectionStateListener;
import uk.azdev.openfire.net.messages.incoming.LoginFailureMessage;

@RunWith(JMock.class)
public class LoginFailureMessageProcessorTest {

    Mockery context = new JUnit4Mockery();

    @Test
    public void testProcessMessage() {
        final ConnectionStateListener stateListener = context.mock(ConnectionStateListener.class);
        context.checking(new Expectations() {

            {
                one(stateListener).loginFailed();
            }
        });
        LoginFailureMessage message = new LoginFailureMessage();
        message.setReason(1L);
        LoginFailureMessageProcessor processor = new LoginFailureMessageProcessor(stateListener);
        processor.processMessage(message);
    }
}
