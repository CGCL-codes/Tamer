package scoreboard.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import scoreboard.config.Team;

/**
 * Container for all properties assigned to a {@link Team}.
 */
public class TeamInfo implements Cloneable {

    protected String name;

    protected int points;

    protected int sets;

    protected List<Integer> pastSetPoints;

    /**
     * Creates and initializes the team info.
     */
    public TeamInfo() {
        reset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TeamInfo clone() {
        try {
            TeamInfo clone = (TeamInfo) super.clone();
            clone.pastSetPoints = new PastCountersValuesList(pastSetPoints);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning failed: ", e);
        }
    }

    /**
     * The team's name.
     */
    public String getName() {
        return name;
    }

    /**
     * The team's HTML displayable name. Special characters are encoded for HTML
     * compatibility.
     */
    public String getHtmlName() {
        return StringEscapeUtils.escapeHtml(name);
    }

    /**
     * The points this team currently has.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Adds one point to the current number of points.
     */
    void incPoints() {
        this.points++;
    }

    /**
     * Number of sets this team has already won.
     */
    public int getSets() {
        return sets;
    }

    /**
     * Adds one to the number of sets this team has won.
     */
    void incSets() {
        this.sets++;
    }

    /**
     * Sets all counters to zero.
     * 
     * @see #resetPoints()
     */
    void reset() {
        points = 0;
        sets = 0;
        pastSetPoints = new PastCountersValuesList();
    }

    /**
     * Only the points are reset to zero.
     * 
     * @see #reset()
     */
    void resetPoints() {
        points = 0;
    }

    /**
     * Changes the team's name.
     * 
     * @param name
     *            The new name.
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * The final points in the past sets.
     */
    public List<Integer> getPastSetPoints() {
        return pastSetPoints;
    }

    /**
     * The final points in the past sets.
     */
    void addPastSetPoints(int finalSetPoints) {
        this.pastSetPoints.add(0, finalSetPoints);
    }

    protected static class PastCountersValuesList extends LinkedList<Integer> {

        private static final long serialVersionUID = -3799050447666515282L;

        public PastCountersValuesList() {
            super();
        }

        public PastCountersValuesList(Collection<? extends Integer> c) {
            super(c);
        }
    }
}
