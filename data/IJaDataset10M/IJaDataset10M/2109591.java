package org.magnos.asset.source;

import static org.junit.Assert.*;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.magnos.asset.Assets;
import org.magnos.asset.props.PropertyFormat;
import org.magnos.asset.server.AssetServer;
import org.magnos.asset.server.ssl.SslServer;
import org.magnos.asset.source.ClasspathSource;
import org.magnos.asset.text.TextFormat;

/**
 * Tests the {@link SslSource} class.
 * 
 * @author Philip Diffenderfer
 *
 */
public class TestSsl {

    private static final int SERVER_PORT = 8435;

    private static final int SERVER_BACKLOG = 32;

    private static final String SERVER_HOST = "127.0.0.1";

    @Before
    public void onBefore() {
        Assets.addFormat(new PropertyFormat());
        Assets.addFormat(new TextFormat());
        Assets.setSource(new SslSource(SERVER_HOST, SERVER_PORT));
    }

    @Test
    public void testSsl() throws Exception {
        AssetServer server = new SslServer(SERVER_PORT, SERVER_BACKLOG);
        server.setSource(new ClasspathSource());
        server.start();
        Properties props = Assets.load("app.properties");
        assertNotNull(props);
        assertEquals(2, props.size());
        assertEquals("bar", props.get("foo"));
        assertEquals("world", props.get("hello"));
        String message = Assets.load("greetings.txt");
        assertNotNull(message);
        assertEquals("Hello World", message);
        server.stop();
    }
}
