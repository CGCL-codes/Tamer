package org.susan.java.design.mediator;

public abstract class N0Colleague {

    private N0Mediator mediator;

    public N0Colleague(N0Mediator mediator) {
        this.mediator = mediator;
    }

    public N0Mediator getMediator() {
        return this.mediator;
    }
}
