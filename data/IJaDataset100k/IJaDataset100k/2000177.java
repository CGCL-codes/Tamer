package org.personalsmartspace.cm.broker.impl.remote;

import java.io.Serializable;
import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;

public class ContextValueMessage {

    private ICtxIdentifier identifier;

    private Serializable value;

    public ICtxIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(ICtxIdentifier identifier) {
        this.identifier = identifier;
    }

    public Serializable getValue() {
        return value;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }
}
