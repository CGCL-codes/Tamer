package org.sopera.metadata.impl;

import org.sopera.metadata.Operation;
import org.w3c.dom.Element;

public interface EndpointNature {

    public boolean supportsOperation(Operation operation);

    public String getTransportUrl();
}
