package com.azirar.dna.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the tr_type_application database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "selectTypeApplicationById", query = "select typeApp from TypeApplication typeApp where typeApp.idTypeApplication = :idTypeApplication"), @NamedQuery(name = "selectTypeApplicationByName", query = "select typeApp from TypeApplication typeApp where typeApp.nom = :nom"), @NamedQuery(name = "selectAllTypeApplications", query = "select typeApp from TypeApplication typeApp") })
@Table(name = "dna_type_application")
public class TypeApplication implements Serializable {

    /** Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** id type application. */
    @Id
    @Column(nullable = false, columnDefinition = "INTEGER", length = 9)
    @GeneratedValue(generator = "inc-gen")
    @GenericGenerator(name = "inc-gen", strategy = "increment")
    private int idTypeApplication;

    /** description. */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /** nom. */
    private String nom;

    /** The color. */
    private String color;

    /**
     * Constructeur tr type application.
     */
    public TypeApplication() {
    }

    /**
	 * Retourne : id type application.
	 *
	 * @return the id type application
	 */
    public int getIdTypeApplication() {
        return this.idTypeApplication;
    }

    /**
	 * Positionne : id type application.
	 *
	 * @param idTypeApplication the new id type application
	 */
    public void setIdTypeApplication(int idTypeApplication) {
        this.idTypeApplication = idTypeApplication;
    }

    /**
	 * Retourne : description.
	 *
	 * @return the description
	 */
    public String getDescription() {
        return this.description;
    }

    /**
	 * Positionne : description.
	 *
	 * @param description the new description
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * Retourne : nom.
	 *
	 * @return the nom
	 */
    public String getNom() {
        return this.nom;
    }

    /**
	 * Positionne : nom.
	 *
	 * @param nom the new nom
	 */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
	 * Gets the color.
	 *
	 * @return the color
	 */
    public String getColor() {
        return color;
    }

    /**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
    public void setColor(String color) {
        this.color = color;
    }
}
