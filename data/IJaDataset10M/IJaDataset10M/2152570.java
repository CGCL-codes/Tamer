package simpatest.timed;

import alice.cartago.*;

public class MyArtifact extends Artifact {

    public MyArtifact() {
    }

    @OPERATION
    void timeIntensiveOp() {
        log("op started.");
        try {
            Thread.sleep(4000);
        } catch (Exception ex) {
        }
        signal("ok");
        log("op finished.");
    }
}
