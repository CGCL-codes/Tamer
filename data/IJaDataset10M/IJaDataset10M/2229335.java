package com.textflex.txtfl;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/** A football season.
 * Seasons consists of scheduled games for a given year in a given season.
 * New seasons can be generated, games can be found for existing seasons,
 * and standings can be calculated during and after seasons.
 */
public class Season {

    private int seasonID = -1;

    private int seasonYear = -1;

    private String leagueName = "";

    private Database db = null;

    private int weeks = -1;

    private SeasonStandings visitorStandings = null;

    private SeasonStandings hostStandings = null;

    /** Constructs a season with reference to the given database.
	 * @param aSeasonYear season year
	 * @param aLeagueName league name
	 * @param aDb database from which additional seasonal
	 * elements can be retrieved
	 */
    public Season(int aSeasonYear, String aLeagueName, Database aDb) {
        seasonYear = aSeasonYear;
        leagueName = aLeagueName;
        db = aDb;
    }

    /** Constructs a season.
	 * @param aSeasonID season ID
	 * @param aSeasonYear season year
	 * @param aLeagueName league name
	 * @param aWeeks number of weeks in the season
	 */
    public Season(int aSeasonID, int aSeasonYear, String aLeagueName, int aWeeks) {
        leagueName = aLeagueName;
        seasonYear = aSeasonYear;
        seasonID = aSeasonID;
        weeks = aWeeks;
    }

    /** Creates a new season with the given number of weeks.
	 * The season and all associated games for all the teams
	 * in the league are inserted into the database.
	 * @param weeks number of weeks in the season
	 */
    public void createSeason(int weeks) {
        seasonID = db.insertSeason(seasonYear, leagueName, weeks);
        Team[] teams = db.selectTeams(leagueName);
        List teamList = Arrays.asList(teams);
        for (int i = 0; i < weeks; i++) {
            Collections.shuffle(teamList);
            db.insertSeasonWeek(seasonID, i, teamList);
        }
        setWeeks(weeks);
    }

    /** Gets the next game for the team in the season.
	 * Each week in the season will be checked from earliest
	 * to latest until a week is found with a game in which
	 * the given team has not completed the game.
	 * @param team team for which the next game is to be found
	 * @return the next game in the season for the team
	 * @throws aborts if no game is found for the given team
	 */
    public Game getNextGame(Team team) throws AbortException {
        SeasonWeek[] seasonWeeks = db.selectNextSeasonWeeks(0, seasonID, team.getTeamID());
        Game game = null;
        if (seasonWeeks != null) {
            if (seasonWeeks.length < 1) {
                throw new AbortException("No more games found for the " + team.getName() + ".");
            }
            game = db.selectGame(seasonWeeks[0].getGameID());
        }
        return game;
    }

    /** Computes the season standings for the given range of weeks.
	 * The seasonal standings are organized according to visitor
	 * and host standings and stored in their respective class variables,
	 * and merged set of standings is returned.
	 * @param startWeek week in season at which to start, indexed 
	 * starting at 0
	 * @param lengthWeeks number of weeks to include in standings
	 * @return merged standings from both visitor and host games
	 * @see #computeStandingsByWeek
	 */
    public SeasonStandings computeStandings(int startWeek, int lengthWeeks) {
        visitorStandings = new SeasonStandings();
        hostStandings = new SeasonStandings();
        for (int i = startWeek; i < startWeek + lengthWeeks; i++) {
            computeStandingsByWeek(visitorStandings, hostStandings, i);
        }
        SeasonStandings mergedStandings = SeasonStandings.mergeStandings(visitorStandings, hostStandings);
        return mergedStandings;
    }

    /** Computes standings for all weeks in the season.
	 * @return merged standings
	 * @see #computeStandings
	 */
    public SeasonStandings computeStandings() {
        return computeStandings(0, weeks);
    }

    /** Computes the season standings for the given week.
	 * Visitor and host standings are managed separately,
	 * and the results are stored in the given standings objects.
	 * @param visitorStandings visitor standings, which will
	 * be incremented according to the results of the week's games
	 * @param hostStandings host standings, which will
	 * be incremented according to the results of the week's games
	 * @param week week for which game results will be added
	 * to the given standings
	 * @see #computeStandings
	 */
    public void computeStandingsByWeek(SeasonStandings visitorStandings, SeasonStandings hostStandings, int week) {
        Game[] games = db.selectGamesFromSeasonWeek(seasonID, week, 1);
        for (int j = 0; j < games.length; j++) {
            int scoreDiff = games[j].getScoreVisitor() - games[j].getScoreHost();
            int visitorID = games[j].getTeamVisitorID();
            int hostID = games[j].getTeamHostID();
            if (scoreDiff > 0) {
                visitorStandings.incrementWins(visitorID);
                hostStandings.incrementLosses(hostID);
            } else if (scoreDiff < 0) {
                visitorStandings.incrementLosses(visitorID);
                hostStandings.incrementWins(hostID);
            } else {
                visitorStandings.incrementTies(visitorID);
                hostStandings.incrementTies(hostID);
            }
        }
    }

    /** Sets up the first round of playoffs.
	 * The number of teams is determined by the number of 
	 * divisions plus the number of additional teams that 
	 * will bring the number of playoff games to be power
	 * of 2. The number of teams will subsequently be
	 * halved each week until only one team per conference
	 * remains. This function essentially turns the season
	 * from a contest at the divisional to a contest at the
	 * conference level.
	 * Assumes that the season standings have already been
	 * generated for each division in the given league.
	 */
    public void setupFirstPlayoffWeek(League league) {
        ArrayList conferences = league.getConferences();
        for (int i = 0; i < conferences.size(); i++) {
            LeagueConference conf = (LeagueConference) conferences.get(i);
            ArrayList divs = conf.getDivisions();
            int numWeeks = (int) Math.ceil(Math.log(divs.size()) / Math.log(2));
            int numTeams = (int) Math.pow(2, numWeeks);
            ArrayList teams = conf.getPlayoffTeams(numTeams);
            db.insertPlayoffSeasonWeek(seasonID, weeks, teams);
        }
    }

    /** Sets up subsequent rounds of playoffs, for games those that 
	 * follow the first round.
	 * The first round brought the contest from the divisional to
	 * the conference level, and now the teams are battling on
	 * relatively equal footing within the conference.
	 */
    public void setupSubsequentPlayoffWeek(League league, int week) {
        ArrayList conferences = league.getConferences();
        ArrayList bowlTeams = new ArrayList();
        for (int i = 0; i < conferences.size(); i++) {
            LeagueConference conf = (LeagueConference) conferences.get(i);
            ArrayList teams = conf.getSubsequentPlayoffTeams();
            if (teams.size() <= 1) {
                bowlTeams.addAll(teams);
            } else {
                db.insertPlayoffSeasonWeek(seasonID, week, teams);
            }
        }
        if (bowlTeams.size() == 2) {
            System.out.println("tXtFL Bowl time!");
            db.insertBowlSeasonWeek(seasonID, week, bowlTeams);
        }
    }

    /** Sets the season ID.
	 * @param aSeasonID season ID
	 */
    public void setSeasonID(int aSeasonID) {
        seasonID = aSeasonID;
    }

    /** Sets the season year.
	 * @param aSeasonYear season year
	 */
    public void setSeasonYear(int aSeasonYear) {
        seasonYear = aSeasonYear;
    }

    /** Sets the name of the league to which this season
	 * is attached.
	 * @param aLeagueName league name
	 */
    public void setLeagueName(String aLeagueName) {
        leagueName = aLeagueName;
    }

    /** Sets the database for team, league, and game 
	 * transactions.
	 * @param aDb database
	 */
    public void setDb(Database aDb) {
        db = aDb;
    }

    /** Sets the number of weeks in the season.
	 * @param aWeeks number of weeks
	 */
    public void setWeeks(int aWeeks) {
        weeks = aWeeks;
    }

    /** Gets the season ID.
	 * @return season ID
	 */
    public int getSeasonID() {
        return seasonID;
    }

    /** Gets the season year.
	 * @return season year
	 */
    public int getSeasonYear() {
        return seasonYear;
    }

    /** Gets the name of the league to which this season
	 * is attached.
	 * @return league name
	 */
    public String getLeagueName() {
        return leagueName;
    }

    /** Gets the number of weeks in the season.
	 * @return number of weeks
	 */
    public int getWeeks() {
        return weeks;
    }
}
