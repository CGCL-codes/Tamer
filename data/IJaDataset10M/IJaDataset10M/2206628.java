package simpatest;

import alice.cartago.*;
import alice.simpa.*;

public class ArtifactWithExtActUser extends Agent {

    @ACTIVITY
    void main() throws Exception {
        ArtifactId id = makeArtifact("myArtifact", "test.MyArtifactWithExtAct");
        use(id, new Op("op1"));
        log("completed.");
        SensorId sid = getSensor("s0");
        focus(id, sid);
        sense(sid, "myEvent");
        log("sensed event");
    }
}
