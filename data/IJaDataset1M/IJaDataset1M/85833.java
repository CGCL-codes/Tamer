package com.hack23.cia.model.impl.application.common;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.hack23.cia.model.api.application.ApplicationEnvironment;
import com.hack23.cia.model.impl.common.BaseEntity;

/**
 * The Class GameBoard.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GameBoard extends BaseEntity implements ApplicationEnvironment {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1381199844545595026L;

    /** The id. */
    private Long id;

    /** The players. */
    private Set<Player> players;

    /** The version. */
    private Long version = 1L;

    /**
     * Instantiates a new game board.
     */
    public GameBoard() {
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     * Gets the players.
     * 
     * @return the players
     */
    @Transient
    public Set<Player> getPlayers() {
        return players;
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
     * Sets the players.
     * 
     * @param players the new players
     */
    public void setPlayers(final Set<Player> players) {
        this.players = players;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }
}
