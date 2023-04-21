package com.leagueplatform.backend.server.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the game database table.
 * 
 */
@Entity
@Table(name = "game")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Lob()
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "game_time", nullable = false)
    private Date gameTime;

    @Column(length = 1)
    private String status;

    @Column(name = "team_home_score")
    private int teamHomeScore;

    @Column(name = "team_visitor_score")
    private int teamVisitorScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_field_id", nullable = false)
    private Field field;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_home_id", nullable = false)
    private Team team1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_visitor_id", nullable = false)
    private Team team2;

    @OneToMany(mappedBy = "game")
    private Set<PlayerGameResult> playerGameResults;

    public Game() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getGameTime() {
        return this.gameTime;
    }

    public void setGameTime(Date gameTime) {
        this.gameTime = gameTime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTeamHomeScore() {
        return this.teamHomeScore;
    }

    public void setTeamHomeScore(int teamHomeScore) {
        this.teamHomeScore = teamHomeScore;
    }

    public int getTeamVisitorScore() {
        return this.teamVisitorScore;
    }

    public void setTeamVisitorScore(int teamVisitorScore) {
        this.teamVisitorScore = teamVisitorScore;
    }

    public Field getField() {
        return this.field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Season getSeason() {
        return this.season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Team getTeam1() {
        return this.team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return this.team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public Set<PlayerGameResult> getPlayerGameResults() {
        return this.playerGameResults;
    }

    public void setPlayerGameResults(Set<PlayerGameResult> playerGameResults) {
        this.playerGameResults = playerGameResults;
    }
}
