package com.appspot.spelstegen.client.entities;

import java.io.Serializable;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Class representing a set
 * 
 * @author Henrik Segesten
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Set implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Integer id;

    @Persistent
    private Sport sport;

    @Persistent
    private Integer player1Score;

    @Persistent
    private Integer player2Score;

    public Set() {
    }

    public Set(Sport sport, Integer player1Score, Integer player2Score) {
        this(null, sport, player1Score, player2Score);
    }

    public Set(Integer id, Sport sport, Integer player1Score, Integer player2Score) {
        super();
        this.id = id;
        this.sport = sport;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(Integer player1Score) {
        this.player1Score = player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(Integer player2Score) {
        this.player2Score = player2Score;
    }
}
