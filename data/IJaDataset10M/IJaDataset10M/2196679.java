package alice.cartago.examples.basic;

import alice.cartago.*;
import alice.cartago.util.Agent;

public class ObservingAgent extends Agent {

    private SensorId sid;

    private ArtifactId aid;

    public ObservingAgent(String name, ICartagoEnvironment env) {
        super(name, env);
    }

    public void run() {
        try {
            SensorId sid0 = linkDefaultSensor();
            use(REGISTRY, new Op("lookupArtifact", "my-artifact"), sid0);
            ArtifactId aid = (ArtifactId) sense(sid0, "lookup_succeeded").getContent();
            SensorId sid = linkDefaultSensor();
            focus(aid, sid);
            while (true) {
                Perception p = sense(sid, 100);
                log("new perception: " + p.getType() + " - event time: " + p.getEventTime());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
