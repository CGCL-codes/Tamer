package org.nexopenframework.management.jee.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.List;
import org.junit.Test;
import org.nexopenframework.management.monitor.channels.ChannelNotification;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Simple JUnit 4.4 TestCase for dealing with {@link ChannelsDigester} features</p>
 * 
 * @see org.nexopenframework.management.jee.support.ChannelsDigester
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class ChannelsDigesterTest {

    @Test
    public void digestChannels() throws ClassNotFoundException {
        final ClassLoader cls = Thread.currentThread().getContextClassLoader();
        final List<ChannelNotification> channels = ChannelsDigester.getChannels("channels.xml", cls);
        assertNotNull(channels);
        assertEquals(2, channels.size());
    }
}
