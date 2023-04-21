package org.petank.server.model;

import java.io.Serializable;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Match implements Serializable {

    private static final long serialVersionUID = -8984845986760026206L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    public Long id;

    @Persistent
    public Long idGroup;

    @Persistent
    public Long idPlace;

    /**
	 * tous les joueurs de l'équipe 1 concaténé
	 */
    @Persistent
    public String player1;

    /**
	 * tous les joueurs de l'équipe 1 concaténé
	 */
    @Persistent
    public String player2;

    @Persistent
    public Float score1;

    @Persistent
    public Float score2;

    @Persistent
    public Date jour;

    @Persistent
    public Float point1;

    @Persistent
    public Float point2;

    /**
	 * tous les joueurs de l'équipe concaténé avec leurs points
	 */
    @Persistent
    public String playersWithPoints;

    /**
	 * normal / anormal
	 */
    @Persistent
    public TypeVictoire typeVictoire;

    /**
	 * officiel / non officiel
	 */
    @Persistent
    public TypeMatch typeMatch;

    @Persistent
    public Long idBareme;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Float getScore1() {
        return score1;
    }

    public void setScore1(Float score1) {
        this.score1 = score1;
    }

    public Float getScore2() {
        return score2;
    }

    public void setScore2(Float score2) {
        this.score2 = score2;
    }

    public Date getJour() {
        return jour;
    }

    public void setJour(Date jour) {
        this.jour = jour;
    }

    public Float getPoint1() {
        return point1;
    }

    public void setPoint1(Float point1) {
        this.point1 = point1;
    }

    public Float getPoint2() {
        return point2;
    }

    public void setPoint2(Float point2) {
        this.point2 = point2;
    }

    public String getPlayersWithPoints() {
        return playersWithPoints;
    }

    public void setPlayersWithPoints(String playersWithPoints) {
        this.playersWithPoints = playersWithPoints;
    }

    public TypeVictoire getTypeVictoire() {
        return typeVictoire;
    }

    public void setTypeVictoire(TypeVictoire typeVictoire) {
        this.typeVictoire = typeVictoire;
    }

    public TypeMatch getTypeMatch() {
        return typeMatch;
    }

    public void setTypeMatch(TypeMatch typeMatch) {
        this.typeMatch = typeMatch;
    }

    public boolean isOfficiel() {
        return this.typeMatch.equals(TypeMatch.OFFICIEL);
    }

    public boolean isNormal() {
        return this.typeVictoire.equals(TypeVictoire.NORMAL);
    }

    public Long getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Long idGroup) {
        this.idGroup = idGroup;
    }

    public Long getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(Long idPlace) {
        this.idPlace = idPlace;
    }

    public Long getIdBareme() {
        return idBareme;
    }

    public void setIdBareme(Long idBareme) {
        this.idBareme = idBareme;
    }
}
