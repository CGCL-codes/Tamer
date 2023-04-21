package org.icehockeymanager.ihm.game.league.helper;

/**
 * LeagueStandings abstract Helper class for all Standing classes.
 * 
 * @author Bernhard von Gunten / Arik Dasen
 * @created December , 2001
 */
public abstract class LeagueStandings {

    /** Owner of this standings */
    private LeagueElement leagueElement = null;

    /**
   * Constructs Standings
   * 
   * @param leagueElement
   *          The leagueElement of this standings
   */
    public LeagueStandings(LeagueElement leagueElement) {
        this.leagueElement = leagueElement;
    }

    /**
   * Returns owner of this standings
   * 
   * @return The leagueElement value
   */
    public LeagueElement getLeagueElement() {
        return leagueElement;
    }
}
