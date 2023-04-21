package com.legstar.cixs.jbossesb.testers.adapter.jms;

/**
 * Tests an adapter using a JMS Client.
 */
public abstract class AbstractAdapterJmsClientTester extends AbstractJmsClientTester {

    /** Name of target adapter. */
    private String _adapterName;

    /**
     * @param adapterName name of target adapter
     */
    public AbstractAdapterJmsClientTester(final String adapterName) {
        super("queue/" + adapterName + "_Request_gw", "queue/" + adapterName + "_Request_gw_reply");
        _adapterName = adapterName;
    }

    /**
     * CICSTS31 Direct Http.
     */
    public void testRoundTripCICSTS31DirectHttp() {
        testRoundTrip("CICSTS31DirectHttp");
    }

    /**
     * CICSTS31 Pooled Http.
     */
    public void testRoundTripCICSTS31PooledHttp() {
        testRoundTrip("CICSTS31PooledHttp");
    }

    /**
     * CICSTS23 Direct Http.
     */
    public void testRoundTripCICSTS23DirectHttp() {
        testRoundTrip("CICSTS23DirectHttp");
    }

    /**
     * CICSTS23 Pooled Http.
     */
    public void testRoundTripCICSTS23PooledHttp() {
        testRoundTrip("CICSTS23PooledHttp");
    }

    /**
     * CICSTS23 Direct Socket.
     */
    public void testRoundTripCICSTS23DirectSocket() {
        testRoundTrip("CICSTS23DirectSocket");
    }

    /**
     * CICSTS23 Pooled Socket.
     */
    public void testRoundTripCICSTS23PooledSocket() {
        testRoundTrip("CICSTS23PooledSocket");
    }

    /**
     * CICSTS23 Direct MQ.
     */
    public void testRoundTripCICSTS23DirectMQ() {
        testRoundTrip("CICSTS23DirectMQ");
    }

    /**
     * CICSTS23 Pooled MQ.
     */
    public void testRoundTripCICSTS23PooledMQ() {
        testRoundTrip("CICSTS23PooledMQ");
    }

    /**
     * CICSTS31 Direct MQ.
     */
    public void testRoundTripCICSTS31DirectMQ() {
        testRoundTrip("CICSTS31DirectMQ");
    }

    /**
     * CICSTS31 Pooled MQ.
     */
    public void testRoundTripCICSTS31PooledMQ() {
        testRoundTrip("CICSTS31PooledMQ");
    }

    /**
     * Mock Direct.
     */
    public void testRoundTripMockDirect() {
        testRoundTrip("MockDirect");
    }

    /**
     * Mock Pooled.
     */
    public void testRoundTripMockPooled() {
        testRoundTrip("MockPooled");
    }

    /**
     * Perform a complete request and check result.
     * @param endpointName the target mainframe endpoint name
     */
    public abstract void testRoundTrip(final String endpointName);

    /**
     * @return the adapter name
     */
    public String getAdapterName() {
        return _adapterName;
    }
}
