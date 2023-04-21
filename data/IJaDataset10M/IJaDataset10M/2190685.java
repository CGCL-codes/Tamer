package com.goodcodeisbeautiful.archtea.config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TomcatConnectorConfigImplTest extends TestCase {

    public static Test suite() {
        return new TestSuite(TomcatConnectorConfigImplTest.class);
    }

    TomcatConnectorConfigImpl _config;

    protected void setUp() throws Exception {
        super.setUp();
        _config = new TomcatConnectorConfigImpl("host", 8080);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetHost() {
        assertEquals("host", _config.getAddress());
    }

    public void testGetPort() {
        assertEquals(8080, _config.getPort());
    }
}
