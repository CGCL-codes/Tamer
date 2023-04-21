package alice.cartagox.examples.basic;

import alice.cartago.*;
import alice.cartago.util.Agent;

public class TriggerNotifier extends Agent {

    public TriggerNotifier(String name, ICartagoEnvironment env) {
        super(name, env);
    }

    public void run() {
        try {
            log("started.");
            ArtifactId trigId = lookupArtifact("trig");
            sleep(200 + new java.util.Random(System.currentTimeMillis()).nextInt(3000));
            use(trigId, new Op("signal"));
            log("completed.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
