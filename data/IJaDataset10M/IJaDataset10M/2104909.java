package com.continuent.tungsten.replicator.thl;

import java.io.Serializable;

/**
 * 
 * This class defines a ProtocolMessage
 * 
 * @author <a href="mailto:teemu.ollakka@continuent.com">Teemu Ollakka</a>
 * @version 1.0
 */
public class ProtocolMessage implements Serializable {

    static final long serialVersionUID = 234224352543L;

    Serializable payload = null;

    public ProtocolMessage(Serializable payload) {
        this.payload = payload;
    }

    public Serializable getPayload() {
        return payload;
    }
}
