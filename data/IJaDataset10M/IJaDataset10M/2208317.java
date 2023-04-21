package simpatest;

import alice.cartago.*;

public class MyArtifactWithEvents extends Artifact {

    @OPERATION
    void generate() {
        signal("ev1", 1, "pippo");
        signal("ev2", 2, new String[] { "pippo", "pluto" });
    }
}
