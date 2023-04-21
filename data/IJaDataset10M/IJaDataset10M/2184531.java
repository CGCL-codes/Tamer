package net.mlw.fball.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.6 $ $Date: 2004/04/01 21:51:07 $
 */
public class Stats implements Serializable {

    private static final Integer INTEGER_ZERO = new Integer(0);

    private static final Double DOUBLE_ZERO = new Double(0);

    private Player player;

    private Team team;

    private String position;

    private Integer season = new Integer(2003);

    private Double quarterBackRating = DOUBLE_ZERO;

    private Integer gamesPlayed = INTEGER_ZERO;

    private Integer passingCompletion = INTEGER_ZERO;

    private Integer passingAttempts = INTEGER_ZERO;

    private Integer passingYards = INTEGER_ZERO;

    private Integer passingTouchdowns = INTEGER_ZERO;

    private Integer passingSacked = INTEGER_ZERO;

    private Integer passingSackedYards = INTEGER_ZERO;

    private Integer passingInterception = INTEGER_ZERO;

    private Integer rushingAttempts = INTEGER_ZERO;

    private Integer rushingYards = INTEGER_ZERO;

    private Integer rushingTouchdowns = INTEGER_ZERO;

    private Integer fumbles = INTEGER_ZERO;

    private Integer funblesLost = INTEGER_ZERO;

    private Integer receivingRecptions = INTEGER_ZERO;

    private Integer receivingYards = INTEGER_ZERO;

    private Integer receivingLong = INTEGER_ZERO;

    private Integer receivingFirstdowns = INTEGER_ZERO;

    private Integer receivingTouchdown = INTEGER_ZERO;

    private Integer kickReturnReturns = INTEGER_ZERO;

    private Integer kickReturnYards = INTEGER_ZERO;

    private Integer kickReturnLong = INTEGER_ZERO;

    private Integer puntReturnReturns = INTEGER_ZERO;

    private Integer puntReturnYards = INTEGER_ZERO;

    private Integer puntReturnLong = INTEGER_ZERO;

    private Integer extraPointAttempt = INTEGER_ZERO;

    private Integer extraPointMade = INTEGER_ZERO;

    private Integer fieldGoalAttempt = INTEGER_ZERO;

    private Integer fieldGoalMade = INTEGER_ZERO;

    private Integer fieldGoalLong = INTEGER_ZERO;

    private Integer fieldGoalRange0To19 = INTEGER_ZERO;

    private Integer fieldGoalRange20To29 = INTEGER_ZERO;

    private Integer fieldGoalRange30To39 = INTEGER_ZERO;

    private Integer fieldGoalRange40To49 = INTEGER_ZERO;

    private Integer fieldGoalRange50Plus = INTEGER_ZERO;

    /**
    */
    protected Stats() {
    }

    /**
    * @return Returns the fumbles.
    */
    public Integer getFumbles() {
        return fumbles;
    }

    /**
    * @param fumbles The fumbles to set.
    */
    public void setFumbles(Integer fumbles) {
        this.fumbles = fumbles;
    }

    /**
    * @return Returns the funblesLost.
    */
    public Integer getFunblesLost() {
        return funblesLost;
    }

    /**
    * @param funblesLost The funblesLost to set.
    */
    public void setFunblesLost(Integer funblesLost) {
        this.funblesLost = funblesLost;
    }

    /**
    * @return Returns the gamesPlayed.
    */
    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    /**
    * @param gamesPlayed The gamesPlayed to set.
    */
    public void setGamesPlayed(Integer gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    /**
    * @return Returns the kickReturnLong.
    */
    public Integer getKickReturnLong() {
        return kickReturnLong;
    }

    /**
    * @param kickReturnLong The kickReturnLong to set.
    */
    public void setKickReturnLong(Integer kickReturnLong) {
        this.kickReturnLong = kickReturnLong;
    }

    /**
    * @return Returns the kickReturnReturns.
    */
    public Integer getKickReturnReturns() {
        return kickReturnReturns;
    }

    /**
    * @param kickReturnReturns The kickReturnReturns to set.
    */
    public void setKickReturnReturns(Integer kickReturnReturns) {
        this.kickReturnReturns = kickReturnReturns;
    }

    /**
    * @return Returns the kickReturnYards.
    */
    public Integer getKickReturnYards() {
        return kickReturnYards;
    }

    /**
    * @param kickReturnYards The kickReturnYards to set.
    */
    public void setKickReturnYards(Integer kickReturnYards) {
        this.kickReturnYards = kickReturnYards;
    }

    /**
    * @return Returns the passingAttempts.
    */
    public Integer getPassingAttempts() {
        return passingAttempts;
    }

    /**
    * @param passingAttempts The passingAttempts to set.
    */
    public void setPassingAttempts(Integer passingAttempts) {
        this.passingAttempts = passingAttempts;
    }

    /**
    * @return Returns the passingCompletion.
    */
    public Integer getPassingCompletion() {
        return passingCompletion;
    }

    /**
    * @param passingCompletion The passingCompletion to set.
    */
    public void setPassingCompletion(Integer passingCompletion) {
        this.passingCompletion = passingCompletion;
    }

    /**
    * @return Returns the passingSacked.
    */
    public Integer getPassingSacked() {
        return passingSacked;
    }

    /**
    * @param passingSacked The passingSacked to set.
    */
    public void setPassingSacked(Integer passingSacked) {
        this.passingSacked = passingSacked;
    }

    /**
    * @return Returns the passingSackedYards.
    */
    public Integer getPassingSackedYards() {
        return passingSackedYards;
    }

    /**
    * @param passingSackedYards The passingSackedYards to set.
    */
    public void setPassingSackedYards(Integer passingSackedYards) {
        this.passingSackedYards = passingSackedYards;
    }

    /**
    * @return Returns the passingTouchdowns.
    */
    public Integer getPassingTouchdowns() {
        return passingTouchdowns;
    }

    /**
    * @param passingTouchdowns The passingTouchdowns to set.
    */
    public void setPassingTouchdowns(Integer passingTouchdowns) {
        this.passingTouchdowns = passingTouchdowns;
    }

    /**
    * @return Returns the passingYards.
    */
    public Integer getPassingYards() {
        return passingYards;
    }

    /**
    * @param passingYards The passingYards to set.
    */
    public void setPassingYards(Integer passingYards) {
        this.passingYards = passingYards;
    }

    /**
    * @return Returns the player.
    */
    public Player getPlayer() {
        return player;
    }

    /**
    * @param player The player to set.
    */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
    * @return Returns the puntReturnLong.
    */
    public Integer getPuntReturnLong() {
        return puntReturnLong;
    }

    /**
    * @param puntReturnLong The puntReturnLong to set.
    */
    public void setPuntReturnLong(Integer puntReturnLong) {
        this.puntReturnLong = puntReturnLong;
    }

    /**
    * @return Returns the puntReturnReturns.
    */
    public Integer getPuntReturnReturns() {
        return puntReturnReturns;
    }

    /**
    * @param puntReturnReturns The puntReturnReturns to set.
    */
    public void setPuntReturnReturns(Integer puntReturnReturns) {
        this.puntReturnReturns = puntReturnReturns;
    }

    /**
    * @return Returns the puntReturnYards.
    */
    public Integer getPuntReturnYards() {
        return puntReturnYards;
    }

    /**
    * @param puntReturnYards The puntReturnYards to set.
    */
    public void setPuntReturnYards(Integer puntReturnYards) {
        this.puntReturnYards = puntReturnYards;
    }

    /**
    * @return Returns the receivingFirstdowns.
    */
    public Integer getReceivingFirstdowns() {
        return receivingFirstdowns;
    }

    /**
    * @param receivingFirstdowns The receivingFirstdowns to set.
    */
    public void setReceivingFirstdowns(Integer receivingFirstdowns) {
        this.receivingFirstdowns = receivingFirstdowns;
    }

    /**
    * @return Returns the receivingLong.
    */
    public Integer getReceivingLong() {
        return receivingLong;
    }

    /**
    * @param receivingLong The receivingLong to set.
    */
    public void setReceivingLong(Integer receivingLong) {
        this.receivingLong = receivingLong;
    }

    /**
    * @return Returns the receivingRecptions.
    */
    public Integer getReceivingRecptions() {
        return receivingRecptions;
    }

    /**
    * @param receivingRecptions The receivingRecptions to set.
    */
    public void setReceivingRecptions(Integer receivingRecptions) {
        this.receivingRecptions = receivingRecptions;
    }

    /**
    * @return Returns the receivingTouchdown.
    */
    public Integer getReceivingTouchdown() {
        return receivingTouchdown;
    }

    /**
    * @param receivingTouchdown The receivingTouchdown to set.
    */
    public void setReceivingTouchdown(Integer receivingTouchdown) {
        this.receivingTouchdown = receivingTouchdown;
    }

    /**
    * @return Returns the receivingYards.
    */
    public Integer getReceivingYards() {
        return receivingYards;
    }

    /**
    * @param receivingYards The receivingYards to set.
    */
    public void setReceivingYards(Integer receivingYards) {
        this.receivingYards = receivingYards;
    }

    /**
    * @return Returns the rushingTouchdowns.
    */
    public Integer getRushingTouchdowns() {
        return rushingTouchdowns;
    }

    /**
    * @param rushingTouchdowns The rushingTouchdowns to set.
    */
    public void setRushingTouchdowns(Integer rushingTouchdowns) {
        this.rushingTouchdowns = rushingTouchdowns;
    }

    /**
    * @return Returns the rushingYards.
    */
    public Integer getRushingYards() {
        return rushingYards;
    }

    /**
    * @param rushingYards The rushingYards to set.
    */
    public void setRushingYards(Integer rushingYards) {
        this.rushingYards = rushingYards;
    }

    /**
    * @return Returns the team.
    */
    public Team getTeam() {
        return team;
    }

    /**
    * @param team The team to set.
    */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
    * @return Returns the season.
    */
    public Integer getSeason() {
        return season;
    }

    /**
    * @param season The season to set.
    */
    public void setSeason(Integer season) {
        this.season = season;
    }

    /**
    * @return Returns the passingInterception.
    */
    public Integer getPassingInterception() {
        return passingInterception;
    }

    /**
    * @param passingInterception The passingInterception to set.
    */
    public void setPassingInterception(Integer passingInterception) {
        this.passingInterception = passingInterception;
    }

    /**
   * @return Returns the position.
   */
    public String getPosition() {
        return position;
    }

    /**
   * @param position The position to set.
   */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
   * @return Returns the quarterBackRating.
   */
    public Double getQuarterBackRating() {
        return quarterBackRating;
    }

    /**
   * @param quarterBackRating The quarterBackRating to set.
   */
    public void setQuarterBackRating(Double quarterBackRating) {
        this.quarterBackRating = quarterBackRating;
    }

    /**
    * @return Returns the rushingAttempts.
    */
    public Integer getRushingAttempts() {
        return rushingAttempts;
    }

    /**
    * @param rushingAttempts The rushingAttempts to set.
    */
    public void setRushingAttempts(Integer rushingAttempts) {
        this.rushingAttempts = rushingAttempts;
    }

    /** @see java.lang.Object#toString()
    */
    public String toString() {
        return new ToStringBuilder(this).append("Season", season).append("Team", team).append("GamesPlayed", gamesPlayed).append("Season", season).append("ReceivingRecptions", receivingRecptions).append("ReceivingYards", receivingYards).append("ReceivingLong", receivingLong).append("ReceivingFirstdowns", receivingFirstdowns).append("ReceivingTouchdown", receivingTouchdown).append("KickReturnReturns", kickReturnReturns).append("KickReturnYards", kickReturnYards).append("KickReturnLong", kickReturnLong).append("PuntReturnReturns", puntReturnReturns).append("PuntReturnYards", puntReturnYards).append("PuntReturnLong", puntReturnLong).append("Fumbles", fumbles).append("FunblesLost", funblesLost).toString();
    }

    /** @see java.lang.Object#equals(java.lang.Object)
    */
    public boolean equals(Object arg0) {
        if (arg0 instanceof Stats) {
            Stats stats = (Stats) arg0;
            return (stats.getSeason().equals(season) && player.getId().equals(player.getId()));
        } else {
            return false;
        }
    }

    /** @see java.lang.Object#hashCode()
    */
    public int hashCode() {
        return new StringBuffer(player.getId()).append(season.toString()).hashCode();
    }

    /**
    * @return Returns the extraPointAttempt.
    */
    public Integer getExtraPointAttempt() {
        return extraPointAttempt;
    }

    /**
    * @param extraPointAttempt The extraPointAttempt to set.
    */
    public void setExtraPointAttempt(Integer extraPointAttempt) {
        this.extraPointAttempt = extraPointAttempt;
    }

    /**
    * @return Returns the extraPointMade.
    */
    public Integer getExtraPointMade() {
        return extraPointMade;
    }

    /**
    * @param extraPointMade The extraPointMade to set.
    */
    public void setExtraPointMade(Integer extraPointMade) {
        this.extraPointMade = extraPointMade;
    }

    /**
    * @return Returns the fieldGoalAttempt.
    */
    public Integer getFieldGoalAttempt() {
        return fieldGoalAttempt;
    }

    /**
    * @param fieldGoalAttempt The fieldGoalAttempt to set.
    */
    public void setFieldGoalAttempt(Integer fieldGoalAttempt) {
        this.fieldGoalAttempt = fieldGoalAttempt;
    }

    /**
    * @return Returns the fieldGoalLong.
    */
    public Integer getFieldGoalLong() {
        return fieldGoalLong;
    }

    /**
    * @param fieldGoalLong The fieldGoalLong to set.
    */
    public void setFieldGoalLong(Integer fieldGoalLong) {
        this.fieldGoalLong = fieldGoalLong;
    }

    /**
    * @return Returns the fieldGoalMade.
    */
    public Integer getFieldGoalMade() {
        return fieldGoalMade;
    }

    /**
    * @param fieldGoalMade The fieldGoalMade to set.
    */
    public void setFieldGoalMade(Integer fieldGoalMade) {
        this.fieldGoalMade = fieldGoalMade;
    }

    /**
    * @return Returns the fieldGoalRange0To19.
    */
    public Integer getFieldGoalRange0To19() {
        return fieldGoalRange0To19;
    }

    /**
    * @param fieldGoalRange0To19 The fieldGoalRange0To19 to set.
    */
    public void setFieldGoalRange0To19(Integer fieldGoalRange0To19) {
        this.fieldGoalRange0To19 = fieldGoalRange0To19;
    }

    /**
    * @return Returns the fieldGoalRange20To29.
    */
    public Integer getFieldGoalRange20To29() {
        return fieldGoalRange20To29;
    }

    /**
    * @param fieldGoalRange20To29 The fieldGoalRange20To29 to set.
    */
    public void setFieldGoalRange20To29(Integer fieldGoalRange20To29) {
        this.fieldGoalRange20To29 = fieldGoalRange20To29;
    }

    /**
    * @return Returns the fieldGoalRange30To39.
    */
    public Integer getFieldGoalRange30To39() {
        return fieldGoalRange30To39;
    }

    /**
    * @param fieldGoalRange30To39 The fieldGoalRange30To39 to set.
    */
    public void setFieldGoalRange30To39(Integer fieldGoalRange30To39) {
        this.fieldGoalRange30To39 = fieldGoalRange30To39;
    }

    /**
    * @return Returns the fieldGoalRange40To49.
    */
    public Integer getFieldGoalRange40To49() {
        return fieldGoalRange40To49;
    }

    /**
    * @param fieldGoalRange40To49 The fieldGoalRange40To49 to set.
    */
    public void setFieldGoalRange40To49(Integer fieldGoalRange40To49) {
        this.fieldGoalRange40To49 = fieldGoalRange40To49;
    }

    /**
    * @return Returns the fieldGoalRange50Plus.
    */
    public Integer getFieldGoalRange50Plus() {
        return fieldGoalRange50Plus;
    }

    /**
    * @param fieldGoalRange50Plus The fieldGoalRange50Plus to set.
    */
    public void setFieldGoalRange50Plus(Integer fieldGoalRange50Plus) {
        this.fieldGoalRange50Plus = fieldGoalRange50Plus;
    }
}
