package alice.cartagox.util;

import alice.cartago.*;
import java.util.*;
import java.io.*;

public class Trigger extends Artifact {

    int counter;

    int nParticipants;

    public Trigger(int nParticipants) {
        counter = 0;
        this.nParticipants = nParticipants;
    }

    @OPERATION
    void register() throws Exception {
        signal("registered");
        nextStep("notifySignals");
    }

    @OPSTEP(guard = "allParticipantsSignaled")
    void notifySignals() {
        signal("signaled");
        counter = 0;
    }

    @GUARD
    boolean allParticipantsSignaled() {
        return counter == nParticipants;
    }

    @OPERATION
    void signal() {
        counter++;
    }
}
