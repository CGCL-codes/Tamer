package suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import xmpp.configuration.ConfigurationTest;
import xmpp.listeners.ChatMessageListenerTest;
import xmpp.listeners.PrivateMessageListenerTest;
import xmpp.messaging.AppearanceMessageTest;
import xmpp.messaging.ParticipantInfoTest;
import xmpp.messaging.PrivateChatMessageTest;
import xmpp.messaging.PrivateMessageTest;
import xmpp.messaging.PublicChatMessageTest;
import xmpp.queue.MessageQueueTest;
import xmpp.queue.TransportQueueIntegrationTest;
import xmpp.queue.TransportQueueTest;
import xmpp.utils.presence.PresenceProcessorTest;
import xmpp.watchers.ConnectionWatcherTest;
import xmpp.watchers.RoomWatcherTest;
import xmpp.watchers.WatchersCollectionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ConfigurationTest.class, MessageQueueTest.class, ParticipantInfoTest.class, PresenceProcessorTest.class, PrivateMessageTest.class, PrivateChatMessageTest.class, PublicChatMessageTest.class, AppearanceMessageTest.class, PrivateMessageListenerTest.class, ChatMessageListenerTest.class, ConnectionWatcherTest.class, RoomWatcherTest.class, TransportQueueTest.class, TransportQueueIntegrationTest.class, WatchersCollectionTest.class })
public class XmppTestsSuite {
}
