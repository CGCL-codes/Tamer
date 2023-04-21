package test;

import alice.cartago.*;
import alice.cartago.util.Agent;

public class LinkArtifactUser extends Agent {

    public LinkArtifactUser(String name) throws CartagoException {
        super(name);
    }

    public void run() {
        log("started");
        try {
            joinWorkspace("wsp2");
            SensorId sid = linkDefaultSensor();
            ArtifactId id1 = makeArtifact("myArtifact", "test.LinkingArtifact");
            joinWorkspace("wsp1", "localhost:1200");
            ArtifactId id2 = lookupArtifact("count");
            switchWorkspace("wsp2");
            log("Linking " + id1 + " " + id2);
            linkArtifacts(id1, "out-1", id2);
            log("artifacts linked: going to test");
            use(id1, new Op("test"), sid);
            Perception p = sense(sid, "count_value", 200000);
            log("count value: " + p.intContent(0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        log("completed.");
    }
}
