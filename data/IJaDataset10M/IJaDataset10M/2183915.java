package com.azirar.dna.entities;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the t_application_contributeur database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "selectApplicationContributeurById", query = "select appCont from ApplicationContributeur appCont where appCont.application.idApplication = :idApplication and appCont.contributeur.idContributeur = :idContributeur"), @NamedQuery(name = "selectAllApplicationContributeurByIdApplication", query = "select appCont from ApplicationContributeur appCont where appCont.application.idApplication = :idApplication"), @NamedQuery(name = "selectAllApplicationContributeurByIdContributeur", query = "select appCont from ApplicationContributeur appCont where appCont.contributeur.idContributeur = :idContributeur"), @NamedQuery(name = "selectAllApplicationContributeur", query = "select appCont from ApplicationContributeur appCont") })
@Table(name = "dna_application_contributeur")
public class ApplicationContributeur implements Serializable {

    /** id application contributeur. */
    @Id
    @Column(nullable = false, columnDefinition = "INTEGER", length = 9)
    @GeneratedValue(generator = "inc-gen")
    @GenericGenerator(name = "inc-gen", strategy = "increment")
    private int idApplicationContributeur;

    /** application. */
    @ManyToOne
    @JoinColumn(name = "idApplication")
    private Application application;

    /** contributeur. */
    @ManyToOne
    @JoinColumn(name = "idContributeur")
    private Contributeur contributeur;

    /**
     * Constructeur t application contributeur.
     */
    public ApplicationContributeur() {
    }

    /**
	 * Retourne : id application contributeur.
	 *
	 * @return the id application contributeur
	 */
    public int getIdApplicationContributeur() {
        return idApplicationContributeur;
    }

    /**
	 * Positionne : id application contributeur.
	 *
	 * @param idApplicationContributeur the new id application contributeur
	 */
    public void setIdApplicationContributeur(int idApplicationContributeur) {
        this.idApplicationContributeur = idApplicationContributeur;
    }

    /**
	 * Retourne : application.
	 *
	 * @return the application
	 */
    public Application getApplication() {
        return application;
    }

    /**
	 * Positionne : application.
	 *
	 * @param application the new application
	 */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
	 * Retourne : contributeur.
	 *
	 * @return the contributeur
	 */
    public Contributeur getContributeur() {
        return contributeur;
    }

    /**
	 * Positionne : contributeur.
	 *
	 * @param contributeur the new contributeur
	 */
    public void setContributeur(Contributeur contributeur) {
        this.contributeur = contributeur;
    }
}
