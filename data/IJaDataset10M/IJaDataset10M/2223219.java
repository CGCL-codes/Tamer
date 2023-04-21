package form;

import java.util.List;

public class RetourRechercherEtudiantBean {

    private String erreur;

    private List<EtudiantBean> listeEtudiant;

    public List<EtudiantBean> getListeEtudiant() {
        return listeEtudiant;
    }

    public void setListeEtudiant(List<EtudiantBean> listeEtudiant) {
        this.listeEtudiant = listeEtudiant;
    }

    public String getErreur() {
        return erreur;
    }

    public void setErreur(String erreur) {
        this.erreur = erreur;
    }
}
