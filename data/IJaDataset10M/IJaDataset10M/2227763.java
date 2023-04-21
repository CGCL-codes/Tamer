package kernel;

import java.util.Date;
import net.ko.kobject.KObject;

public class KProjet extends KObject {

    private String nom;

    private Date dateCreation;

    private int idUtilisateur;

    private KUtilisateur utilisateur;

    public KProjet() {
        super();
        keyFields = "id";
        utilisateur = new KUtilisateur();
        belongsTo("utilisateur");
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "KProjet [nom=" + nom + ", utilisateur=" + utilisateur.getNom() + "]";
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUser) {
        this.idUtilisateur = idUser;
    }
}
