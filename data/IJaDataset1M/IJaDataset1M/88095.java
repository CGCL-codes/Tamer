package net.sf.josceleton.connection.impl;

import net.sf.josceleton.commons.test.jmock.AbstractMockeryTest;
import net.sf.josceleton.core.api.entity.message.JointMessage;
import net.sf.josceleton.core.api.entity.message.UserMessage;
import org.testng.annotations.Test;

public class ConnectionAdapterTest extends AbstractMockeryTest {

    @Test
    public final void onJointMessageDoesNothing() {
        new ConnectionAdapter() {
        }.onJointMessage(this.mock(JointMessage.class));
    }

    @Test
    public final void onUserMessageDoesNothing() {
        new ConnectionAdapter() {
        }.onUserMessage(this.mock(UserMessage.class));
    }
}
