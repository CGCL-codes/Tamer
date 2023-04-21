package nl.utwente.ewi.hmi.deira.iam.vvciam;

import nl.utwente.ewi.hmi.deira.iam.vvciam.match.MatchTracker;

/** Event geeft aan dat de genomen niet eens een redelijk schot op doel was.
*/
public class WorthlessPenaltyKickEvent extends RSEvent {

    /**Constructor*/
    public WorthlessPenaltyKickEvent() {
        super(0.91, -0.06, "worthlesspenaltykick", null);
    }

    /**Constructor*/
    public WorthlessPenaltyKickEvent(String team1) {
        super(0.91, -0.06, "worthlesspenaltykick", null);
        this.team1 = team1;
    }

    /** Geeft null */
    protected static RSEvent checkEvent(MatchTracker match, java.util.List<Situation> sits, EventGenerator eg) {
        return null;
    }
}
