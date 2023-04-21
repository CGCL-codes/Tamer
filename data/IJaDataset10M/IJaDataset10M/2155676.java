package com.google.web.bindery.requestfactory.shared.testing;

import com.google.web.bindery.requestfactory.shared.RequestTransport;

/**
 * A no-op implementation of {@link RequestTransport} that can be used for unit
 * testing.
 */
public class FakeRequestTransport implements RequestTransport {

    /**
   * No-op.
   */
    @Override
    public void send(String payload, TransportReceiver receiver) {
    }
}
