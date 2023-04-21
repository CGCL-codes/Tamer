package scoreboard.model;

import java.util.List;
import scoreboard.config.Team;

/**
 * Container for all properties assigned to a {@link Team}.
 */
public class TeamInfoTennis extends TeamInfo implements Cloneable {

    private static final String[] POINT_REPRESENTATION = new String[] { "0", "15", "30", "40", "Adv.", "Win" };

    private int games;

    private List<Integer> pastSetGames;

    /**
     * {@inheritDoc}
     */
    @Override
    protected TeamInfoTennis clone() {
        TeamInfoTennis clone = (TeamInfoTennis) super.clone();
        clone.pastSetGames = new PastCountersValuesList(pastSetGames);
        return clone;
    }

    /**
     * The games this team currently has.
     */
    public int getGames() {
        return games;
    }

    /**
     * Adds one to the current number of won games.
     */
    void incGames() {
        this.games++;
    }

    /**
     * Sets all counters to zero.
     * 
     * @see #resetGames()
     */
    void reset() {
        super.reset();
        games = 0;
        pastSetGames = new PastCountersValuesList();
    }

    /**
     * Only the points are reset to zero.
     * 
     * @see #reset()
     */
    void resetGames() {
        games = 0;
    }

    /**
     * The final games in the past sets in ascending chronological order.
     */
    public List<Integer> getPastSetGames() {
        return pastSetGames;
    }

    /**
     * The final games in the past sets.
     */
    void addPastSetGames(int finalSetGames) {
        this.pastSetGames.add(finalSetGames);
    }

    @Override
    public List<Integer> getPastSetPoints() {
        throw new UnsupportedOperationException("Not supported in " + this.getClass().getName());
    }

    @Override
    void addPastSetPoints(int finalSetPoints) {
        throw new UnsupportedOperationException("Not supported in " + this.getClass().getName());
    }

    /**
     * The points this team has in current set formatted for printing.
     */
    public String getNonTieBreakPoints() {
        try {
            return POINT_REPRESENTATION[getPoints()];
        } catch (IndexOutOfBoundsException e) {
            return "-";
        }
    }

    public void decPoints() {
        this.points--;
    }
}
