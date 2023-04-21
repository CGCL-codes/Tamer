package com.hack23.cia.model.impl.sweden;

import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import com.hack23.cia.model.api.common.ResourceType;

/**
 * The Class ParliamentMemberBallotRecord.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DiscriminatorValue("ParliamentMemberBallotRecord")
public class ParliamentMemberBallotRecord extends AbstractBallotMetaData {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7843524383033778447L;

    /** The first vote date. */
    private Date firstVoteDate = new Date();

    /** The last vote date. */
    private Date lastVoteDate = new Date(0L);

    /** The opponent. */
    private long opponent = 0;

    /** The percentage absent. */
    private long percentageAbsent;

    /** The percentage loosing. */
    private long percentageLoosing;

    /** The percentage loyal. */
    private long percentageLoyal;

    /** The percentage present. */
    private long percentagePresent;

    /** The percentage rebel. */
    private long percentageRebel;

    /** The percentage winning. */
    private long percentageWinning;

    /** The rebel. */
    private long rebel = 0;

    /** The resource type. */
    private ResourceType resourceType = ResourceType.ApplicationData;

    /**
     * Instantiates a new parliament member ballot record.
     */
    public ParliamentMemberBallotRecord() {
        super();
    }

    /**
     * Gets the first vote date.
     * 
     * @return the first vote date
     */
    @Temporal(TemporalType.DATE)
    public Date getFirstVoteDate() {
        return firstVoteDate;
    }

    /**
     * Gets the last vote date.
     * 
     * @return the last vote date
     */
    @Temporal(TemporalType.DATE)
    public Date getLastVoteDate() {
        return lastVoteDate;
    }

    /**
     * Gets the loyal.
     * 
     * @return the loyal
     */
    @Transient
    public long getLoyal() {
        return (totalVotes - rebel);
    }

    /**
     * Gets the opponent.
     * 
     * @return the opponent
     */
    public long getOpponent() {
        return opponent;
    }

    /**
     * Gets the percentage absent.
     * 
     * @return the percentage absent
     */
    @Formula("absentVotes * 100 / totalVotes")
    public long getPercentageAbsent() {
        return percentageAbsent;
    }

    /**
     * Gets the percentage loosing.
     * 
     * @return the percentage loosing
     */
    @Formula("opponent * 100 / totalVotes")
    public long getPercentageLoosing() {
        return percentageLoosing;
    }

    /**
     * Gets the percentage loyal.
     * 
     * @return the percentage loyal
     */
    @Formula("(totalVotes -rebel)*100/totalVotes")
    public long getPercentageLoyal() {
        return percentageLoyal;
    }

    /**
     * Gets the percentage present.
     * 
     * @return the percentage present
     */
    @Formula("(totalVotes - absentVotes) * 100/ totalVotes")
    public long getPercentagePresent() {
        return percentagePresent;
    }

    /**
     * Gets the percentage rebel.
     * 
     * @return the percentage rebel
     */
    @Formula("(rebel * 100)/ totalVotes")
    public long getPercentageRebel() {
        return percentageRebel;
    }

    /**
     * Gets the percentage winning.
     * 
     * @return the percentage winning
     */
    @Formula("(totalVotes -opponent)* 100/ totalVotes")
    public long getPercentageWinning() {
        return percentageWinning;
    }

    /**
     * Gets the present.
     * 
     * @return the present
     */
    @Transient
    public long getPresent() {
        return (totalVotes - absentVotes);
    }

    /**
     * Gets the rebel.
     * 
     * @return the rebel
     */
    public long getRebel() {
        return rebel;
    }

    @Override
    @Enumerated(EnumType.STRING)
    public ResourceType getResourceType() {
        return this.resourceType;
    }

    /**
     * Gets the winning.
     * 
     * @return the winning
     */
    @Transient
    public long getWinning() {
        return (totalVotes - opponent);
    }

    @Override
    @Transient
    public void newVote(final Vote vote) {
        super.newVote(vote);
        if (vote.getDatum().before(getFirstVoteDate())) {
            setFirstVoteDate(vote.getDatum());
        }
        if (vote.getDatum().after(getLastVoteDate())) {
            setLastVoteDate(vote.getDatum());
        }
    }

    /**
     * Sets the first vote date.
     * 
     * @param firstVoteDate the new first vote date
     */
    public void setFirstVoteDate(final Date firstVoteDate) {
        this.firstVoteDate = firstVoteDate;
    }

    /**
     * Sets the last vote date.
     * 
     * @param lastVoteDate the new last vote date
     */
    public void setLastVoteDate(final Date lastVoteDate) {
        this.lastVoteDate = lastVoteDate;
    }

    /**
     * Sets the opponent.
     * 
     * @param opponent the new opponent
     */
    public void setOpponent(final long opponent) {
        this.opponent = opponent;
    }

    /**
     * Sets the percentage absent.
     * 
     * @param percentageAbsent the new percentage absent
     */
    public void setPercentageAbsent(final long percentageAbsent) {
        this.percentageAbsent = percentageAbsent;
    }

    /**
     * Sets the percentage loosing.
     * 
     * @param percentageLoosing the new percentage loosing
     */
    public void setPercentageLoosing(final long percentageLoosing) {
        this.percentageLoosing = percentageLoosing;
    }

    /**
     * Sets the percentage loyal.
     * 
     * @param percentageLoyal the new percentage loyal
     */
    public void setPercentageLoyal(final long percentageLoyal) {
        this.percentageLoyal = percentageLoyal;
    }

    /**
     * Sets the percentage present.
     * 
     * @param percentagePresent the new percentage present
     */
    public void setPercentagePresent(final long percentagePresent) {
        this.percentagePresent = percentagePresent;
    }

    /**
     * Sets the percentage rebel.
     * 
     * @param percentageRebel the new percentage rebel
     */
    public void setPercentageRebel(final long percentageRebel) {
        this.percentageRebel = percentageRebel;
    }

    /**
     * Sets the percentage winning.
     * 
     * @param percentageWinning the new percentage winning
     */
    public void setPercentageWinning(final long percentageWinning) {
        this.percentageWinning = percentageWinning;
    }

    /**
	 * Sets the rebel.
	 * 
	 * @param rebel the new rebel
	 */
    public void setRebel(final long rebel) {
        this.rebel = rebel;
    }

    public void setResourceType(final ResourceType resourceType) {
        this.resourceType = resourceType;
    }
}
