package de.hattrickorganizer.logik.matchengine.engine.common;

import de.hattrickorganizer.logik.matchengine.TeamData;
import de.hattrickorganizer.logik.matchengine.TeamRatings;

/**
 * TODO Missing Class Documentation
 *
 * @author TODO Author Name
 */
public class TeamGameData extends TeamData {

    private boolean isHome;

    private int actionAlreadyPlayed;

    private int actionNumber;

    private int counterAction;

    /**
     * Creates a new TeamGameData object.
     *
     * @param _action TODO Missing Constructuor Parameter Documentation
     * @param possess TODO Missing Constructuor Parameter Documentation
     * @param rchance TODO Missing Constructuor Parameter Documentation
     * @param lchance TODO Missing Constructuor Parameter Documentation
     * @param mchance TODO Missing Constructuor Parameter Documentation
     * @param rrisk TODO Missing Constructuor Parameter Documentation
     * @param lrisk TODO Missing Constructuor Parameter Documentation
     * @param mrisk TODO Missing Constructuor Parameter Documentation
     * @param type TODO Missing Constructuor Parameter Documentation
     * @param level TODO Missing Constructuor Parameter Documentation
     */
    public TeamGameData(int _action, double possess, double rchance, double lchance, double mchance, double rrisk, double lrisk, double mrisk, int type, int level) {
        super("", new TeamRatings(possess, rrisk, mrisk, lrisk, rchance, mchance, lchance), type, level);
        actionNumber = _action;
        actionAlreadyPlayed = 0;
        counterAction = 0;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public final int getActionAlreadyPlayed() {
        return actionAlreadyPlayed;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param d TODO Missing Method Parameter Documentation
     */
    public final void setActionNumber(int d) {
        actionNumber = d;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public final int getActionNumber() {
        return actionNumber;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public final int getCounterAction() {
        return counterAction;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param b TODO Missing Method Parameter Documentation
     */
    public final void setHome(boolean b) {
        isHome = b;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public final boolean isHome() {
        return isHome;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public final double getTotalChance() {
        return (getRatings().getRightAttack() + getRatings().getLeftAttack() + getRatings().getMiddleAttack()) / 3.0;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public final double getTotalRisk() {
        return (getRatings().getRightDef() + getRatings().getLeftDef() + getRatings().getMiddleDef()) / 3.0;
    }

    /**
     * TODO Missing Method Documentation
     */
    public final void addActionPlayed() {
        actionAlreadyPlayed++;
    }

    /**
     * TODO Missing Method Documentation
     */
    public final void addCounterActionPlayed() {
        counterAction++;
    }
}
