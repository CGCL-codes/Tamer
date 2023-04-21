package com.hack23.cia.model.impl.application.administration;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.hack23.cia.model.api.application.configuration.ApplicationEnvironment;
import com.hack23.cia.model.api.common.TypeContext;
import com.hack23.cia.model.impl.common.BaseEntity;

/**
 * The Class PoliticalGame.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PoliticalGame extends BaseEntity implements ApplicationEnvironment {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1381199844545595026L;

    /** The game boards. */
    private Set<GameBoard> gameBoards;

    /** The id. */
    private Long id;

    /** The name. */
    private String name;

    /** The version. */
    private Long version = 1L;

    /**
	 * Instantiates a new political game.
	 */
    public PoliticalGame() {
    }

    @Override
    @Transient
    protected TypeContext getApplicationTypeContext() {
        return TypeContext.Configuration;
    }

    /**
	 * Gets the game boards.
	 * 
	 * @return the game boards
	 */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "PoliticalGameBoards")
    public Set<GameBoard> getGameBoards() {
        return gameBoards;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Override
    @Version
    public Long getVersion() {
        return version;
    }

    /**
	 * Sets the game boards.
	 * 
	 * @param gameBoards
	 *            the new game boards
	 */
    public void setGameBoards(final Set<GameBoard> gameBoards) {
        this.gameBoards = gameBoards;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
