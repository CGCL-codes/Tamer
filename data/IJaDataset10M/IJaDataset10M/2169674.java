package com.textflex.txtfl;

/** Divisions within league conferences.
 * Each division consists of a set of teams that vie for the 
 * divisional title, the vaunted gateway into the playoffs.
 * Second-tier teams striving for a playoff berth can make
 * a run for the wild-card, a wild-goose chase for a last-minute
 * entrance into the conference battleground.
 */
public class LeagueDivision extends LeagueSubgroup {

    /** Header for the division name column in the teams
	 * spreadsheet.
	 * The name of the team's division should match one
	 * of the appropriate division names in the leagues
	 * spreadsheet.
	 * @see League#CONFERENCES for specification of how to
	 * establish league and subgroup names in the leagues
	 * spreadsheet
	 */
    public static final String DIVISION_NAME = "DivisionName";

    private int conferenceID = -1;

    /** Constructs an empty division.
	 */
    public LeagueDivision() {
        super();
    }

    /** Constructs a league division.
	 * @param aName the name of the division
	 */
    public LeagueDivision(String aName) {
        super(aName);
    }

    /** Sets the ID of the conference of which the division
	 * is a part.
	 * @param aConferenceID the conference ID
	 */
    public void setConferenceID(int aConferenceID) {
        conferenceID = aConferenceID;
    }

    /** Gets the ID of the conference of which the division
	 * is a part.
	 * @return the conference ID
	 */
    public int getConferenceID() {
        return conferenceID;
    }
}
