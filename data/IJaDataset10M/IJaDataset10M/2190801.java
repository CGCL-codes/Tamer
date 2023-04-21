package org.cheetah.core.endpoint.spi;

import java.net.URI;
import org.cheetah.core.endpoint.Endpoint;
import org.cheetah.core.endpoint.EndpointConsumer;
import org.cheetah.core.endpoint.EndpointProducer;

public class ServiceEndpoint implements Endpoint {

    public EndpointConsumer createConsumer(String uri) throws Exception {
        throw new UnsupportedOperationException();
    }

    public EndpointProducer createProducer(String uri) throws Exception {
        return new ServiceEndpointProducer(new URI(uri).getSchemeSpecificPart());
    }
}
