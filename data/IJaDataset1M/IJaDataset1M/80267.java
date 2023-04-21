package unittests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ MarshalerTests.class, SimpleUUIDTest.class, ThreadPoolTest.class, PacketTransportTests.class, QueuedTransportTests.class, WholeSystem.class })
public class UrmiTest {
}
