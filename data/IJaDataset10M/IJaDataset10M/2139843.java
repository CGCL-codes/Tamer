package org.hopto.pentaj.jexin.node;

/**
 * Creates {@link MockTraceClient}s.
 */
public class MockTraceClientFactory implements TraceClientFactory {

    /**
	 * @see org.hopto.pentaj.jexin.node.TraceClientFactory#getTraceClient()
	 */
    public TraceClient getTraceClient() {
        return new MockTraceClient();
    }
}
