package org.mobicents.media.server.mgcp.controller;

import java.io.IOException;
import java.util.Collection;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mobicents.media.Component;
import org.mobicents.media.server.spi.Connection;
import org.mobicents.media.server.spi.ConnectionType;
import org.mobicents.media.server.spi.Endpoint;
import org.mobicents.media.server.spi.EndpointState;
import org.mobicents.media.server.spi.MediaType;
import org.mobicents.media.server.spi.ResourceUnavailableException;
import org.mobicents.media.server.spi.TooManyConnectionsException;
import org.mobicents.media.server.spi.dsp.DspFactory;

/**
 *
 * @author kulikov
 */
public class ConfiguratorTest {

    private Configurator configurator;

    public ConfiguratorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        configurator = new Configurator(getClass().getResourceAsStream("/mgcp-conf.xml"));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of activate method, of class Configurator.
     */
    @Test
    public void testActivate() throws Exception {
        MyEndpoint endpoint = new MyEndpoint("mobicents/ivr/1");
        MgcpEndpoint activity = configurator.activate(endpoint, null, "127.0.0.1", 9201);
        assertTrue(activity != null);
    }

    private class MyEndpoint implements Endpoint {

        private String name;

        public MyEndpoint(String name) {
            this.name = name;
        }

        public String getLocalName() {
            return name;
        }

        public EndpointState getState() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Collection<MediaType> getMediaTypes() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void start() throws ResourceUnavailableException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void stop() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Connection createConnection(ConnectionType type) throws TooManyConnectionsException, ResourceUnavailableException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void deleteConnection(Connection connection) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void deleteAllConnections() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String describe(MediaType mediaType) throws ResourceUnavailableException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setDspFactory(DspFactory dspFactory) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Component getResource(MediaType mediaType, Class intf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
