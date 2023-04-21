package edu.brook.sugarscape;

import org.ascape.model.Agent;
import org.ascape.model.Cell;
import org.ascape.model.CellOccupant;
import org.ascape.model.rule.Rule;

public class TrackSocialNetwork extends Rule {

    public TrackSocialNetwork() {
        super("Track Social Network");
    }

    public void execute(Agent agent) {
        ((Cell) agent).setNetwork(((CellOccupant) agent).findNeighborsOnHost());
    }
}
