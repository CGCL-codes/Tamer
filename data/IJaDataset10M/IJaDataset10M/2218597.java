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

/**
 * The persistent class for the league database table.
 * 
 */
@Entity
@Table(name = "league")
public class League implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Lob()
    private String description;

    @Column(name = "league_name", nullable = false, length = 255)
    private String leagueName;

    @Column(length = 64)
    private String phone;

    @Column(nullable = false, length = 1)
    private String type;

    @OneToMany(mappedBy = "league")
    private Set<Announcement> announcements;

    @OneToMany(mappedBy = "league")
    private Set<Field> fields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lr_user_id", nullable = false)
    private Uzer uzer;

    @OneToMany(mappedBy = "league")
    private Set<Season> seasons;

    @OneToMany(mappedBy = "league")
    private Set<Team> teams;

    public League() {
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

    public String getLeagueName() {
        return this.leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Announcement> getAnnouncements() {
        return this.announcements;
    }

    public void setAnnouncements(Set<Announcement> announcements) {
        this.announcements = announcements;
    }

    public Set<Field> getFields() {
        return this.fields;
    }

    public void setFields(Set<Field> fields) {
        this.fields = fields;
    }

    public Uzer getUzer() {
        return this.uzer;
    }

    public void setUzer(Uzer uzer) {
        this.uzer = uzer;
    }

    public Set<Season> getSeasons() {
        return this.seasons;
    }

    public void setSeasons(Set<Season> seasons) {
        this.seasons = seasons;
    }

    public Set<Team> getTeams() {
        return this.teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
}
