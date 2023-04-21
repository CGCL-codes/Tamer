package com.jcertif.web.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 
 * @author Mamadou
 *
 */
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    private Calendar dateDebut;

    private Calendar dateFin;

    private String description;

    private Long id;

    private String motCle;

    private String nom;

    private String salle;

    private String sommaire;

    private Long speakersId;

    private String sujets;

    public Event() {
        super();
    }

    public Event(Long id, Calendar dateDebut, Calendar dateFin, String description, String motCle, String nom, String salle, String sommaire, Long speakersId, String sujets) {
        super();
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.motCle = motCle;
        this.nom = nom;
        this.salle = salle;
        this.sommaire = sommaire;
        this.speakersId = speakersId;
        this.sujets = sujets;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateDebut == null) ? 0 : dateDebut.hashCode());
        result = prime * result + ((dateFin == null) ? 0 : dateFin.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((motCle == null) ? 0 : motCle.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        result = prime * result + ((salle == null) ? 0 : salle.hashCode());
        result = prime * result + ((sommaire == null) ? 0 : sommaire.hashCode());
        result = prime * result + ((speakersId == null) ? 0 : speakersId.hashCode());
        result = prime * result + ((sujets == null) ? 0 : sujets.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Event other = (Event) obj;
        if (dateDebut == null) {
            if (other.dateDebut != null) return false;
        } else if (!dateDebut.equals(other.dateDebut)) return false;
        if (dateFin == null) {
            if (other.dateFin != null) return false;
        } else if (!dateFin.equals(other.dateFin)) return false;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (motCle == null) {
            if (other.motCle != null) return false;
        } else if (!motCle.equals(other.motCle)) return false;
        if (nom == null) {
            if (other.nom != null) return false;
        } else if (!nom.equals(other.nom)) return false;
        if (salle == null) {
            if (other.salle != null) return false;
        } else if (!salle.equals(other.salle)) return false;
        if (sommaire == null) {
            if (other.sommaire != null) return false;
        } else if (!sommaire.equals(other.sommaire)) return false;
        if (speakersId == null) {
            if (other.speakersId != null) return false;
        } else if (!speakersId.equals(other.speakersId)) return false;
        if (sujets == null) {
            if (other.sujets != null) return false;
        } else if (!sujets.equals(other.sujets)) return false;
        return true;
    }

    /**
	 * @return the dateDebut
	 */
    public Calendar getDateDebut() {
        return dateDebut;
    }

    /**
	 * @param dateDebut the dateDebut to set
	 */
    public void setDateDebut(Calendar dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
	 * @return the dateFin
	 */
    public Calendar getDateFin() {
        return dateFin;
    }

    /**
	 * @param dateFin the dateFin to set
	 */
    public void setDateFin(Calendar dateFin) {
        this.dateFin = dateFin;
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return the motCle
	 */
    public String getMotCle() {
        return motCle;
    }

    /**
	 * @param motCle the motCle to set
	 */
    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    /**
	 * @return the nom
	 */
    public String getNom() {
        return nom;
    }

    /**
	 * @param nom the nom to set
	 */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
	 * @return the salle
	 */
    public String getSalle() {
        return salle;
    }

    /**
	 * @param salle the salle to set
	 */
    public void setSalle(String salle) {
        this.salle = salle;
    }

    /**
	 * @return the sommaire
	 */
    public String getSommaire() {
        return sommaire;
    }

    /**
	 * @param sommaire the sommaire to set
	 */
    public void setSommaire(String sommaire) {
        this.sommaire = sommaire;
    }

    /**
	 * @return the speakersId
	 */
    public Long getSpeakersId() {
        return speakersId;
    }

    /**
	 * @param speakersId the speakersId to set
	 */
    public void setSpeakersId(Long speakersId) {
        this.speakersId = speakersId;
    }

    /**
	 * @return the sujets
	 */
    public String getSujets() {
        return sujets;
    }

    /**
	 * @param sujets the sujets to set
	 */
    public void setSujets(String sujets) {
        this.sujets = sujets;
    }
}
