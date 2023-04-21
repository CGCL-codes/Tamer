package com.btmatthews.maven.wagon.sourceforge.test;

import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Abstract test case from which all delegate test cases will be derived. During
 * set up a socket factory is registered for the https protocol and the web
 * client is initialised. The socket factory is unregistered during tear down.
 * 
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @version 1.0
 */
public abstract class AbstractDelegateTestCase extends AbstractSourceForgeWagonTestCase {

    /**
     * The port number for the HTTPS protocol.
     */
    private static final int HTTPS_PORT = 443;

    /**
     * The prefix for the HTTPS protocol.
     */
    private static final String HTTPS_PREFIX = "https";

    /**
     * The web client.
     */
    private WebClient client;

    /**
     * Initialise the test case with a name.
     * 
     * @param name
     *            The test case name.
     */
    protected AbstractDelegateTestCase(final String name) {
        super(name);
    }

    /**
     * Get the web client.
     * 
     * @return The web client.
     */
    protected final WebClient getClient() {
        return this.client;
    }

    protected void setUp() throws Exception {
        super.setUp();
        final ProtocolSocketFactory socketFactory = new EasySSLProtocolSocketFactory();
        final Protocol protocol = new Protocol(AbstractDelegateTestCase.HTTPS_PREFIX, socketFactory, AbstractDelegateTestCase.HTTPS_PORT);
        Protocol.registerProtocol(AbstractDelegateTestCase.HTTPS_PREFIX, protocol);
        this.client = new WebClient(BrowserVersion.FIREFOX_2);
    }

    protected void tearDown() throws Exception {
        this.client = null;
        Protocol.unregisterProtocol(AbstractDelegateTestCase.HTTPS_PREFIX);
        super.tearDown();
    }
}
