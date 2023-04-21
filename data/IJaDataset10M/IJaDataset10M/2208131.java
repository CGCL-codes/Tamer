package com.textflex.txtfl;

/** Keeps track of win-loss-tie record for a given team.
 * This class can be used to track a team's record for an
 * arbitrary period of time, whether a full season, only the 
 * postseason, or for a select period of games.
 */
public class TeamRecord {

    private int teamID = -1;

    private int wins = 0;

    private int losses = 0;

    private int ties = 0;

    private double percentage = 0.0;

    /** Constructs a team record for the given team ID.
	 * @param aTeamID the ID of the team being tracked
	 */
    public TeamRecord(int aTeamID) {
        teamID = aTeamID;
    }

    /** Calculates the win-loss-tie percentage for the given record.
	 * The percentage is calculated using the formula:
	 * (win + 0.5*losses) / (win + losses + ties)
	 * @return the win-loss-tie percentage
	 */
    public double calcPercentage() {
        percentage = (double) ((wins + 0.5 * ties) / (wins + losses + ties));
        return percentage;
    }

    /** Merges records two team records.
	 * The teamID is assumed to be that of the record1.
	 * Wins, losses, and ties, but not percentage calculations 
	 * are merged.
	 * @param record1 first team record to be merged; this record's teamID 
	 * will be copied to the new record
	 * @param record2 second team record to be merged
	 * @return the merged record
	 */
    public static TeamRecord mergeRecords(TeamRecord record1, TeamRecord record2) {
        TeamRecord record = new TeamRecord(record1.getTeamID());
        record.setWins(record1.getWins() + record2.getWins());
        record.setLosses(record1.getLosses() + record2.getLosses());
        record.setTies(record1.getTies() + record2.getTies());
        return record;
    }

    /** Sets the team ID.
	 * @param aTeamID the team ID
	 */
    public void setTeamID(int aTeamID) {
        teamID = aTeamID;
    }

    /** Sets the number of wins.
	 * @param aWins number of wins
	 */
    public void setWins(int aWins) {
        wins = aWins;
    }

    /** Increments the number of wins by 1.
	 */
    public void incrementWins() {
        wins += 1;
    }

    /** Sets the number of losses.
	 * @param aLosses the number of losses
	 */
    public void setLosses(int aLosses) {
        losses = aLosses;
    }

    /** Increments the number of losses by 1.
	 */
    public void incrementLosses() {
        losses += 1;
    }

    /** Sets the number of ties.
	 * @param aTies the number of ties
	 */
    public void setTies(int aTies) {
        ties = aTies;
    }

    /** Increments the number of ties by 1.
	 */
    public void incrementTies() {
        ties += 1;
    }

    /** Gets the team ID.
	 * @return the team ID.
	 */
    public int getTeamID() {
        return teamID;
    }

    /** Gets the number of wins.
	 * @return number of wins
	 */
    public int getWins() {
        return wins;
    }

    /** Gets the number of losses.
	 * @return number of losses
	 */
    public int getLosses() {
        return losses;
    }

    /** Gets the number of ties.
	 * @return number of ties
	 */
    public int getTies() {
        return ties;
    }

    /** Gets the win-loss-tie percentage
	 * @return win-loss-tie percentage
	 */
    public double getPercentage() {
        return percentage;
    }
}
