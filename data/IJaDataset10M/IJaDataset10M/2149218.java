package com.ontotext.ordi.sar.test.soap;

import com.ontotext.ordi.sar.remote.server.Server;
import com.ontotext.sar.test.SARTestCase;

/**
 * Base class for SOAP tests.
 * 
 * @author atanas
 *
 */
public abstract class SoapTestCase extends SARTestCase {

    public static final String LR_SMALL = "small___1205937667782___1206";

    public static final String LR_SMALLEST = "smallest___1205939503750___4747";

    protected static final String endpointBase = "http://localhost:8081/axis/services/";

    public static final String endpoint(String service) {
        return endpointBase + service;
    }

    protected Server server;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.server = new Server();
        this.server.run(false);
    }

    @Override
    protected void tearDown() throws Exception {
        this.server.stop();
        super.tearDown();
    }
}
