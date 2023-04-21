package com.shudes.pt.pojo;

import java.io.*;
import java.util.*;
import com.shudes.util.*;

public class PlayerWinnings implements Serializable {

    private PlayerWinningsId id;

    private Date datePlayed;

    private GameLevel gameLevel;

    private PokerSite pokerSite;

    private Player player;

    private PTSession session;

    private Player opponent;

    private Double wonFrom;

    private Integer timesBeat;

    private Player realPlayer;

    private Player realOpponent;

    public String toString() {
        return Dumper.INSTANCE.dump(this);
    }

    public Date getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(Date datePlayed) {
        this.datePlayed = datePlayed;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
    }

    public PlayerWinningsId getId() {
        return id;
    }

    public void setId(PlayerWinningsId id) {
        this.id = id;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PokerSite getPokerSite() {
        return pokerSite;
    }

    public void setPokerSite(PokerSite pokerSite) {
        this.pokerSite = pokerSite;
    }

    public Player getRealOpponent() {
        return realOpponent;
    }

    public void setRealOpponent(Player realOpponent) {
        this.realOpponent = realOpponent;
    }

    public Player getRealPlayer() {
        return realPlayer;
    }

    public void setRealPlayer(Player realPlayer) {
        this.realPlayer = realPlayer;
    }

    public PTSession getSession() {
        return session;
    }

    public void setSession(PTSession session) {
        this.session = session;
    }

    public Integer getTimesBeat() {
        return timesBeat;
    }

    public void setTimesBeat(Integer timesBeat) {
        this.timesBeat = timesBeat;
    }

    public Double getWonFrom() {
        return wonFrom;
    }

    public void setWonFrom(Double wonFrom) {
        this.wonFrom = wonFrom;
    }
}
