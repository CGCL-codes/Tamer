package uk.azdev.openfire.friendlist.messageprocessors;

import org.hamcrest.beans.HasPropertyWithValue;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.incoming.LoginChallengeMessage;

@RunWith(JMock.class)
public class LoginChallengeMessageProcessorTest {

    Mockery context = new JUnit4Mockery();

    @Test
    public void testProcessMessage() {
        OpenFireConfiguration config = new OpenFireConfiguration();
        config.setUsername("testuser");
        config.setPassword("testpass");
        LoginChallengeMessage message = new LoginChallengeMessage();
        message.setSalt("d3cd8b9eacb901fc153858786b047d1bb826ea75");
        final String expectedSaltedPass = "25e1911c4a08f46e864f156335c54e68aed701f0";
        final IMessageSender messageSender = context.mock(IMessageSender.class);
        context.checking(new Expectations() {

            {
                one(messageSender).sendMessage(with(new HasPropertyWithValue<LoginChallengeMessage>("saltedPassword", equal(expectedSaltedPass))));
            }
        });
        LoginChallengeMessageProcessor processor = new LoginChallengeMessageProcessor(messageSender, config);
        processor.processMessage(message);
        context.assertIsSatisfied();
    }
}
