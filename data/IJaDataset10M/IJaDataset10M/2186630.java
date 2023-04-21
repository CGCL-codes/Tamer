package delphorm.entite.questionnaire;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import delphorm.entite.personne.Utilisateur;

public class InstanceQuestionnaire {

    private Utilisateur auteur;

    private Date dateRemplissage;

    private List<InstanceQuestion> instancesQuestion = new ArrayList<InstanceQuestion>();

    private Questionnaire questionnaire;

    private Long id;

    private Character etat;

    public Character getEtat() {
        return etat;
    }

    public void setEtat(Character etat) {
        this.etat = etat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Utilisateur getAuteur() {
        return auteur;
    }

    public void setAuteur(Utilisateur auteur) {
        this.auteur = auteur;
    }

    public Date getDateRemplissage() {
        return dateRemplissage;
    }

    public void setDateRemplissage(Date dateRemplissage) {
        this.dateRemplissage = dateRemplissage;
    }

    public List<InstanceQuestion> getInstancesQuestion() {
        return instancesQuestion;
    }

    public void setInstancesQuestion(List instancesQuestion) {
        this.instancesQuestion = instancesQuestion;
    }
}
