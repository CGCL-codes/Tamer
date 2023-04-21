package fr.ua.iutlens.suivi.model.suivipro;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import fr.ua.iutlens.suivi.model.BaseEntity;
import fr.ua.iutlens.suivi.model.personne.Etudiant;

/**
 * Entity implementation class for Entity: ActivitePro
 * 
 */
@Entity
public class ActivitePro extends BaseEntity implements Serializable {

    @ManyToOne
    private Etudiant etudiant;

    @ManyToOne
    private Entreprise entreprise;

    @Column(name = "date_deb_activite")
    @Temporal(TemporalType.DATE)
    private Date dateDebActivite;

    @Column(name = "date_fin_activite")
    @Temporal(TemporalType.DATE)
    private Date dateFinActivite;

    @Column(name = "type_contrat", length = 100)
    private String typeContrat;

    @Column(length = 100)
    private String poste;

    @Column(name = "description_activite")
    private String descriptionActivite;

    @Column(name = "commentaire_activite")
    private String commentaireActivite;

    private static final long serialVersionUID = 1L;

    public ActivitePro() {
        super();
    }

    public Date getDateDebActivite() {
        return this.dateDebActivite;
    }

    public void setDateDebActivite(Date dateDebActivite) {
        this.dateDebActivite = dateDebActivite;
    }

    public Date getDateFinActivite() {
        return this.dateFinActivite;
    }

    public void setDateFinActivite(Date dateFinActivite) {
        this.dateFinActivite = dateFinActivite;
    }

    public String getTypeContrat() {
        return this.typeContrat;
    }

    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
    }

    public String getPoste() {
        return this.poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getDescriptionActivite() {
        return this.descriptionActivite;
    }

    public void setDescriptionActivite(String descriptionActivite) {
        this.descriptionActivite = descriptionActivite;
    }

    public String getCommentaireActivite() {
        return this.commentaireActivite;
    }

    public void setCommentaireActivite(String commentaire_activite) {
        this.commentaireActivite = commentaire_activite;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    @Override
    public String getDisplayText() {
        return etudiant.getNomPersonne() + "_" + entreprise.getNomEntreprise();
    }
}
