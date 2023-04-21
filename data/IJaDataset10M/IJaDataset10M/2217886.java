package com.zeat.doubleleg.records;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import com.zeat.doubleleg.util.TeamEnum;

/**
 * Represents a wrestler and contains all statistics for the wrestler throughout a season.
 * 
 * @author Adam Taylor
 * @version 1.0
 * Copyright 2007 Adam Taylor
 * This program is distributed under the GNU Lesser General Public License.
 */
public class Wrestler implements Serializable {

    /** Generated UID. */
    private static final long serialVersionUID = -8802667557237770168L;

    /** The name of the wrestler. */
    private String name;

    /** The team the wrestler is on. */
    private TeamEnum team;

    /** The total wins for this wrestler. */
    private int overallWins = 0;

    /** The total losses for this wrestler. */
    private int overallLosses = 0;

    /** Collection of all matches this wrestler participated in. */
    private Collection<Match> matches;

    /**
	 * Default constructor.
	 */
    public Wrestler() {
        matches = new ArrayList<Match>();
        this.name = "Default";
        this.team = TeamEnum.RECREATIONAL;
    }

    /**
	 * Constructor for a wrestler.
	 * @param name The name of the wrestler.
	 * @param team The team the wrestler is on.
	 */
    public Wrestler(String name, TeamEnum team) {
        matches = new ArrayList<Match>();
        this.name = name;
        this.team = team;
    }

    /**
	 * Constructor for a wrestler.
	 * @param name The name of the wrestler.
	 * @param team The team the wrestler is on.
	 * @param matches A collection of matches the wrestler has participated in.
	 */
    public Wrestler(String name, TeamEnum team, Collection<Match> matches) {
        matches = new ArrayList<Match>();
        this.name = name;
        this.team = team;
        if (!matches.isEmpty()) {
            this.matches.addAll(matches);
        }
    }

    /**
	 * Add a new match for this wrestler.
	 * @param match The match to add to this wrestler.
	 */
    public void addMatch(Match match) {
        if (match.getOutcome().isWin()) {
            incrementOverallWins();
        } else {
            incrementOverallLosses();
        }
        this.matches.add(match);
    }

    /**
	 * Add a collection of matches for this wrestler.
	 * @param matches Collection of matches to add to this wrestler.
	 */
    public void addMatches(Collection<Match> matches) {
        for (Match match : matches) {
            if (match.getOutcome().isWin()) {
                incrementOverallWins();
            } else {
                incrementOverallLosses();
            }
        }
        this.matches.addAll(matches);
    }

    /**
	 * Remove a match from this wrestler.
	 * @param match Match to remove.
	 */
    public void removeMatch(Match match) {
        if (match.getOutcome().isWin()) {
            decrementOverallWins();
        } else {
            decrementOverallLosses();
        }
        this.matches.remove(match);
    }

    /**
	 * Remove all matches from this wrestler.
	 */
    public void removeAllMatches() {
        setOverallLosses(0);
        setOverallWins(0);
        this.matches.clear();
    }

    /**
	 * @return The name of the wrestler.
	 */
    public String getName() {
        return this.name;
    }

    /**
	 * Sets the name of the wrestler.
	 * @param name The name to give the wrestler.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return The team the wrestler is on.
	 */
    public TeamEnum getTeam() {
        return team;
    }

    /**
	 * Sets the team the wrestler is on.
	 * @param team The team the wrestler is on.
	 */
    public void setTeam(TeamEnum team) {
        this.team = team;
    }

    /**
	 * @return Collections of matches this wrestler has competed in.
	 */
    public Collection<Match> getMatches() {
        return this.matches;
    }

    /**
	 * Sets a collection of matches for the wrestler.
	 * @param matches Collection of matches the wrestler has competed in.
	 */
    public void setMatches(Collection<Match> matches) {
        this.matches = matches;
    }

    /**
	 * @return The total number of wins for this wrestler.
	 */
    public int getOverallWins() {
        return this.overallWins;
    }

    /**
	 * Sets the total number of wins for this wrestler.
	 * @param overallWins The total wins for this wrestler.
	 */
    public void setOverallWins(int overallWins) {
        this.overallWins = overallWins;
    }

    /**
	 * @return The total number of losses for this wrestler.
	 */
    public int getOverAllLosses() {
        return this.overallLosses;
    }

    /**
	 * Sets the total number of losses for this wrestler.
	 * @param overAllLosses The total losses for this wrestler.
	 */
    public void setOverallLosses(int overAllLosses) {
        this.overallLosses = overAllLosses;
    }

    /**
	 * Increments the total number of wins.
	 */
    public void incrementOverallWins() {
        this.overallWins++;
    }

    /**
	 * Increments the total number of losses.
	 */
    public void incrementOverallLosses() {
        this.overallLosses++;
    }

    /**
	 * Decrement the total number of wins.
	 */
    public void decrementOverallWins() {
        this.overallWins--;
    }

    /**
	 * Decrement the total number of losses.
	 */
    public void decrementOverallLosses() {
        this.overallLosses--;
    }

    public String toString() {
        return getName();
    }

    public Wrestler clone() {
        Wrestler newWrestler = new Wrestler(this.name, this.team, this.matches);
        newWrestler.setOverallWins(this.overallWins);
        newWrestler.setOverallLosses(this.overallLosses);
        return newWrestler;
    }
}
