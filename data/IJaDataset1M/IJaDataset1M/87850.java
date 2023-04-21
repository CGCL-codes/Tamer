package com.tysanclan.site.projectewok.entities;

import javax.persistence.*;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class UntenabilityVoteChoice extends BaseDomainObject {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UntenabilityVoteChoice")
    @SequenceGenerator(name = "UntenabilityVoteChoice", sequenceName = "SEQ_ID_UntenabilityVoteChoice")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Index(name = "IDX_UntenabilityVoteChoice_Vote")
    private UntenabilityVote vote;

    @Column
    private boolean inFavor;

    @ManyToOne(fetch = FetchType.LAZY)
    @Index(name = "IDX_UntenabilityVote_Caster")
    private User caster;

    /**
	 * Creates a new UntenabilityVoteChoice object
	 */
    public UntenabilityVoteChoice() {
    }

    /**
	 * Returns the ID of this UntenabilityVoteChoice
	 */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
	 * Sets the ID of this UntenabilityVoteChoice
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return The Vote of this UntenabilityVoteChoice
	 */
    public UntenabilityVote getVote() {
        return this.vote;
    }

    /**
	 * Sets the Vote of this UntenabilityVoteChoice
	 * 
	 * @param vote
	 *            The Vote of this UntenabilityVoteChoice
	 */
    public void setVote(UntenabilityVote vote) {
        this.vote = vote;
    }

    /**
	 * @return The InFavor of this UntenabilityVoteChoice
	 */
    public boolean isInFavor() {
        return this.inFavor;
    }

    /**
	 * Sets the InFavor of this UntenabilityVoteChoice
	 * 
	 * @param inFavor
	 *            The InFavor of this UntenabilityVoteChoice
	 */
    public void setInFavor(boolean inFavor) {
        this.inFavor = inFavor;
    }

    /**
	 * @return The Caster of this UntenabilityVoteChoice
	 */
    public User getCaster() {
        return this.caster;
    }

    /**
	 * Sets the Caster of this UntenabilityVoteChoice
	 * 
	 * @param caster
	 *            The Caster of this UntenabilityVoteChoice
	 */
    public void setCaster(User caster) {
        this.caster = caster;
    }
}
