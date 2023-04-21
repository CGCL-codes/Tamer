package com.ivis.xprocess.web.dao;

import com.ivis.xprocess.web.elements.TransientGatewaytype;

public class GatewaytypeDAO {

    private TransientGatewaytype transientGateway;

    public GatewaytypeDAO(TransientGatewaytype transientGateway) {
        this.transientGateway = transientGateway;
    }

    public String getLabel() {
        return transientGateway.getLabel();
    }

    public String getDescription() {
        return transientGateway.getDescription();
    }

    public String getUuid() {
        return transientGateway.getUuid();
    }

    public String getId() {
        return transientGateway.getId();
    }
}
