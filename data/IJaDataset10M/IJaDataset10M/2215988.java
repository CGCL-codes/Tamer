package vh.data.match;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import vh.data.club.VHClub;
import vh.data.contest.VHContest;
import vh.data.contest.VHContestPhase;
import vh.data.season.VHSeason;

/**
 * Represents a single match which has to (has been) played regardless of the competition
 * the match belongs to.
 * 
 * @version $Id: Match.java 50 2006-11-05 15:16:20Z janjanke $
 * @author jankejan
 */
@Entity
@Table(name = "matches")
@NamedQueries({ @NamedQuery(name = "countCreatedMatches", query = "select count(*) from VHMatch m where m.d_season.d_nId = :seasonId" + " and m.d_contest.d_strId = :contestId and m.d_contestPhase = :contestPhase"), @NamedQuery(name = "countCreatedMatchesWithRound", query = "select count(*) from VHMatch m where m.d_season.d_nId = :seasonId" + " and m.d_contest.d_strId = :contestId and m.d_contestPhase = :contestPhase and m.d_nRound = :round"), @NamedQuery(name = "countPlayedMatches", query = "select count(*) from VHMatch m where m.d_season.d_nId = :seasonId" + " and m.d_contest.d_strId = :contestId and m.d_contestPhase = :contestPhase and m.d_fPlayed = true") })
public class VHMatch {

    /**
   * Unique match id.
   */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int d_nId;

    /**
   * The season this match is associated with.
   */
    @ManyToOne
    @JoinColumn(name = "season_id")
    private VHSeason d_season;

    /**
   * The competition that is attached to this account.
   */
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private VHContest d_contest;

    /**
   * A competition has multiple phases (also called sub types). This variable indicates
   * for which contest phase the match is intended for (e.g. regular season, play-off or
   * relegation).
   */
    @Enumerated(EnumType.STRING)
    @Column(name = "contest_phase")
    private VHContestPhase d_contestPhase;

    /**
   * The round of the current competition phase (e.g. 3rd day of regular season, or first
   * play-off round)
   */
    @Column(name = "round")
    private int d_nRound;

    /**
   * Used within KO style competition phases in which two teams affront each other more
   * than once. E.g. in a play-off round up to seven sub rounds may be played.
   */
    @Column(name = "subround")
    private int d_nSubRound = 0;

    /**
   * The receiving club.
   */
    @ManyToOne
    @JoinColumn(name = "home_club_id")
    private VHClub d_clubHome;

    /**
   * The visiting club.
   */
    @ManyToOne
    @JoinColumn(name = "away_club_id")
    private VHClub d_clubAway;

    /**
   * Indicates whether this match has already been played.
   */
    @Column(name = "played")
    private boolean d_fPlayed = false;

    /**
   * Is <code>true</code> when the home club has not made a move for this match.
   */
    @Column(name = "home_nmr")
    private boolean d_fNmrHome = false;

    /**
   * Is <code>true</code> when the away club has not made a move for this match.
   */
    @Column(name = "away_nmr")
    private boolean d_fNmrAway = false;

    /**
   * Number of goals scored by the home team.
   */
    @Column(name = "home_goals")
    private int d_nGoalsHome = 0;

    /**
   * Number of goals scored by the visiting team.
   */
    @Column(name = "away_goals")
    private int d_nGoalsAway = 0;

    /**
   * Number of goals scored by the home team during a shootout session.
   */
    @Column(name = "home_goals_shootout")
    private int d_nGoalsShootoutHome = 0;

    /**
   * Number of goals scored by the visiting team during a shootout session.
   */
    @Column(name = "away_goals_shootout")
    private int d_nGoalsShootoutAway = 0;

    /**
   * Number of points awarded to the home team.
   */
    @Column(name = "home_points")
    private int d_nPointsHome = 0;

    /**
   * Number of points awarded to the visiting team.
   */
    @Column(name = "away_points")
    private int d_nPointsAway = 0;

    /**
   * What is the overall cost of this game in terms of goals for the home team (bonus is
   * included).
   */
    @Column(name = "home_cost")
    private BigDecimal d_decCostHome = new BigDecimal("0.0");

    /**
   * What is the overall cost of this game in terms of goals for the visiting team (bonus
   * is included).
   */
    @Column(name = "away_cost")
    private BigDecimal d_decCostAway = new BigDecimal("0.0");

    /**
   * How many bonus goals are awarded to the home team.
   */
    @Column(name = "home_bonus")
    private BigDecimal d_decBonusHome = new BigDecimal("0.0");

    /**
   * How many bonus goals are awarded to the visiting team.
   */
    @Column(name = "away_bonus")
    private BigDecimal d_decBonusAway = new BigDecimal("0.0");

    /**
   * Creates a new match.
   * 
   * @param season the season the match is attached to
   * @param contest the competition the match is attached to
   * @param contestPhase the competition phase the match is part of
   * @param nRound the round within the competition phase
   * @param nSubRound the subround (always 0 for round robin style games, only useful for
   *        KO games)
   * @param home the club playing at home
   * @param away the club playing on the road
   */
    public VHMatch(VHSeason season, VHContest contest, VHContestPhase contestPhase, int nRound, int nSubRound, VHClub home, VHClub away) {
        d_season = season;
        d_contest = contest;
        d_contestPhase = contestPhase;
        d_nRound = nRound;
        d_nSubRound = nSubRound;
        d_clubHome = home;
        d_clubAway = away;
    }

    /** Default parameterless constructor. */
    protected VHMatch() {
    }

    /**
   * Returns the unique match id.
   */
    public int getId() {
        return d_nId;
    }

    /**
   * Returns the number of bonus goals awarded for the visiting team.
   */
    public BigDecimal getBonusAway() {
        return d_decBonusAway;
    }

    /**
   * Returns the number of bonus goals awarded for the home team.
   */
    public BigDecimal getBonusHome() {
        return d_decBonusHome;
    }

    /**
   * Returns the visiting club playing this match.
   */
    public VHClub getClubAway() {
        return d_clubAway;
    }

    /**
   * Returns the home club that hosts the match.
   */
    public VHClub getClubHome() {
        return d_clubHome;
    }

    /**
   * Indicates whether the visiting team has forgotten to make a move for this match.
   * 
   * @return <code>true</code> if the visiting team did not set any goals for this match
   */
    public boolean isNmrAway() {
        return d_fNmrAway;
    }

    /**
   * Indicates whether the home team has forgotten to make a move for this match.
   * 
   * @return <code>true</code> if the home team did not set any goals for this match
   */
    public boolean isNmrHome() {
        return d_fNmrHome;
    }

    /**
   * Returns the competition this match is a part of.
   */
    public VHContest getContest() {
        return d_contest;
    }

    /**
   * Returns the contest phase this match is associated with.
   */
    public VHContestPhase getContestPhase() {
        return d_contestPhase;
    }

    /**
   * Returns the overall cost of goals (including the bonus) which is devited from the
   * visiting team's account after the match.
   */
    public BigDecimal getCostAway() {
        return d_decCostAway;
    }

    /**
   * Returns the overall cost of goals (including the bonus) which is devited from the
   * hosting team's account after the match.
   */
    public BigDecimal getCostHome() {
        return d_decCostHome;
    }

    /**
   * Returns the number of goals scored by the visiting team.
   */
    public int getGoalsAway() {
        return d_nGoalsAway;
    }

    /**
   * Returns the number of goals scored by the home team.
   */
    public int getGoalsHome() {
        return d_nGoalsHome;
    }

    /**
   * If this match had to be decided in a shootout, this method returns the number of
   * goals scored by the visiting team during the shootout session.
   */
    public int getGoalsShootoutAway() {
        return d_nGoalsShootoutAway;
    }

    /**
   * If this match had to be decided in a shootout, this method returns the number of
   * goals scored by the home team during the shootout session.
   */
    public int getGoalsShootoutHome() {
        return d_nGoalsShootoutHome;
    }

    /**
   * Returns the number of points earned by the visiting team.
   */
    public int getPointsAway() {
        return d_nPointsAway;
    }

    /**
   * Returns the number of points earned by the home team.
   */
    public int getPointsHome() {
        return d_nPointsHome;
    }

    /**
   * Returns the number of the round of the current contest phase this match belongs to
   * (e.g. the 3rd match day of the regular season or the 2nd round of the play-off or
   * play-out competition).
   */
    public int getRound() {
        return d_nRound;
    }

    /**
   * Returns the season this match takes place in.
   */
    public VHSeason getSeason() {
        return d_season;
    }

    /**
   * If this match belongs to a contest phase played in KO-style, the value returned by
   * this method indicates which match of the KO round this match is (e.g. in a
   * best-of-seven play-off the value can be anything from 1 to 7). If this match belongs
   * to a round robin style contest phase, it always returns 0 and has no particular
   * relevance.
   */
    public int getSubround() {
        return d_nSubRound;
    }

    /**
   * Returns the winning club or <code>null</code> if the match has not yet been played.
   * 
   * @return the winning club or <code>null</code>
   */
    public VHClub getWinner() {
        if (d_nGoalsHome == d_nGoalsAway) return null;
        if (d_nGoalsHome > d_nGoalsAway) return d_clubHome; else return d_clubAway;
    }

    /**
   * Returns <code>true</code> if this match is finished (i.e. has already been played),
   * otherwise <code>false</code> is returned.
   */
    public boolean isPlayed() {
        return d_fPlayed;
    }

    /**
   * Returns <code>true</code> if the match has been played and the result has been
   * obtained by playing a shootout session. If the match has been decided without
   * shootout or is not even finished, <code>false</code> is returned.
   */
    public boolean hasShootout() {
        return isPlayed() && (d_nGoalsShootoutHome > 0 || d_nGoalsShootoutAway > 0);
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return d_nId == ((VHMatch) obj).d_nId;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return d_nId;
    }

    /**
   * Returns the match id followed by the names of the two clubs affronting each other.
   * The home team is mentionned first.
   * 
   * @see java.lang.Object#toString()
   */
    @Override
    public String toString() {
        return "(" + d_nId + ") " + d_clubHome.getName() + " - " + d_clubAway.getName();
    }
}
