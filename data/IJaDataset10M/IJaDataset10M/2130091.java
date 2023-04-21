package org.edemocrazy.democracy.core.domain;

import javax.persistence.Entity;

@Entity
public class AtomicAgent extends Agent {

    private static final long serialVersionUID = -8213090131046642286L;

    public AtomicAgent() {
    }

    public AtomicAgent(String name) {
        super(name);
    }
}
