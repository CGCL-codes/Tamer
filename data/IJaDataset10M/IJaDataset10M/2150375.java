package ru.itbrains.jicard.wssecuritypolicy;

import ru.itbrains.jicard.wspolicy.Policy;

public class TransportBinding {

    private Policy policy;

    public TransportBinding() {
    }

    public TransportBinding(Policy policy) {
        this.policy = policy;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public String toString() {
        return "TransportBinding{" + "policy=" + policy + '}';
    }
}
