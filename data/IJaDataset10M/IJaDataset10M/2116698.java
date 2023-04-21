package com.skillworld.webapp.model.team;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TeamRanking")
public class TeamRanking {

    private Long idRankingT;

    private Date date;

    private Team team1;

    private Team team2;

    private Team team3;

    private Team team4;

    private Team team5;

    public TeamRanking() {
    }

    @SequenceGenerator(name = "TeamRankingIdGenerator", sequenceName = "TeamRankingSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TeamRankingIdGenerator")
    @Column(name = "idRankingT")
    public long getIdRankingT() {
        return idRankingT;
    }

    public void setIdRankingT(long idRankingT) {
        this.idRankingT = idRankingT;
    }

    @Id
    @Column(name = "idDepartment")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idDepartment")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Id
    @Column(name = "idTeam1")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTeam")
    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    @Id
    @Column(name = "idTeam2")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTeam")
    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    @Id
    @Column(name = "idTeam3")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTeam")
    public Team getTeam3() {
        return team3;
    }

    public void setTeam3(Team team3) {
        this.team3 = team3;
    }

    @Id
    @Column(name = "idTeam4")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTeam")
    public Team getTeam4() {
        return team4;
    }

    public void setTeam4(Team team4) {
        this.team4 = team4;
    }

    @Id
    @Column(name = "idTeam5")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTeam")
    public Team getTeam5() {
        return team5;
    }

    public void setTeam5(Team team5) {
        this.team5 = team5;
    }
}
