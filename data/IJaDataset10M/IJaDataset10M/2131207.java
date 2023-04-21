package delphorm.web.forms;

import delphorm.entite.personne.Utilisateur;

public class ModifierEtatQuestionnaireFormulaire extends Utilisateur {

    Long numeroquestionnaire;

    String etat;

    public Long getNumeroquestionnaire() {
        return numeroquestionnaire;
    }

    public void setNumeroquestionnaire(Long numeroquestionnaire) {
        this.numeroquestionnaire = numeroquestionnaire;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
