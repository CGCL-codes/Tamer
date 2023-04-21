package simpatest;

import alice.simpa.*;

public class Agent004 extends Agent {

    @ACTIVITY_AGENDA(todos = {  }, reactions = { @REACTION(activity = "response", event = "in_sensor(_,_,") })
    void main() {
    }

    @ACTIVITY
    void a() {
        log("a");
        forceShutdown();
    }

    @ACTIVITY
    void b() {
        log("b");
    }
}
