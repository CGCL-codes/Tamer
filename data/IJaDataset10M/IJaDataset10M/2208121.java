package com.hack23.cia.model.impl.sweden;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.hack23.cia.model.api.common.ResourceType;
import com.hack23.cia.model.api.sweden.ParliamentMetaData;
import com.hack23.cia.model.impl.common.BaseEntity;

/**
 * The Class VoteMetaData.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VoteMetaData extends BaseEntity implements ParliamentMetaData {

    /**
	 * The Enum Outcome.
	 */
    public enum Outcome {

        /** The Lost. */
        Lost, /** The Won. */
        Won
    }

    /**
	 * The Enum PoliticalPartyBehavior.
	 */
    public enum PoliticalPartyBehavior {

        /** The Loyal. */
        Loyal, /** The Neutral. */
        Neutral, /** The Rebel. */
        Rebel
    }

    /**
     * The Enum ProffessionalBehavior.
     */
    public enum ProffessionalBehavior {

        /** The Absent. */
        Absent, /** The Present. */
        Present
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private Long id;

    /** The outcome. */
    private Outcome outcome;

    /** The political party behavior. */
    private PoliticalPartyBehavior politicalPartyBehavior;

    /** The proffessional behavior. */
    private ProffessionalBehavior proffessionalBehavior;

    /** The resource type. */
    private ResourceType resourceType = ResourceType.ApplicationData;

    /** The version. */
    private Long version = 1L;

    /**
     * Instantiates a new vote meta data.
     */
    public VoteMetaData() {
        super();
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     * Gets the outcome.
     * 
     * @return the outcome
     */
    @Enumerated(EnumType.STRING)
    public Outcome getOutcome() {
        return outcome;
    }

    /**
     * Gets the political party behavior.
     * 
     * @return the political party behavior
     */
    @Enumerated(EnumType.STRING)
    public PoliticalPartyBehavior getPoliticalPartyBehavior() {
        return politicalPartyBehavior;
    }

    /**
     * Gets the proffessional behavior.
     * 
     * @return the proffessional behavior
     */
    @Enumerated(EnumType.STRING)
    public ProffessionalBehavior getProffessionalBehavior() {
        return proffessionalBehavior;
    }

    @Override
    @Enumerated(EnumType.STRING)
    public ResourceType getResourceType() {
        return this.resourceType;
    }

    @Override
    @Version
    public Long getVersion() {
        return version;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    /**
	 * Sets the outcome.
	 * 
	 * @param outcome the new outcome
	 */
    public void setOutcome(final Outcome outcome) {
        this.outcome = outcome;
    }

    /**
     * Sets the political party behavior.
     * 
     * @param politicalPartyBehavior the new political party behavior
     */
    public void setPoliticalPartyBehavior(final PoliticalPartyBehavior politicalPartyBehavior) {
        this.politicalPartyBehavior = politicalPartyBehavior;
    }

    /**
	 * Sets the proffessional behavior.
	 * 
	 * @param proffessionalBehavior the new proffessional behavior
	 */
    public void setProffessionalBehavior(final ProffessionalBehavior proffessionalBehavior) {
        this.proffessionalBehavior = proffessionalBehavior;
    }

    public void setResourceType(final ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }
}
