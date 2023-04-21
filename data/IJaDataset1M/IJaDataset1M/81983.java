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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the season database table.
 * 
 */
@Entity
@Table(name = "season")
public class Season implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "season_end", nullable = false)
    private Date seasonEnd;

    @Column(name = "season_name", nullable = false, length = 255)
    private String seasonName;

    @Temporal(TemporalType.DATE)
    @Column(name = "season_start", nullable = false)
    private Date seasonStart;

    @Column(nullable = false, length = 1)
    private String type;

    @OneToMany(mappedBy = "season")
    private Set<Game> games;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    public Season() {
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

    public Date getSeasonEnd() {
        return this.seasonEnd;
    }

    public void setSeasonEnd(Date seasonEnd) {
        this.seasonEnd = seasonEnd;
    }

    public String getSeasonName() {
        return this.seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public Date getSeasonStart() {
        return this.seasonStart;
    }

    public void setSeasonStart(Date seasonStart) {
        this.seasonStart = seasonStart;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Game> getGames() {
        return this.games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public League getLeague() {
        return this.league;
    }

    public void setLeague(League league) {
        this.league = league;
    }
}
