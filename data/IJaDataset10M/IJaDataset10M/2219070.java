package com.legstar.cixs.jbossesb.testers.adapter.http;

import com.legstar.cixs.jbossesb.testers.AbstractHttpClientTester;

/**
 * Tests an adapter using an HTTP Client.
 */
public abstract class AbstractAdapterHttpClientTester extends AbstractHttpClientTester {

    /** Name of target adapter. */
    private String _adapterName;

    /** Target URL. */
    private String _url;

    /**
     * @param adapterName name of target adapter
     */
    public AbstractAdapterHttpClientTester(final String adapterName) {
        super();
        _adapterName = adapterName;
        _url = "http://" + getJBossEsbHost() + ":8765/legstar/services" + "/" + _adapterName + "/";
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
     * @return the target adapter URL
     */
    public String getUrl() {
        return _url;
    }

    /**
     * @return the adapter name
     */
    public String getAdapterName() {
        return _adapterName;
    }
}
