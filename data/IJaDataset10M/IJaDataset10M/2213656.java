package org.jnormalform.orphanedInstanciation.warn;

class Example {

    private int EXPECTED_COUNT = 1;

    int M() {
        if (true) {
            new Object();
        }
        return EXPECTED_COUNT;
    }
}
