package test;

import alice.cartago.*;

public class Counter1 extends Artifact {

    @OPERATION
    void init() {
        defineObsProperty("count", 0);
    }

    @OPERATION
    void inc() {
        int count = getObsProperty("count").intValue();
        updateObsProperty("count", count + 1);
    }
}
