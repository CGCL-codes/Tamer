package fr.gouv.defense.terre.esat.formathlon.entity;

import fr.gouv.defense.terre.esat.formathlon.entity.validation.annotation.Length;
import fr.gouv.defense.terre.esat.formathlon.entity.validation.annotation.NoSpaceBeforeAfter;
import fr.gouv.defense.terre.esat.formathlon.entity.validation.annotation.NotEmpty;
import fr.gouv.defense.terre.esat.formathlon.persistence.param.JpaRequestNames;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entité CelluleCours correspondant à un cours de l'ETRS.
 * Exemple : DSI, BENS, BAC ...
 * 
 * @author maxime.guinchard
 * @version 1.0
 */
@NamedQueries({ @NamedQuery(name = JpaRequestNames.LISTER_SALLE_TRIEE_PAR_NOM, query = "select object(o) from Salle as o order by o.nom") })
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Salle implements Serializable {

    /**
     * Utilise pour la serialisation.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Permet de gerer la concurrence d'acces avec JPA2.
     */
    @Version
    private Integer version;

    /**
     * Identifiant technique.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Nom de la salle.
     */
    @NoSpaceBeforeAfter
    @NotEmpty
    @NotNull
    @Length(max = 20, min = 1)
    @Column(nullable = false, unique = true, length = 20)
    private String nom;

    /**
     * Nombre de places de la salle.
     */
    @NotNull
    @Min(0)
    @Max(30)
    @Column(nullable = false)
    private Integer nbPlaces;

    @OneToMany(mappedBy = "salle", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    private List<Seance> lstSeances = new ArrayList<Seance>();

    /**
     * Constructeur par défaut.
     */
    public Salle() {
    }

    /**
     * Constructeur par défaut.
     * @param nom nom de la salle.
     * @param nbPlaces nombre de place de la salle.
     */
    public Salle(String nom, Integer nbPlaces) {
        this.nom = nom;
        this.nbPlaces = nbPlaces;
    }

    /**
     * 
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * 
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * 
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Salle other = (Salle) obj;
        if ((this.nom == null) ? (other.nom != null) : !this.nom.equals(other.nom)) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.nom != null ? this.nom.hashCode() : 0);
        return hash;
    }

    /**
     * 
     * @return
     */
    @Override
    public String toString() {
        return nom;
    }

    /**
     * 
     * @return
     */
    public Integer getNbPlaces() {
        return nbPlaces;
    }

    /**
     * 
     * @param nbPlaces
     */
    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    /**
     * 
     * @return
     */
    public List<Seance> getLstSeances() {
        return lstSeances;
    }

    /**
     * 
     * @param lstSeances
     */
    @XmlTransient
    public void setLstSeances(List<Seance> lstSeances) {
        this.lstSeances = lstSeances;
    }

    /**
     * 
     * @return
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
