package org.SoccerTournament.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "userId")
public class Player extends User {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Team myTeam;

    private String position;

    public Long getId() {
        return id;
    }

    @Column(name = "position")
    public String getPosition() {
        return position;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    public Team getMyTeam() {
        return myTeam;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setMyTeam(Team myTeam) {
        this.myTeam = myTeam;
    }
}
